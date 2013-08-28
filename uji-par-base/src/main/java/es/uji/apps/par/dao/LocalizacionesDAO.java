package es.uji.apps.par.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

import es.uji.apps.par.db.LocalizacionDTO;
import es.uji.apps.par.db.QLocalizacionDTO;
import es.uji.apps.par.model.Localizacion;

@Repository
public class LocalizacionesDAO extends BaseDAO
{
    private QLocalizacionDTO qParLocalizacionDTO = QLocalizacionDTO.localizacionDTO;

    @Transactional
    public List<LocalizacionDTO> get(String sortParameter, int start, int limit)
    {
    	List<LocalizacionDTO> localizacion = new ArrayList<LocalizacionDTO>();

    	//.orderBy(qParLocalizacionDTO.nombreVa.asc()
        for (LocalizacionDTO localizacionDB : getQueryLocalizaciones().
        		orderBy(getSort(qParLocalizacionDTO, sortParameter)).offset(start).limit(limit).list(qParLocalizacionDTO))
        {
            localizacion.add(localizacionDB);
        }

        return localizacion;
    }
    
    public List<LocalizacionDTO> get() {
		return get("", 0, 100);
	}
    
    @Transactional
    private JPAQuery getQueryLocalizaciones() {
    	JPAQuery query = new JPAQuery(entityManager);
    	return query.from(qParLocalizacionDTO);
    }

    @Transactional
    public long remove(long id)
    {
        JPADeleteClause delete = new JPADeleteClause(entityManager, qParLocalizacionDTO);
        return delete.where(qParLocalizacionDTO.id.eq(id)).execute();
    }

    @Transactional
    public Localizacion add(Localizacion localizacion)
    {
        LocalizacionDTO localizacionDTO = new LocalizacionDTO();
        localizacionDTO.setNombreEs(localizacion.getNombreEs());
        localizacionDTO.setNombreVa(localizacion.getNombreVa());
        localizacionDTO.setCodigo(localizacion.getCodigo());
        localizacionDTO.setTotalEntradas(new BigDecimal(localizacion.getTotalEntradas()));

        entityManager.persist(localizacionDTO);

        localizacion.setId(localizacionDTO.getId());
        return localizacion;
    }

    @Transactional
    public Localizacion update(Localizacion localizacion)
    {
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qParLocalizacionDTO);
        update.set(qParLocalizacionDTO.nombreEs, localizacion.getNombreEs())
                .set(qParLocalizacionDTO.codigo, localizacion.getCodigo())
                .set(qParLocalizacionDTO.nombreVa, localizacion.getNombreVa())
                .set(qParLocalizacionDTO.totalEntradas, new BigDecimal(localizacion.getTotalEntradas()))
                .where(qParLocalizacionDTO.id.eq(localizacion.getId())).execute();

        return localizacion;
    }

    public LocalizacionDTO getLocalizacionById(long id)
    {
        return entityManager.find(LocalizacionDTO.class, id);
    }

    public LocalizacionDTO getLocalizacionByCodigo(String codigo)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qParLocalizacionDTO).where(qParLocalizacionDTO.codigo.eq(codigo))
                .singleResult(qParLocalizacionDTO);
    }

	public int getTotalLocalizaciones() {
		return (int) getQueryLocalizaciones().count();
	}
}
