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
public class TiposEventosDAO
{
    @PersistenceContext
    private EntityManager entityManager;

    private QParTipoEventoDTO qTipoEventoDTO = QParTipoEventoDTO.parTipoEventoDTO;

    @Transactional
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

    @Transactional
    public long removeTipoEvento(long id)
    {
        JPADeleteClause delete = new JPADeleteClause(entityManager, qTipoEventoDTO);
        return delete.where(qTipoEventoDTO.id.eq(id)).execute();
    }

    @Transactional
    public ParTipoEvento addTipoEvento(ParTipoEvento tipoEvento)
    {
    	ParTipoEventoDTO tipoeventoDTO = new ParTipoEventoDTO();
        tipoeventoDTO.setNombreEs(tipoEvento.getNombreEs());
        tipoeventoDTO.setNombreEn(tipoEvento.getNombreEn());
        tipoeventoDTO.setNombreVa(tipoEvento.getNombreVa());

        entityManager.persist(tipoeventoDTO);

        tipoEvento.setId(tipoeventoDTO.getId());
        return tipoEvento;
    }

    @Transactional
    public ParTipoEvento updateTipoEvento(ParTipoEvento tipoEvento)
    {
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qTipoEventoDTO);
        update.set(qTipoEventoDTO.nombreEs, tipoEvento.getNombreEs()).
        	set(qTipoEventoDTO.nombreEn, tipoEvento.getNombreEn()).
        	set(qTipoEventoDTO.nombreVa, tipoEvento.getNombreVa())
        	.where(qTipoEventoDTO.id.eq(tipoEvento.getId())).execute();
        
        return tipoEvento;
    }
}
