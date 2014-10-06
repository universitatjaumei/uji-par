package es.uji.apps.par.services.dao;

import es.uji.apps.par.dao.SalasDAO;
import es.uji.apps.par.db.SalaDTO;
import es.uji.apps.par.model.Sala;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.dao.PlantillasDAO;
import es.uji.apps.par.model.Plantilla;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager")
@ContextConfiguration(locations = { "/applicationContext-db-test.xml" })
public class PlantillasPreciosDAOTest {
	private static final String SORT = "";
	private static final int START = 0;
	private static final int LIMIT = 100;

	@Autowired
	PlantillasDAO plantillasPreciosDAO;

    @Autowired
    SalasDAO salasDAO;
	
	@Test
    @Transactional
    public void getPlantillasPrecios()
    {
        Assert.assertNotNull(plantillasPreciosDAO.get(true, PlantillasPreciosDAOTest.SORT, PlantillasPreciosDAOTest.START, PlantillasPreciosDAOTest.LIMIT));
    }

    @Test
    @Transactional
    public void addPlantilla()
    {
        Plantilla parPlantillaPrecio = preparaPlantilla();
        Plantilla plantilla = plantillasPreciosDAO.add(parPlantillaPrecio);

        Assert.assertNotNull(plantilla.getId());
    }

    private Plantilla preparaPlantilla()
    {
        SalaDTO salaDTO = new SalaDTO();
        salaDTO.setNombre("Sala1");
        salasDAO.persistSala(salaDTO);
        Plantilla plantilla = new Plantilla("Nombre Localizacion");
        plantilla.setSala(Sala.salaDTOtoSala(salaDTO));
        return plantilla;
    }

    @Test
    @Transactional
    public void updatePlantillaPrecio()
    {
        Plantilla plantilla = plantillasPreciosDAO.add(preparaPlantilla());

        Assert.assertNotNull(plantilla.getId());

        plantilla.setNombre("Prueba2");
        Plantilla plantillaActualizada = plantillasPreciosDAO.update(plantilla);
        Assert.assertEquals(plantilla.getId(), plantillaActualizada.getId());
    }
}
