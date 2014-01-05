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
    public TipoEventoDTO getTipoEventoByNombreVa(String nombreVa) {
        
        JPAQuery query = new JPAQuery(entityManager);

         List<TipoEventoDTO> tipos = query
                .from(qTipoEventoDTO)
                .where(qTipoEventoDTO.nombreVa.eq(nombreVa))
                .list(qTipoEventoDTO);
         
         if (tipos.size() == 0)
             return null;
         else
             return tipos.get(0);
    }
    
    @Transactional
    public TipoEventoDTO getTipoEventoByNombreEs(String nombreEs) {
        
        JPAQuery query = new JPAQuery(entityManager);

         List<TipoEventoDTO> tipos = query
                .from(qTipoEventoDTO)
                .where(qTipoEventoDTO.nombreEs.eq(nombreEs))
                .list(qTipoEventoDTO);
         
         if (tipos.size() == 0)
             return null;
         else
             return tipos.get(0);
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
        TipoEventoDTO tipoEventoDTO = new TipoEventoDTO();
        tipoEventoDTO.setNombreEs(tipoEvento.getNombreEs());
        tipoEventoDTO.setNombreVa(tipoEvento.getNombreVa());
        tipoEventoDTO.setExportarICAA(tipoEvento.getExportarICAA());

        entityManager.persist(tipoEventoDTO);

        tipoEvento.setId(tipoEventoDTO.getId());
        return tipoEvento;
    }

    @Transactional
    public TipoEvento updateTipoEvento(TipoEvento tipoEvento)
    {
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qTipoEventoDTO);
        update.set(qTipoEventoDTO.nombreEs, tipoEvento.getNombreEs())
                .set(qTipoEventoDTO.nombreVa, tipoEvento.getNombreVa())
                .set(qTipoEventoDTO.exportarICAA, tipoEvento.getExportarICAA())
                .where(qTipoEventoDTO.id.eq(tipoEvento.getId())).execute();

        return tipoEvento;
    }
    
    @Transactional
	public int getTotalTipusEventos() {
		return (int) getQueryTiposEventos().count();
	}
}
