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
import es.uji.apps.par.db.ParEventoDTO;
import es.uji.apps.par.model.ParEvento;
import es.uji.apps.par.model.ParTipoEvento;

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
		ParEvento parEvento = preparaEvento();
		parEvento = eventosDAO.addEvento(parEvento);
		
		Assert.assertNotNull(parEvento.getId());
	}
	
	@Test
	@Transactional
	public void deleteEvento() {
		Assert.assertEquals(1, eventosDAO.removeEvento(eventosDAO.getEventos().get(0).getId()));
	}

	private ParEvento preparaEvento() {
		return new ParEvento("Nombre", tiposEventosDAO.getTiposEventos().get(0));
	}

	@Test
	@Transactional
	public void updateEvento() {
		ParEvento parEvento = new ParEvento();
		parEvento.setId(eventosDAO.getEventos().get(0).getId());
		ParTipoEvento parTipoEvento = new ParTipoEvento();
		parTipoEvento.setId(eventosDAO.getEventos().get(0).getParTiposEvento().getId());
		parEvento.setParTipoEvento(parTipoEvento);
		
		parEvento.setTituloEs("Prueba2");
		ParEvento eventoActualizado = eventosDAO.updateEvento(parEvento);
		Assert.assertEquals(parEvento.getId(), eventoActualizado.getId());
	}
	
	@Test
	@Transactional
	public void addEventoConIdiomas() {
		ParEvento parEvento = preparaEvento();
		parEvento.setTituloEn("english");
		parEvento.setCaracteristicasVa("valencia");
		parEvento.setComentariosEs("comentarios");
		parEvento = eventosDAO.addEvento(parEvento);
		
		Assert.assertNotNull(parEvento.getId());
	}
	
	@Test
	@Transactional
	public void deleteImagen() {
		ParEvento parEvento = preparaEvento();
		parEvento.setTituloEn("english");
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
