package es.uji.apps.par;


@SuppressWarnings("serial")
public class NoHayButacasLibresException extends GeneralPARException
{
    private final Long sesionId;
    private final String localizacion;

    public NoHayButacasLibresException(Long sesionId, String localizacion)
    {
        super(SESION_SIN_BUTACAS_LIBRES_CODE, SESION_SIN_BUTACAS_LIBRES + String.format("sesionId = %d, localizacion=%s", sesionId,
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
