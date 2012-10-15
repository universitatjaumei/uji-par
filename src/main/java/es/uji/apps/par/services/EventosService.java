package es.uji.apps.par.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.dao.EventosDAO;
import es.uji.apps.par.model.ParEvento;

@Service
public class EventosService
{
    @Autowired
    private EventosDAO eventosDAO;
    
    public List<ParEvento> getEventos()
    {
        return eventosDAO.getEventos();
    }

    public void removeEvento(Integer id)
    {
        eventosDAO.removeEvento(id);
    }

    public ParEvento addEvento(ParEvento evento)
    {
        return eventosDAO.addEvento(evento);
    }

    public void updateEvento(ParEvento evento)
    {
        eventosDAO.updateEvento(evento);
    }
}
