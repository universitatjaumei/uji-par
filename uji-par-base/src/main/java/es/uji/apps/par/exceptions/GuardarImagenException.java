package es.uji.apps.par.exceptions;


@SuppressWarnings("serial")
public class GuardarImagenException extends GeneralPARException
{
    public GuardarImagenException(String message)
    {
        super(ADE_ERROR_CODE, ADE_ERROR + message);
    }
}
