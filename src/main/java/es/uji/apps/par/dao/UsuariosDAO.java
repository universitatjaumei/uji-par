package es.uji.apps.par.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

import es.uji.apps.par.db.QParUsuarioDTO;
import es.uji.apps.par.db.UsuarioDTO;
import es.uji.apps.par.model.Usuario;

@Repository
public class UsuariosDAO
{
    @PersistenceContext
    private EntityManager entityManager;

    private QParUsuarioDTO qUserDTO = QParUsuarioDTO.parUsuarioDTO;

    @Transactional
    public List<Usuario> getUsers()
    {
        JPAQuery query = new JPAQuery(entityManager);

        List<Usuario> users = new ArrayList<Usuario>();

        for (UsuarioDTO userDB : query.from(qUserDTO).list(qUserDTO))
        {
            users.add(new Usuario(userDB));
        }

        return users;
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
}
