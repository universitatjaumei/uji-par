package es.uji.apps.par.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.dao.TiposEventosDAO;
import es.uji.apps.par.model.ParTipoEvento;
import es.uji.apps.par.model.ParUsuario;

@Service
public class TiposEventosService
{
    @Autowired
    private TiposEventosDAO tiposEventosDAO;
    
    public List<ParTipoEvento> getTiposEventos()
    {
        return tiposEventosDAO.getTiposEventos();
    }

    public void removeTipoEvento(Integer id)
    {
        tiposEventosDAO.removeTipoEvento(id);
    }

    public ParTipoEvento addTipoEvento(ParTipoEvento tipoEvento)
    {
        return tiposEventosDAO.addTipoEvento(tipoEvento);
    }

    public void updateTipoEvento(ParTipoEvento tipoEvento)
    {
        tiposEventosDAO.updateTipoEvento(tipoEvento);
    }
}
