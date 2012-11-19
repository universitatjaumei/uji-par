package es.uji.apps.par.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.dao.EventosDAO;
import es.uji.apps.par.db.ParEventoDTO;
import es.uji.apps.par.exceptions.ParCampoRequeridoException;
import es.uji.apps.par.model.ParEvento;

@Service
public class EventosService
{
    @Autowired
    private EventosDAO eventosDAO;
    
    public List<ParEvento> getEventos()
    {
        List<ParEvento> listaParEvento = new ArrayList<ParEvento>();
        
    	for (ParEventoDTO eventoDB : eventosDAO.getEventos())
        {
            listaParEvento.add(new ParEvento(eventoDB));
        }
        return listaParEvento;
    }

    public void removeEvento(Integer id)
    {
        eventosDAO.removeEvento(id);
    }

    public ParEvento addEvento(ParEvento evento) throws ParCampoRequeridoException
    {
    	checkRequiredFields(evento);
        return eventosDAO.addEvento(evento);
    }

    private void checkRequiredFields(ParEvento evento) throws ParCampoRequeridoException {
		if (evento.getTitulo() == null || evento.getTitulo().isEmpty())
			throw new ParCampoRequeridoException("Título");
		if (evento.getTipoEvento() == null)
			throw new ParCampoRequeridoException("Tipo de evento");
	}

	public void updateEvento(ParEvento evento) throws ParCampoRequeridoException
    {
		checkRequiredFields(evento);
        eventosDAO.updateEvento(evento);
    }
}
