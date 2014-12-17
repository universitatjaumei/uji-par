package es.uji.apps.par.services;

import es.uji.apps.par.dao.AbonadosDAO;
import es.uji.apps.par.model.Abonado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AbonadosService {

    @Autowired
    private AbonadosDAO abonadosDAO;

    @Autowired
    private ComprasService comprasService;

    public List<Abonado> getAbonadosPorAbono(Long abonoId, String sortParameter, int start, int limit)
    {
        return abonadosDAO.getAbonadosPorAbono(abonoId, sortParameter, start, limit);
    }

    public int getTotalAbonadosPorAbono(Long abonoId) {
        return abonadosDAO.getTotalAbonadosPorAbono(abonoId);
    }

    @Transactional
    public void removeAbonado(Long abonadoId)
    {
        comprasService.anularCompraAbonado(abonadoId);
        abonadosDAO.anularAbonado(abonadoId);
    }

    @Transactional
    public void updateAbonado(Abonado abonado) {
        abonadosDAO.updateAbonado(abonado);
        comprasService.actualizaDatosAbonado(abonado);
    }
}
