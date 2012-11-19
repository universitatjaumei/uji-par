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
import es.uji.apps.par.dao.SesionesDAO;
import es.uji.apps.par.dao.TiposEventosDAO;
import es.uji.apps.par.model.ParEvento;
import es.uji.apps.par.model.ParSesion;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class SesionesDAOTest {
	
	@Autowired
	EventosDAO eventosDAO;
	
	@Autowired
	TiposEventosDAO tiposEventosDAO;
	
	@Autowired
	SesionesDAO sesionesDAO;
	
	
	private ParSesion preparaSesion() {
		ParSesion parSesion = new ParSesion();
		parSesion.setEvento(eventosDAO.getEventos().get(0));
		parSesion.setFechaCelebracion("01/01/2012");
		
		return parSesion;
	}
	
	@Test
	@Transactional
	public void addSesionAndDeleteIt() {
		ParSesion parSesion = preparaSesion();
		sesionesDAO.addSesion(parSesion.getEvento().getId(), parSesion);
		Assert.assertEquals(1, sesionesDAO.removeSesion(parSesion.getId()));
	}

	@Test
	@Transactional
	public void addSesionWithoutFechaCelebracion() {
		
	}
	
	@Test
	@Transactional
	public void addSesionWithFechaEndVentaAnteriorFechaStartVenta() {
		
	}
	
	@Test
	@Transactional
	public void addSesionWithFechaEndVentaPosteriorFechaCelebracion() {
		
	}
	
	@Test
	@Transactional
	public void addSesionWithFechaStartVentaPosteriorFechaCelebracion() {
		
	}
	
	@Test
	@Transactional
	public void addSesionWithoutHoraCelebracion() {
		
	}
}
