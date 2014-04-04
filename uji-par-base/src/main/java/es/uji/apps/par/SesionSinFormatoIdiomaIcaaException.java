package es.uji.apps.par;

@SuppressWarnings("serial")
public class SesionSinFormatoIdiomaIcaaException extends GeneralPARException {

	public SesionSinFormatoIdiomaIcaaException(long eventoId, String formato, String versionLinguistica) {
		super(String.format("Evento sin sesiones para estos datos: eventoId = %d, formato=%s, versionLinguistica=%s, numero=%s", eventoId,
                formato, versionLinguistica));
	}
}
