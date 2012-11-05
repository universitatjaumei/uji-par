package es.uji.apps.par.dao;

import java.util.List;

import es.uji.apps.par.exceptions.ParException;
import es.uji.apps.par.model.ParUsuario;

public interface UsuariosDAO
{
    List<ParUsuario> getUsers();

    long removeUser(long id);

    ParUsuario addUser(ParUsuario user);

    ParUsuario updateUser(ParUsuario user);
}