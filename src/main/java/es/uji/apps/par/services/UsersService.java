package es.uji.apps.par.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.dao.UsuariosDAO;
import es.uji.apps.par.exceptions.ParCampoRequeridoException;
import es.uji.apps.par.exceptions.ParException;
import es.uji.apps.par.exceptions.ParUsuarioYaExiste;
import es.uji.apps.par.model.ParUsuario;

@Service
public class UsersService
{
    @Autowired
    private UsuariosDAO usuariosDAO;
    
    public List<ParUsuario> getUsuarios()
    {
        return usuariosDAO.getUsers();
    }

    public void removeUser(Integer id)
    {
        usuariosDAO.removeUser(id);
    }

    public ParUsuario addUser(ParUsuario user) throws ParException
    {
    	checkRequiredFields(user);
    	
    	if (usuariosDAO.userExists(user))
    		throw new ParUsuarioYaExiste();
    	else
    		return usuariosDAO.addUser(user);
    }

    private void checkRequiredFields(ParUsuario user) throws ParCampoRequeridoException {
		if (user.getMail() == null || user.getMail().isEmpty())
			throw new ParCampoRequeridoException("Mail");
		if (user.getNombre() == null || user.getNombre().isEmpty())
			throw new ParCampoRequeridoException("Nombre");
		if (user.getUsuario() == null || user.getUsuario().isEmpty())
			throw new ParCampoRequeridoException("Usuario");
	}

	public void updateUser(ParUsuario user) throws ParCampoRequeridoException
    {
		checkRequiredFields(user);
        usuariosDAO.updateUser(user);
    }
}
