package es.uji.apps.par;

import es.uji.apps.par.exceptions.GeneralPARException;

@SuppressWarnings("serial")
public class EventoNoEncontradoException extends GeneralPARException
{

    public EventoNoEncontradoException(Long eventoId)
    {
        super("Evento no encontrado: id=" + eventoId);
    }
}
