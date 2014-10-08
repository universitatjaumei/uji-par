package es.uji.apps.par.services.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.exceptions.UsuarioYaExisteException;
import es.uji.apps.par.dao.UsuariosDAO;
import es.uji.apps.par.model.Usuario;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager = "transactionManager")
@ContextConfiguration(locations = { "/applicationContext-db-test.xml" })
public class UsuariosDAOTest
{

    @Autowired
    UsuariosDAO usuariosDAO;

    private Usuario preparaUsuario()
    {
        Usuario usuario = new Usuario();
        usuario.setNombre("Prueba");
        usuario.setUsuario("login");
        usuario.setMail("mail");

        return usuario;
    }

    @Test
    @Transactional
    public void getUsuarios()
    {
        Assert.assertNotNull(usuariosDAO.getUsers("", 0, 100));
    }

    @Test
    @Transactional
    public void addUsuario() throws UsuarioYaExisteException
    {
        Usuario parUsuario = preparaUsuario();
        Usuario usuario = usuariosDAO.addUser(parUsuario);

        Assert.assertNotNull(usuario.getId());
    }

    @Test
    @Transactional
    public void deleteUsuario()
    {
    	Usuario parUsuario = preparaUsuario();
        parUsuario = usuariosDAO.addUser(parUsuario);
        Assert.assertEquals(1, usuariosDAO.removeUser(parUsuario.getId()));
    }

    @Test
    @Transactional
    public void updateUsuario() throws UsuarioYaExisteException
    {
    	Usuario parUsuario = preparaUsuario();
        parUsuario = usuariosDAO.addUser(parUsuario);
        
        parUsuario.setNombre("Prueba2");
        parUsuario.setMail("mail");
        parUsuario.setUsuario("usuario");
        Usuario usuarioActualizado = usuariosDAO.updateUser(parUsuario);
        Assert.assertEquals(parUsuario.getId(), usuarioActualizado.getId());
    }

    @Test(expected = Exception.class)
    @Transactional
    public void updateUsuarioBorrandoMail() throws UsuarioYaExisteException
    {
        Usuario parUsuario = new Usuario();
        parUsuario.setId(usuariosDAO.getUsers("", 0, 100).get(0).getId());
        parUsuario.setNombre("Prueba2");
        usuariosDAO.updateUser(parUsuario);
    }
}
