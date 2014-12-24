package es.uji.apps.par.dao;

import com.mysema.query.jpa.impl.JPAQuery;
import es.uji.apps.par.config.Configuration;
import es.uji.apps.par.database.DatabaseHelper;
import es.uji.apps.par.database.DatabaseHelperFactory;
import es.uji.apps.par.db.QTpvsDTO;
import es.uji.apps.par.db.TpvsDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class TpvsDAO extends BaseDAO {
    private QTpvsDTO qTpvsDTO = QTpvsDTO.tpvsDTO;

    private DatabaseHelper databaseHelper;

    public TpvsDAO() {
        databaseHelper = DatabaseHelperFactory.newInstance();
    }

    @Transactional
    public void addTpvDefault() {
        TpvsDTO tpvsDTO = new TpvsDTO();
        tpvsDTO.setNombre(Configuration.getTpvNombre());
        tpvsDTO.setCode(Configuration.getTpvCode());
        tpvsDTO.setCurrency(Configuration.getTpvCurrency());
        tpvsDTO.setLangCaCode(Configuration.getTpvLangCaCode());
        tpvsDTO.setLangEsCode(Configuration.getTpvLangEsCode());
        tpvsDTO.setOrderPrefix(Configuration.getTpvOrderPrefixCodeCajamar());
        tpvsDTO.setTerminal(Configuration.getTpvTerminal());
        tpvsDTO.setTransactionCode(Configuration.getTpvTransaction());
        tpvsDTO.setDefaultTpv(true);

        entityManager.persist(tpvsDTO);
    }

    @Transactional
    public TpvsDTO getTpvDefault() {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qTpvsDTO).where(qTpvsDTO.defaultTpv.isTrue()).uniqueResult(qTpvsDTO);
    }

    @Transactional
    public List<TpvsDTO> getTpvs(String sortParameter, int start, int limit) {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qTpvsDTO).orderBy(getSort(qTpvsDTO, sortParameter)).
                offset(start).limit(limit).list(qTpvsDTO);
    }
}
