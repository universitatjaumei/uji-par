package es.uji.apps.par.services.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.dao.EventosDAO;
import es.uji.apps.par.dao.LocalizacionesDAO;
import es.uji.apps.par.dao.PlantillasDAO;
import es.uji.apps.par.dao.PreciosPlantillaDAO;
import es.uji.apps.par.dao.TiposEventosDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager")
@ContextConfiguration(locations = { "/applicationContext-db-test.xml" })
public class PreciosDAOTest {
	private static final String SORT = "";
	private static final int START = 0;
	private static final int LIMIT = 100;

	@Autowired
	PreciosPlantillaDAO preciosDAO;
	
	@Autowired
	LocalizacionesDAO localizacionesDAO;
	
	@Autowired
	TiposEventosDAO tiposEventosDAO;
	
	@Autowired
	EventosDAO eventosDAO;
	
	@Autowired
	PlantillasDAO plantillaPreciosDAO;
	
	@Test
    @Transactional
    public void getPrecios()
    {
        Assert.assertNotNull(preciosDAO.getPreciosOfPlantilla(Long.valueOf("1"), PreciosDAOTest.SORT, PreciosDAOTest.START, PreciosDAOTest.LIMIT));
    }

    /*@Test
    @Transactional
    public void addPrecio()
    {
        PreciosPlantilla parPrecio = preparaPrecio();
        PreciosPlantilla precio = preciosDAO.add(parPrecio);

        Assert.assertNotNull(precio.getId());
    }

    private PreciosPlantilla preparaPrecio()
    {
    	Localizacion localizacion = new Localizacion("Nombre");
    	localizacion = localizacionesDAO.add(localizacion);
    	
    	TipoEvento tipoEvento = new TipoEvento("tipo evento");
    	tipoEvento = tiposEventosDAO.addTipoEvento(tipoEvento);
    	
    	Evento evento = new Evento("Evento", tipoEvento);
    	evento = eventosDAO.addEvento(evento);
    	
    	Plantilla plantillaPrecios = new Plantilla("test");
    	plantillaPrecios = plantillaPreciosDAO.add(plantillaPrecios);
    	
        return new PreciosPlantilla(Localizacion.localizacionDTOtoLocalizacion(localizacionesDAO.get().get(0)), 
        		Plantilla.plantillaPreciosDTOtoPlantillaPrecios(plantillaPreciosDAO.get(false, PreciosDAOTest.SORT, PreciosDAOTest.START, PreciosDAOTest.LIMIT).get(0)));
    }

    @Test
    @Transactional
    public void updatePrecio()
    {
        PreciosPlantilla precio = preciosDAO.add(preparaPrecio());

        Assert.assertNotNull(precio.getId());

        precio.setInvitacion(new BigDecimal(1));
        PreciosPlantilla precioActualizado = preciosDAO.update(precio);
        Assert.assertEquals(precio.getId(), precioActualizado.getId());
        Assert.assertEquals(1, precioActualizado.getInvitacion().intValue());
    }*/
}
