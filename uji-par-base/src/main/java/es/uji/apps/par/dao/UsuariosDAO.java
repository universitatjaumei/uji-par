package es.uji.apps.par.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

import es.uji.apps.par.db.QUsuarioDTO;
import es.uji.apps.par.db.UsuarioDTO;
import es.uji.apps.par.model.Usuario;

@Repository
public class UsuariosDAO extends BaseDAO
{
    private QUsuarioDTO qUserDTO = QUsuarioDTO.usuarioDTO;

    @Transactional
    public List<Usuario> getUsers(String sortParameter, int start, int limit)
    {
        List<Usuario> users = new ArrayList<Usuario>();
        List<UsuarioDTO> usuariosDTO = getQueryUsuarios().orderBy(getSort(qUserDTO, sortParameter)).limit(limit).offset(start).list(qUserDTO);

        for (UsuarioDTO userDB : usuariosDTO)
        {
            users.add(new Usuario(userDB));
        }

        return users;
    }

    @Transactional
	private JPAQuery getQueryUsuarios() {
		JPAQuery query = new JPAQuery(entityManager);
		return query.from(qUserDTO);
	}

    @Transactional
    public long removeUser(long id)
    {
        JPADeleteClause delete = new JPADeleteClause(entityManager, qUserDTO);
        return delete.where(qUserDTO.id.eq(id)).execute();
    }

    @Transactional
    public Usuario addUser(Usuario user)
    {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre(user.getNombre());
        usuarioDTO.setMail(user.getMail());
        usuarioDTO.setUsuario(user.getUsuario());

        entityManager.persist(usuarioDTO);

        user.setId(usuarioDTO.getId());
        return user;
    }

    @Transactional
    public Usuario updateUser(Usuario user)
    {
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qUserDTO);
        update.set(qUserDTO.nombre, user.getNombre()).set(qUserDTO.mail, user.getMail())
                .set(qUserDTO.usuario, user.getUsuario()).where(qUserDTO.id.eq(user.getId()))
                .execute();

        return user;
    }

    @Transactional
    public boolean userExists(Usuario user)
    {
    	JPAQuery query = new JPAQuery(entityManager);
        List<UsuarioDTO> usuarios = query.from(qUserDTO)
                .where(qUserDTO.usuario.eq(user.getUsuario())).list(qUserDTO);

        if (usuarios.size() > 0)
            return true;
        else
            return false;
    }
    
    @Transactional
	public int getTotalUsuarios() {
		return (int) getQueryUsuarios().count();
	}
}
