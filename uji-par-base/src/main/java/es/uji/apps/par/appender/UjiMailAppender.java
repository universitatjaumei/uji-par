package es.uji.apps.par.appender;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.net.SMTPAppender;
import org.apache.log4j.spi.LoggingEvent;

import com.sun.mail.smtp.SMTPTransport;

import es.uji.apps.par.config.Configuration;
import es.uji.commons.messaging.client.MessagingClient;
import es.uji.commons.messaging.client.model.MailMessage;

public class UjiMailAppender extends SMTPAppender
{
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
            mensaje.addToRecipient(getTo());
            mensaje.setContent(body);

            client.send(mensaje);
        }
        catch (Exception e)
        {
            LogLog.error("Error occured while sending e-mail notification.", e);
        }
    }

    private String getMailContent()
    {
        StringBuffer sbuf = new StringBuffer();
        String t = layout.getHeader();

        if (t != null)
            sbuf.append(t);

        int len = cb.length();

        for (int i = 0; i < len; i++)
        {
            LoggingEvent event = cb.get();
            sbuf.append(layout.format(event));
            if (layout.ignoresThrowable())
            {
                String[] s = event.getThrowableStrRep();
                if (s != null)
                {
                    for (int j = 0; j < s.length; j++)
                    {
                        sbuf.append(s[j]);
                        sbuf.append(Layout.LINE_SEP);
                    }
                }
            }
        }
        t = layout.getFooter();

        if (t != null)
            sbuf.append(t);

        return sbuf.toString();
    }

    /**
     * Pulled email send stuff i.e. Transport.send()/Transport.sendMessage(). So
     * that on required this logic can be enhanced.
     * 
     * @param msg
     *            Email Message
     * @throws MessagingException
     */
    protected void send(Message msg) throws MessagingException
    {
        SMTPTransport t = (SMTPTransport) session.getTransport("smtps");
        try
        {
            t.connect(getSMTPHost(), getSMTPUsername(), getSMTPPassword());
            t.sendMessage(msg, msg.getAllRecipients());
        }
        finally
        {
            System.out.println("Response: " + t.getLastServerResponse());
            t.close();
        }
    }
}