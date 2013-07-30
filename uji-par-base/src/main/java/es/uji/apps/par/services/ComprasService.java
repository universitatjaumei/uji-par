package es.uji.apps.par.services;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.ButacaOcupadaException;
import es.uji.apps.par.CompraSinButacasException;
import es.uji.apps.par.FueraDePlazoVentaInternetException;
import es.uji.apps.par.NoHayButacasLibresException;
import es.uji.apps.par.ResponseMessage;
import es.uji.apps.par.dao.ButacasDAO;
import es.uji.apps.par.dao.ComprasDAO;
import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.model.Butaca;
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

    public ResultadoCompra realizaCompraTaquilla(Long sesionId, List<Butaca> butacasSeleccionadas) throws NoHayButacasLibresException, ButacaOcupadaException, CompraSinButacasException
    {
        return realizaCompra(sesionId, "", "", "", "", butacasSeleccionadas, true);
    }

    public ResultadoCompra realizaCompraInternet(Long sesionId, String nombre, String apellidos, String telefono,
            String email, List<Butaca> butacasSeleccionadas) throws FueraDePlazoVentaInternetException, NoHayButacasLibresException, ButacaOcupadaException, CompraSinButacasException
    {
        Sesion sesion = sesionesService.getSesion(sesionId);

        if (!sesion.getEnPlazoVentaInternet())
            throw new FueraDePlazoVentaInternetException(sesionId);

        return realizaCompra(sesionId, nombre, apellidos, telefono, email, butacasSeleccionadas, false);

    }
    
    @Transactional
    private synchronized ResultadoCompra realizaCompra(Long sesionId, String nombre, String apellidos, String telefono, String email,
            List<Butaca> butacasSeleccionadas, boolean taquilla) throws NoHayButacasLibresException, ButacaOcupadaException, CompraSinButacasException
    {
        if (butacasSeleccionadas.size() == 0)
            throw new CompraSinButacasException();
        
        ResultadoCompra resultadoCompra = new ResultadoCompra();

        CompraDTO compraDTO = comprasDAO.guardaCompra(nombre, apellidos, telefono, email, new Date(), taquilla);
        butacasDAO.reservaButacas(sesionId, compraDTO, butacasSeleccionadas);
        resultadoCompra.setCorrecta(true);
            
        return resultadoCompra;
    }

}
