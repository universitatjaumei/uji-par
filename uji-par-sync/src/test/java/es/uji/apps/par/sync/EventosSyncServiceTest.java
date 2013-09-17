package es.uji.apps.par.sync;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.dao.EventosDAO;
import es.uji.apps.par.dao.TiposEventosDAO;
import es.uji.apps.par.db.EventoDTO;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.TipoEvento;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager")
@ContextConfiguration(locations = { "/applicationContext-db-test.xml" })
public class EventosSyncServiceTest extends SyncBaseTest
{
    private static final String RSS_CA = "rss-ca.xml";
    private static final String RSS_ES = "rss-es.xml";

    @Autowired
    EventosSyncService syncService;

    @Autowired
    EventosDAO eventosDAO;

    @Autowired
    TiposEventosDAO tiposEventosDAO;

    @Before
    public void setup()
    {
        insertaTiposEventos();
    }

    private void insertaTiposEventos()
    {
        TipoEvento tipo = new TipoEvento();
        tipo.setNombreVa("Teatre");
        tipo.setNombreEs("Teatro");

        tiposEventosDAO.addTipoEvento(tipo);
    }

    @Test
    @Transactional
    public void testSyncNuevosItems() throws JAXBException, MalformedURLException, IOException
    {
        syncService.sync(loadFromClasspath(RSS_CA));

        List<Evento> eventos = eventosDAO.getEventosConSesiones();

        assertEquals("Número de eventos nuevos", 2, eventos.size());
    }

    @Test
    @Transactional
    public void testSyncNuevoItemDatos() throws JAXBException, MalformedURLException, IOException
    {
        syncService.sync(loadFromClasspath(RSS_CA));

        EventoDTO evento = eventosDAO.getEventoByRssId("1");

        assertNotNull("Evento nuevo insertado", evento);

        assertEquals("rssId del evento", "1", evento.getRssId());
        assertEquals("Título VA del evento", "Madre Coraje", evento.getTituloVa());
        assertTrue(
                "Descripción VA del evento",
                evento.getDescripcionVa().startsWith(
                        "<p>\r\n\t<strong>Adaptaci&oacute; i direcci&oacute;</strong>: Ricardo Iniesta"));
        
        assertEquals("Características VA del evento", "Basada en la Historia de la vida de la estafadora y aventurera Coraje de Grimmelshausen.", evento.getCaracteristicasVa());
        assertEquals("Duración VA del evento", "120", evento.getDuracionVa());
        assertEquals("Tipo del evento", "Teatre", evento.getParTiposEvento().getNombreVa());

        assertEquals("Asientos numerados del evento", BigDecimal.ONE.intValue(), evento.getAsientosNumerados()
                .intValue());
        assertNotNull("Tipo del evento no nulo", evento.getParTiposEvento());
        assertEquals("Imagen del evento", 18946, evento.getImagen().length);
    }

    @Test
    @Transactional
    public void testSyncNuevoItemDatosIdiomaCa() throws JAXBException, MalformedURLException, IOException
    {
        syncService.sync(loadFromClasspath(RSS_CA));

        EventoDTO evento = eventosDAO.getEventoByRssId("1");

        assertEquals("Título VA del evento", "Madre Coraje", evento.getTituloVa());
        assertTrue(
                "Descripción VA del evento",
                evento.getDescripcionVa().startsWith(
                        "<p>\r\n\t<strong>Adaptaci&oacute; i direcci&oacute;</strong>: Ricardo Iniesta"));
        
        assertEquals("Características VA del evento", "Basada en la Historia de la vida de la estafadora y aventurera Coraje de Grimmelshausen.", evento.getCaracteristicasVa());
        
        assertEquals("Duración VA del evento", "120", evento.getDuracionVa());
        assertEquals("Tipo del evento", "Teatre", evento.getParTiposEvento().getNombreVa());
    }

    @Test
    @Transactional
    public void testSyncNuevoItemDatosIdiomaEs() throws JAXBException, MalformedURLException, IOException
    {
        syncService.sync(loadFromClasspath(RSS_ES));

        EventoDTO evento = eventosDAO.getEventoByRssId("1");

        assertEquals("Título ES del evento", "Madre Coraje (ES)", evento.getTituloEs());
        assertTrue(
                "Descripción ES del evento",
                evento.getDescripcionEs().startsWith(
                        "(ES)<p>\r\n\t<strong>Adaptaci&oacute; i direcci&oacute;</strong>: Ricardo Iniesta"));
        assertEquals("Duración ES del evento", "120 (ES)", evento.getDuracionEs());
        assertEquals("Tipo del evento", "Teatro", evento.getParTiposEvento().getNombreEs());
        assertEquals("Características del evento",
                "Basada en la Historia de la vida de la estafadora y aventurera Coraje de Grimmelshausen. (ES)",
                evento.getCaracteristicasEs());
    }

    @Test
    @Transactional
    // Comprobar que al ir a guardar el campo título del otro idioma no es null ni cadena vacía (petaría la constraint de Oracle)
    public void testSyncNuevoItemOtroIdiomaOk() throws JAXBException, MalformedURLException, IOException
    {
        syncService.sync(loadFromClasspath(RSS_CA));

        EventoDTO evento = eventosDAO.getEventoByRssId("1");

        assertNotNull("Título del otro idioma no nulo", evento.getTituloEs());
        assertTrue("Título del otro idioma distinto de \"\"", !evento.getTituloEs().equals(""));
    }

    @Test
    @Transactional
    public void testSyncYaExistente() throws JAXBException, MalformedURLException, IOException
    {
        EventoDTO eventoDTO = new EventoDTO();
        eventoDTO.setRssId("1");
        eventosDAO.updateEventoDTO(eventoDTO);

        syncService.sync(loadFromClasspath(RSS_CA));

        EventoDTO evento = eventosDAO.getEventoByRssId("1");

        assertEquals("Título VA de item existente", "Madre Coraje", evento.getTituloVa());
        assertEquals("Tipo del evento", "Teatre", evento.getParTiposEvento().getNombreVa());
    }
    
    @Test
    @Transactional
    public void testSyncYaExistenteNoModificaOtroIdioma() throws JAXBException, MalformedURLException, IOException
    {
        EventoDTO eventoDTO = new EventoDTO();
        eventoDTO.setRssId("1");
        eventoDTO.setTituloEs("Título ES");
        eventoDTO.setCaracteristicasEs("Características ES");
        eventoDTO.setDescripcionEs("Descripción ES");
        eventoDTO.setDuracionEs("Duración ES");
        eventosDAO.updateEventoDTO(eventoDTO);

        syncService.sync(loadFromClasspath(RSS_CA));

        EventoDTO evento = eventosDAO.getEventoByRssId("1");

        assertEquals("Título ES de item existente", "Título ES", evento.getTituloEs());
        assertEquals("Características ES de item existente", "Características ES", evento.getCaracteristicasEs());
        assertEquals("Descripción ES de item existente", "Descripción ES", evento.getDescripcionEs());
        assertEquals("Duración ES de item existente", "Duración ES", evento.getDuracionEs());
    }
}
