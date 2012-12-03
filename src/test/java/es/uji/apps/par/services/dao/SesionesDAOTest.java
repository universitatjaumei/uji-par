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
		Assert.assertEquals(0, parSesion.getId());
		sesionesDAO.addSesion(parSesion.getEvento().getId(), parSesion);
		Assert.assertNotNull(parSesion.getId());
		Assert.assertNotSame(0, parSesion.getId());
	}

	@Test
	@Transactional
	public void addSesionWithoutFechaCelebracion() {
		ParSesion parSesion = preparaSesion();
		parSesion.setFechaCelebracionWithDate(null);
		parSesion = sesionesDAO.addSesion(parSesion.getEvento().getId(), parSesion);
		Assert.assertNotNull(parSesion.getId());
	}
	
	@Test
	@Transactional
	public void addSesionWithFechaEndVentaAnteriorFechaStartVenta() {
		ParSesion parSesion = preparaSesion();
		parSesion.setFechaInicioVentaOnline("01/01/2012");
		parSesion.setFechaFinVentaOnline("01/01/2011");
		parSesion = sesionesDAO.addSesion(parSesion.getEvento().getId(), parSesion);
		Assert.assertNotNull(parSesion.getId());
	}
	
	@Test
	@Transactional
	public void addSesionWithFechaEndVentaPosteriorFechaCelebracion() {
		ParSesion parSesion = preparaSesion();
		parSesion.setFechaInicioVentaOnline("01/01/2012");
		parSesion.setFechaFinVentaOnline("02/01/2012");
		parSesion = sesionesDAO.addSesion(parSesion.getEvento().getId(), parSesion);
		Assert.assertNotNull(parSesion.getId());
	}
	
	@Test
	@Transactional
	public void addSesionWithFechaStartVentaPosteriorFechaCelebracion() {
		ParSesion parSesion = preparaSesion();
		parSesion.setFechaInicioVentaOnline("02/01/2012");
		parSesion = sesionesDAO.addSesion(parSesion.getEvento().getId(), parSesion);
		Assert.assertNotNull(parSesion.getId());
	}
	
	@Test
	@Transactional
	public void addSesionWithoutHoraCelebracion() {
		ParSesion parSesion = preparaSesion();
		parSesion = sesionesDAO.addSesion(parSesion.getEvento().getId(), parSesion);
		Assert.assertNotNull(parSesion.getId());
	}
}
