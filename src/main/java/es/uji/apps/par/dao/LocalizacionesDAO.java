package es.uji.apps.par.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

import es.uji.apps.par.db.ParLocalizacionDTO;
import es.uji.apps.par.db.QParLocalizacionDTO;
import es.uji.apps.par.model.ParLocalizacion;

@Repository
public class LocalizacionesDAO {
	
	@PersistenceContext
    private EntityManager entityManager;

    private QParLocalizacionDTO qParLocalizacionDTO = QParLocalizacionDTO.parLocalizacionDTO;

    @Transactional
	public List<ParLocalizacion> get() {
    	JPAQuery query = new JPAQuery(entityManager);

        List<ParLocalizacion> localizacion = new ArrayList<ParLocalizacion>();

        for (ParLocalizacionDTO localizacionDB : query.from(qParLocalizacionDTO).list(qParLocalizacionDTO))
        {
            localizacion.add(new ParLocalizacion(localizacionDB));
        }

        return localizacion;
	}

    @Transactional
	public long remove(long id) {
		JPADeleteClause delete = new JPADeleteClause(entityManager, qParLocalizacionDTO);
        return delete.where(qParLocalizacionDTO.id.eq(id)).execute();
	}

    @Transactional
	public ParLocalizacion add(ParLocalizacion localizacion) {
		ParLocalizacionDTO localizacionDTO = new ParLocalizacionDTO();
        localizacionDTO.setNombreEs(localizacion.getNombreEs());
        localizacionDTO.setNombreVa(localizacion.getNombreVa());
        localizacionDTO.setNombreEn(localizacion.getNombreEn());
        localizacionDTO.setTotalEntradas(new BigDecimal(localizacion.getTotalEntradas()));

        entityManager.persist(localizacionDTO);

        localizacion.setId(localizacionDTO.getId());
        return localizacion;
	}

    @Transactional
	public ParLocalizacion update(ParLocalizacion localizacion) {
		JPAUpdateClause update = new JPAUpdateClause(entityManager, qParLocalizacionDTO);
        update.set(qParLocalizacionDTO.nombreEs, localizacion.getNombreEs()).
        	set(qParLocalizacionDTO.nombreVa, localizacion.getNombreVa()).
        	set(qParLocalizacionDTO.nombreEn, localizacion.getNombreEn()).
        	set(qParLocalizacionDTO.totalEntradas, new BigDecimal(localizacion.getTotalEntradas()))
        	.where(qParLocalizacionDTO.id.eq(localizacion.getId())).execute();
        
        return localizacion;
	}
    
    
}
