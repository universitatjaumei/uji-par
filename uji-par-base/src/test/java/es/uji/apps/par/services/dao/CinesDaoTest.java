package es.uji.apps.par.services.dao;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.dao.CinesDAO;
import es.uji.apps.par.db.CineDTO;
import es.uji.apps.par.model.Cine;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager")
@ContextConfiguration(locations = { "/applicationContext-db-test.xml" })
public class CinesDaoTest extends BaseDAOTest
{
    @Autowired
    CinesDAO cinesDao;

    @Test
    @Transactional
    public void getCines()
    {
        List<CineDTO> cines = cinesDao.getCines();
        Assert.assertEquals(0, cines.size());
    }

    @Test
    @Transactional
    public void insertaUna()
    {
        Cine cine = new Cine("a", "cine 1", "12345678F", "Real nº 1", "1", "2", "12000", "AB SL", "123", "964123456",
                new BigDecimal(21));

        cinesDao.addCine(cine);

        List<CineDTO> cines = cinesDao.getCines();

        Assert.assertEquals(1, cines.size());
        Assert.assertTrue(cines.get(0).getId() != 0);
        Assert.assertEquals("cine 1", cines.get(0).getNombre());
    }
}
