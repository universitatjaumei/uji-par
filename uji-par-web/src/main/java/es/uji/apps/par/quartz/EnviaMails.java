package es.uji.apps.par.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.config.Configuration;
import es.uji.apps.par.dao.MailDAO;
import es.uji.apps.par.services.EntradasService;
import es.uji.apps.par.services.MailInterface;
import es.uji.commons.messaging.client.MessageNotSentException;

@Service
public class EnviaMails
{
	@Autowired
	MailDAO mailDAO;
	
	@Autowired
	EntradasService entradasService;
	
	private static final Logger log = LoggerFactory.getLogger(EnviaMails.class);
	
	public static MailInterface newInstanceMailSender()
    {
		log.info("Inicializamos la clase de envio de mails: " + Configuration.getMailingClass());
    	try {
    		return (MailInterface) Class.forName(Configuration.getMailingClass()).newInstance();
    	} catch(Exception e) {
    		throw new RuntimeException("Imposible instanciar la clase de envio de mails: " + Configuration.getMailingClass());
    	}
    }
	
    public void ejecuta() throws MessageNotSentException
    {
    	log.info("Inicializamos envío de mails");
    	MailInterface mailService = EnviaMails.newInstanceMailSender();
        mailService.enviaPendientes(mailDAO, entradasService);
    }
}
