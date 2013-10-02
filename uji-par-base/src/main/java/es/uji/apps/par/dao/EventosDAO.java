package es.uji.apps.par.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.Tuple;
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPASubQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.QTuple;
import com.mysema.query.types.path.StringPath;

import es.uji.apps.par.db.EventoDTO;
import es.uji.apps.par.db.QEventoDTO;
import es.uji.apps.par.db.QSesionDTO;
import es.uji.apps.par.db.QTipoEventoDTO;
import es.uji.apps.par.db.SesionDTO;
import es.uji.apps.par.db.TipoEventoDTO;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.Sesion;
import es.uji.apps.par.model.TipoEvento;

@Repository
public class EventosDAO extends BaseDAO
{
    private QEventoDTO qEventoDTO = QEventoDTO.eventoDTO;

    @Transactional
    public List<Evento> getEventos(String sortParameter, int start, int limit)
    {
        return getEventosConPrimeraFechaCelebracion(false, sortParameter, start, limit);
    }
    
    @Transactional
    public List<Evento> getEventosActivos(String sortParameter, int start, int limit)
    {
        return getEventosConPrimeraFechaCelebracion(true, sortParameter, start, limit);
    }
    
    @SuppressWarnings({ "rawtypes" })
	protected OrderSpecifier<String> getSort(EntityPath entity, String sortParameter, EntityPath entityOpcional) {
    	
        if (hasSort(sortParameter) && !sortParameter.contains("fechaPrimeraSesion"))
        {
    		return super.getSort(entity, sortParameter);
        }
        else
        {
            StringPath strPath = new StringPath(entityOpcional, "fechaCelebracion");
            return sortParameter.contains("DESC")? strPath.desc():strPath.asc();
        }
    }
    
    @Transactional
    public List<Evento> getEventosConSesiones()
    {
        QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;
        JPAQuery query = new JPAQuery(entityManager);
        List<EventoDTO> eventos = query.from(qEventoDTO).leftJoin(qEventoDTO.parSesiones, qSesionDTO).fetch().list(qEventoDTO);
        
        // En la consulta no podemos usar el distinct por culpa del BLOB
        eventos = eliminaRepetidos(eventos);
        
        return toEventosConSesiones(eventos);
    }
    
	private List<EventoDTO> eliminaRepetidos(List<EventoDTO> eventos)
    {
	    List<EventoDTO> result = new ArrayList<EventoDTO>();
	    Set<Long> idsProcesados = new HashSet<Long>();
	    
	    for (EventoDTO eventoDTO : eventos)
        {
	        if (!idsProcesados.contains(eventoDTO.getId()))
	        {
	            result.add(eventoDTO);
	            idsProcesados.add(eventoDTO.getId());
	        }
            
        }
	    
        return result;
    }

    private List<Evento> toEventosConSesiones(List<EventoDTO> eventosDTO)
    {
	    List<Evento> eventos = new ArrayList<Evento>();
	    
	    for (EventoDTO eventoDTO: eventosDTO)
	    {
	        Evento evento = Evento.eventoDTOtoEvento(eventoDTO);
	        
	        List<Sesion> sesiones = new ArrayList<Sesion>();
	        
	        if (eventoDTO.getParSesiones() != null)
	        {
                for (SesionDTO sesionDTO:eventoDTO.getParSesiones())
                {
                    sesiones.add(Sesion.SesionDTOToSesionSinEvento(sesionDTO));
                }
	        }
	        
	        evento.setSesiones(sesiones);
	        
	        eventos.add(evento);
	    }

	    return eventos;
    }

    @Transactional
    private List<Evento> getEventosConPrimeraFechaCelebracion(boolean activos, String sortParameter, int start, int limit)
    {
    	QSesionDTO qSesion = QSesionDTO.sesionDTO;
        List<Object[]> listadoTuplasConFecha;
        JPAQuery query = new JPAQuery();
        OrderSpecifier<String> sort;
    	JPASubQuery subquery = new JPASubQuery();
    	subquery.from(qSesion);
    	
    	//es necesario tenerlo por separado porque se evita enviar la columna blob de la imagen (da problemas cuando en la query hay distinct, order by...)
    	QTuple fields = new QTuple(qEventoDTO.caracteristicasEs, qEventoDTO.caracteristicasVa,
                qEventoDTO.comentariosEs, qEventoDTO.comentariosVa, qEventoDTO.companyiaEs,
                qEventoDTO.companyiaVa, qEventoDTO.descripcionEs, qEventoDTO.descripcionVa,
                qEventoDTO.duracionEs, qEventoDTO.duracionVa, qEventoDTO.id,
                qEventoDTO.parTiposEvento, qEventoDTO.interpretesEs,
                qEventoDTO.interpretesVa, qEventoDTO.premiosEs, qEventoDTO.premiosVa,
                qEventoDTO.tituloEs, qEventoDTO.tituloVa, qEventoDTO.imagenSrc,
                qEventoDTO.imagenContentType, qEventoDTO.asientosNumerados, qEventoDTO.retencionSgae, qEventoDTO.ivaSgae, qEventoDTO.porcentajeIva, qEventoDTO.rssId, 
                qSesion.fechaCelebracion);
    	
   		sort = getSort(qEventoDTO, sortParameter, qSesion);
        query  = (activos)?getQueryEventosActivos():getQueryEventos();
        
		listadoTuplasConFecha = query.offset(start).limit(limit).orderBy(sort).
        	listDistinct(fields, subquery.where(qEventoDTO.id.eq(qSesion.parEvento.id)).unique(qSesion.fechaCelebracion.min()));
		return listadoTuplasConFechaAListadoEventos(listadoTuplasConFecha);
    }
	
