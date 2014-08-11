package es.uji.apps.par;


@SuppressWarnings("serial")
public class SinIvaException extends GeneralPARException
{
    private String evento;

    public SinIvaException(String evento)
    {
        super(EVENTO_SIN_IVA_CODE, EVENTO_SIN_IVA + String.format("%s", evento));
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
