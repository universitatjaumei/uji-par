package es.uji.apps.par;

import es.uji.apps.par.exceptions.GeneralPARException;

@SuppressWarnings("serial")
public class EventoConCompras extends GeneralPARException {

	public EventoConCompras(long idEvento) {
		super("No es permet canviar si la sessió és numerada o no, quan un event ja té compres realitzades");
	}

}
