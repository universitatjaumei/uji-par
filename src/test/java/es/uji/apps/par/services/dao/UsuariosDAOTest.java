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
import es.uji.apps.par.exceptions.ParUsuarioYaExiste;
import es.uji.apps.par.model.ParUsuario;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class UsuariosDAOTest {

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
	public void addUsuario() throws ParUsuarioYaExiste {
		ParUsuario parUsuario = preparaUsuario();
		ParUsuario usuario = usuariosDAO.addUser(parUsuario);
		
		Assert.assertNotNull(usuario.getId());
	}
	
	@Test
	@Transactional
	public void deleteUsuario() {
		Assert.assertEquals(1, usuariosDAO.removeUser(usuariosDAO.getUsers().get(0).getId()));
	}
	
	@Test
	@Transactional
	public void updateUsuario() throws ParUsuarioYaExiste {
		ParUsuario parUsuario = new ParUsuario();
		parUsuario.setId(usuariosDAO.getUsers().get(0).getId());
		parUsuario.setNombre("Prueba2");
		parUsuario.setMail("mail");
		parUsuario.setUsuario("usuario");
		ParUsuario usuarioActualizado = usuariosDAO.updateUser(parUsuario);
		Assert.assertEquals(parUsuario.getId(), usuarioActualizado.getId());
	}
	
	@Test(expected=Exception.class)
	@Transactional
	public void updateUsuarioBorrandoMail() throws ParUsuarioYaExiste {
		ParUsuario parUsuario = new ParUsuario();
		parUsuario.setId(usuariosDAO.getUsers().get(0).getId());
		parUsuario.setNombre("Prueba2");
		usuariosDAO.updateUser(parUsuario);
	}
}
