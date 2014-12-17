package es.uji.apps.par.dao;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import es.uji.apps.par.db.*;
import es.uji.apps.par.model.Abono;
import es.uji.apps.par.model.Plantilla;
import es.uji.apps.par.model.SesionAbono;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AbonosDAO extends BaseDAO {

    private QAbonoDTO qAbonoDTO = QAbonoDTO.abonoDTO;
    private QSesionAbonoDTO qSesionAbonoDTO = QSesionAbonoDTO.sesionAbonoDTO;

    @Transactional
    public List<Abono> getAbonos(String sortParameter, int start, int limit)
    {
        List<Abono> abono = new ArrayList<Abono>();
        List<AbonoDTO> abonosDTO = getQueryAbonos().orderBy(getSort(qAbonoDTO, sortParameter)).where(qAbonoDTO.anulado.eq(false).or(qAbonoDTO.anulado.isNull())).
                offset(start).limit(limit).list(qAbonoDTO);

        for (AbonoDTO abonoDB : abonosDTO)
        {
            abono.add(new Abono(abonoDB));
        }

        return abono;
    }

    @Transactional
    private JPAQuery getQueryAbonos() {
        JPAQuery query = new JPAQuery(entityManager);
        return query.from(qAbonoDTO);
    }

    @Transactional
    public Abono addAbono(Abono abono)
    {
        AbonoDTO abonoDTO = new AbonoDTO();
        abonoDTO.setNombre(abono.getNombre());
        abonoDTO.setParPlantilla(Plantilla.plantillaPreciosToPlantillaPreciosDTO(abono.getPlantillaPrecios()));

        entityManager.persist(abonoDTO);

        abono.setId(abonoDTO.getId());
        updateSesionesAbono(abonoDTO.getId(), abono.getSesiones());
        return abono;
    }

    @Transactional
    private void updateSesionesAbono(long abonoId, List<SesionAbono> sesiones) {
        JPADeleteClause del = new JPADeleteClause(entityManager, qSesionAbonoDTO);
        del.where(qSesionAbonoDTO.parAbono.id.eq(abonoId)).execute();

        if (sesiones != null) {
            for (SesionAbono sesion: sesiones) {
                SesionAbonoDTO sesionAbonoDTO = new SesionAbonoDTO();
                sesionAbonoDTO.setParAbono(new AbonoDTO(abonoId));
                sesionAbonoDTO.setParSesion(new SesionDTO(sesion.getSesion().getId()));
                entityManager.persist(sesionAbonoDTO);
            }
        }
    }

    @Transactional
    public long removeAbono(long id)
    {
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qAbonoDTO);
        update.set(qAbonoDTO.anulado, true).where(qAbonoDTO.id.eq(id)).execute();

        return id;
    }

    @Transactional
    public Abono updateAbono(Abono abono)
    {
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qAbonoDTO);
        update.set(qAbonoDTO.nombre, abono.getNombre())
                .set(qAbonoDTO.parPlantilla, Plantilla.plantillaPreciosToPlantillaPreciosDTO(abono.getPlantillaPrecios()))
                .where(qAbonoDTO.id.eq(abono.getId())).execute();

        updateSesionesAbono(abono.getId(), abono.getSesiones());
        return abono;
    }

    @Transactional
    public Abono updateCamposActualizablesConAbonadosAbono(Abono abono) {
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qAbonoDTO);
        update.set(qAbonoDTO.nombre, abono.getNombre())
                .where(qAbonoDTO.id.eq(abono.getId())).execute();

        return abono;
    }

    @Transactional
    public int getTotalAbonos() {
        return (int) getQueryAbonos().where(qAbonoDTO.anulado.eq(false).or(qAbonoDTO.anulado.isNull())).count();
    }

    @Transactional
    public Abono getAbono(Long abonoId) {
        AbonoDTO abonosDTO = getQueryAbonos().leftJoin(qAbonoDTO.parSesiones, qSesionAbonoDTO).fetch().where(qAbonoDTO.id.eq(abonoId).and(qAbonoDTO.anulado.eq(false).or(qAbonoDTO.anulado.isNull()))).uniqueResult(qAbonoDTO);

        return new Abono(abonosDTO);
    }
}
