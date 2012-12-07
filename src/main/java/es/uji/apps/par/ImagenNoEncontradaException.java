package es.uji.apps.par;

@SuppressWarnings("serial")
public class ImagenNoEncontradaException extends GeneralPARException {
	
	public ImagenNoEncontradaException(Integer eventoId) {
		super("Imagen no encontrada para el evento " + eventoId);
	}
}
