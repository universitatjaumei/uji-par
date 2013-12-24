package es.uji.apps.par.sync;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
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
import es.uji.apps.par.dao.PlantillasDAO;
import es.uji.apps.par.dao.SesionesDAO;
import es.uji.apps.par.dao.TiposEventosDAO;
import es.uji.apps.par.db.EventoDTO;
import es.uji.apps.par.db.SesionDTO;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.Plantilla;
import es.uji.apps.par.model.TipoEvento;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager")
@ContextConfiguration(locations = { "/applicationContext-db-test.xml" })
public class EventosSyncServiceBenicassimTest extends SyncBaseTest
{
    private static final String RSS_CA = "benicassim/rss-ca.xml";
    private static final String RSS_ES = "benicassim/rss-es.xml";

    @Autowired
    EventosSyncService syncService;

    @Autowired
    EventosDAO eventosDAO;

    @Autowired
    SesionesDAO sesionesDao;

    @Autowired
    TiposEventosDAO tiposEventosDAO;

    @Autowired
    PlantillasDAO plantillasDao;

    @Before
    public void setup()
    {
        syncService.setTipo("benicassim");
        insertaPlantillaPrecios();
        insertaTiposEventos();
    }

    private void insertaPlantillaPrecios()
    {
        Plantilla plantilla = new Plantilla("test");
        plantillasDao.add(plantilla);
    }

    private void insertaTiposEventos()
    {
        insertaTipo("Teatro", "Teatre");
        insertaTipo("Cine", "Cinema");
        insertaTipo("Libro y lectura", "Llibre i lectura");
        insertaTipo("Música", "Música");

        insertaTipo("Actividades infantiles", "Activitats infantils");
        insertaTipo("Exposiciones", "Exposicions");
        insertaTipo("Teatro infantil", "Teatre infantil");
    }

    private void insertaTipo(String nombreEs, String nombreVa)
    {
        TipoEvento tipo = new TipoEvento();
        tipo.setNombreEs(nombreEs);
        tipo.setNombreVa(nombreVa);
        tiposEventosDAO.addTipoEvento(tipo);
    }

    @Test
    @Transactional
    public void testSyncNuevosItems() throws JAXBException, MalformedURLException, IOException
    {
        syncService.sync(loadFromClasspath(RSS_CA));

        List<Evento> eventos = eventosDAO.getEventosConSesiones();

        assertEquals("Número de eventos nuevos", 16, eventos.size());
    }

    @Test
    @Transactional
    public void testSyncNuevoItemDatos() throws JAXBException, MalformedURLException, IOException
    {
        syncService.sync(loadFromClasspath(RSS_CA));

        EventoDTO evento = eventosDAO.getEventoByRssId("839");

        assertNotNull("Evento nuevo insertado", evento);

        assertEquals("rssId del evento", "839", evento.getRssId());
        assertEquals("Título VA del evento", "Nova reunió del club de lectura", evento.getTituloVa());
        assertTrue("Descripción VA del evento",
                evento.getDescripcionVa().startsWith("<p>\"Nada\" de Carmen Laforet a debat en el club de lectura"));

        assertNotNull("Tipo del evento no nulo", evento.getParTiposEvento());
        assertEquals("Tipo del evento", "Llibre i lectura", evento.getParTiposEvento().getNombreVa());

        //assertEquals("Asientos numerados del evento", true, evento.getAsientosNumerados());

        assertEquals("URL imagen del evento",
                "http://www.benicassimcultura.es/ficheros_sw/adjuntos/52a5e39eb363b_laforet.jpg", evento.getImagenSrc());
        assertEquals("Contenido imagen del evento", 81057, evento.getImagen().length);
        assertEquals("Content-type imagen del evento", "image/jpg", evento.getImagenContentType());
    }

