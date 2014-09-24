package es.uji.apps.par.dao;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.jpa.sql.JPASQLQuery;
import es.uji.apps.par.db.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.path.StringPath;

import es.uji.apps.par.database.DatabaseHelper;
import es.uji.apps.par.database.DatabaseHelperFactory;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.Sesion;
import es.uji.apps.par.model.TipoEvento;
import es.uji.apps.par.utils.DateUtils;

@Repository
public class EventosDAO extends BaseDAO
{
    private QEventoDTO qEventoDTO = QEventoDTO.eventoDTO;
    private QEventoMultisesionDTO qEventoMultisesionDTO = QEventoMultisesionDTO.eventoMultisesionDTO;
    
    private DatabaseHelper databaseHelper;

    public EventosDAO()
    {
        databaseHelper = DatabaseHelperFactory.newInstance();
    }

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
    		return super.getSort(entity, sortParameter);

		StringPath strPath = new StringPath(entityOpcional, "fechaCelebracion");
		return strPath.desc();
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
        String sql = "select distinct e.CARACTERISTICAS_ES, e.CARACTERISTICAS_VA, e.COMENTARIOS_ES, e.COMENTARIOS_VA, e.COMPANYIA_ES, e.COMPANYIA_VA, " +
        		" e.DESCRIPCION_ES, e.DESCRIPCION_VA, e.DURACION_ES, e.DURACION_VA, t.ID as tipoId, t.NOMBRE_ES as parTiposEvento, t.NOMBRE_VA , e.PREMIOS_ES, " +
        		" e.PREMIOS_VA, e.TITULO_ES as tituloes, e.TITULO_VA as titulova, e.IMAGEN_SRC as imagensrc, e.IMAGEN_CONTENT_TYPE, e.ID, e.ASIENTOS_NUMERADOS as asientosnumerados, " +
        		" e.RETENCION_SGAE as retencionsgae, e.IVA_SGAE as ivasgae, e.PORCENTAJE_IVA as porcentajeiva, " +
        		" e.RSS_ID as rssid, (select min(s.FECHA_CELEBRACION) from PAR_SESIONES s where e.id=s.EVENTO_ID) as fechaPrimeraSesion, " +
        		" e.EXPEDIENTE, e.COD_DISTRI, e.NOM_DISTRI, e.NACIONALIDAD, e.VO, e.METRAJE, e.SUBTITULOS, t.exportar_icaa, e.formato, " +
				" (select count(*) from par_eventos_multisesion pem where pem.evento_id = e.id) " +
        		" from PAR_EVENTOS e left outer join PAR_SESIONES s on e.id=s.EVENTO_ID inner join PAR_TIPOS_EVENTO t on e.TIPO_EVENTO_ID=t.id " +
        		(activos?getWhereActivos():getWhereTodos()) +
        		" order by ";
        
        Type type = new TypeToken<List<Map<String,String>>>(){}.getType();
        List<Map<String,String>> order = new Gson().fromJson(sortParameter, type);
        
        sql += order.get(0).get("property") + " " + order.get(0).get("direction");
        
        String sqlPaginado = paginaConsulta(start, limit, sql);
        
        Query query = entityManager.createNativeQuery(sqlPaginado);
        
        List<Object[]> result = query.getResultList();
        
