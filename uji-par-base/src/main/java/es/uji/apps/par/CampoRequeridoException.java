package es.uji.apps.par;


@SuppressWarnings("serial")
public class CampoRequeridoException extends GeneralPARException
{

    public static final String CAMPO_OBLIGATORIO = "El campo es obligatorio: ";

    public CampoRequeridoException(String message)
    {
        super(CAMPO_OBLIGATORIO + message);
    }
}
