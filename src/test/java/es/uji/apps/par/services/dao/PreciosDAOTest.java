package es.uji.apps.par.services.dao;

import java.math.BigDecimal;

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
import es.uji.apps.par.dao.PlantillasPreciosDAO;
import es.uji.apps.par.dao.PreciosDAO;
import es.uji.apps.par.dao.TiposEventosDAO;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.Localizacion;
import es.uji.apps.par.model.PlantillaPrecios;
import es.uji.apps.par.model.Precio;
import es.uji.apps.par.model.TipoEvento;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager")
@ContextConfiguration(locations = { "/applicationContext-test.xml" })
public class PreciosDAOTest {

	@Autowired
	PreciosDAO preciosDAO;
	
	@Autowired
	LocalizacionesDAO localizacionesDAO;
	
	@Autowired
	TiposEventosDAO tiposEventosDAO;
	
	@Autowired
	EventosDAO eventosDAO;
	
	@Autowired
	PlantillasPreciosDAO plantillaPreciosDAO;
	
	@Test
    @Transactional
    public void getPrecios()
    {
        Assert.assertNotNull(preciosDAO.getPreciosOfPlantilla(Long.valueOf("1")));
    }

    @Test
    @Transactional
    public void addPrecio()
    {
        Precio parPrecio = preparaPrecio();
        Precio precio = preciosDAO.add(parPrecio);

        Assert.assertNotNull(precio.getId());
    }

    private Precio preparaPrecio()
    {
    	Localizacion localizacion = new Localizacion("Nombre");
    	localizacion = localizacionesDAO.add(localizacion);
    	
    	TipoEvento tipoEvento = new TipoEvento("tipo evento");
    	tipoEvento = tiposEventosDAO.addTipoEvento(tipoEvento);
    	
    	Evento evento = new Evento("Evento", tipoEvento);
    	evento = eventosDAO.addEvento(evento);
    	
    	PlantillaPrecios plantillaPrecios = new PlantillaPrecios("test");
    	plantillaPrecios = plantillaPreciosDAO.add(plantillaPrecios);
    	
        return new Precio(Localizacion.localizacionDTOtoLocalizacion(localizacionesDAO.get().get(0)), 
        		PlantillaPrecios.plantillaPreciosDTOtoPlantillaPrecios(plantillaPreciosDAO.get().get(0)));
    }

    @Test
    @Transactional
    public void updatePrecio()
    {
        Precio precio = preciosDAO.add(preparaPrecio());

        Assert.assertNotNull(precio.getId());

        precio.setInvitacion(new BigDecimal(1));
        Precio precioActualizado = preciosDAO.update(precio);
        Assert.assertEquals(precio.getId(), precioActualizado.getId());
        Assert.assertEquals(1, precioActualizado.getInvitacion().intValue());
    }
}
