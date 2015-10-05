package es.uji.apps.par.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import es.uji.apps.par.db.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

import es.uji.apps.par.model.Localizacion;

@Repository
public class LocalizacionesDAO extends BaseDAO
{
    private QLocalizacionDTO qParLocalizacionDTO = QLocalizacionDTO.localizacionDTO;

    @Transactional
    public List<LocalizacionDTO> get(String sortParameter, int start, int limit, Integer salaId)
    {
    	List<LocalizacionDTO> localizacion = new ArrayList<LocalizacionDTO>();

    	//.orderBy(qParLocalizacionDTO.nombreVa.asc()
        for (LocalizacionDTO localizacionDB : getQueryLocalizaciones(salaId).
        		orderBy(getSort(qParLocalizacionDTO, sortParameter)).offset(start).limit(limit).list(qParLocalizacionDTO))
        {
            localizacion.add(localizacionDB);
        }

        return localizacion;
    }
    
    public List<LocalizacionDTO> getFromSesion(long idSesion) {
    	List<LocalizacionDTO> localizacion = new ArrayList<LocalizacionDTO>();
    	JPAQuery query = new JPAQuery(entityManager);
    	QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;
    	QSalaDTO qSalaDTO = QSalaDTO.salaDTO;

    	List<LocalizacionDTO> localizacionesDTO = query.from(qSesionDTO, qSalaDTO, qParLocalizacionDTO).
    		where(qSesionDTO.parSala.id.eq(qSalaDTO.id).and(qParLocalizacionDTO.sala.id.eq(qSalaDTO.id)).and(qSesionDTO.id.eq(idSesion))).
    		orderBy(getSort(qParLocalizacionDTO, "")).offset(0).limit(100).list(qParLocalizacionDTO);

    	for (LocalizacionDTO localizacionDB : localizacionesDTO)
        {
            localizacion.add(localizacionDB);
        }

        return localizacion;
	}
    
    @Transactional
    private JPAQuery getQueryLocalizaciones(Integer salaId) {
    	JPAQuery query = new JPAQuery(entityManager);
    	return query.from(qParLocalizacionDTO).where(qParLocalizacionDTO.sala.id.eq((long) salaId));
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
    public Localizacion add(Localizacion localizacion, SalaDTO salaDTO)
    {
        LocalizacionDTO localizacionDTO = new LocalizacionDTO();
        localizacionDTO.setNombreEs(localizacion.getNombreEs());
        localizacionDTO.setNombreVa(localizacion.getNombreVa());
        localizacionDTO.setCodigo(localizacion.getCodigo());
        localizacionDTO.setTotalEntradas(new BigDecimal(localizacion.getTotalEntradas()));
        localizacionDTO.setSala(salaDTO);

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

	public int getTotalLocalizaciones(Integer salaId) {
		return (int) getQueryLocalizaciones(salaId).count();
	}
}
