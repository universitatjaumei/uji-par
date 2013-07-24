package es.uji.apps.par.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.FueraDePlazoVentaInternetException;
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

    public ResultadoCompra realizaCompraTaquilla(Long sesionId, List<Butaca> butacasSeleccionadas)
    {
        return realizaCompra(sesionId, "", "", "", "", butacasSeleccionadas, true);
    }

    public ResultadoCompra realizaCompraInternet(Long sesionId, String nombre, String apellidos, String telefono,
            String email, List<Butaca> butacasSeleccionadas) throws FueraDePlazoVentaInternetException
    {
        Sesion sesion = sesionesService.getSesion(sesionId);

        if (!sesion.getEnPlazoVentaInternet())
            throw new FueraDePlazoVentaInternetException(sesionId);

        return realizaCompra(sesionId, nombre, apellidos, telefono, email, butacasSeleccionadas, false);

    }
    
    @Transactional
    private ResultadoCompra realizaCompra(Long sesionId, String nombre, String apellidos, String telefono, String email,
            List<Butaca> butacasSeleccionadas, boolean taquilla)
    {
        ResultadoCompra resultadoCompra = new ResultadoCompra();

        CompraDTO compraDTO = comprasDAO.guardaCompra(nombre, apellidos, telefono, email, new Date(), taquilla);
        butacasDAO.reservaButacas(sesionId, compraDTO, butacasSeleccionadas);
        resultadoCompra.setCorrecta(true);

        return resultadoCompra;
    }

}
