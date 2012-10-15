package es.uji.apps.par.dao;

import java.util.List;

import es.uji.apps.par.model.ParUsuario;

public interface UsuariosDAO
{
    List<ParUsuario> getUsers();

    void removeUser(long id);

    ParUsuario addUser(ParUsuario user);

    void updateUser(ParUsuario user);
}