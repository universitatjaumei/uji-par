package es.uji.apps.par.exceptions;

public class ParCampoRequeridoException extends ParException {
	
	public static final String CAMPO_OBLIGATORIO = "El campo es obligatorio: "; 

	public ParCampoRequeridoException(String message) {
		super(CAMPO_OBLIGATORIO + message);
	}
}
