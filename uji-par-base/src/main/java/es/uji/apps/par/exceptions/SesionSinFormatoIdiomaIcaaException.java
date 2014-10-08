package es.uji.apps.par.exceptions;

@SuppressWarnings("serial")
public class SesionSinFormatoIdiomaIcaaException extends GeneralPARException {

	public SesionSinFormatoIdiomaIcaaException(long eventoId, String formato, String versionLinguistica) {
		super(EVENTO_SIN_SESIONES_CODE, EVENTO_SIN_SESIONES + String.format("eventoId = %d, formato=%s, versionLinguistica=%s, numero=%s", eventoId,
                formato, versionLinguistica));
	}
	
	public SesionSinFormatoIdiomaIcaaException(String titulo) {
		super(EVENTO_SIN_FORMATO_CODE, EVENTO_SIN_FORMATO + String.format(titulo));
	}
}
