package es.uji.apps.par;

@SuppressWarnings("serial")
public class NoHayButacasLibresException extends GeneralPARException
{
    private final Long sesionId;
    private final String localizacion;

    public NoHayButacasLibresException(Long sesionId, String localizacion)
    {
        super(String.format("No hay butacas libres: sesionId = %d, localizacion=%s", sesionId,
                localizacion));

        this.sesionId = sesionId;
        this.localizacion = localizacion;
    }

    public Long getSesionId()
    {
        return sesionId;
    }

    public String getLocalizacion()
    {
        return localizacion;
    }
}
