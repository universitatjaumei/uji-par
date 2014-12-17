package es.uji.apps.par.dao;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import es.uji.apps.par.db.AbonadoDTO;
import es.uji.apps.par.db.QAbonadoDTO;
import es.uji.apps.par.db.QCompraDTO;
import es.uji.apps.par.model.Abonado;
import es.uji.apps.par.model.Abono;
import es.uji.apps.par.model.Plantilla;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AbonadosDAO extends BaseDAO {

    private QAbonadoDTO qAbonadoDTO = QAbonadoDTO.abonadoDTO;
    private QCompraDTO qCompraDTO = QCompraDTO.compraDTO;

    @Transactional
    public AbonadoDTO getAbonado(long idAbonado) {
        JPAQuery query = new JPAQuery(entityManager);
        return query.from(qAbonadoDTO).leftJoin(qAbonadoDTO.parCompras, qCompraDTO).fetch().where(qAbonadoDTO.id.eq(idAbonado)).uniqueResult(qAbonadoDTO);
    }

    @Transactional
    public List<Abonado> getAbonadosPorAbono(long abonoId, String sortParameter, int start, int limit)
    {
        List<Abonado> abonados = new ArrayList<Abonado>();
        List<AbonadoDTO> abonadosDTO = getQueryAbonados().where(qAbonadoDTO.parAbono.id.eq(abonoId).and(qAbonadoDTO.anulado.eq(false).or(qAbonadoDTO.anulado.isNull()))).orderBy(getSort(qAbonadoDTO, sortParameter)).
                offset(start).limit(limit).list(qAbonadoDTO);

        for (AbonadoDTO abonadoDB : abonadosDTO)
        {
            abonados.add(new Abonado(abonadoDB));
        }

        return abonados;
    }

    @Transactional
    public long anularAbonado(long id)
    {
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qAbonadoDTO);
        update.set(qAbonadoDTO.anulado, true).where(qAbonadoDTO.id.eq(id)).execute();

        return id;
    }

    @Transactional
    public Abonado addAbonado(Abonado abonado)
    {
        AbonadoDTO abonadoDTO = new AbonadoDTO(abonado);

        entityManager.persist(abonadoDTO);

        abonado.setId(abonadoDTO.getId());
        return abonado;
    }

    @Transactional
    private JPAQuery getQueryAbonados() {
        JPAQuery query = new JPAQuery(entityManager);
        return query.from(qAbonadoDTO);
    }

    @Transactional
    public int getTotalAbonadosPorAbono(long abonoId) {
        return (int) getQueryAbonados().where(qAbonadoDTO.parAbono.id.eq(abonoId).and(qAbonadoDTO.anulado.eq(false).or(qAbonadoDTO.anulado.isNull()))).count();
    }

    @Transactional
    public Abonado updateAbonado(Abonado abonado) {
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qAbonadoDTO);
        update.set(qAbonadoDTO.nombre, abonado.getNombre())
                .set(qAbonadoDTO.apellidos, abonado.getApellidos())
                .set(qAbonadoDTO.direccion, abonado.getDireccion())
                .set(qAbonadoDTO.poblacion, abonado.getPoblacion())
                .set(qAbonadoDTO.cp, abonado.getCp())
                .set(qAbonadoDTO.provincia, abonado.getProvincia())
                .set(qAbonadoDTO.telefono, abonado.getTelefono())
                .set(qAbonadoDTO.email, abonado.getEmail())
                .set(qAbonadoDTO.infoPeriodica, abonado.getInfoPeriodica())
                .where(qAbonadoDTO.id.eq(abonado.getId())).execute();

        return abonado;
    }
}
