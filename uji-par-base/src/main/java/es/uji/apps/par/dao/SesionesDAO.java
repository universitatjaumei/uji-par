package es.uji.apps.par.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPASubQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

import es.uji.apps.par.db.PreciosSesionDTO;
import es.uji.apps.par.db.QButacaDTO;
import es.uji.apps.par.db.QPreciosSesionDTO;
import es.uji.apps.par.db.QSalaDTO;
import es.uji.apps.par.db.QSesionDTO;
import es.uji.apps.par.db.SesionDTO;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.Plantilla;
import es.uji.apps.par.model.Sala;
import es.uji.apps.par.model.Sesion;
import es.uji.apps.par.utils.DateUtils;

@Repository
public class SesionesDAO extends BaseDAO
{
    private QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;
    private QPreciosSesionDTO qPreciosSesionDTO = QPreciosSesionDTO.preciosSesionDTO;

    @Transactional
    public List<SesionDTO> getSesiones(long eventoId, String sortParameter, int start, int limit)
    {
        return getSesiones(eventoId, false, sortParameter, start, limit);
    }
    
    @Transactional
    public List<SesionDTO> getSesionesActivas(long eventoId, String sortParameter, int start, int limit)
    {
        return getSesiones(eventoId, true, sortParameter, start, limit);
    }    

    @Transactional
    private List<SesionDTO> getSesiones(long eventoId, boolean activos, String sortParameter, int start, int limit)
    {
        List<SesionDTO> sesion = new ArrayList<SesionDTO>();
        
        if (activos)
        	sesion = getQuerySesionesActivas(eventoId).orderBy(getSort(qSesionDTO, sortParameter)).offset(start).limit(limit).list(qSesionDTO);
        else
            sesion = getQuerySesiones(eventoId).orderBy(getSort(qSesionDTO, sortParameter)).offset(start).limit(limit).list(qSesionDTO);

        return sesion;
    }
    
    @Transactional
    public List<Object[]> getSesionesConButacasVendidas(long eventoId, boolean activas, String sortParameter, int start, int limit)
    {
        QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;
        QButacaDTO qButacaDTO = QButacaDTO.butacaDTO;
        
        JPAQuery query; 
        
        if (activas)
            query = getQuerySesionesActivas(eventoId);
        else
            query = getQuerySesiones(eventoId);
        
        JPASubQuery queryVendidas = new JPASubQuery();
        queryVendidas.from(qButacaDTO);
        queryVendidas.where(qSesionDTO.id.eq(qButacaDTO.parSesion.id)
                            .and(qButacaDTO.anulada.eq(false)));
        
        List<Object[]> sesiones = query.orderBy(getSort(qSesionDTO, sortParameter)).offset(start).limit(limit).list(qSesionDTO, queryVendidas.count());

        return sesiones;
    }
    
    @Transactional
    private JPAQuery getQuerySesionesActivas(long eventoId) {
        QSalaDTO qSalaDTO = QSalaDTO.salaDTO;
        
    	JPAQuery query = new JPAQuery(entityManager);
    	
    	Timestamp now = new Timestamp(DateUtils.dateConMargenTrasVenta().getTime());
    	
    	return query.from(qSesionDTO).leftJoin(qSesionDTO.parSala, qSalaDTO).fetch()
        .where(qSesionDTO.parEvento.id.eq(eventoId).and(qSesionDTO.fechaCelebracion.after(now)));
    }
    
    @Transactional
    private JPAQuery getQuerySesiones(long eventoId) {
    	JPAQuery query = new JPAQuery(entityManager);
    	return query.from(qSesionDTO).where(qSesionDTO.parEvento.id.eq(eventoId));
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
    public Sesion addSesion(Sesion sesion)
    {
        SesionDTO sesionDTO = Sesion.SesionToSesionDTO(sesion);
        
        persistSesion(sesionDTO);

        sesion.setId(sesionDTO.getId());
        return sesion;
    }
    
    @Transactional
    public void updateSesion(Sesion sesion)
    {
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qSesionDTO);
        update.
        	set(qSesionDTO.canalInternet, sesion.getCanalInternet())
            .set(qSesionDTO.canalTaquilla, sesion.getCanalTaquilla())
            .set(qSesionDTO.fechaCelebracion,
                 DateUtils.dateToTimestampSafe(sesion.getFechaCelebracion()))
            .set(qSesionDTO.fechaFinVentaOnline,
                 DateUtils.dateToTimestampSafe(sesion.getFechaFinVentaOnline()))
            .set(qSesionDTO.fechaInicioVentaOnline,
                 DateUtils.dateToTimestampSafe(sesion.getFechaInicioVentaOnline()))
            .set(qSesionDTO.horaApertura, sesion.getHoraApertura())
            .set(qSesionDTO.parEvento, Evento.eventoToEventoDTO(sesion.getEvento()))
            .set(qSesionDTO.parPlantilla, Plantilla.plantillaPreciosToPlantillaPreciosDTO(sesion.getPlantillaPrecios()))
            .set(qSesionDTO.nombre, sesion.getNombre())
            .set(qSesionDTO.formato, sesion.getFormato());
        
         if (sesion.getSala() != null && sesion.getSala().getId()!=0)
             update.set(qSesionDTO.parSala, Sala.salaToSalaDTO(sesion.getSala()));
        
         update.where(qSesionDTO.id.eq(sesion.getId())).execute();
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
    public List<PreciosSesionDTO> getPreciosSesion(long sesionId, String sortParameter, int start, int limit)
    {
		return getQueryPreciosSesion(sesionId).orderBy(getSort(qPreciosSesionDTO, sortParameter)).offset(start).limit(limit).list(qPreciosSesionDTO);
    }

	@Transactional
	private JPAQuery getQueryPreciosSesion(long sesionId) {
		JPAQuery query = new JPAQuery(entityManager);
		return query.from(qPreciosSesionDTO).where(qPreciosSesionDTO.parSesione.id.eq(sesionId));
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

	@Transactional
	public int getTotalSesionesActivas(Long eventoId) {
		return (int) getQuerySesionesActivas(eventoId).count();
	}

	@Transactional
	public int getTotalSesiones(Long eventoId) {
		return (int) getQuerySesiones(eventoId).count();
	}

	@Transactional
	public int getTotalPreciosSesion(Long sesionId) {
		return (int) getQueryPreciosSesion(sesionId).count();
	}
}
