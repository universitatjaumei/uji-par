package es.uji.apps.par.ficheros.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.dao.CinesDAO;
import es.uji.apps.par.dao.EventosDAO;
import es.uji.apps.par.dao.PlantillasDAO;
import es.uji.apps.par.dao.SalasDAO;
import es.uji.apps.par.dao.SesionesDAO;
import es.uji.apps.par.dao.TiposEventosDAO;
import es.uji.apps.par.ficheros.registros.RegistroBuzon;
import es.uji.apps.par.ficheros.registros.RegistroSala;
import es.uji.apps.par.model.Cine;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.Plantilla;
import es.uji.apps.par.model.Sala;
import es.uji.apps.par.model.Sesion;
import es.uji.apps.par.model.TipoEvento;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-db-test.xml" })
public class FicherosServiceTest
{
    @Autowired
    private FicherosService service;

    @Autowired
    private CinesDAO cinesDao;

    @Autowired
    private SalasDAO salasDao;

    @Autowired
    private EventosDAO eventosDAO;

    @Autowired
    private TiposEventosDAO tiposEventoDAO;

    @Autowired
    private SesionesDAO sesionesDAO;

    @Autowired
    private PlantillasDAO plantillasDAO;

    private TipoEvento tipoEvento;

    private Sala sala;

    private Plantilla plantilla;

    private Evento evento;

    @Before
    public void setup()
    {
        creaCine();
        sala = creaSala("567", "Sala 1");
        tipoEvento = creaTipoEvento();
        plantilla = creaPlantilla();
        evento = creaEvento(tipoEvento);
    }

    @Test
    @Transactional
    public void testGeneraRegistroBuzon() throws InterruptedException
    {
        Sesion sesion = creaSesion(sala, evento, plantilla);

        Date fechaEnvio = new Date();

        RegistroBuzon registro = service.generaRegistroBuzon(fechaEnvio, "FL", Arrays.asList(sesion));

        Assert.assertEquals("123", registro.getCodigo());
        Assert.assertEquals(fechaEnvio, registro.getFechaEnvio());
        Assert.assertEquals("FL", registro.getTipo());
        Assert.assertEquals(null, registro.getFechaEnvioHabitualAnterior());

        Assert.assertEquals(1, registro.getSesiones());
        Assert.assertEquals(0, registro.getEspectadores());
        Assert.assertEquals(0.0, registro.getRecaudacion().doubleValue(), 0.00001);

        // TODO: Falta comprobar el número de líneas
        //Assert.assertEquals(1 + 1 + 1 + 1 + , registro.getLineas());
    }

    @Test
    @Transactional
    public void testGeneraRegistroSalasUnaSala() throws InterruptedException
    {
        Sesion sesion = creaSesion(sala, evento, plantilla);

        List<RegistroSala> registros = service.generaRegistrosSala(Arrays.asList(sesion));

        Assert.assertEquals(1, registros.size());
        Assert.assertEquals("567", registros.get(0).getCodigo());
        Assert.assertEquals("Sala 1", registros.get(0).getNombre());
    }
    

    @Test
    @Transactional
    public void testGeneraRegistroSalasVariasSalas() throws InterruptedException
    {
        Sala sala2 = creaSala("678", "Sala 2");
        Sesion sesion1 = creaSesion(sala, evento, plantilla);
        Sesion sesion2 = creaSesion(sala2, evento, plantilla);

        List<RegistroSala> registros = service.generaRegistrosSala(Arrays.asList(sesion1, sesion2));

        Assert.assertEquals(2, registros.size());
        Assert.assertEquals("567", registros.get(0).getCodigo());
        Assert.assertEquals("Sala 1", registros.get(0).getNombre());
        Assert.assertEquals("678", registros.get(1).getCodigo());
        Assert.assertEquals("Sala 2", registros.get(1).getNombre());
    }

    private Sesion creaSesion(Sala sala, Evento evento, Plantilla plantilla)
    {
        Sesion sesion = new Sesion();
        sesion.setFechaCelebracion("11/12/2013");
        sesion.setEvento(evento);
        sesion.setSala(sala);
        sesion.setPlantillaPrecios(plantilla);

        sesionesDAO.addSesion(sesion);
        return sesion;
    }

    private Plantilla creaPlantilla()
    {
        Plantilla plantilla = new Plantilla();
        plantillasDAO.add(plantilla);
        return plantilla;
    }

    private Evento creaEvento(TipoEvento tipoEvento)
    {
        Evento evento = new Evento();
        evento.setTipoEvento(tipoEvento.getId());
        eventosDAO.addEvento(evento);
        return evento;
    }

    private TipoEvento creaTipoEvento()
    {
        TipoEvento tipoEvento = new TipoEvento();
        tipoEvento.setNombreEs("Cine");
        tipoEvento.setNombreVa("Cinema");
        tiposEventoDAO.addTipoEvento(tipoEvento);
        return tipoEvento;
    }

    private void creaCine()
    {
        Cine cine = new Cine();
        cine.setCodigo("123");
        cinesDao.addCine(cine);
    }

    private Sala creaSala(String codigo, String nombre)
    {
        Sala sala = new Sala();
        sala.setCodigo(codigo);
        sala.setNombre(nombre);
        salasDao.addSala(sala);
        return sala;
    }
}
