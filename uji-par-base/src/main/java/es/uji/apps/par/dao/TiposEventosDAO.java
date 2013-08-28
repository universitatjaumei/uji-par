package es.uji.apps.par.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

import es.uji.apps.par.db.QTipoEventoDTO;
import es.uji.apps.par.db.TipoEventoDTO;
import es.uji.apps.par.model.TipoEvento;

@Repository
public class TiposEventosDAO extends BaseDAO
{
    private QTipoEventoDTO qTipoEventoDTO = QTipoEventoDTO.tipoEventoDTO;

    @Transactional
    public List<TipoEvento> getTiposEventos(String sortParameter, int start, int limit)
    {
        List<TipoEvento> tipoEvento = new ArrayList<TipoEvento>();
        List<TipoEventoDTO> tipusEventosDTO = getQueryTiposEventos().orderBy(getSort(qTipoEventoDTO, sortParameter)).
        		offset(start).limit(limit).list(qTipoEventoDTO);

        for (TipoEventoDTO tipoEventoDB : tipusEventosDTO)
        {
            tipoEvento.add(new TipoEvento(tipoEventoDB));
        }

        return tipoEvento;
    }

    @Transactional
	private JPAQuery getQueryTiposEventos() {
		JPAQuery query = new JPAQuery(entityManager);
		return query.from(qTipoEventoDTO);
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
    
    @Transactional
	public int getTotalTipusEventos() {
		return (int) getQueryTiposEventos().count();
	}
}
