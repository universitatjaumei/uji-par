package es.uji.apps.par.services;

import es.uji.apps.par.dao.MailDAO;
import es.uji.commons.messaging.client.MessageNotSentException;


public interface MailInterface {
	public void enviaPendientes(MailDAO mailDAO) throws MessageNotSentException;
	public void anyadeEnvio(String to, String titulo, String texto);
}
