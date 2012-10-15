package es.uji.apps.par.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import es.uji.apps.par.dao.UsuariosDAO;
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

    public ParUsuario addUser(ParUsuario user)
    {
        return usuariosDAO.addUser(user);
    }

    public void updateUser(ParUsuario user)
    {
        usuariosDAO.updateUser(user);
    }
}
