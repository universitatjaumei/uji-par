package es.uji.apps.par.services;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import es.uji.apps.par.config.Configuration;
import es.uji.apps.par.dao.MailDAO;
import es.uji.apps.par.db.MailDTO;

@Service
public class MailService
{
    public static Logger log = Logger.getLogger(MailService.class);
    
    @Autowired
    MailDAO mailDao;

    public void anyadeEnvio(String to, String titulo, String texto)
    {
        mailDao.insertaMail(Configuration.getMailFrom(), to, titulo, texto);
    }

    private void enviaSmtp(String de, String para, String titulo, String texto)
    {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", Configuration.getMailHost());

        Session session = Session.getDefaultInstance(properties);

        try
        {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(de));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(para));

            message.setSubject(titulo);
            message.setText(texto);

            log.info(String.format("Enviando mail a %s ...", para));

            Transport.send(message);

            log.info(String.format("Mail enviado a %s ...", para));
        }
        catch (MessagingException e)
        {
            String msj = String.format("Error enviando mail: to=%s, from=%s, título=%s, texto=%s", para, de, titulo,
                    texto);
            log.error(msj, e);
        }
    }

    public synchronized void enviaPendientes()
    {
        log.info("Enviando mails pendientes...");

        List<MailDTO> mails = mailDao.getMailsPendientes();

        for (MailDTO mail : mails)
        {
            enviaSmtp(mail.getDe(), mail.getPara(), mail.getTitulo(), mail.getTexto());
            mailDao.marcaEnviado(mail.getId());
        }
    }

    public static void main(String[] args)
    {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext-db.xml");

        MailService mail = ctx.getBean(MailService.class);

        mail.enviaPendientes();
    }
}
