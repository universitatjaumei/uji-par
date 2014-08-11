package es.uji.apps.par;

@SuppressWarnings("serial")
public class EventoNoEncontradoException extends GeneralPARException
{
    public EventoNoEncontradoException(Long eventoId)
    {
        super(EVENTO_NO_ENCONTRADO_CODE, EVENTO_NO_ENCONTRADO + "id=" + eventoId);
    }
}
