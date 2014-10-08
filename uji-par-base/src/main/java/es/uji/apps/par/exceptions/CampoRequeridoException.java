package es.uji.apps.par.exceptions;


@SuppressWarnings("serial")
public class CampoRequeridoException extends GeneralPARException
{
    public CampoRequeridoException(String message)
    {
        super(REQUIRED_FIELD_CODE, REQUIRED_FIELD + message);
    }
}
