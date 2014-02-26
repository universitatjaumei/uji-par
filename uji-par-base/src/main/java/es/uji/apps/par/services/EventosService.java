package es.uji.apps.par.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.CampoRequeridoException;
import es.uji.apps.par.EventoConCompras;
import es.uji.apps.par.EventoNoEncontradoException;
import es.uji.apps.par.config.Configuration;
import es.uji.apps.par.dao.ComprasDAO;
import es.uji.apps.par.dao.EventosDAO;
import es.uji.apps.par.db.EventoDTO;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.EventoParaSync;

@Service
public class EventosService
{
    @Autowired
    private EventosDAO eventosDAO;
    
    @Autowired
    private ComprasDAO comprasDAO;

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

    public Evento addEvento(Evento evento) throws CampoRequeridoException
    {
        checkRequiredFields(evento);
        return eventosDAO.addEvento(evento);
    }

    private void checkRequiredFields(Evento evento) throws CampoRequeridoException
    {
    	if (evento.getRssId() == null || evento.getRssId().isEmpty())
            throw new CampoRequeridoException("RSS Id");
        if (evento.getTituloEs() == null || evento.getTituloEs().isEmpty())
            throw new CampoRequeridoException("Título");
        if (evento.getParTiposEvento() == null)
            throw new CampoRequeridoException("Tipo de evento");
    }

    public void updateEvento(Evento evento) throws CampoRequeridoException, EventoConCompras
    {
        checkRequiredFields(evento);
        
        if (hasEventoCompras(evento) && modificanAsientosNumerados(evento))
        	throw new EventoConCompras(evento.getId());
        else
        	eventosDAO.updateEvento(evento);
    }

	private boolean modificanAsientosNumerados(Evento evento) {
		EventoDTO eventoDTO = eventosDAO.getEventoById(evento.getId());
		return eventoDTO.getAsientosNumerados() != evento.getAsientosNumerados();
	}

	private boolean hasEventoCompras(Evento evento) {
		return comprasDAO.getComprasOfEvento(evento.getId()).size() > 0;
	}

    public Evento getEvento(Long eventoId) throws EventoNoEncontradoException
    {
        List<EventoDTO> listaEventosDTO = eventosDAO.getEventoDTO(eventoId.longValue());

        if (listaEventosDTO.size() > 0)
            return new Evento(listaEventosDTO.get(0), true);
        else
            throw new EventoNoEncontradoException(eventoId);
    }
    
    public Evento getEventoByRssId(Long contenidoId) throws EventoNoEncontradoException
    {
        EventoDTO eventoDTO = eventosDAO.getEventoByRssId(contenidoId.toString());

        if (eventoDTO == null)
            throw new EventoNoEncontradoException(contenidoId);
        else
            return new Evento(eventoDTO, true);
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

	//url: "http://www.example.com/test/23173?idioma=##IDIOMA##"
	public List<EventoParaSync> getEventosActivosParaVentaOnline() {
		List<EventoDTO> eventosDTO = eventosDAO.getEventosActivosParaVentaOnline();
		List<EventoParaSync> eventosParaSync = new ArrayList<EventoParaSync>();
		String urlPrefix = Configuration.getUrlPublic() + "/rest/evento/";
		String urlSuffix = "?lang=##IDIOMA##";
		
		for (EventoDTO eventoDTO: eventosDTO) {
			EventoParaSync eventoParaSync = new EventoParaSync(eventoDTO.getRssId(), urlPrefix + eventoDTO.getRssId() + urlSuffix);
			eventosParaSync.add(eventoParaSync);
		}
		return eventosParaSync;
	}
}
