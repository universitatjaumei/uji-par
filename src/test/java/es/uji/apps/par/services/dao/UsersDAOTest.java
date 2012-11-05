package es.uji.apps.par.services.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.dao.UsuariosDAO;
import es.uji.apps.par.model.ParUsuario;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class UsersDAOTest {

	@Autowired
	UsuariosDAO usuariosDAO;
	
	private ParUsuario preparaUsuario() {
		ParUsuario usuario = new ParUsuario();
		usuario.setNombre("Prueba");
		usuario.setUsuario("login");
		usuario.setMail("mail");
		
		return usuario;
	}
	
	@Test
	@Transactional
	public void getUsuarios() {
		Assert.assertNotNull(usuariosDAO.getUsers());
	}
	
	@Test
	@Transactional
	public void addUsuarioAndDeleteIt() {
		ParUsuario parUsuario = preparaUsuario();
		ParUsuario usuario = usuariosDAO.addUser(parUsuario);
		
		Assert.assertNotNull(usuario.getId());
		
		Assert.assertEquals(1, usuariosDAO.removeUser(usuario.getId()));
	}
	
	@Test
	@Transactional
	public void updateUsuario() {
		ParUsuario parUsuario = preparaUsuario();
		ParUsuario usuario = usuariosDAO.addUser(parUsuario);
		
		Assert.assertNotNull(usuario.getId());
		
		usuario.setNombre("Prueba2");
		ParUsuario usuarioActualizado = usuariosDAO.updateUser(usuario);
		Assert.assertEquals(usuario.getId(), usuarioActualizado.getId());
		
		Assert.assertEquals(1, usuariosDAO.removeUser(usuario.getId()));
	}
}
