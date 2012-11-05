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

import es.uji.apps.par.db.ParUsuarioDTO;
import es.uji.apps.par.db.QParUsuarioDTO;
import es.uji.apps.par.model.ParUsuario;

@Repository
public class UsuariosDAODatabaseImpl implements UsuariosDAO
{
    @PersistenceContext
    private EntityManager entityManager;

    private QParUsuarioDTO qUserDTO = QParUsuarioDTO.parUsuarioDTO;

    @Override
    @Transactional
    public List<ParUsuario> getUsers()
    {
        JPAQuery query = new JPAQuery(entityManager);

        List<ParUsuario> users = new ArrayList<ParUsuario>();

        for (ParUsuarioDTO userDB : query.from(qUserDTO).list(qUserDTO))
        {
            users.add(new ParUsuario(userDB));
        }

        return users;
    }

    @Override
    @Transactional
    public long removeUser(long id)
    {
        JPADeleteClause delete = new JPADeleteClause(entityManager, qUserDTO);
        return delete.where(qUserDTO.id.eq(id)).execute();
    }

    @Override
    @Transactional
    public ParUsuario addUser(ParUsuario user)
    {
    	ParUsuarioDTO usuarioDTO = new ParUsuarioDTO();
        usuarioDTO.setNombre(user.getNombre());
        usuarioDTO.setMail(user.getMail());
        usuarioDTO.setUsuario(user.getUsuario());
	
        entityManager.persist(usuarioDTO);
	
        user.setId(usuarioDTO.getId());
        return user;
    }

    @Override
    @Transactional
    public ParUsuario updateUser(ParUsuario user)
    {
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qUserDTO);
        update.set(qUserDTO.nombre, user.getNombre())
        	.set(qUserDTO.mail, user.getMail())
        	.set(qUserDTO.usuario, user.getUsuario())
        	.where(qUserDTO.id.eq(user.getId())).execute();
        
        return user;
    }
}
