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

import es.uji.apps.par.db.QParTipoEventoDTO;
import es.uji.apps.par.db.TipoEventoDTO;
import es.uji.apps.par.model.TipoEvento;

@Repository
public class TiposEventosDAO
{
    @PersistenceContext
    private EntityManager entityManager;

    private QParTipoEventoDTO qTipoEventoDTO = QParTipoEventoDTO.parTipoEventoDTO;

    @Transactional
    public List<TipoEvento> getTiposEventos()
    {
        JPAQuery query = new JPAQuery(entityManager);

        List<TipoEvento> tipoEvento = new ArrayList<TipoEvento>();

        for (TipoEventoDTO tipoEventoDB : query.from(qTipoEventoDTO).list(qTipoEventoDTO))
        {
            tipoEvento.add(new TipoEvento(tipoEventoDB));
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
    public TipoEvento addTipoEvento(TipoEvento tipoEvento)
    {
        TipoEventoDTO tipoeventoDTO = new TipoEventoDTO();
        tipoeventoDTO.setNombreEs(tipoEvento.getNombreEs());
        tipoeventoDTO.setNombreVa(tipoEvento.getNombreVa());

        entityManager.persist(tipoeventoDTO);

        tipoEvento.setId(tipoeventoDTO.getId());
        return tipoEvento;
    }

    @Transactional
    public TipoEvento updateTipoEvento(TipoEvento tipoEvento)
    {
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qTipoEventoDTO);
        update.set(qTipoEventoDTO.nombreEs, tipoEvento.getNombreEs())
                .set(qTipoEventoDTO.nombreVa, tipoEvento.getNombreVa())
                .where(qTipoEventoDTO.id.eq(tipoEvento.getId())).execute();

        return tipoEvento;
    }
}
