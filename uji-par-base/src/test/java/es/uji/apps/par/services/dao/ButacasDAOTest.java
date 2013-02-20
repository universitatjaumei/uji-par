package es.uji.apps.par.services.dao;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.dao.ButacasDAO;
import es.uji.apps.par.dao.LocalizacionesDAO;
import es.uji.apps.par.dao.SesionesDAO;
import es.uji.apps.par.db.ButacaDTO;
import es.uji.apps.par.db.LocalizacionDTO;
import es.uji.apps.par.db.SesionDTO;
import es.uji.apps.par.model.Localizacion;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager")
@ContextConfiguration(locations = { "/applicationContext-db-test.xml" })
public class ButacasDAOTest
{

    @Autowired
    ButacasDAO butacasDao;

    @Autowired
    SesionesDAO sesionesDao;

    @Autowired
    LocalizacionesDAO localizacionesDao;

    private Localizacion localizacion;

    private SesionDTO sesion;

    @Before
    public void before()
    {
        localizacion = preparaLocalizacion("anfiteatro");
        sesion = preparaSesion();
    }

    @Test
    @Transactional
    public void getButacas()
    {
        Localizacion localizacion2 = preparaLocalizacion("platea1");

        ButacaDTO butaca = preparaButaca(sesion, Localizacion.localizacionToLocalizacionDTO(localizacion), "1", "2",
                BigDecimal.ONE);
        butacasDao.addButaca(butaca);

        ButacaDTO butaca2 = preparaButaca(sesion, Localizacion.localizacionToLocalizacionDTO(localizacion2), "2", "3",
                BigDecimal.ONE);
        butacasDao.addButaca(butaca2);

        List<ButacaDTO> butacas = butacasDao.getButacas(sesion.getId(), localizacion.getCodigo());

        Assert.assertEquals(1, butacas.size());
        Assert.assertEquals(butaca, butacas.get(0));
    }

    @Test
    @Transactional
    public void butacaLibreAlInicio()
    {
        Localizacion localizacion = preparaLocalizacion("anfiteatro");
        SesionDTO sesion = preparaSesion();

        Assert.assertFalse(butacasDao.estaOcupada(sesion.getId(), localizacion.getCodigo(), "1", "2"));
    }

    @Test
    @Transactional
    public void getButacaOcupada()
    {
        ButacaDTO butaca = preparaButaca(sesion, Localizacion.localizacionToLocalizacionDTO(localizacion), "1", "2",
                BigDecimal.ONE);
        butacasDao.addButaca(butaca);

        Assert.assertTrue(butacasDao.estaOcupada(sesion.getId(), localizacion.getCodigo(), "1", "2"));
    }

    private SesionDTO preparaSesion()
    {
        SesionDTO sesion = new SesionDTO();
        return sesionesDao.persistSesion(sesion);
    }

    private Localizacion preparaLocalizacion(String codigoLocalizacion)
    {
        Localizacion localizacion = new Localizacion();
        localizacion.setCodigo(codigoLocalizacion);

        return localizacionesDao.add(localizacion);
    }

    private ButacaDTO preparaButaca(SesionDTO sesion, LocalizacionDTO localizacion, String fila, String numero,
            BigDecimal precio)
    {
        ButacaDTO butacaDTO = new ButacaDTO();

        butacaDTO.setFila(fila);
        butacaDTO.setNumero(numero);
        butacaDTO.setPrecio(precio);
        butacaDTO.setParSesion(sesion);
        butacaDTO.setParLocalizacion(localizacion);

        return butacaDTO;
    }

    /*
    @Test
    @Transactional
    public void addButaca()
    {
        ButacaDTO parButaca = preparaButaca();
        parButaca = butacasDao.addButaca(parButaca);

        Assert.assertNotNull(parButaca.getId());
    }



    @Test
    @Transactional
    public void deleteButaca()
    {
    	Butaca parButaca = preparaButaca();
    	parButaca = ButacasDAO.addButaca(parButaca);
        Assert.assertEquals(1, ButacasDAO.removeButaca(parButaca.getId()));
    }



    @Test
    @Transactional
    public void updateButaca()
    {
    	Butaca parButaca = preparaButaca();
    	parButaca = ButacasDAO.addButaca(parButaca);

        parButaca.setTituloEs("Prueba2");
        Butaca ButacaActualizado = ButacasDAO.updateButaca(parButaca);
        Assert.assertEquals(parButaca.getId(), ButacaActualizado.getId());
    }

    @Test
    @Transactional
    public void addButacaConIdiomas()
    {
        Butaca parButaca = preparaButaca();
        parButaca.setCaracteristicasVa("valencia");
        parButaca.setComentariosEs("comentarios");
        parButaca = ButacasDAO.addButaca(parButaca);

        Assert.assertNotNull(parButaca.getId());
    }

    @Test
    @Transactional
    public void deleteImagen()
    {
        Butaca parButaca = preparaButaca();
        parButaca.setCaracteristicasVa("valencia");
        parButaca.setComentariosEs("comentarios");
        parButaca.setImagen("hola".getBytes());
        parButaca.setImagenSrc("hola");
        parButaca.setImagenContentType("");
        parButaca = ButacasDAO.addButaca(parButaca);

        Assert.assertNotNull(parButaca.getId());
        ButacasDAO.deleteImagen(parButaca.getId());

    }
    */
}
