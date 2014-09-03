package es.uji.apps.par.appender;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.net.SMTPAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.helpers.CyclicBuffer;

import com.sun.mail.smtp.SMTPTransport;

import es.uji.apps.par.config.Configuration;
import es.uji.commons.messaging.client.MessagingClient;
import es.uji.commons.messaging.client.model.MailMessage;

public class UjiMailAppender extends SMTPAppender
{
	private static final Logger log = LoggerFactory.getLogger(UjiMailAppender.class);
    protected Session session;

    private MessagingClient client;

    public UjiMailAppender()
    {
        super();

        client = new MessagingClient();
    }

    protected void sendBuffer()
    {
        if (!Configuration.getEnviarMailsError().equals("true"))
        {
            return;
        }

        try
        {
            String body = getMailContent();

            MailMessage mensaje = new MailMessage("PAR");
            mensaje.setTitle(getSubject() + " (" + Configuration.getEntorno() + ")");
            mensaje.setContentType(MediaType.TEXT_PLAIN);
            mensaje.setSender(getFrom());
            String mails = "";
            for (String to: getToAsListOfString()) {
            	mails += (mails.equals(""))?to:","+to;
            }
            mensaje.addToRecipient(mails);
            mensaje.setContent(body);

            client.send(mensaje);
        }
        catch (Exception e)
        {
            log.error("Error occured while sending e-mail notification.", e);
        }
    }

    private String getMailContent()
    {
        StringBuffer sbuf = new StringBuffer();
        String t = layout.getFileHeader();

        if (t != null)
            sbuf.append(t);

        //int len = getCyclicBufferTracker().getBufferSize();

        for (CyclicBuffer<ILoggingEvent> mm: getCyclicBufferTracker().allComponents())
        //for (int i = 0; i < len; i++)
        {
            ILoggingEvent event = mm.get();
            sbuf.append(layout.doLayout(event));
            /*if (layout.ignoresThrowable())
            {
                StackTraceElementProxy[] s = event.getThrowableProxy().getStackTraceElementProxyArray();
                if (s != null)
                {
                    for (int j = 0; j < s.length; j++)
                    {
                        sbuf.append(s[j]);
                        sbuf.append(CoreConstants.LINE_SEPARATOR);
                    }
                }
            }*/
        }
        t = layout.getFileFooter();

        if (t != null)
            sbuf.append(t);

        return sbuf.toString();
    }

    protected void send(Message msg) throws MessagingException
    {
        SMTPTransport t = (SMTPTransport) session.getTransport("smtps");
        try
        {
            t.connect(getSMTPHost(), getUsername(), getPassword());
            t.sendMessage(msg, msg.getAllRecipients());
        }
        finally
        {
            System.out.println("Response: " + t.getLastServerResponse());
            t.close();
        }
    }
}