	@Transactional
	private JPAQuery getQueryEventos() {
    	QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;
		JPAQuery query = new JPAQuery(entityManager);
		return query.from(qEventoDTO).leftJoin(qEventoDTO.parSesiones, qSesionDTO).distinct();
	}

    @Transactional
	private JPAQuery getQueryEventosActivos() {
		QTipoEventoDTO qTipoEventoDTO = QTipoEventoDTO.tipoEventoDTO;
        QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;
		JPAQuery query = new JPAQuery(entityManager);
		Timestamp now = new Timestamp(new Date().getTime());
		return query
		    .from(qEventoDTO, qTipoEventoDTO)
		    .innerJoin(qEventoDTO.parSesiones, qSesionDTO)
		    .distinct()
		    .where(qEventoDTO.parTiposEvento.id.eq(qTipoEventoDTO.id).and(qSesionDTO.fechaCelebracion.after(now)));
	}

	@Transactional
	private List<Evento> listadoTuplasConFechaAListadoEventos(List<Object[]> listadoTuplasConFecha) {
		List<Evento> listadoEventos = new ArrayList<Evento>();
		for (Object[] eventoConFecha : listadoTuplasConFecha) {
			Tuple tuplas = (Tuple) eventoConFecha[0];
			Date fechaPrimeraSesion = (Date) eventoConFecha[1];
			Evento evento = tuplasToEvento(tuplas);
			evento.setFechaPrimeraSesion(fechaPrimeraSesion);
			listadoEventos.add(evento);
		}
		return listadoEventos;
	}

	@Transactional
    private Evento tuplasToEvento(Tuple tupla) {
    	Evento evento = new Evento();

        evento.setCaracteristicasEs(tupla.get(qEventoDTO.caracteristicasEs));
        evento.setCaracteristicasVa(tupla.get(qEventoDTO.caracteristicasVa));

        evento.setComentariosEs(tupla.get(qEventoDTO.comentariosEs));
        evento.setComentariosVa(tupla.get(qEventoDTO.comentariosVa));

        evento.setCompanyiaEs(tupla.get(qEventoDTO.companyiaEs));
        evento.setCompanyiaVa(tupla.get(qEventoDTO.companyiaVa));

        evento.setDescripcionEs(tupla.get(qEventoDTO.descripcionEs));
        evento.setDescripcionVa(tupla.get(qEventoDTO.descripcionVa));

        evento.setDuracionEs(tupla.get(qEventoDTO.duracionEs));
        evento.setDuracionVa(tupla.get(qEventoDTO.duracionVa));

        if (tupla.get(qEventoDTO.parTiposEvento) != null) {
            evento.setTipoEvento(tupla.get(qEventoDTO.parTiposEvento).getId());
            evento.setParTipoEvento(TipoEvento.tipoEventoDTOToTipoEvento(tupla.get(qEventoDTO.parTiposEvento)));
        }

        evento.setPremiosEs(tupla.get(qEventoDTO.premiosEs));
        evento.setPremiosVa(tupla.get(qEventoDTO.premiosVa));

        evento.setTituloEs(tupla.get(qEventoDTO.tituloEs));
        evento.setTituloVa(tupla.get(qEventoDTO.tituloVa));

        evento.setImagenSrc(tupla.get(qEventoDTO.imagenSrc));
        evento.setImagenContentType(tupla.get(qEventoDTO.imagenContentType));

        evento.setId(tupla.get(qEventoDTO.id));
        
        evento.setAsientosNumerados(tupla.get(qEventoDTO.asientosNumerados));
        evento.setIvaSGAE(tupla.get(qEventoDTO.ivaSgae));
        evento.setRetencionSGAE(tupla.get(qEventoDTO.retencionSgae));
        evento.setPorcentajeIVA(tupla.get(qEventoDTO.porcentajeIva));
        
        evento.setRssId(tupla.get(qEventoDTO.rssId));

        return evento;
	}

    @Transactional
    public List<EventoDTO> getEventoDTO(Long id)
    {
        QTipoEventoDTO qTipoEventoDTO = QTipoEventoDTO.tipoEventoDTO;
        JPAQuery query = new JPAQuery(entityManager);

        return query
                .from(qEventoDTO, qTipoEventoDTO)
                .where(qEventoDTO.parTiposEvento.id.eq(qTipoEventoDTO.id).and(qEventoDTO.id.eq(id)))
                .list(qEventoDTO);
    }

