package es.uji.apps.par;


@SuppressWarnings("serial")
public class SinIvaException extends GeneralPARException
{
    private String evento;

    public SinIvaException(String evento)
    {
        super(String.format("Evento sin IVA: %s", evento));
        this.evento = evento;
    }

    public String getEvento()
    {
        return evento;
    }

    public void setEvento(String evento)
    {
        this.evento = evento;
    }

}
