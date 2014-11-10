package es.uji.apps.par.services;

import com.mysema.query.Tuple;
import es.uji.apps.par.config.Configuration;
import es.uji.apps.par.dao.ButacasDAO;
import es.uji.apps.par.dao.ComprasDAO;
import es.uji.apps.par.dao.SesionesDAO;
import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.db.SesionDTO;
import es.uji.apps.par.exceptions.*;
import es.uji.apps.par.model.*;
import es.uji.apps.par.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ComprasService
{
    @Autowired
    private ComprasDAO comprasDAO;

    @Autowired
    private ButacasDAO butacasDAO;

    @Autowired
    private SesionesService sesionesService;

	@Autowired
	private SesionesDAO sesionesDAO;

    public ResultadoCompra registraCompraTaquilla(Long sesionId, List<Butaca> butacasSeleccionadas)
            throws NoHayButacasLibresException, ButacaOcupadaException, CompraSinButacasException, IncidenciaNotFoundException {
        return registraCompra(sesionId, butacasSeleccionadas, true);
    }

    /*public ResultadoCompra reservaCompraTaquilla(Long sesionId, Date desde, Date hasta,
                                                  List<Butaca> butacasSeleccionadas)
            throws NoHayButacasLibresException, ButacaOcupadaException, CompraSinButacasException
    {
        return registraCompra(sesionId, butacasSeleccionadas, true);
    }*/

    @Transactional(rollbackForClassName={"CompraButacaDescuentoNoDisponible","FueraDePlazoVentaInternetException",
    		"NoHayButacasLibresException","ButacaOcupadaException","CompraSinButacasException"})
    public ResultadoCompra realizaCompraInternet(Long sesionId, List<Butaca> butacasSeleccionadas, String uuidCompraActual) throws Exception
    {
        Sesion sesion = sesionesService.getSesion(sesionId);
        Evento evento = sesion.getEvento();
        Map<String, Map<Long, PreciosSesion>> preciosPorZona = sesionesService.getPreciosSesionPublicosPorLocalizacion(sesionId);

        if (!sesion.getEnPlazoVentaInternet())
            throw new FueraDePlazoVentaInternetException(sesionId);
        
        for (Butaca butaca : butacasSeleccionadas)
        {
        	Map<Long, PreciosSesion> mapaTarifasPrecios = preciosPorZona.get(butaca.getLocalizacion());
        	
        	//TODO -> Con los cambios de ahora, esta parte igual no funciona para la UJI (ya que solamente lo usa ella)
        	if (butaca.getTipo() != null)
        		if (esButacaDescuentoNoDisponible(butaca.getTipo(), evento, mapaTarifasPrecios.get(butaca.getTipo())))
        			throw new CompraButacaDescuentoNoDisponible();
        }
        
        // Si teníamos una compra en marcha la eliminamos (puede pasar cuando intentamos pagar con tarjeta y volvemos atrás)        
        if (uuidCompraActual != null && !uuidCompraActual.equals(""))
            comprasDAO.borrarCompraNoPagada(uuidCompraActual);

        return registraCompra(sesionId, butacasSeleccionadas, false);
    }

    
	// Intentamos comprar una butaca de tipo descuento pero el descuento no está disponible (casos: descuento 0 ó cine/teatro con precio < 8 €)
    public boolean esButacaDescuentoNoDisponible(String tipoButaca, Evento evento, PreciosSesion precioLocalizacion)
    {
        return tipoButaca != null && tipoButaca.equals("descuento") && 
        		(descuentoCero(precioLocalizacion) || cineTeatroMenorDe(evento, precioLocalizacion, new BigDecimal(8)));
    }

    private boolean descuentoCero(PreciosSesion precioSesion)
    {
        return precioSesion.getDescuento() == null || precioSesion.getDescuento().equals(BigDecimal.ZERO);
    }
    
    private boolean cineTeatroMenorDe(Evento evento, PreciosSesion precioSesion, BigDecimal descuentoLimite)
    {
        String tipoEvento = evento.getParTiposEvento().getNombreEs().toLowerCase();
        
        return (tipoEvento.equals("cine") || tipoEvento.equals("teatro")) && precioSesion.getPrecio().compareTo(descuentoLimite) < 0;
    }

    @Transactional(rollbackForClassName={"NoHayButacasLibresException","ButacaOcupadaException",
    		"CompraSinButacasException","IncidenciaNotFoundException"})
    private synchronized ResultadoCompra registraCompra(Long sesionId, List<Butaca> butacasSeleccionadas, boolean taquilla)
            throws NoHayButacasLibresException, ButacaOcupadaException, CompraSinButacasException, IncidenciaNotFoundException {
        if (butacasSeleccionadas.size() == 0)
            throw new CompraSinButacasException();

        ResultadoCompra resultadoCompra = new ResultadoCompra();
        BigDecimal importe = calculaImporteButacas(sesionId, butacasSeleccionadas, taquilla);
        CompraDTO compraDTO = comprasDAO.insertaCompra(sesionId, new Date(), taquilla, importe);
        butacasDAO.reservaButacas(sesionId, compraDTO, butacasSeleccionadas);

        resultadoCompra.setCorrecta(true);
        resultadoCompra.setId(compraDTO.getId());
        resultadoCompra.setUuid(compraDTO.getUuid());

        if (taquilla) {
            SesionDTO sesionDTO = sesionesDAO.getSesion(sesionId);
            long totalAnuladas = sesionesDAO.getButacasAnuladasTotal(sesionId);
            boolean isVentaDegradada = DateUtils.isDataDegradada(sesionDTO.getFechaCelebracion());
            boolean isReprogramada = sesionesDAO.isSesionReprogramada(sesionDTO.getFechaCelebracion(),
                    sesionDTO.getParSala().getId(), sesionId);

            int tipoIncidenciaId = sesionesDAO.getTipoIncidenciaSesion(totalAnuladas, isVentaDegradada, isReprogramada);
            sesionesDAO.setIncidencia(sesionId, tipoIncidenciaId);
        }

        return resultadoCompra;
    }

    public BigDecimal calculaImporteButacas(Long sesionId, List<Butaca> butacasSeleccionadas, boolean taquilla)
    {
        BigDecimal importe = new BigDecimal("0");
        Map<String, Map<Long, PreciosSesion>> preciosLocalizacion = sesionesService.getPreciosSesionPorLocalizacion(sesionId);

        for (Butaca butaca : butacasSeleccionadas)
        {
        	Map<Long, PreciosSesion> mapaTarifasPrecios = preciosLocalizacion.get(butaca.getLocalizacion());
            importe = importe.add(mapaTarifasPrecios.get(Long.valueOf(butaca.getTipo())).getPrecio());
        }

        if (!taquilla)
        {
            BigDecimal gastosGestion = new BigDecimal(Configuration.getGastosGestion());
                    
            importe = importe.add(gastosGestion);
        }
        
        return importe;
    }

    @Transactional
    public void marcaPagada(long idCompra)
    {
        comprasDAO.marcarPagada(idCompra);
        if (Configuration.isIdEntrada()) {
            butacasDAO.asignarIdEntrada(idCompra);
        }
    }

    @Transactional
	public void marcarPagadaConReferenciaDePago(Long idCompra, String referenciaDePago) {
		
		comprasDAO.marcarPagadaConReferenciaDePago(idCompra, referenciaDePago);
        if (Configuration.isIdEntrada()) {
            butacasDAO.asignarIdEntrada(idCompra);
        }
	}

    public void eliminaPendientes() throws IncidenciaNotFoundException {
        comprasDAO.eliminaComprasPendientes();
    }

    public CompraDTO getCompraById(long idCompra)
    {
        return comprasDAO.getCompraById(idCompra);
    }

    public CompraDTO getCompraByUuid(String uuidCompra)
    {
        return comprasDAO.getCompraByUuid(uuidCompra);
    }

    public void marcaPagadaPasarela(long idCompra, String codigoPago)
    {
        comprasDAO.marcarPagadaPasarela(idCompra, codigoPago);
    }

    public void rellenaDatosComprador(String uuidCompra, String nombre, String apellidos, String direccion,
            String poblacion, String cp, String provincia, String telefono, String email, String infoPeriodica)
    {
        comprasDAO.rellenaDatosComprador(uuidCompra, nombre, apellidos, direccion, poblacion, cp, provincia, telefono, email, infoPeriodica);
    }
    
    private Date addHoraMinutoToFecha(Date fecha, int hora, int minuto) {
    	Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.set(Calendar.HOUR_OF_DAY, hora);
        cal.set(Calendar.MINUTE, minuto);
        return cal.getTime();
    }

    @Transactional(rollbackForClassName={"NoHayButacasLibresException","ButacaOcupadaException",
	"CompraSinButacasException"})
    public ResultadoCompra reservaButacas(Long sesionId, Date desde, Date hasta, List<Butaca> butacasSeleccionadas, String observaciones, 
    		int horaInicial, int horaFinal, int minutoInicial, int minutoFinal) 
            throws NoHayButacasLibresException, ButacaOcupadaException, CompraSinButacasException
    {
        if (butacasSeleccionadas.size() == 0)
            throw new CompraSinButacasException();

        ResultadoCompra resultadoCompra = new ResultadoCompra();
        
        
        desde = addHoraMinutoToFecha(desde, horaInicial, minutoInicial);
        hasta = addHoraMinutoToFecha(hasta, horaFinal, minutoFinal);

        CompraDTO compraDTO = comprasDAO.reserva(sesionId, new Date(), desde, hasta, observaciones);

        butacasDAO.reservaButacas(sesionId, compraDTO, butacasSeleccionadas);

        resultadoCompra.setCorrecta(true);
        resultadoCompra.setId(compraDTO.getId());
        resultadoCompra.setUuid(compraDTO.getUuid());

        return resultadoCompra;
    }

    public List<Compra> getComprasBySesion(long sesionId, int showAnuladas, String sortParameter, int start, int limit, int showOnline, String search)
    {
        List<Compra> result = new ArrayList<Compra>();
        
        List<Tuple> tuples = comprasDAO.getComprasBySesion(sesionId, showAnuladas, sortParameter, start, limit, showOnline, search);

        for (Tuple tupla: tuples) {
        	CompraDTO compraDTO = tupla.get(0, CompraDTO.class);
        	BigDecimal importe = tupla.get(1, BigDecimal.class);
        	compraDTO.setImporte(importe);
            result.add(new Compra(compraDTO));
        }
        
        return result;
    }
    

    public List<Compra> getComprasBySesionFechaSegundos(long sesionId, int showAnuladas, String sortParameter, int start, int limit, int showOnline, String search)
    {
        List<Compra> compras = getComprasBySesion(sesionId, showAnuladas, sortParameter, start, limit, showOnline, search);
        
        for (Compra compra : compras)
        {
            compra.setFecha(new Date(compra.getFecha().getTime()/1000));
            if (compra.getDesde() != null)
            	compra.setDesde(new Date(compra.getDesde().getTime()/1000));
            
            if (compra.getHasta() != null)
            	compra.setHasta(new Date(compra.getHasta().getTime()/1000));
        }
        
        return compras;
    }

	public int getTotalComprasBySesion(Long sesionId, int showAnuladas, int showOnline, String search) {
		return comprasDAO.getTotalComprasBySesion(sesionId, showAnuladas, showOnline, search);
	}

	public void anularCompraReserva(Long idCompraReserva) throws IncidenciaNotFoundException {
		comprasDAO.anularCompraReserva(idCompraReserva, true);
	}

	@Transactional
    public void desanularCompraReserva(Long idCompraReserva) throws ButacaOcupadaAlActivarException {
        comprasDAO.desanularCompraReserva(idCompraReserva);

		CompraDTO compraDTO = getCompraById(idCompraReserva);
		if (compraDTO.getReserva() == null || compraDTO.getReserva() == false) {
			long totalAnuladas = sesionesDAO.getButacasAnuladasTotal(compraDTO.getParSesion().getId());
			if (totalAnuladas == 0L) {
				sesionesDAO.removeAnulacionVentasFromSesion(compraDTO.getParSesion().getId());
			}
		}
	}
	
    public void anulaReservasCaducadas() throws IncidenciaNotFoundException {
        List<CompraDTO> aCaducar = comprasDAO.getReservasACaducar(new Date());
        
        for (CompraDTO compraDTO:aCaducar)
			comprasDAO.anularCompraReserva(compraDTO.getId(), false);
    }
	
	@Transactional
    public void anularButacas(List<Long> idsButacas) throws IncidenciaNotFoundException {
	    for (Long idButaca: idsButacas)
	    	butacasDAO.anularButaca(idButaca);
	}

    public void rellenaCodigoPagoPasarela(long idCompra, String recibo) {
        comprasDAO.rellenaCodigoPagoPasarela(idCompra, recibo);
    }

	public void passarACompra(Long sesionId, Long idCompraReserva) {
		comprasDAO.passarACompra(sesionId, idCompraReserva);
	}
}