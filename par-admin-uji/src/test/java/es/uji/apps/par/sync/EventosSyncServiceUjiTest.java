package es.uji.apps.par.sync;

import es.uji.apps.par.dao.*;
import es.uji.apps.par.model.Cine;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.Sala;
import es.uji.apps.par.model.TipoEvento;
import es.uji.apps.par.services.EventosSyncService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager")
@ContextConfiguration(locations = {
        "/applicationContext-db-test.xml",
        "/applicationContext-uji-test.xml",
})
public class EventosSyncServiceUjiTest extends SyncBaseTest
{
    private static final String RSS_TIPO_GRADUACION = "uji/rss-graduacion.xml";

    @Autowired
    EventosSyncService syncService;

    @Autowired
    EventosDAO eventosDAO;

    @Autowired
    TiposEventosDAO tiposEventosDAO;

    @Autowired
    TpvsDAO tpvsDAO;

    @Autowired
    private SalasDAO salasDAO;

    @Autowired
    private CinesDAO cinesDAO;

    @Before
    public void setup()
    {
        syncService.setTipo("uji");
        insertaTpvDefault();
        insertaSala();
        insertaTiposEventos();
    }

    private void insertaSala()
    {
        Cine cine = cinesDAO.addCine(new Cine("CODIGO", "nombre", "cif", "direccion", "codigoMunicipio", "nombreMunicipio", "cp", "empresa", "codigoRegistro", "telefono", BigDecimal.ONE));
        salasDAO.addSala(new Sala("SALA", "SALA", 10, 0, 10, "Cine", "Formato", "", cine));
    }

    private void insertaTpvDefault()
    {
        tpvsDAO.addTpvDefault();
    }

    private void insertaTiposEventos()
    {
        TipoEvento tipo = new TipoEvento();
        tipo.setNombreVa("Teatre");
        tipo.setNombreEs("Teatro");

        tiposEventosDAO.addTipoEvento(tipo);

        tipo = new TipoEvento();
        tipo.setNombreVa("Cinema");
        tipo.setNombreEs("Cine");

        tiposEventosDAO.addTipoEvento(tipo);
    }

    @Test
    @Transactional
    @Ignore
    public void testSyncNuevosItemsGraduacion() throws Exception
    {
        syncService.sync(loadFromClasspath(RSS_TIPO_GRADUACION));

        List<Evento> eventos = eventosDAO.getEventosConSesiones();

        assertEquals("Número de eventos nuevos", 2, eventos.size());
    }
}
