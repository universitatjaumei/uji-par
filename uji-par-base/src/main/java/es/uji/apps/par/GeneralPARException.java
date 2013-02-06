package es.uji.apps.par;

@SuppressWarnings("serial")
public class GeneralPARException extends Exception
{

    protected String message;
    protected int code;

    public static final String ERROR_GENERAL_MESS = "Error general";

    public GeneralPARException(String message)
    {
        super(message);
    }
}
