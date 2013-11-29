package es.uji.apps.par.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPAQuery;

import es.uji.apps.par.db.CineDTO;
import es.uji.apps.par.db.QCineDTO;

@Repository
public class CinesDAO extends BaseDAO
{
    private QCineDTO qCineDTO = QCineDTO.cineDTO;

    @Transactional
    public List<CineDTO> getCines()
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qCineDTO).list(qCineDTO);
    }

    @Transactional
    public void addCine(CineDTO cineDTO)
    {
        entityManager.persist(cineDTO);
    }
}
