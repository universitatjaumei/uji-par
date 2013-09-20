package es.uji.apps.par.sync.main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import es.uji.apps.par.sync.EventosSyncService;

@Service
public class Main
{
    private static final int SYNC_SLEEP = 10 * 60 * 1000;

    public static Logger log = Logger.getLogger(Main.class);

    @Autowired
    EventosSyncService syncService;

    private void sync() throws MalformedURLException, JAXBException, IOException
    {
        URL rssCaUrl = new URL("http://ujiapps.uji.es/cultura/paranimf/programacio/&idioma=ca&formato=rss");
        syncService.sync(rssCaUrl.openStream());

        URL rssEsUrl = new URL("http://ujiapps.uji.es/cultura/paranimf/programacio/&idioma=es&formato=rss");
        syncService.sync(rssEsUrl.openStream());
    }

    public static void main(String[] args) throws MalformedURLException, JAXBException, IOException
    {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext-db.xml");

        final Main main = ctx.getBean(Main.class);

        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    try
                    {
                        main.sync();
                    }
                    catch (Exception e)
                    {
                        log.error(e);
                    }

                    try
                    {
                        Thread.sleep(SYNC_SLEEP);
                    }
                    catch (InterruptedException e)
                    {
                    }
                }
            }
        };

        runnable.run();
    }
}
