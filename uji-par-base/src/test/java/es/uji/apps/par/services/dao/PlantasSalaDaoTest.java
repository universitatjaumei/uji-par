package es.uji.apps.par.services.dao;

import java.math.BigDecimal;
import java.util.Arrays;
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
import es.uji.apps.par.db.CineDTO;
import es.uji.apps.par.db.PlantaSalaDTO;
import es.uji.apps.par.db.SalaDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager")
@ContextConfiguration(locations = { "/applicationContext-db-test.xml" })
public class PlantasSalaDaoTest extends BaseDAOTest
{
    @Autowired
    SalasDAO salasDao;

    @Autowired
    CinesDAO cinesDao;

    private CineDTO cineDTO;

    private SalaDTO salaDTO;

    @Before
    public void before()
    {
        cineDTO = new CineDTO("a", "cine 1", "12345678F", "Real nº 1", "1", "2", "12000", "AB SL", "123",
                "964123456", new BigDecimal(21));
        salaDTO = new SalaDTO(cineDTO, "b", "sala 1", 4, 3, 2, "asd", "qwe", "subtitulado");
    }

    @Test
    @Transactional
    public void insertaUna()
    {
        List<PlantaSalaDTO> parPlantas = Arrays.asList(new PlantaSalaDTO(salaDTO, "planta 1"));
        salaDTO.setParPlantas(parPlantas);
        
        cinesDao.addCine(cineDTO);
        salasDao.addSala(salaDTO);

        SalaDTO sala = salasDao.getSalas().get(0);

        Assert.assertEquals(1, sala.getParPlantas().size());
        Assert.assertEquals("planta 1", sala.getParPlantas().get(0).getNombre());
    }

}
