package es.uji.apps.par.exceptions;

public class ParImagenNotFoundException extends ParException {
	
	public ParImagenNotFoundException(Integer eventoId) {
		super("Imagen no encontrada para el evento " + eventoId);
	}
}
