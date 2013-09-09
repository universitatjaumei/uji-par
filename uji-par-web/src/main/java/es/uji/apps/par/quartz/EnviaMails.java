package es.uji.apps.par.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.services.MailService;
import es.uji.commons.messaging.client.MessageNotSentException;

@Service
public class EnviaMails
{
    @Autowired
    MailService mailService;

    public void ejecuta() throws MessageNotSentException
    {
        mailService.enviaPendientes();
    }
}
