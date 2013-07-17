package es.uji.apps.par.services.dao;

import java.math.BigDecimal;
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
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.dao.ButacasDAO;
import es.uji.apps.par.dao.ComprasDAO;
import es.uji.apps.par.db.ButacaDTO;
import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.db.SesionDTO;
import es.uji.apps.par.model.Butaca;
import es.uji.apps.par.model.Localizacion;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager")
@ContextConfiguration(locations = { "/applicationContext-db-test.xml" })
public class ButacasDAOTest extends BaseDAOTest
{
    @Autowired
    ButacasDAO butacasDao;

    @Autowired
    ComprasDAO comprasDao;

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

    @Test
    @Transactional
    public void reservaButacas()
    {
        ButacaDTO butacaDTO = preparaButaca(sesion, Localizacion.localizacionToLocalizacionDTO(localizacion), "1", "2",
                BigDecimal.ONE);

        Butaca butaca = new Butaca(butacaDTO);

        CompraDTO compraDTO = comprasDao.guardaCompra("Pepe", "Perez", "964123456", "prueba@example.com", new Date());
        butacasDao.reservaButacas(sesion.getId(), compraDTO, Arrays.asList(butaca));

        List<ButacaDTO> butacas = butacasDao.getButacas(sesion.getId(), localizacion.getCodigo());

        Assert.assertEquals(sesion.getId(), butacas.get(0).getParSesion().getId());
        Assert.assertEquals(localizacion.getId(), butacas.get(0).getParLocalizacion().getId());
        Assert.assertEquals(butaca.getFila(), butacas.get(0).getFila());
        Assert.assertEquals(butaca.getNumero(), butacas.get(0).getNumero());
        Assert.assertEquals(BigDecimal.ONE, butacas.get(0).getPrecio());
    }
}