        return listadoTuplasConFechaAListadoEventos(result);
    }

    private String paginaConsulta(int start, int limit, String sql)
    {
        return databaseHelper.paginate(start, limit, sql);
    }
    
    private String getWhereTodos()
    {
        return "";
    }

    private String getWhereActivos()
    {
        return " where s.FECHA_CELEBRACION >= TO_DATE('" + DateUtils.dateToSpanishStringWithHour(DateUtils.dateConMargenTrasVenta()) + "','DD/MM/YYYY HH24:MI') ";
    }
    
    @Transactional
    private Evento objectArrayToEvento(Object [] array) {
        Evento evento = new Evento();

        evento.setCaracteristicasEs((String)array[0]);
        evento.setCaracteristicasVa((String)array[1]);

        evento.setComentariosEs((String)array[2]);
        evento.setComentariosVa((String)array[3]);

        evento.setCompanyiaEs((String)array[4]);
        evento.setCompanyiaVa((String)array[5]);

        evento.setDescripcionEs((String)array[6]);
        evento.setDescripcionVa((String)array[7]);

        evento.setDuracionEs((String)array[8]);
        evento.setDuracionVa((String)array[9]);

        if (array[10] != null) {

            TipoEvento tipoEvento = new TipoEvento();
            tipoEvento.setId(databaseHelper.castId(array[10]));
            tipoEvento.setNombreEs((String) array[11]);
            tipoEvento.setNombreVa((String) array[12]);
            tipoEvento.setExportarICAA(databaseHelper.castBoolean(array[33]));
            evento.setParTipoEvento(tipoEvento);
        }

        evento.setPremiosEs((String) array[13]);
        evento.setPremiosVa((String) array[14]);

        evento.setTituloEs((String) array[15]);
        evento.setTituloVa((String) array[16]);

        evento.setImagenSrc((String) array[17]);
        evento.setImagenContentType((String) array[18]);

        evento.setId(databaseHelper.castId(array[19]));
        
        evento.setAsientosNumerados(databaseHelper.castBoolean(array[20]));
        evento.setRetencionSGAE(databaseHelper.castBigDecimal(array[21]));
        evento.setIvaSGAE(databaseHelper.castBigDecimal(array[22]));
        evento.setPorcentajeIVA(databaseHelper.castBigDecimal(array[23]));
        
        evento.setRssId((String) array[24]);
        
        Timestamp ts = (Timestamp) array[25];
        if (ts != null)
            evento.setFechaPrimeraSesion(new Date(ts.getTime()));
        
        evento.setExpediente((String) array[26]);
        evento.setCodigoDistribuidora((String) array[27]);
        evento.setNombreDistribuidora((String) array[28]);
        evento.setNacionalidad((String) array[29]);
        evento.setVo((String) array[30]);
        evento.setMetraje((String) array[31]);
        evento.setSubtitulos((String) array[32]);
        evento.setFormato((String) array[34]);

		if (array.length >= 36) {
			BigDecimal numeroEventosHijos = databaseHelper.castBigDecimal(array[35]);
			evento.setMultisesion((numeroEventosHijos.compareTo(BigDecimal.ZERO)==0)?"":"on");
		}

        return evento;
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
		
		Timestamp now = new Timestamp(DateUtils.dateConMargenTrasVenta().getTime());
		
		return query
		    .from(qEventoDTO, qTipoEventoDTO)
		    .innerJoin(qEventoDTO.parSesiones, qSesionDTO)
		    .distinct()
		    .where(qEventoDTO.parTiposEvento.id.eq(qTipoEventoDTO.id).and(qSesionDTO.fechaCelebracion.after(now)));
	}
	@Transactional
	private List<Evento> listadoTuplasConFechaAListadoEventos(List<Object[]> listEventoFechaSesion) {
		List<Evento> listadoEventos = new ArrayList<Evento>();
		for (Object[] eventoConFecha : listEventoFechaSesion) {
			
			Evento evento = objectArrayToEvento(eventoConFecha);
			listadoEventos.add(evento);
		}
		return listadoEventos;
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
		updateEventosMultisesion(eventoDTO.getId(), evento.getEventosMultisesion());
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

        eventoDTO.setInterpretesEs(evento.getInterpretesEs());
        eventoDTO.setInterpretesVa(evento.getInterpretesVa());

        if (evento.getParTiposEvento() != null)
        {
            TipoEventoDTO parTipoEventoDTO = new TipoEventoDTO();
            parTipoEventoDTO.setId(evento.getParTiposEvento().getId());
            eventoDTO.setParTiposEvento(parTipoEventoDTO);
        }
        eventoDTO.setPremiosEs(evento.getPremiosEs());
        eventoDTO.setPremiosVa(evento.getPremiosVa());

        eventoDTO.setRssId(evento.getRssId());
        eventoDTO.setTituloEs(evento.getTituloEs());
        eventoDTO.setTituloVa(evento.getTituloVa());
        
        eventoDTO.setPorcentajeIva(evento.getPorcentajeIVA());
        eventoDTO.setAsientosNumerados(evento.getAsientosNumerados());
        eventoDTO.setRetencionSgae(evento.getRetencionSGAE());
        eventoDTO.setIvaSgae(evento.getIvaSGAE());

        eventoDTO.setExpediente(evento.getExpediente());
        eventoDTO.setFormato(evento.getFormato());
        eventoDTO.setCodigoDistribuidora(evento.getCodigoDistribuidora());
        eventoDTO.setNombreDistribuidora(evento.getNombreDistribuidora());
        eventoDTO.setNacionalidad(evento.getNacionalidad());
        eventoDTO.setVo(evento.getVo());
        eventoDTO.setMetraje(evento.getMetraje());
        eventoDTO.setSubtitulos(evento.getSubtitulos());
        
        if (evento.getId() != 0)
            eventoDTO.setId(evento.getId());

        return eventoDTO;
    }

    @Transactional
    public Evento updateEvento(Evento evento)
    {
		EventoDTO eventoDTO = getEventoById(evento.getId());

		eventoDTO.setRssId(evento.getRssId());
		eventoDTO.setTituloEs(evento.getTituloEs());
		eventoDTO.setTituloVa(evento.getTituloVa());
		eventoDTO.setDescripcionEs(evento.getDescripcionEs());
		eventoDTO.setDescripcionVa(evento.getDescripcionVa());
		eventoDTO.setComentariosEs(evento.getComentariosEs());
		eventoDTO.setComentariosVa(evento.getComentariosVa());
		eventoDTO.setInterpretesEs(evento.getInterpretesEs());
		eventoDTO.setInterpretesVa(evento.getInterpretesVa());
		eventoDTO.setDuracionEs(evento.getDuracionEs());
		eventoDTO.setDuracionVa(evento.getDuracionVa());
		eventoDTO.setPremiosEs(evento.getPremiosEs());
		eventoDTO.setPremiosVa(evento.getPremiosVa());
		eventoDTO.setCaracteristicasEs(evento.getCaracteristicasEs());
		eventoDTO.setCaracteristicasVa(evento.getCaracteristicasVa());
		eventoDTO.setComentariosEs(evento.getComentariosEs());
		eventoDTO.setComentariosVa(evento.getComentariosVa());

		eventoDTO.setRetencionSgae(evento.getRetencionSGAE());
		eventoDTO.setIvaSgae(evento.getIvaSGAE());
		eventoDTO.setPorcentajeIva(evento.getPorcentajeIVA());

		eventoDTO.setAsientosNumerados(evento.getAsientosNumerados());
		eventoDTO.setExpediente(evento.getExpediente());
        eventoDTO.setFormato(evento.getFormato());
		eventoDTO.setCodigoDistribuidora(evento.getCodigoDistribuidora());
		eventoDTO.setNombreDistribuidora(evento.getNombreDistribuidora());
		eventoDTO.setNacionalidad(evento.getNacionalidad());
		eventoDTO.setVo(evento.getVo());
		eventoDTO.setMetraje(evento.getMetraje());
		eventoDTO.setSubtitulos(evento.getSubtitulos());

		eventoDTO.setParTiposEvento(TipoEvento.tipoEventoToTipoEventoDTO(evento.getParTiposEvento()));

		if (evento.getImagenSrc() != null && !evento.getImagenSrc().equals("")) {
			eventoDTO.setImagen(evento.getImagen());
			eventoDTO.setImagenSrc(evento.getImagenSrc());
			eventoDTO.setImagenContentType(evento.getImagenContentType());
		}

		entityManager.persist(eventoDTO);

		updateEventosMultisesion(evento.getId(), evento.getEventosMultisesion());
        return evento;
    }

	@Transactional
	private void updateEventosMultisesion(long idEvento, List<Evento> eventosMultisesion) {
		JPADeleteClause del = new JPADeleteClause(entityManager, qEventoMultisesionDTO);
		del.where(qEventoMultisesionDTO.parEvento.id.eq(idEvento)).execute();

		if (eventosMultisesion != null) {
			for (Evento eventoHijo: eventosMultisesion) {
				EventoMultisesionDTO eventoMultisesionDTO = new EventoMultisesionDTO();
				eventoMultisesionDTO.setParEvento(new EventoDTO(idEvento));
				eventoMultisesionDTO.setParEventoHijo(new EventoDTO(eventoHijo.getId()));
				entityManager.persist(eventoMultisesionDTO);
			}
		}
	}

	@Transactional
    public EventoDTO updateEventoDTO(EventoDTO eventoDTO)
    {
       return entityManager.merge(eventoDTO);
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
                .leftJoin(qEventoDTO.parTiposEvento, qTipoEvento).fetch()
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

    @Transactional
	public List<EventoDTO> getEventosActivosParaVentaOnline() {
		JPAQuery query = new JPAQuery(entityManager);
		QSesionDTO qSesionDTO = new QSesionDTO("qSesionDTO");
		Calendar cal = Calendar.getInstance();
		Timestamp currentTime = new Timestamp(cal.getTime().getTime());
		
		return query.from(qEventoDTO).join(qEventoDTO.parSesiones, qSesionDTO).where(
			qSesionDTO.canalInternet.eq(true).and(
			qSesionDTO.fechaInicioVentaOnline.before(currentTime).and(
			qSesionDTO.fechaFinVentaOnline.after(currentTime)))).list(qEventoDTO);
	}

	@Transactional
	public List<EventoDTO> getPeliculas() {
		JPAQuery query = new JPAQuery(entityManager);
		QTipoEventoDTO tiposICAA = new QTipoEventoDTO("tiposICAA");

		return query.from(qEventoDTO).where(
				qEventoDTO.parTiposEvento.id.in(
						new JPASubQuery().from(tiposICAA).where(tiposICAA.exportarICAA.eq(true)).list(tiposICAA.id)
				)
		).orderBy(qEventoDTO.tituloEs.asc()).orderBy(qEventoDTO.tituloVa.asc()).list(qEventoDTO);
	}

	@Transactional
	public List<EventoDTO> getPeliculas(long eventoId) {
		JPAQuery query = new JPAQuery(entityManager);

		return query.from(qEventoMultisesionDTO).where(qEventoMultisesionDTO.parEvento.id.eq(eventoId)).orderBy
				(qEventoMultisesionDTO.id.asc()).list(qEventoMultisesionDTO.parEventoHijo);
	}
}