    @Test
    @Transactional
    public void testSyncSesiones() throws JAXBException, MalformedURLException, IOException
    {
        syncService.sync(loadFromClasspath(RSS_CA));

        EventoDTO evento = eventosDAO.getEventoByRssId("789");

        List<SesionDTO> sesiones = sesionesDao.getSesiones(evento.getId(), "", 0, 100);

        assertEquals("Número sesiones", 3, sesiones.size());
    }

    @Test
    @Transactional
    public void testSyncNuevoItemDatosIdiomaCa() throws JAXBException, MalformedURLException, IOException
    {
        syncService.sync(loadFromClasspath(RSS_CA));

        EventoDTO evento = eventosDAO.getEventoByRssId("839");

        assertEquals("Título VA del evento", "Nova reunió del club de lectura", evento.getTituloVa());
        assertTrue("Descripción VA del evento",
                evento.getDescripcionVa().startsWith("<p>\"Nada\" de Carmen Laforet a debat en el club de lectura"));

        assertEquals("Tipo del evento", "Llibre i lectura", evento.getParTiposEvento().getNombreVa());
    }

    @Test
    @Transactional
    public void testSyncNuevoItemDatosIdiomaEs() throws JAXBException, MalformedURLException, IOException
    {
        syncService.sync(loadFromClasspath(RSS_ES));

        EventoDTO evento = eventosDAO.getEventoByRssId("839");

        assertEquals("Título ES del evento", "Nueva reunión del club de lectura", evento.getTituloEs());
        assertTrue("Descripción ES del evento",
                evento.getDescripcionEs().startsWith("<p>\"Nada\" de Carmen Laforet a debate en el club de lectura"));

        assertEquals("Tipo del evento", "Libro y lectura", evento.getParTiposEvento().getNombreEs());
    }

    @Test
    @Transactional
    // Comprobar que al ir a guardar el campo título del otro idioma no es null ni cadena vacía (petaría la constraint de Oracle)
    public void testSyncNuevoItemOtroIdiomaOk() throws JAXBException, MalformedURLException, IOException
    {
        syncService.sync(loadFromClasspath(RSS_CA));

        EventoDTO evento = eventosDAO.getEventoByRssId("839");

        assertNotNull("Título del otro idioma no nulo", evento.getTituloEs());
        assertTrue("Título del otro idioma distinto de \"\"", !evento.getTituloEs().equals(""));
    }

    @Test
    @Transactional
    public void testSyncYaExistente() throws JAXBException, MalformedURLException, IOException
    {
        EventoDTO eventoDTO = new EventoDTO();
        eventoDTO.setRssId("839");
        eventosDAO.updateEventoDTO(eventoDTO);

        syncService.sync(loadFromClasspath(RSS_CA));

        EventoDTO evento = eventosDAO.getEventoByRssId("839");

        assertEquals("Título VA de item existente", "Nova reunió del club de lectura", evento.getTituloVa());
        assertEquals("Tipo del evento", "Llibre i lectura", evento.getParTiposEvento().getNombreVa());
    }

    @Test
    @Transactional
    public void testSyncYaExistenteNoModificaOtroIdioma() throws JAXBException, MalformedURLException, IOException
    {
        EventoDTO eventoDTO = new EventoDTO();
        eventoDTO.setRssId("839");
        eventoDTO.setTituloEs("Título ES");
        eventoDTO.setCaracteristicasEs("Características ES");
        eventoDTO.setDescripcionEs("Descripción ES");
        eventoDTO.setDuracionEs("Duración ES");
        eventosDAO.updateEventoDTO(eventoDTO);

        syncService.sync(loadFromClasspath(RSS_CA));

        EventoDTO evento = eventosDAO.getEventoByRssId("839");

        assertEquals("Título ES de item existente", "Título ES", evento.getTituloEs());
        assertEquals("Características ES de item existente", "Características ES", evento.getCaracteristicasEs());
        assertEquals("Descripción ES de item existente", "Descripción ES", evento.getDescripcionEs());
        assertEquals("Duración ES de item existente", "Duración ES", evento.getDuracionEs());
    }
}
