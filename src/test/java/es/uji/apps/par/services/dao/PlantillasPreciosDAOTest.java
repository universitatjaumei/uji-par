package es.uji.apps.par.services.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.dao.PlantillasPreciosDAO;
import es.uji.apps.par.model.PlantillaPrecios;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager")
@ContextConfiguration(locations = { "/applicationContext-test.xml" })
public class PlantillasPreciosDAOTest {

	@Autowired
	PlantillasPreciosDAO plantillasPreciosDAO;
	
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
        PlantillaPrecios parPlantillaPrecio = preparaPlantilla();
        PlantillaPrecios plantilla = plantillasPreciosDAO.add(parPlantillaPrecio);

        Assert.assertNotNull(plantilla.getId());
    }

    private PlantillaPrecios preparaPlantilla()
    {
        return new PlantillaPrecios("Nombre Localizacion");
    }

    @Test
    @Transactional
    public void updatePlantillaPrecio()
    {
        PlantillaPrecios plantilla = plantillasPreciosDAO.add(preparaPlantilla());

        Assert.assertNotNull(plantilla.getId());

        plantilla.setNombre("Prueba2");
        PlantillaPrecios plantillaActualizada = plantillasPreciosDAO.update(plantilla);
        Assert.assertEquals(plantilla.getId(), plantillaActualizada.getId());
    }
}
