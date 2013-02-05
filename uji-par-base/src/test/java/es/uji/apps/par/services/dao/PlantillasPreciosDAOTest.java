package es.uji.apps.par.services.dao;

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

	@Autowired
	PlantillasDAO plantillasPreciosDAO;
	
	@Test
    @Transactional
    public void getPlantillasPrecios()
    {
        Assert.assertNotNull(plantillasPreciosDAO.get());
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
        return new Plantilla("Nombre Localizacion");
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
