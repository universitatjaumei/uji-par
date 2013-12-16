package es.uji.apps.par.ficheros.service;

import java.math.BigDecimal;
import java.text.ParseException;
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

import es.uji.apps.par.ButacaOcupadaException;
import es.uji.apps.par.CompraSinButacasException;
import es.uji.apps.par.NoHayButacasLibresException;
import es.uji.apps.par.dao.CinesDAO;
import es.uji.apps.par.dao.EventosDAO;
import es.uji.apps.par.dao.LocalizacionesDAO;
import es.uji.apps.par.dao.PlantillasDAO;
import es.uji.apps.par.dao.PreciosPlantillaDAO;
import es.uji.apps.par.dao.SalasDAO;
import es.uji.apps.par.dao.SesionesDAO;
import es.uji.apps.par.dao.TiposEventosDAO;
import es.uji.apps.par.ficheros.registros.RegistroBuzon;
import es.uji.apps.par.ficheros.registros.RegistroSala;
import es.uji.apps.par.ficheros.registros.RegistroSesion;
import es.uji.apps.par.model.Butaca;
import es.uji.apps.par.model.Cine;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.Localizacion;
import es.uji.apps.par.model.Plantilla;
import es.uji.apps.par.model.PreciosPlantilla;
import es.uji.apps.par.model.PreciosSesion;
import es.uji.apps.par.model.ResultadoCompra;
import es.uji.apps.par.model.Sala;
import es.uji.apps.par.model.Sesion;
import es.uji.apps.par.model.TipoEvento;
import es.uji.apps.par.services.ComprasService;
import es.uji.apps.par.utils.DateUtils;

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

    @Autowired
    private LocalizacionesDAO localizacionesDAO;

    @Autowired
    private PreciosPlantillaDAO preciosPlantillaDAO;

    @Autowired
    private ComprasService comprasService;

    private TipoEvento tipoEvento;
    private Sala sala;
    private Plantilla plantilla;
    private Evento evento;
    private Localizacion localizacion;
    private PreciosPlantilla precioPlantilla;
    private PreciosSesion precioSesion;

    @Before
    public void setup()
    {
        creaCine();
        localizacion = creaLocalizacion("Platea");
        sala = creaSala("567", "Sala 1");
        tipoEvento = creaTipoEvento();
        plantilla = creaPlantilla();
        precioPlantilla = creaPrecioPlantilla(1.10, 0.5, 0.0);
        precioSesion = creaPrecioSesion(precioPlantilla);
        evento = creaEvento(tipoEvento);
    }

    @Test
    @Transactional
    public void testGeneraRegistroBuzonSinEspectadores() throws Exception
    {
        Sesion sesion = creaSesion(sala, evento);

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
    public void testGeneraRegistroBuzonConEspectadores() throws Exception
    {
        Sesion sesion = creaSesion(sala, evento);

        Butaca butaca1 = creaButaca("1", "1", "normal");
        Butaca butaca2 = creaButaca("1", "2", "normal");

        registraCompra(sesion, butaca1, butaca2);

        RegistroBuzon registro = service.generaRegistroBuzon(new Date(), "FL", Arrays.asList(sesion));

        Assert.assertEquals(1, registro.getSesiones());
        Assert.assertEquals(2, registro.getEspectadores());
        Assert.assertEquals(2.20, registro.getRecaudacion().doubleValue(), 0.00001);

        // TODO: Falta comprobar el número de líneas
    }

    @Test
    @Transactional
    public void testGeneraRegistroBuzonConEspectadoresVariasComprasEnVariasSesiones() throws Exception
    {
        Sesion sesion1 = creaSesion(sala, evento);
        Sesion sesion2 = creaSesion(sala, evento);

        Butaca butaca1 = creaButaca("1", "1", "normal");
        Butaca butaca2 = creaButaca("1", "2", "normal");

        registraCompra(sesion1, butaca1, butaca2);

        Butaca butaca3 = creaButaca("1", "4", "descuento");

        registraCompra(sesion2, butaca3);

        RegistroBuzon registro = service.generaRegistroBuzon(new Date(), "FL", Arrays.asList(sesion1, sesion2));

        Assert.assertEquals(2, registro.getSesiones());
        Assert.assertEquals(3, registro.getEspectadores());
        Assert.assertEquals(2.70, registro.getRecaudacion().doubleValue(), 0.00001);

        // TODO: Falta comprobar el número de líneas
    }

    @Test
    @Transactional
    public void testGeneraRegistroSalasUnaSala() throws Exception
    {
        Sesion sesion = creaSesion(sala, evento);

        List<RegistroSala> registros = service.generaRegistrosSala(Arrays.asList(sesion));

        Assert.assertEquals(1, registros.size());
        Assert.assertEquals("567", registros.get(0).getCodigo());
        Assert.assertEquals("Sala 1", registros.get(0).getNombre());
    }

    @Test
    @Transactional
    public void testGeneraRegistroSalasVariasSalas() throws Exception
    {
        Sala sala2 = creaSala("678", "Sala 2");
        Sesion sesion1 = creaSesion(sala, evento);
        Sesion sesion2 = creaSesion(sala2, evento);

        List<RegistroSala> registros = service.generaRegistrosSala(Arrays.asList(sesion1, sesion2));

        Assert.assertEquals(2, registros.size());
        Assert.assertEquals("567", registros.get(0).getCodigo());
        Assert.assertEquals("Sala 1", registros.get(0).getNombre());
        Assert.assertEquals("678", registros.get(1).getCodigo());
        Assert.assertEquals("Sala 2", registros.get(1).getNombre());
    }
    
    @Test
    @Transactional
    public void testGeneraRegistroSesion() throws Exception
    {
        Sesion sesion1 = creaSesion(sala, evento);

        List<RegistroSesion> registros = service.generaRegistrosSesion(Arrays.asList(sesion1));

        Assert.assertEquals(1, registros.size());
        Assert.assertEquals(1, registros.get(0).getPeliculas());
        Assert.assertEquals(DateUtils.spanishStringWithHourstoDate("11/12/2013 22:00"), registros.get(0).getFecha());
        Assert.assertEquals(0, registros.get(0).getEspectadores());
        Assert.assertEquals(0.0, registros.get(0).getRecaudacion().floatValue(), 0.000001);
    }

    @Test
    @Transactional
    public void testGeneraRegistroSesionVariasSesiones() throws Exception
    {
        Sesion sesion1 = creaSesion(sala, evento, "1/2/2013", "16:00");
        Butaca butaca1 = creaButaca("1", "1", "normal");
        Butaca butaca2 = creaButaca("1", "2", "normal");

        registraCompra(sesion1, butaca1, butaca2);
        
        Sesion sesion2 = creaSesion(sala, evento, "3/4/2013", "20:30");
        Butaca butaca3 = creaButaca("1", "3", "descuento");
        Butaca butaca4 = creaButaca("1", "4", "descuento");
        Butaca butaca5 = creaButaca("2", "5", "normal");
        
        registraCompra(sesion2, butaca3, butaca4, butaca5);

        List<RegistroSesion> registros = service.generaRegistrosSesion(Arrays.asList(sesion1, sesion2));

        Assert.assertEquals(2, registros.size());
        
        Assert.assertEquals("567", registros.get(0).getCodigoSala());
        Assert.assertEquals(1, registros.get(0).getPeliculas());
        Assert.assertEquals(DateUtils.spanishStringWithHourstoDate("1/2/2013 16:00"), registros.get(0).getFecha());
        Assert.assertEquals(2, registros.get(0).getEspectadores());
        Assert.assertEquals(2.20, registros.get(0).getRecaudacion().floatValue(), 0.000001);

        Assert.assertEquals("567", registros.get(1).getCodigoSala());
        Assert.assertEquals(DateUtils.spanishStringWithHourstoDate("3/4/2013 20:30"), registros.get(1).getFecha());
        Assert.assertEquals(1, registros.get(1).getPeliculas());
        Assert.assertEquals(3, registros.get(1).getEspectadores());
        Assert.assertEquals(2.10, registros.get(1).getRecaudacion().floatValue(), 0.000001);
        
        //TODO: Falta incidencia por sesión
    }

    private void registraCompra(Sesion sesion1, Butaca... butacas) throws NoHayButacasLibresException,
            ButacaOcupadaException, CompraSinButacasException
    {
        ResultadoCompra resultado1 = comprasService.registraCompraTaquilla(sesion1.getId(), Arrays.asList(butacas));
        comprasService.marcaPagada(resultado1.getId());
    }

    private Sesion creaSesion(Sala sala, Evento evento, String fecha, String hora) throws ParseException
    {
        Sesion sesion = new Sesion();
        sesion.setFechaCelebracionWithDate(DateUtils.spanishStringWithHourstoDate(fecha + " " + hora));
        sesion.setFechaInicioVentaOnline("1/12/2011");
        sesion.setFechaFinVentaOnline("11/12/2012");
        sesion.setEvento(evento);
        sesion.setSala(sala);
        sesion.setPlantillaPrecios(plantilla);
        sesion.setPreciosSesion(Arrays.asList(precioSesion));

        sesionesDAO.addSesion(sesion);
        return sesion;
    }

    private Sesion creaSesion(Sala sala, Evento evento) throws ParseException
    {
        return creaSesion(sala, evento, "11/12/2013", "22:00");
    }

    private PreciosSesion creaPrecioSesion(PreciosPlantilla precioPlantilla)
    {
        PreciosSesion precioSesion = new PreciosSesion(precioPlantilla);

        sesionesDAO.addPrecioSesion(PreciosSesion.precioSesionToPrecioSesionDTO(precioSesion));

        return precioSesion;
    }

    private Plantilla creaPlantilla()
    {
        Plantilla plantilla = new Plantilla("test");
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

    private Localizacion creaLocalizacion(String nombre)
    {
        Localizacion localizacion = new Localizacion(nombre);
        localizacion.setCodigo(nombre);

        localizacionesDAO.add(localizacion);

        return localizacion;
    }

    private Butaca creaButaca(String fila, String numero, String tipo)
    {
        Butaca butaca = new Butaca();

        butaca.setLocalizacion("Platea");
        butaca.setFila(fila);
        butaca.setNumero(numero);
        butaca.setTipo(tipo);

        return butaca;
    }

    private PreciosPlantilla creaPrecioPlantilla(double normal, double descuento, double invitacion)
    {
        PreciosPlantilla preciosPlantilla = new PreciosPlantilla(localizacion, plantilla);

        preciosPlantilla.setPrecio(new BigDecimal(normal));
        preciosPlantilla.setDescuento(new BigDecimal(descuento));
        preciosPlantilla.setInvitacion(new BigDecimal(invitacion));

        preciosPlantillaDAO.add(preciosPlantilla);

        return preciosPlantilla;
    }
}
