package es.uji.apps.par.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.CampoRequeridoException;
import es.uji.apps.par.ImagenNoEncontradaException;
import es.uji.apps.par.dao.EventosDAO;
import es.uji.apps.par.db.EventoDTO;
import es.uji.apps.par.model.Evento;

@Service
public class EventosService
{
    @Autowired
    private EventosDAO eventosDAO;

    public List<Evento> getEventos()
    {
        List<Evento> listaParEvento = new ArrayList<Evento>();

        for (EventoDTO eventoDB : eventosDAO.getEventos())
        {
            listaParEvento.add(new Evento(eventoDB, false));
        }
        return listaParEvento;
    }

    public void removeEvento(Integer id)
    {
        eventosDAO.removeEvento(id);
    }

    public Evento addEvento(Evento evento) throws CampoRequeridoException
    {
        checkRequiredFields(evento);
        return eventosDAO.addEvento(evento);
    }

    private void checkRequiredFields(Evento evento) throws CampoRequeridoException
    {
        if (evento.getTituloEs() == null || evento.getTituloEs().isEmpty())
            throw new CampoRequeridoException("Título");
        if (evento.getParTipoEvento() == null)
            throw new CampoRequeridoException("Tipo de evento");
    }

    public void updateEvento(Evento evento) throws CampoRequeridoException
    {
        checkRequiredFields(evento);
        eventosDAO.updateEvento(evento);
    }

    public Evento getEvento(Integer eventoId) throws ImagenNoEncontradaException
    {
        List<EventoDTO> listaEventosDTO = eventosDAO.getEventoDTO(eventoId.longValue());

        if (listaEventosDTO.size() > 0)
            return new Evento(listaEventosDTO.get(0), true);
        else
            throw new ImagenNoEncontradaException(eventoId);
    }

    public void removeImagen(Integer eventoId)
    {
        eventosDAO.deleteImagen(eventoId);
    }
}
