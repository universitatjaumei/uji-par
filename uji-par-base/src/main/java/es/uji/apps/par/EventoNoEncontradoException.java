package es.uji.apps.par;


@SuppressWarnings("serial")
public class EventoNoEncontradoException extends GeneralPARException
{

    public EventoNoEncontradoException(Long eventoId)
    {
        super("Evento no encontrado: id=" + eventoId);
    }
}
