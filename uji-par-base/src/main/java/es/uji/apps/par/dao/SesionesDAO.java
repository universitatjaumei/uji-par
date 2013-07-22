package es.uji.apps.par.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

import es.uji.apps.par.DateUtils;
import es.uji.apps.par.db.PreciosSesionDTO;
import es.uji.apps.par.db.QPreciosSesionDTO;
import es.uji.apps.par.db.QSesionDTO;
import es.uji.apps.par.db.SesionDTO;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.Plantilla;
import es.uji.apps.par.model.Sesion;

@Repository
public class SesionesDAO
{
    @PersistenceContext
    private EntityManager entityManager;

    private QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;
    private QPreciosSesionDTO qPreciosSesionDTO = QPreciosSesionDTO.preciosSesionDTO;

    @Transactional
    public List<SesionDTO> getSesiones(long eventoId)
    {
        return getSesiones(eventoId, false);
    }
    
    @Transactional
    public List<SesionDTO> getSesionesActivas(long eventoId)
    {
        return getSesiones(eventoId, true);
    }    

    @Transactional
    private List<SesionDTO> getSesiones(long eventoId, boolean activos)
    {
        List<SesionDTO> sesion = new ArrayList<SesionDTO>();

        JPAQuery query;
        
        if (activos)
        {
            Timestamp now = new Timestamp(new Date().getTime());
            
            query = new JPAQuery(entityManager).from(qSesionDTO)
                .where(qSesionDTO.parEvento.id.eq(eventoId).and(qSesionDTO.fechaCelebracion.after(now)));
        }
        else
        {
            query = new JPAQuery(entityManager).from(qSesionDTO)
                    .where(qSesionDTO.parEvento.id.eq(eventoId));            
        }
        
        for (SesionDTO sesionDB : query.list(qSesionDTO))
        {
            sesion.add(sesionDB);
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
    public SesionDTO persistSesion(SesionDTO sesionDTO) 
    {
        entityManager.persist(sesionDTO);
        return sesionDTO;
	}
    
    
    @Transactional
    public void updateSesion(Sesion sesion)
    {
        boolean actualTransactionActive = org.springframework.transaction.support.TransactionSynchronizationManager.isActualTransactionActive();
        
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qSesionDTO);
        update.
        	/*set(qSesionDTO.canalInternet, sesion.getCanalInternet())
            .set(qSesionDTO.canalTaquilla, sesion.getCanalTaquilla())*/
        	set(qSesionDTO.canalInternet, new BigDecimal(1))
        	.set(qSesionDTO.canalTaquilla, new BigDecimal(1))
            .set(qSesionDTO.fechaCelebracion,
                 DateUtils.dateToTimestampSafe(sesion.getFechaCelebracion()))
            .set(qSesionDTO.fechaFinVentaOnline,
                 DateUtils.dateToTimestampSafe(sesion.getFechaFinVentaOnline()))
            .set(qSesionDTO.fechaInicioVentaOnline,
                 DateUtils.dateToTimestampSafe(sesion.getFechaInicioVentaOnline()))
            .set(qSesionDTO.horaApertura, sesion.getHoraAperturaPuertas())
            .set(qSesionDTO.parEvento, Evento.eventoToEventoDTO(sesion.getEvento()))
            .set(qSesionDTO.parPlantilla, Plantilla.plantillaPreciosToPlantillaPreciosDTO(sesion.getPlantillaPrecios()))
            .where(qSesionDTO.id.eq(sesion.getId())).execute();
    }

    @Transactional
	public void addPrecioSesion(PreciosSesionDTO precioSesionDTO) {
		entityManager.persist(precioSesionDTO);
	}

    @Transactional
	public void deleteExistingPreciosSesion(long sesionId) {
		JPADeleteClause delete = new JPADeleteClause(entityManager, qPreciosSesionDTO);
        delete.where(qPreciosSesionDTO.parSesione.id.eq(sesionId)).execute();
	}

	@Transactional
    public List<PreciosSesionDTO> getPreciosSesion(long sesionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        List<PreciosSesionDTO> preciosSesion = new ArrayList<PreciosSesionDTO>();

        for (PreciosSesionDTO preciosSesionDB : query.from(qPreciosSesionDTO)
                .where(qPreciosSesionDTO.parSesione.id.eq(sesionId)).list(qPreciosSesionDTO))
        {
            preciosSesion.add(preciosSesionDB);
        }

        return preciosSesion;
    }

	@Transactional
	public PreciosSesionDTO persistPreciosSesion(PreciosSesionDTO precioSesionDTO) {
		entityManager.persist(precioSesionDTO);
		return precioSesionDTO;
	}
	
	@Transactional
    public SesionDTO getSesion(long sesionId)
    {
        JPAQuery query = new JPAQuery(entityManager);
        return query.from(qSesionDTO).leftJoin(qSesionDTO.parPreciosSesions, qPreciosSesionDTO).
                where(qSesionDTO.id.eq(sesionId)).uniqueResult(qSesionDTO);
    }
}
