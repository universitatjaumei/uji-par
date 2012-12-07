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
import es.uji.apps.par.dao.TiposEventosDAO;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.TipoEvento;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class EventosDAOTest {

	@Autowired
	EventosDAO eventosDAO;
	
	@Autowired
	TiposEventosDAO tiposEventosDAO;
	
	@Test
	@Transactional
	public void getEventos() {
		Assert.assertNotNull(eventosDAO.getEventos());
	}
	
	@Test
	@Transactional
	public void addEvento() {
		Evento parEvento = preparaEvento();
		parEvento = eventosDAO.addEvento(parEvento);
		
		Assert.assertNotNull(parEvento.getId());
	}
	
	@Test
	@Transactional
	public void deleteEvento() {
		Assert.assertEquals(1, eventosDAO.removeEvento(eventosDAO.getEventos().get(0).getId()));
	}

	private Evento preparaEvento() {
		return new Evento("Nombre", tiposEventosDAO.getTiposEventos().get(0));
	}

	@Test
	@Transactional
	public void updateEvento() {
		Evento parEvento = new Evento();
		parEvento.setId(eventosDAO.getEventos().get(0).getId());
		TipoEvento parTipoEvento = new TipoEvento();
		parTipoEvento.setId(eventosDAO.getEventos().get(0).getParTiposEvento().getId());
		parEvento.setParTipoEvento(parTipoEvento);
		
		parEvento.setTituloEs("Prueba2");
		Evento eventoActualizado = eventosDAO.updateEvento(parEvento);
		Assert.assertEquals(parEvento.getId(), eventoActualizado.getId());
	}
	
	@Test
	@Transactional
	public void addEventoConIdiomas() {
		Evento parEvento = preparaEvento();
		parEvento.setCaracteristicasVa("valencia");
		parEvento.setComentariosEs("comentarios");
		parEvento = eventosDAO.addEvento(parEvento);
		
		Assert.assertNotNull(parEvento.getId());
	}
	
	@Test
	@Transactional
	public void deleteImagen() {
		Evento parEvento = preparaEvento();
		parEvento.setCaracteristicasVa("valencia");
		parEvento.setComentariosEs("comentarios");
		parEvento.setImagen("hola".getBytes());
		parEvento.setImagenSrc("hola");
		parEvento.setImagenContentType("");
		parEvento = eventosDAO.addEvento(parEvento);
		
		Assert.assertNotNull(parEvento.getId());
		eventosDAO.deleteImagen(parEvento.getId());
		
		/*ParEventoDTO eventoDespuesDeBorrarLaImagen = eventosDAO.getEventoDTO(parEvento.getId()).get(0);
		Assert.assertNull(eventoDespuesDeBorrarLaImagen.getImagen());*/
	}
}
