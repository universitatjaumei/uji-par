package es.uji.apps.par.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

import es.uji.apps.par.DateUtils;
import es.uji.apps.par.db.EventoDTO;
import es.uji.apps.par.db.QParSesionDTO;
import es.uji.apps.par.db.SesionDTO;
import es.uji.apps.par.model.Sesion;

@Repository
public class SesionesDAO
{
    @PersistenceContext
    private EntityManager entityManager;

    private QParSesionDTO qSesionDTO = QParSesionDTO.parSesionDTO;

    @Transactional
    public List<Sesion> getSesiones(long sesionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        List<Sesion> sesion = new ArrayList<Sesion>();

        for (SesionDTO sesionDB : query.from(qSesionDTO)
                .where(qSesionDTO.parEvento.id.eq(sesionId)).list(qSesionDTO))
        {
            sesion.add(new Sesion(sesionDB));
        }

        return sesion;
    }

    @Transactional
    public long removeSesion(long id)
    {
        JPADeleteClause delete = new JPADeleteClause(entityManager, qSesionDTO);
        return delete.where(qSesionDTO.id.eq(id)).execute();
    }

    @Transactional
    public Sesion addSesion(long eventoId, Sesion sesion)
    {
        SesionDTO sesionDTO = new SesionDTO();
        sesionDTO.setCanalInternet(sesion.getCanalInternet());
        sesionDTO.setCanalTaquilla(sesion.getCanalTaquilla());
        sesionDTO.setFechaCelebracion(DateUtils.dateToTimestampSafe(sesion.getFechaCelebracion()));
        sesionDTO.setFechaFinVentaOnline(DateUtils.dateToTimestampSafe(sesion
                .getFechaFinVentaOnline()));
        sesionDTO.setFechaInicioVentaOnline(DateUtils.dateToTimestampSafe(sesion
                .getFechaInicioVentaOnline()));
        sesionDTO.setHoraApertura(sesion.getHoraAperturaPuertas());

        EventoDTO parEventoDTO = createParEventoDTOWithId(eventoId);
        sesionDTO.setParEvento(parEventoDTO);

        entityManager.persist(sesionDTO);

        sesion.setId(sesionDTO.getId());
        return sesion;
    }

    private EventoDTO createParEventoDTOWithId(long eventoId)
    {
        EventoDTO parEventoDTO = new EventoDTO();
        parEventoDTO.setId(eventoId);
        return parEventoDTO;
    }

    @Transactional
    public void updateSesion(long eventoId, Sesion sesion)
    {
        sesion.setEvento(createParEventoDTOWithId(eventoId));

        JPAUpdateClause update = new JPAUpdateClause(entityManager, qSesionDTO);
        update.set(qSesionDTO.canalInternet, sesion.getCanalInternet())
                .set(qSesionDTO.canalTaquilla, sesion.getCanalTaquilla())
                .set(qSesionDTO.fechaCelebracion,
                        DateUtils.dateToTimestampSafe(sesion.getFechaCelebracion()))
                .set(qSesionDTO.fechaFinVentaOnline,
                        DateUtils.dateToTimestampSafe(sesion.getFechaFinVentaOnline()))
                .set(qSesionDTO.fechaInicioVentaOnline,
                        DateUtils.dateToTimestampSafe(sesion.getFechaInicioVentaOnline()))
                .set(qSesionDTO.horaApertura, sesion.getHoraAperturaPuertas())
                .set(qSesionDTO.parEvento, sesion.getEvento())
                .where(qSesionDTO.id.eq(sesion.getId())).execute();
    }
}
