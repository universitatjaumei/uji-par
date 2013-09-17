package es.uji.apps.par.sync;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.dao.EventosDAO;
import es.uji.apps.par.dao.TiposEventosDAO;
import es.uji.apps.par.db.EventoDTO;
import es.uji.apps.par.db.TipoEventoDTO;
import es.uji.apps.par.sync.parse.RssParser;
import es.uji.apps.par.sync.rss.jaxb.Item;
import es.uji.apps.par.sync.rss.jaxb.Rss;
import es.uji.apps.par.utils.Utils;

@Service
public class EventosSyncService
{
    @Autowired
    EventosDAO eventosDAO;

    @Autowired
    TiposEventosDAO tipoEventosDAO;

    @Autowired
    RssParser rssParser;

    @Transactional
    public void sync(InputStream rssInputStream) throws JAXBException, MalformedURLException, IOException
    {
        Rss rss = rssParser.parse(rssInputStream);

        for (Item item : rss.getChannel().getItems())
        {
            if (item.getEsquema() != null && item.getEsquema().equals("paranimf"))
            {
                EventoDTO evento = eventosDAO.getEventoByRssId(item.getContenidoId());

                if (evento == null)
                {
                    evento = new EventoDTO();
                    evento.setRssId(item.getContenidoId());
                }

                updateDatosEvento(item, evento);

                eventosDAO.updateEventoDTO(evento);
            }
            
            //throw new RuntimeException("Corta");
        }
    }

    private void updateDatosEvento(Item item, EventoDTO evento) throws MalformedURLException, IOException
    {
        if (item.getSeientsNumerats() != null)
        {
            evento.setAsientosNumerados(item.getSeientsNumerats().equals("si") ? BigDecimal.ONE : BigDecimal.ZERO);
        }

        String urlImagen = item.getEnclosures().get(0).getUrl();
        evento.setImagen(getImageFromUrl(urlImagen));
        evento.setImagenSrc(urlImagen);
        
        evento.setImagenContentType(item.getEnclosures().get(0).getType());

        if (item.getIdioma().equals("ca"))
        {
            evento.setTituloVa(item.getTitle());
            evento.setCaracteristicasVa(item.getResumen());
            evento.setDuracionVa(item.getDuracio());
            evento.setDescripcionVa(item.getContenido());

            if (item.getTipo() != null)
            {
                String tipo = Utils.toUppercaseFirst(item.getTipo());

                TipoEventoDTO tipoEvento = tipoEventosDAO.getTipoEventoByNombreVa(tipo);
                evento.setParTiposEvento(tipoEvento);
            }
        }
        else if (item.getIdioma().equals("es"))
        {
            evento.setTituloEs(item.getTitle());
            evento.setCaracteristicasEs(item.getResumen());
            evento.setDuracionEs(item.getDuracio());
            evento.setDescripcionEs(item.getContenido());

            if (item.getTipo() != null)
            {
                String tipo = Utils.toUppercaseFirst(item.getTipo());

                TipoEventoDTO tipoEvento = tipoEventosDAO.getTipoEventoByNombreEs(tipo);
                evento.setParTiposEvento(tipoEvento);
            }
        }

        // Para que no de error en BD
        if (evento.getTituloEs() == null)
        {
            evento.setTituloEs(evento.getTituloVa());
        }

        // Para que no de error en BD
        if (evento.getTituloVa() == null)
        {
            evento.setTituloVa(evento.getTituloEs());
        }
    }

    private byte[] getImageFromUrl(String url) throws MalformedURLException, IOException
    {
        InputStream urlInputStream = new URL(url).openStream();

        return IOUtils.toByteArray(urlInputStream);
    }
}

