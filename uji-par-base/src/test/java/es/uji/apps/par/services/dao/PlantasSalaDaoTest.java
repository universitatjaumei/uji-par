package es.uji.apps.par.services.dao;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.dao.CinesDAO;
import es.uji.apps.par.dao.SalasDAO;
import es.uji.apps.par.model.Cine;
import es.uji.apps.par.model.PlantaSala;
import es.uji.apps.par.model.Sala;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager")
@ContextConfiguration(locations = { "/applicationContext-db-test.xml" })
public class PlantasSalaDaoTest extends BaseDAOTest
{
    @Autowired
    SalasDAO salasDao;

    @Autowired
    CinesDAO cinesDao;

    private Cine cine;

    private Sala sala;

    @Before
    public void before()
    {
        cine = new Cine("a", "cine 1", "12345678F", "Real nº 1", "1", "2", "12000", "AB SL", "123", "964123456",
                new BigDecimal(21));

        sala = new Sala("b", "sala 1", 4, 3, 2, "asd", "qwe", "subtitulado", cine);
    }

    @Test
    @Transactional
    public void insertaUna()
    {
        cinesDao.addCine(cine);
        salasDao.addSala(sala);
        salasDao.addPlanta(new PlantaSala("planta 1", sala));

        List<PlantaSala> plantas = salasDao.getPlantas(sala.getId());

        Assert.assertEquals(1, plantas.size());
        Assert.assertEquals("planta 1", plantas.get(0).getNombre());
    }

}
