package es.uji.apps.par.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPAQuery;

import es.uji.apps.par.db.QSalaDTO;
import es.uji.apps.par.db.SalaDTO;

@Repository
public class SalasDAO extends BaseDAO
{
    private QSalaDTO qSalaDTO = QSalaDTO.salaDTO;

    @Transactional
    public List<SalaDTO> getSalas()
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qSalaDTO).list(qSalaDTO);
    }

    @Transactional
    public void addSala(SalaDTO salaDTO)
    {
        entityManager.persist(salaDTO);
    }
}