    @Transactional
    public long removeEvento(long id)
    {
        JPADeleteClause delete = new JPADeleteClause(entityManager, qEventoDTO);
        return delete.where(qEventoDTO.id.eq(id)).execute();
    }

    @Transactional
    public Evento addEvento(Evento evento)
    {
        EventoDTO eventoDTO = new EventoDTO();
        eventoDTO = rellenarParEventoDTOConParEvento(evento, eventoDTO);

        entityManager.persist(eventoDTO);

        evento.setId(eventoDTO.getId());
        return evento;
    }

    @Transactional
    private EventoDTO rellenarParEventoDTOConParEvento(Evento evento, EventoDTO eventoDTO)
    {
        eventoDTO.setCaracteristicasEs(evento.getCaracteristicasEs());
        eventoDTO.setCaracteristicasVa(evento.getCaracteristicasVa());

        eventoDTO.setComentariosEs(evento.getComentariosEs());
        eventoDTO.setComentariosVa(evento.getComentariosVa());

        eventoDTO.setCompanyiaEs(evento.getCompanyiaEs());
        eventoDTO.setCompanyiaVa(evento.getCompanyiaVa());

        eventoDTO.setDescripcionEs(evento.getDescripcionEs());
        eventoDTO.setDescripcionVa(evento.getDescripcionVa());

        eventoDTO.setDuracionEs(evento.getDuracionEs());
        eventoDTO.setDuracionVa(evento.getDuracionVa());

        if (evento.getImagen() != null && evento.getImagen().length > 0)
        {
            eventoDTO.setImagen(evento.getImagen());
            eventoDTO.setImagenSrc(evento.getImagenSrc());
            eventoDTO.setImagenContentType(evento.getImagenContentType());
        }

        if (evento.getParTiposEvento() != null)
        {
            TipoEventoDTO parTipoEventoDTO = new TipoEventoDTO();
            parTipoEventoDTO.setId(evento.getParTiposEvento().getId());
            eventoDTO.setParTiposEvento(parTipoEventoDTO);
        }
        eventoDTO.setPremiosEs(evento.getPremiosEs());
        eventoDTO.setPremiosVa(evento.getPremiosVa());

        eventoDTO.setTituloEs(evento.getTituloEs());
        eventoDTO.setTituloVa(evento.getTituloVa());
        
        eventoDTO.setPorcentajeIva(evento.getPorcentajeIVA());
        eventoDTO.setAsientosNumerados(evento.getAsientosNumerados());
        eventoDTO.setRetencionSgae(evento.getRetencionSGAE());
        eventoDTO.setIvaSgae(evento.getIvaSGAE());

        if (evento.getId() != 0)
            eventoDTO.setId(evento.getId());

        return eventoDTO;
    }

    @Transactional
    public Evento updateEvento(Evento evento)
    {
        List<EventoDTO> listaEventos = getEventoDTO(evento.getId());

        if (listaEventos.size() > 0)
        {
            EventoDTO eventoDTO = listaEventos.get(0);
            eventoDTO.setRetencionSgae(evento.getRetencionSGAE());
            eventoDTO.setIvaSgae(evento.getIvaSGAE());
            eventoDTO.setPorcentajeIva(evento.getPorcentajeIVA());

            entityManager.persist(eventoDTO);
        }

        return evento;
    }
    
    @Transactional
    public void updateEventoDTO(EventoDTO eventoDTO)
    {
        entityManager.persist(eventoDTO);
    }
    
    @Transactional
    public void deleteImagen(long eventoId)
    {
        JPAUpdateClause jpaUpdate = new JPAUpdateClause(entityManager, qEventoDTO);

        jpaUpdate.setNull(qEventoDTO.imagen).setNull(qEventoDTO.imagenContentType)
                .setNull(qEventoDTO.imagenSrc).where(qEventoDTO.id.eq(eventoId)).execute();
    }

    @Transactional
	public EventoDTO getEventoById(long eventoId) {
		return entityManager.find(EventoDTO.class, eventoId);
	}
    
    @Transactional
    public EventoDTO getEventoByRssId(String rssId) {
        
        QTipoEventoDTO qTipoEvento = QTipoEventoDTO.tipoEventoDTO;
        
        JPAQuery query = new JPAQuery(entityManager);

         List<EventoDTO> eventos = query
                .from(qEventoDTO)
                .leftJoin(qEventoDTO.parTiposEvento, qTipoEvento)
                .fetch()
                .where(qEventoDTO.rssId.eq(rssId))
                .list(qEventoDTO);
         
         if (eventos.size() == 0)
             return null;
         else if (eventos.size() == 1)
             return eventos.get(0);
         else
             throw new RuntimeException("Hay varios eventos con el mismo RSS_ID");
    }

    @Transactional
	public int getTotalEventosActivos() {
		return (int) getQueryEventosActivos().count();
	}

    @Transactional
	public int getTotalEventos() {
		return (int) getQueryEventos().count();
	}
}
