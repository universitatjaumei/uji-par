package es.uji.apps.par.exceptions;


@SuppressWarnings("serial")
public class UsuarioYaExisteException extends GeneralPARException
{
    public UsuarioYaExisteException()
    {
        super(USUARIO_YA_EXISTE_CODE);
    }
}
