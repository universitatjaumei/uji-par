package es.uji.apps.par.services.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.dao.TiposEventosDAO;
import es.uji.apps.par.model.ParTipoEvento;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class TiposEventosDAOTest {

	@Autowired
	TiposEventosDAO tiposEventosDAO;
	
	@Test
	@Transactional
	public void getTiposEventos() {
		Assert.assertNotNull(tiposEventosDAO.getTiposEventos());
	}
	
	@Test
	@Transactional
	public void addTipoEvento() {
		ParTipoEvento parTipoEvento = preparaTipoEvento();
		ParTipoEvento tipoEvento = tiposEventosDAO.addTipoEvento(parTipoEvento);
		
		Assert.assertNotNull(tipoEvento.getId());
	}
	
	private ParTipoEvento preparaTipoEvento() {
		return new ParTipoEvento("NombreTipoEvento");
	}
	
	@Test
	@Transactional
	public void updateTipoEvento() {
		ParTipoEvento parTipoEvento = new ParTipoEvento();
		parTipoEvento.setId(tiposEventosDAO.getTiposEventos().get(0).getId());
		parTipoEvento.setNombreEs("Prueba2");
		ParTipoEvento tipoEventoActualizado = tiposEventosDAO.updateTipoEvento(parTipoEvento);
		Assert.assertEquals(parTipoEvento.getId(), tipoEventoActualizado.getId());
	}
	
	@Test
	@Transactional
	public void addTipoEventoConIdiomas() {
		ParTipoEvento parTipoEvento = preparaTipoEvento();
		parTipoEvento.setNombreEn("english");
		parTipoEvento.setNombreVa("valencia");
		ParTipoEvento tipoEvento = tiposEventosDAO.addTipoEvento(parTipoEvento);
		
		Assert.assertNotNull(tipoEvento.getId());
	}
}
