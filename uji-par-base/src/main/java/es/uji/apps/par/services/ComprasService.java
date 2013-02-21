package es.uji.apps.par.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.dao.ButacasDAO;
import es.uji.apps.par.dao.ComprasDAO;
import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.model.Butaca;
import es.uji.apps.par.model.ResultadoCompra;

@Service
public class ComprasService
{
    @Autowired
    private ComprasDAO comprasDAO;

    @Autowired
    private ButacasDAO butacasDAO;

    public ResultadoCompra realizaCompra(Long sesionId, String nombre, String apellidos, String telefono, String email,
            List<Butaca> butacasSeleccionadas)
    {
        ResultadoCompra resultadoCompra = new ResultadoCompra();

        CompraDTO compraDTO = comprasDAO.guardaCompra(nombre, apellidos, telefono, email);
        butacasDAO.reservaButacas(sesionId, compraDTO, butacasSeleccionadas);
        resultadoCompra.setCorrecta(true);

        return resultadoCompra;
    }

}
