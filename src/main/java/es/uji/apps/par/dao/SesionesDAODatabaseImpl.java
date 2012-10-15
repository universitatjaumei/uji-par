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

import es.uji.apps.par.db.ParEventoDTO;
import es.uji.apps.par.db.ParSesionDTO;
import es.uji.apps.par.db.QParSesionDTO;
import es.uji.apps.par.model.ParSesion;

@Repository
public class SesionesDAODatabaseImpl implements SesionesDAO
{
    @PersistenceContext
    private EntityManager entityManager;

    private QParSesionDTO qSesionDTO = QParSesionDTO.parSesionDTO;

    @Override
    @Transactional
    public List<ParSesion> getSesiones(long sesionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        List<ParSesion> sesion = new ArrayList<ParSesion>();

        for (ParSesionDTO sesionDB : query.from(qSesionDTO)
        	.where(qSesionDTO.parEvento.id.eq(sesionId))
        	.list(qSesionDTO))
        {
            sesion.add(new ParSesion(sesionDB));
        }

        return sesion;
    }

    @Override
    @Transactional
    public void removeSesion(long id)
    {
        JPADeleteClause delete = new JPADeleteClause(entityManager, qSesionDTO);
        delete.where(qSesionDTO.id.eq(id)).execute();
    }

    @Override
    @Transactional
    public ParSesion addSesion(long eventoId, ParSesion sesion)
    {
    	ParSesionDTO sesionDTO = new ParSesionDTO();
        sesionDTO.setCanalInternet(sesion.getCanalInternet());
        sesionDTO.setCanalTaquilla(sesion.getCanalTaquilla());
        sesionDTO.setFechaCelebracion(sesion.getFechaCelebracion());
        sesionDTO.setFechaFinVentaOnline(sesion.getFechaFinVentaOnline());
        sesionDTO.setFechaInicioVentaOnline(sesion.getFechaInicioVentaOnline());
        sesionDTO.setHoraApertura(sesion.getHoraAperturaPuertas());
        
        ParEventoDTO parEventoDTO = createParEventoDTOWithId(eventoId);
        sesionDTO.setParEvento(parEventoDTO);

        entityManager.persist(sesionDTO);

        sesion.setId(sesionDTO.getId());
        return sesion;
    }

	private ParEventoDTO createParEventoDTOWithId(long eventoId) {
		ParEventoDTO parEventoDTO = new ParEventoDTO();
        parEventoDTO.setId(eventoId);
		return parEventoDTO;
	}

    @Override
    @Transactional
    public void updateSesion(long eventoId, ParSesion sesion)
    {
    	sesion.setEvento(createParEventoDTOWithId(eventoId));
    	
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qSesionDTO);
        update.set(qSesionDTO.canalInternet, sesion.getCanalInternet())
        	.set(qSesionDTO.canalTaquilla, sesion.getCanalTaquilla())
        	.set(qSesionDTO.fechaCelebracion, sesion.getFechaCelebracion())
        	.set(qSesionDTO.fechaFinVentaOnline, sesion.getFechaFinVentaOnline())
        	.set(qSesionDTO.fechaInicioVentaOnline, sesion.getFechaInicioVentaOnline())
        	.set(qSesionDTO.horaApertura, sesion.getHoraAperturaPuertas())
        	.set(qSesionDTO.parEvento, sesion.getEvento())
        	.where(qSesionDTO.id.eq(sesion.getId())).execute();
    }
}
