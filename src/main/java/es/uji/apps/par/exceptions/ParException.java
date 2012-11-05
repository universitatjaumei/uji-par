package es.uji.apps.par.exceptions;


public class ParException extends Exception {
	
	protected String message;
	protected int code;
	
	public static final String ERROR_GENERAL_MESS = "Error general";

	public ParException(String message){
		super(message);
	}
}
