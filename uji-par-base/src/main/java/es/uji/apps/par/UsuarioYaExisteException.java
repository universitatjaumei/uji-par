package es.uji.apps.par;


@SuppressWarnings("serial")
public class UsuarioYaExisteException extends GeneralPARException
{

    public static final String USUARIO_YA_EXISTE = "Ya existe un usuario con este nombre de usuario";

    public UsuarioYaExisteException()
    {
        super(USUARIO_YA_EXISTE);
    }
}
