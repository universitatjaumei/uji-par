package es.uji.apps.par.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.ButacaOcupadaException;
import es.uji.apps.par.CompraButacaDescuentoCero;
import es.uji.apps.par.CompraInvitacionPorInternetException;
import es.uji.apps.par.CompraSinButacasException;
import es.uji.apps.par.FueraDePlazoVentaInternetException;
import es.uji.apps.par.NoHayButacasLibresException;
import es.uji.apps.par.config.Configuration;
import es.uji.apps.par.dao.ButacasDAO;
import es.uji.apps.par.dao.ComprasDAO;
import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.model.Butaca;
import es.uji.apps.par.model.Compra;
import es.uji.apps.par.model.PreciosSesion;
import es.uji.apps.par.model.ResultadoCompra;
import es.uji.apps.par.model.Sesion;

@Service
public class ComprasService
{
    @Autowired
    private ComprasDAO comprasDAO;

    @Autowired
    private ButacasDAO butacasDAO;

    @Autowired
    private SesionesService sesionesService;

    public ResultadoCompra registraCompraTaquilla(Long sesionId, List<Butaca> butacasSeleccionadas)
            throws NoHayButacasLibresException, ButacaOcupadaException, CompraSinButacasException
    {
        return registraCompra(sesionId, butacasSeleccionadas, true);
    }
    
    public ResultadoCompra reservaCompraTaquilla(Long sesionId, Date desde, Date hasta, List<Butaca> butacasSeleccionadas)
            throws NoHayButacasLibresException, ButacaOcupadaException, CompraSinButacasException
    {
        return registraCompra(sesionId, butacasSeleccionadas, true);
    }

    @Transactional
    public ResultadoCompra realizaCompraInternet(Long sesionId, List<Butaca> butacasSeleccionadas, String uuidCompraActual) throws FueraDePlazoVentaInternetException,
            NoHayButacasLibresException, ButacaOcupadaException, CompraSinButacasException, CompraInvitacionPorInternetException, CompraButacaDescuentoCero
    {
        Sesion sesion = sesionesService.getSesion(sesionId);
        Map<String, PreciosSesion> precios = sesionesService.getPreciosSesionPorLocalizacion(sesionId);

        if (!sesion.getEnPlazoVentaInternet())
            throw new FueraDePlazoVentaInternetException(sesionId);
        
        for (Butaca butaca : butacasSeleccionadas)
        {
            if (butaca.getTipo().equals("invitacion"))
                throw new CompraInvitacionPorInternetException();
            
            if (esButacaDescuentoCero(butaca, precios))
                throw new CompraButacaDescuentoCero();
        }
        
        // Si teníamos una compra en marcha la eliminamos (puede pasar cuando intentamos pagar con tarjeta y volvemos atrás)        
        if (uuidCompraActual != null && !uuidCompraActual.equals(""))
            comprasDAO.borrarCompraNoPagada(uuidCompraActual);

        return registraCompra(sesionId, butacasSeleccionadas, false);
    }
    

    // Intentamos comprar una butaca de tipo descuento que tiene precio 0
    private boolean esButacaDescuentoCero(Butaca butaca, Map<String, PreciosSesion> precios)
    {
        return butaca.getTipo().equals("descuento") && precios.get(butaca.getLocalizacion()).getDescuento().equals(BigDecimal.ZERO);
    }

    public ResultadoCompra realizaCompraInternet(Long sesionId, int platea1Normal, int platea1Descuento,
            int platea2Normal, int platea2Descuento, String uuidCompra) throws FueraDePlazoVentaInternetException, 
            NoHayButacasLibresException, ButacaOcupadaException, CompraSinButacasException, CompraInvitacionPorInternetException, CompraButacaDescuentoCero
    {
        List<Butaca> butacasSeleccionadas = new ArrayList<Butaca>();
        
        for (int i=0; i<platea1Normal; i++)
            butacasSeleccionadas.add(new Butaca("platea1", "normal"));

        for (int i=0; i<platea1Descuento; i++)
            butacasSeleccionadas.add(new Butaca("platea1", "descuento"));
        
        for (int i=0; i<platea2Normal; i++)
            butacasSeleccionadas.add(new Butaca("platea2", "normal"));

        for (int i=0; i<platea2Descuento; i++)
            butacasSeleccionadas.add(new Butaca("platea2", "descuento"));
        
        return realizaCompraInternet(sesionId, butacasSeleccionadas, uuidCompra);
    }    

    @Transactional
    private synchronized ResultadoCompra registraCompra(Long sesionId, List<Butaca> butacasSeleccionadas, boolean taquilla)
            throws NoHayButacasLibresException, ButacaOcupadaException, CompraSinButacasException
    {
        if (butacasSeleccionadas.size() == 0)
            throw new CompraSinButacasException();

        ResultadoCompra resultadoCompra = new ResultadoCompra();

        BigDecimal importe = calculaImporteButacas(sesionId, butacasSeleccionadas, taquilla);

        CompraDTO compraDTO;

        compraDTO = comprasDAO.insertaCompra(sesionId, new Date(), taquilla,
                importe);

        butacasDAO.reservaButacas(sesionId, compraDTO, butacasSeleccionadas);

        resultadoCompra.setCorrecta(true);
        resultadoCompra.setId(compraDTO.getId());
        resultadoCompra.setUuid(compraDTO.getUuid());

        return resultadoCompra;
    }

    public BigDecimal calculaImporteButacas(Long sesionId, List<Butaca> butacasSeleccionadas, boolean taquilla)
    {
        BigDecimal importe = new BigDecimal("0");
        Map<String, PreciosSesion> preciosLocalizacion = sesionesService.getPreciosSesionPorLocalizacion(sesionId);

        for (Butaca butaca : butacasSeleccionadas)
        {
            PreciosSesion precioLocalizacion = preciosLocalizacion.get(butaca.getLocalizacion());

            if (butaca.getTipo().equals("normal"))
                importe = importe.add(precioLocalizacion.getPrecio());
            else if (butaca.getTipo().equals("descuento"))
                importe = importe.add(precioLocalizacion.getDescuento());
            else if (butaca.getTipo().equals("invitacion"))
                importe = importe.add(precioLocalizacion.getInvitacion());
            else
                throw new RuntimeException("Butaca con tipo de precio no reconocido: " + butaca);
        }

        if (!taquilla)
        {
            BigDecimal gastosGestion = new BigDecimal(Configuration.getGastosGestion());
                    
            importe = importe.add(gastosGestion);
        }
        
        return importe;
    }

    public void marcaPagada(long idCompra)
    {
        comprasDAO.marcarPagada(idCompra);
    }

    public void eliminaPendientes()
    {
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

    @Transactional
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
        
        List<CompraDTO> compras = comprasDAO.getComprasBySesion(sesionId, showAnuladas, sortParameter, start, limit, showOnline, search);

        for (CompraDTO compraDTO: compras)
            result.add(new Compra(compraDTO));
        
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

	public void anularCompraReserva(Long idCompraReserva) {
		comprasDAO.anularCompraReserva(idCompraReserva);
	}
	
    public void desanularCompraReserva(Long idCompraReserva) {
        comprasDAO.desanularCompraReserva(idCompraReserva);
    }	
	
    public void anulaReservasCaducadas()
    {
        List<CompraDTO> aCaducar = comprasDAO.getReservasACaducar(new Date());
        
        for (CompraDTO compraDTO:aCaducar)
        {
            anularCompraReserva(compraDTO.getId());
        }
    }

	public void anularButaca(Long idButaca) {
		comprasDAO.anularButaca(idButaca);
	}
}


