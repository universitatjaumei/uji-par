package es.uji.apps.par.exceptions;

public class ParUsuarioYaExiste extends ParException {

	public static final String USUARIO_YA_EXISTE = "Ya existe un usuario con este nombre de usuario";
	
	public ParUsuarioYaExiste() {
		super(USUARIO_YA_EXISTE);
	}
}
