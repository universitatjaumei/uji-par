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

import es.uji.apps.par.db.ParTipoEventoDTO;
import es.uji.apps.par.db.QParTipoEventoDTO;
import es.uji.apps.par.model.ParTipoEvento;

@Repository
public class TiposEventosDAODatabaseImpl implements TiposEventosDAO
{
    @PersistenceContext
    private EntityManager entityManager;

    private QParTipoEventoDTO qTipoEventoDTO = QParTipoEventoDTO.parTipoEventoDTO;

    @Override
    public List<ParTipoEvento> getTiposEventos()
    {
        JPAQuery query = new JPAQuery(entityManager);

        List<ParTipoEvento> tipoEvento = new ArrayList<ParTipoEvento>();

        for (ParTipoEventoDTO tipoEventoDB : query.from(qTipoEventoDTO).list(qTipoEventoDTO))
        {
            tipoEvento.add(new ParTipoEvento(tipoEventoDB));
        }

        return tipoEvento;
    }

    @Override
    @Transactional
    public void removeTipoEvento(long id)
    {
        JPADeleteClause delete = new JPADeleteClause(entityManager, qTipoEventoDTO);
        delete.where(qTipoEventoDTO.id.eq(id)).execute();
    }

    @Override
    @Transactional
    public ParTipoEvento addTipoEvento(ParTipoEvento tipoEvento)
    {
    	ParTipoEventoDTO tipoeventoDTO = new ParTipoEventoDTO();
        tipoeventoDTO.setNombre(tipoEvento.getNombre());

        entityManager.persist(tipoeventoDTO);

        tipoEvento.setId(tipoeventoDTO.getId());
        return tipoEvento;
    }

    @Override
    @Transactional
    public void updateTipoEvento(ParTipoEvento tipoEvento)
    {
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qTipoEventoDTO);
        update.set(qTipoEventoDTO.nombre, tipoEvento.getNombre())
        	.where(qTipoEventoDTO.id.eq(tipoEvento.getId())).execute();
    }
}
