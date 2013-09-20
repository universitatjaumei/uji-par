package es.uji.apps.par.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.CampoRequeridoException;
import es.uji.apps.par.EventoNoEncontradoException;
import es.uji.apps.par.dao.EventosDAO;
import es.uji.apps.par.db.EventoDTO;
import es.uji.apps.par.model.Evento;

@Service
public class EventosService
{
    @Autowired
    private EventosDAO eventosDAO;

    public List<Evento> getEventosConSesiones()
    {
       return eventosDAO.getEventosConSesiones();
    }
    
    public List<Evento> getEventos(String sort, int start, int limit)
    {
       return getEventos(false, sort, start, limit);
    }
    
    public List<Evento> getEventosActivos(String sort, int start, int limit)
    {
       return getEventos(true, sort, start, limit);
    }
    
    private List<Evento> getEventos(boolean activos, String sort, int start, int limit)
    {
        if (activos)
            return eventosDAO.getEventosActivos(sort, start, limit);
        else
            return eventosDAO.getEventos(sort, start, limit);
    }

    public void removeEvento(Integer id)
    {
        eventosDAO.removeEvento(id);
    }

    public void updateEvento(Evento evento) throws CampoRequeridoException
    {
        eventosDAO.updateEvento(evento);
    }

    public Evento getEvento(Long eventoId) throws EventoNoEncontradoException
    {
        List<EventoDTO> listaEventosDTO = eventosDAO.getEventoDTO(eventoId.longValue());

        if (listaEventosDTO.size() > 0)
            return new Evento(listaEventosDTO.get(0), true);
        else
            throw new EventoNoEncontradoException(eventoId);
    }

    public void removeImagen(Integer eventoId)
    {
        eventosDAO.deleteImagen(eventoId);
    }

	public int getTotalEventosActivos() {
		return eventosDAO.getTotalEventosActivos();
	}

	public int getTotalEventos() {
		return eventosDAO.getTotalEventos();
	}
}
