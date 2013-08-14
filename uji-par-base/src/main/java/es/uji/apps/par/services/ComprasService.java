package es.uji.apps.par.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.ButacaOcupadaException;
import es.uji.apps.par.CompraSinButacasException;
import es.uji.apps.par.FueraDePlazoVentaInternetException;
import es.uji.apps.par.NoHayButacasLibresException;
import es.uji.apps.par.dao.ButacasDAO;
import es.uji.apps.par.dao.ComprasDAO;
import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.model.Butaca;
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

    public ResultadoCompra registraCompraTaquilla(Long sesionId, List<Butaca> butacasSeleccionadas) throws NoHayButacasLibresException, ButacaOcupadaException, CompraSinButacasException
    {
        return registraCompra(sesionId, "", "", "", "", butacasSeleccionadas, true);
    }

    public ResultadoCompra realizaCompraInternet(Long sesionId, String nombre, String apellidos, String telefono,
            String email, List<Butaca> butacasSeleccionadas) throws FueraDePlazoVentaInternetException, NoHayButacasLibresException, ButacaOcupadaException, CompraSinButacasException
    {
        Sesion sesion = sesionesService.getSesion(sesionId);

        if (!sesion.getEnPlazoVentaInternet())
            throw new FueraDePlazoVentaInternetException(sesionId);

        return registraCompra(sesionId, nombre, apellidos, telefono, email, butacasSeleccionadas, false);
    }
    
    @Transactional
    private synchronized ResultadoCompra registraCompra(Long sesionId, String nombre, String apellidos, String telefono, String email,
            List<Butaca> butacasSeleccionadas, boolean taquilla) throws NoHayButacasLibresException, ButacaOcupadaException, CompraSinButacasException
    {
        if (butacasSeleccionadas.size() == 0)
            throw new CompraSinButacasException();
        
        ResultadoCompra resultadoCompra = new ResultadoCompra();
        
        BigDecimal importe = calculaImporteButacas(sesionId, butacasSeleccionadas);

        CompraDTO compraDTO;
        
        compraDTO = comprasDAO.insertaCompra(sesionId, nombre, apellidos, telefono, email, new Date(), taquilla, importe);
        
        butacasDAO.reservaButacas(sesionId, compraDTO, butacasSeleccionadas);
        
        resultadoCompra.setCorrecta(true);
        resultadoCompra.setId(compraDTO.getId());
        resultadoCompra.setUuid(compraDTO.getUuid());
            
        return resultadoCompra;
    }

    public BigDecimal calculaImporteButacas(Long sesionId, List<Butaca> butacasSeleccionadas)
    {
        BigDecimal importe = new BigDecimal("0");
        Map<String, PreciosSesion> preciosLocalizacion = sesionesService.getPreciosSesionPorLocalizacion(sesionId);
        
        for (Butaca butaca: butacasSeleccionadas)
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
        
        return importe;
    }
    
}
