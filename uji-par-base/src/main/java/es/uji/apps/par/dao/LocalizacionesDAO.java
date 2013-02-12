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

import es.uji.apps.par.db.LocalizacionDTO;
import es.uji.apps.par.db.QLocalizacionDTO;
import es.uji.apps.par.model.Localizacion;

@Repository
public class LocalizacionesDAO
{

    @PersistenceContext
    private EntityManager entityManager;

    private QLocalizacionDTO qParLocalizacionDTO = QLocalizacionDTO.localizacionDTO;

    @Transactional
    public List<LocalizacionDTO> get()
    {
        JPAQuery query = new JPAQuery(entityManager);

        List<LocalizacionDTO> localizacion = new ArrayList<LocalizacionDTO>();

        for (LocalizacionDTO localizacionDB : query.from(qParLocalizacionDTO).orderBy(qParLocalizacionDTO.nombreVa.asc()).list(
                qParLocalizacionDTO))
        {
            localizacion.add(localizacionDB);
        }

        return localizacion;
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
                .set(qParLocalizacionDTO.nombreVa, localizacion.getNombreVa())
                .set(qParLocalizacionDTO.totalEntradas,
                        new BigDecimal(localizacion.getTotalEntradas()))
                .where(qParLocalizacionDTO.id.eq(localizacion.getId())).execute();

        return localizacion;
    }

	public LocalizacionDTO getLocalizacionById(long id) {
		return entityManager.find(LocalizacionDTO.class, id);
	}

}
