package es.uji.apps.par.exceptions;


@SuppressWarnings("serial")
public class EventoConCompras extends GeneralPARException {

	public EventoConCompras(long idEvento) {
		super(SESION_CON_COMPRAS_CODE);
	}

}
