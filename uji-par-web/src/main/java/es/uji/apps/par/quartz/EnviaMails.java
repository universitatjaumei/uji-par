package es.uji.apps.par.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.services.MailService;

@Service
public class EnviaMails
{
    @Autowired
    MailService mailService;

    public void ejecuta()
    {
        mailService.enviaPendientes();
    }
}
