package es.uji.apps.par.dao;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import es.uji.apps.par.db.QSesionAbonoDTO;
import es.uji.apps.par.db.SesionAbonoDTO;
import es.uji.apps.par.model.SesionAbono;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SesionesAbonosDAO extends BaseDAO {

    private QSesionAbonoDTO qSesionAbonoDTO = QSesionAbonoDTO.sesionAbonoDTO;

    @Transactional
    public List<SesionAbono> getSesiones(Long abonoId, String sortParameter, int start, int limit)
    {
        List<SesionAbono> sesionesAbono = new ArrayList<SesionAbono>();
        List<SesionAbonoDTO> sesionesAbonoDTO = getQueryAbonos().where(qSesionAbonoDTO.parAbono.id.eq(abonoId)).orderBy(getSort(qSesionAbonoDTO, sortParameter)).
                offset(start).limit(limit).list(qSesionAbonoDTO);

        for (SesionAbonoDTO sesionAbonoDB : sesionesAbonoDTO)
        {
            sesionesAbono.add(new SesionAbono(sesionAbonoDB));
        }

        return sesionesAbono;
    }

    @Transactional
    private JPAQuery getQueryAbonos() {
        JPAQuery query = new JPAQuery(entityManager);
        return query.from(qSesionAbonoDTO);
    }

    @Transactional
    public int getTotalSesionesAbono(Long abonoId) {
        return (int) getQueryAbonos().where(qSesionAbonoDTO.parAbono.id.eq(abonoId)).count();
    }

    @Transactional
    public long removeSesionAbono(long id) {
        JPADeleteClause delete = new JPADeleteClause(entityManager, qSesionAbonoDTO);
        return delete.where(qSesionAbonoDTO.id.eq(id)).execute();
    }
}
