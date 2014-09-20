package es.uji.apps.par.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.uji.apps.par.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.Tuple;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.types.expr.BooleanExpression;

import es.uji.apps.par.IncidenciaNotFoundException;
import es.uji.apps.par.SesionSinFormatoIdiomaIcaaException;
import es.uji.apps.par.ficheros.registros.RegistroPelicula;
import es.uji.apps.par.ficheros.registros.RegistroSesion;
import es.uji.apps.par.ficheros.registros.RegistroSesionPelicula;
import es.uji.apps.par.ficheros.registros.TipoIncidencia;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.Plantilla;
import es.uji.apps.par.model.Sala;
import es.uji.apps.par.model.Sesion;
import es.uji.apps.par.utils.DateUtils;
import es.uji.apps.par.utils.Utils;

@Repository
public class SesionesDAO extends BaseDAO
{
    private static SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("HHmm");
    
    private QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;
    private QPreciosSesionDTO qPreciosSesionDTO = QPreciosSesionDTO.preciosSesionDTO;
    
    @Autowired
    private EventosDAO eventosDAO;

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
            sesion = getQuerySesionesActivas(eventoId).orderBy(getSort(qSesionDTO, sortParameter)).offset(start)
                    .limit(limit).list(qSesionDTO);
        else
            sesion = getQuerySesiones(eventoId).orderBy(getSort(qSesionDTO, sortParameter)).offset(start).limit(limit)
                    .list(qSesionDTO);

        return sesion;
    }

    @Transactional
    public List<Tuple> getSesionesConButacasVendidas(long eventoId, boolean activas, String sortParameter,
            int start, int limit)
    {
        QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;
        QButacaDTO qButacaDTO = QButacaDTO.butacaDTO;
        QCompraDTO qCompraDTO = QCompraDTO.compraDTO;

        JPAQuery query;

        if (activas)
            query = getQuerySesionesActivas(eventoId);
        else
            query = getQuerySesiones(eventoId);

        JPASubQuery queryVendidas = new JPASubQuery();
        queryVendidas.from(qButacaDTO, qCompraDTO);
        queryVendidas.where(qSesionDTO.id.eq(qButacaDTO.parSesion.id).and(qButacaDTO.anulada.eq(false).or(qButacaDTO.anulada.isNull())).
        		and(qButacaDTO.parCompra.id.eq(qCompraDTO.id).and(qCompraDTO.reserva.eq(false))));
        
        JPASubQuery queryReservadas = new JPASubQuery();
        queryReservadas.from(qButacaDTO, qCompraDTO);
        queryReservadas.where(qSesionDTO.id.eq(qButacaDTO.parSesion.id).and(qButacaDTO.anulada.eq(false).or(qButacaDTO.anulada.isNull())).
        		and(qButacaDTO.parCompra.id.eq(qCompraDTO.id).and(qCompraDTO.reserva.eq(true))));

        List<Tuple> sesiones = query.orderBy(getSort(qSesionDTO, sortParameter))/*.offset(start).limit(limit)*/
                .list(qSesionDTO, queryVendidas.list(qButacaDTO).count(), queryReservadas.list(qButacaDTO).count());

        return sesiones;
    }

    @Transactional
    private JPAQuery getQuerySesionesActivas(long eventoId)
    {
        QSalaDTO qSalaDTO = QSalaDTO.salaDTO;

        JPAQuery query = new JPAQuery(entityManager);

        Timestamp now = new Timestamp(DateUtils.dateConMargenTrasVenta().getTime());

        return query.from(qSesionDTO).leftJoin(qSesionDTO.parSala, qSalaDTO).fetch()
                .where(qSesionDTO.parEvento.id.eq(eventoId).and(qSesionDTO.fechaCelebracion.after(now)));
    }

    @Transactional
    private JPAQuery getQuerySesiones(long eventoId)
    {
    	QSalaDTO qSalaDTO = QSalaDTO.salaDTO;
        JPAQuery query = new JPAQuery(entityManager);
        return query.from(qSesionDTO).leftJoin(qSesionDTO.parSala, qSalaDTO).fetch().
        		where(qSesionDTO.parEvento.id.eq(eventoId));
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
        addSesionFormatoIdiomaIfNeeded(sesion.getEvento().getId(), sesion.getFormato(), sesion.getVersionLinguistica());
        return sesion;
    }
    
    @Transactional
    private void addSesionFormatoIdiomaIfNeeded(long eventoId, String formato, String versionLinguistica) {
		EventoDTO eventoDTO = eventosDAO.getEventoById(eventoId);
        if (eventoDTO.getParTiposEvento().getExportarICAA()) {
        	if (formato != null && versionLinguistica != null) {
		        List<SesionFormatoIdiomaICAADTO> sesionesFormatIdiomaICAA = 
		        		getSesionFormatoIdiomaIcaa(formato, versionLinguistica, eventoId);
		        
		        if (sesionesFormatIdiomaICAA.size() == 0)
		        	addSesionFormatoIdiomaICAA(formato, versionLinguistica, eventoId);
        	}
        }
	}

    @Transactional
	private void addSesionFormatoIdiomaICAA(String formato,	String versionLinguistica, long eventoId) {
		SesionFormatoIdiomaICAADTO sesionFormatoIdiomaICAA = new SesionFormatoIdiomaICAADTO();
		sesionFormatoIdiomaICAA.setFormato(formato);
		sesionFormatoIdiomaICAA.setVersionLinguistica(versionLinguistica);
		sesionFormatoIdiomaICAA.setParEvento(new EventoDTO(eventoId));
		entityManager.persist(sesionFormatoIdiomaICAA);
	}

    @Transactional
	public List<SesionFormatoIdiomaICAADTO> getSesionFormatoIdiomaIcaa(String formato, String versionLinguistica, long eventoId) {
		QSesionFormatoIdiomaICAADTO qSesionFormatoIdiomaICAA = QSesionFormatoIdiomaICAADTO.sesionFormatoIdiomaICAADTO;
        JPAQuery query = new JPAQuery(entityManager);
        return query.from(qSesionFormatoIdiomaICAA).where(
        	qSesionFormatoIdiomaICAA.formato.eq(formato)
        	.and(qSesionFormatoIdiomaICAA.versionLinguistica.eq(versionLinguistica))
        	.and(qSesionFormatoIdiomaICAA.parEvento.id.eq(eventoId))
        ).list(qSesionFormatoIdiomaICAA);
	}

    @Transactional
    public void updateSesion(Sesion sesion)
    {
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qSesionDTO);
        update.set(qSesionDTO.canalInternet, sesion.getCanalInternet())
                .set(qSesionDTO.canalTaquilla, sesion.getCanalTaquilla())
                .set(qSesionDTO.fechaCelebracion, DateUtils.dateToTimestampSafe(DateUtils.addTimeToDate(sesion.getFechaCelebracion(), sesion.getHoraCelebracion())))
                .set(qSesionDTO.fechaFinVentaOnline, DateUtils.dateToTimestampSafe(DateUtils.addTimeToDate(sesion.getFechaFinVentaOnline(), sesion.getHoraFinVentaOnline())))
                .set(qSesionDTO.fechaInicioVentaOnline,
                		DateUtils.dateToTimestampSafe(DateUtils.addTimeToDate(sesion.getFechaInicioVentaOnline(), sesion.getHoraInicioVentaOnline())))
                .set(qSesionDTO.horaApertura, sesion.getHoraApertura())
                .set(qSesionDTO.parEvento, Evento.eventoToEventoDTO(sesion.getEvento()))
                .set(qSesionDTO.parPlantilla,
                        Plantilla.plantillaPreciosToPlantillaPreciosDTO(sesion.getPlantillaPrecios()))
                .set(qSesionDTO.nombre, sesion.getNombre())
                .set(qSesionDTO.formato, sesion.getFormato())
                .set(qSesionDTO.versionLinguistica, sesion.getVersionLinguistica())
                .set(qSesionDTO.rssId, sesion.getRssId());

        if (sesion.getSala() != null && sesion.getSala().getId() != 0)
            update.set(qSesionDTO.parSala, Sala.salaToSalaDTO(sesion.getSala()));

        update.where(qSesionDTO.id.eq(sesion.getId())).execute();
        addSesionFormatoIdiomaIfNeeded(sesion.getEvento().getId(), sesion.getFormato(), sesion.getVersionLinguistica());
    }

    @Transactional
    public void addPrecioSesion(PreciosSesionDTO precioSesionDTO)
    {
        entityManager.persist(precioSesionDTO);
    }

    @Transactional
    public void deleteExistingPreciosSesion(long sesionId)
    {
   		JPADeleteClause delete = new JPADeleteClause(entityManager, qPreciosSesionDTO);
   		delete.where(qPreciosSesionDTO.parSesione.id.eq(sesionId)).execute();
    }

    @Transactional
    public List<PreciosSesionDTO> getPreciosSesion(long sesionId, String sortParameter, int start, int limit)
    {
        return getQueryPreciosSesion(sesionId).orderBy(getSort(qPreciosSesionDTO, sortParameter)).offset(start)
                .limit(limit).list(qPreciosSesionDTO);
    }

    @Transactional
    private JPAQuery getQueryPreciosSesion(long sesionId)
    {
    	QTarifaDTO qTarifa = QTarifaDTO.tarifaDTO;
        JPAQuery query = new JPAQuery(entityManager);
        return query.from(qPreciosSesionDTO, qTarifa).where(qPreciosSesionDTO.parSesione.id.eq(sesionId).
        		and(qTarifa.id.eq(qPreciosSesionDTO.parTarifa.id)));
    }

    @Transactional
    public PreciosSesionDTO persistPreciosSesion(PreciosSesionDTO precioSesionDTO)
    {
        entityManager.persist(precioSesionDTO);
        return precioSesionDTO;
    }

    @Transactional
    public SesionDTO getSesion(long sesionId)
    {
        JPAQuery query = new JPAQuery(entityManager);
        return query.from(qSesionDTO).
        		leftJoin(qSesionDTO.parPreciosSesions, qPreciosSesionDTO).fetch()
                .where(qSesionDTO.id.eq(sesionId)).uniqueResult(qSesionDTO);
    }

    @Transactional
    public int getTotalSesionesActivas(Long eventoId)
    {
        return (int) getQuerySesionesActivas(eventoId).count();
    }

    @Transactional
    public int getTotalSesiones(Long eventoId)
    {
        return (int) getQuerySesiones(eventoId).count();
    }

    @Transactional
    public int getTotalPreciosSesion(Long sesionId)
    {
        return (int) getQueryPreciosSesion(sesionId).count();
    }

    @Transactional
    public List<RegistroSesion> getRegistrosSesiones(List<Sesion> sesiones) throws IncidenciaNotFoundException
    {
        QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;
        QCompraDTO qCompraDTO = QCompraDTO.compraDTO;
        QSalaDTO qSalaDTO = QSalaDTO.salaDTO;
        QButacaDTO qButacaDTO = QButacaDTO.butacaDTO;
		QEventoMultisesionDTO eventoMultisesionDTO = new QEventoMultisesionDTO("eventoMultisesionDTO");

        List<Long> idsSesiones = Sesion.getIdsSesiones(sesiones);
        JPAQuery query = new JPAQuery(entityManager);
        
        List<Tuple> resultado = query
        	.from(qSesionDTO)
        	.join(qSesionDTO.parSala, qSalaDTO)
        	.leftJoin(qSesionDTO.parCompras, qCompraDTO).on(qCompraDTO.anulada.isNull().or(qCompraDTO.anulada.eq(false)))
        	.leftJoin(qCompraDTO.parButacas, qButacaDTO).on(qButacaDTO.anulada.isNull().or(qButacaDTO.anulada.eq(false)))
            .where(qSesionDTO.id.in(idsSesiones))
            .distinct()
            .groupBy(qSesionDTO.id, qSalaDTO.codigo, qSesionDTO.fechaCelebracion, qSesionDTO.incidenciaId)
            .list(qSesionDTO.id, qSalaDTO.codigo, qSesionDTO.fechaCelebracion, qButacaDTO.precio.sum(), qSesionDTO.incidenciaId,
					new JPASubQuery().from(eventoMultisesionDTO).where(eventoMultisesionDTO.parEvento.id.eq(qSesionDTO.parEvento
							.id)).count());
        
        List<RegistroSesion> registros = new ArrayList<RegistroSesion>();
        
        for (Tuple row: resultado)
        {
			int idIncidencia = Utils.safeObjectToInt(row.get(4, BigDecimal.class));

			if (!isIncidenciaCancelacionEvento(idIncidencia)) {
				long idSesion = row.get(0, Long.class);
				String codigoSala = (String) row.get(1, String.class);
				Date fechaCelebracion = row.get(2, Date.class);
				BigDecimal recaudacion = row.get(3, BigDecimal.class);
				Long peliculasMultisesion = row.get(5, Long.class);
				Long espectadores = getEspectadores(idSesion);

				RegistroSesion registro = new RegistroSesion();

				registro.setCodigoSala(codigoSala);
				registro.setEspectadores(espectadores.intValue());
				registro.setPeliculas((peliculasMultisesion == 0L)?1:peliculasMultisesion.intValue());
				registro.setFecha(fechaCelebracion);
				registro.setHora(HOUR_FORMAT.format(fechaCelebracion));
				registro.setIncidencia(TipoIncidencia.intToTipoIncidencia(idIncidencia));

				if (recaudacion == null)
					registro.setRecaudacion(BigDecimal.ZERO);
				else
					registro.setRecaudacion(recaudacion);

				registros.add(registro);
			}
		}

        return registros;
    }

    private Long getEspectadores(long sesionId) {
    	QButacaDTO qButacaDTO = QButacaDTO.butacaDTO;
    	JPAQuery query = new JPAQuery(entityManager);
    	return query.from(qButacaDTO).
    		where(qButacaDTO.parSesion.id.eq(sesionId).and(
    			qButacaDTO.anulada.eq(false).or(qButacaDTO.anulada.isNull()
    		))
    	).count();
	}

	@Transactional(rollbackFor=SesionSinFormatoIdiomaIcaaException.class)
    public List<RegistroSesionPelicula> getRegistrosSesionesPeliculas(List<Sesion> sesiones) throws SesionSinFormatoIdiomaIcaaException, IncidenciaNotFoundException {
        QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;
        QEventoDTO qEventoDTO = QEventoDTO.eventoDTO;
		QEventoMultisesionDTO qEventoMultisesionDTO = new QEventoMultisesionDTO("qEventoMultisesionDTO");
        
        List<Long> idsSesiones = Sesion.getIdsSesiones(sesiones);
        
        JPAQuery query = new JPAQuery(entityManager);

        List<SesionDTO> resultado = query
                .from(qSesionDTO)
                .join(qSesionDTO.parEvento, qEventoDTO)
                .join(qSesionDTO.parSala).fetch()
                .where(qSesionDTO.id.in(idsSesiones))
                .distinct()
                .list(qSesionDTO);
        
        List<RegistroSesionPelicula> registros = new ArrayList<RegistroSesionPelicula>();
        
        for (SesionDTO sesionDTO : resultado)
        {
        	if (sesionDTO.getFormato() == null)
        		throw new SesionSinFormatoIdiomaIcaaException(sesionDTO.getParEvento().getTituloVa());

			if (!isIncidenciaCancelacionEvento(sesionDTO.getIncidenciaId())) {
				List<SesionFormatoIdiomaICAADTO> sesionesFormatoIdiomaIcaa =
						getSesionFormatoIdiomaIcaa(sesionDTO.getFormato(), sesionDTO.getVersionLinguistica(), sesionDTO.getParEvento().getId());

				if (sesionesFormatoIdiomaIcaa.size() == 0) {
					throw new SesionSinFormatoIdiomaIcaaException(sesionDTO.getParEvento().getId(),
							sesionDTO.getFormato(), sesionDTO.getVersionLinguistica());
				}
				List<EventoDTO> eventosMultisesion = eventosDAO.getPeliculas(sesionDTO.getParEvento().getId());
				if (eventosMultisesion.size() > 0) {
					for (EventoDTO eventoMultisesion: eventosMultisesion) {
						RegistroSesionPelicula registro = new RegistroSesionPelicula();
						registro.setCodigoSala(sesionDTO.getParSala().getCodigo());
						registro.setCodigoPelicula((int) eventoMultisesion.getId());
						registro.setFecha(sesionDTO.getFechaCelebracion());
						registro.setHora(HOUR_FORMAT.format(sesionDTO.getFechaCelebracion()));
						registros.add(registro);
					}
				} else {
					RegistroSesionPelicula registro = new RegistroSesionPelicula();
					registro.setCodigoSala(sesionDTO.getParSala().getCodigo());
					registro.setCodigoPelicula((int) sesionDTO.getParEvento().getId());
					registro.setFecha(sesionDTO.getFechaCelebracion());
					registro.setHora(HOUR_FORMAT.format(sesionDTO.getFechaCelebracion()));
					registros.add(registro);
				}
			}
        }
        
        return registros;
    }

	public boolean isIncidenciaCancelacionEvento(Integer incidenciaId) throws IncidenciaNotFoundException {
		TipoIncidencia incidenciaOcurrida = TipoIncidencia.intToTipoIncidencia(Utils.safeObjectToInt(incidenciaId));

		if (incidenciaOcurrida != TipoIncidencia.ANULACIO_COMPLETA && incidenciaOcurrida != TipoIncidencia
				.ANULACIO_PROGRAMACIO)
			return false;
		return true;
	}

    @Transactional
    public List<RegistroPelicula> getRegistrosPeliculas(List<Sesion> sesiones) throws IncidenciaNotFoundException {
        QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;
        QEventoDTO qEventoDTO = QEventoDTO.eventoDTO;
        
        List<Long> idsSesiones = Sesion.getIdsSesiones(sesiones);
        
        JPAQuery query = new JPAQuery(entityManager);

        List<Tuple> resultado = query
                .from(qSesionDTO)
                .join(qSesionDTO.parEvento, qEventoDTO)
                .join(qSesionDTO.parSala).fetch()
                .where(qSesionDTO.id.in(idsSesiones))
                .distinct()
                .list(qSesionDTO, qEventoDTO.id);
        
        List<RegistroPelicula> registros = new ArrayList<RegistroPelicula>();
        
        for (Tuple row: resultado)
        {
            SesionDTO sesion = row.get(0, SesionDTO.class);
            Long idEvento = row.get(1, Long.class);

			if (!isIncidenciaCancelacionEvento(sesion.getIncidenciaId())) {
				List<EventoDTO> peliculasMultisesion = eventosDAO.getPeliculas(idEvento);
				if (peliculasMultisesion.size() > 0) {
					for (EventoDTO peliculaMultisesion: peliculasMultisesion) {
						RegistroPelicula registro = new RegistroPelicula();
						registro.setCodigoSala(sesion.getParSala().getCodigo());
						registro.setCodigoPelicula((int) peliculaMultisesion.getId());
						registro.setCodigoExpediente(peliculaMultisesion.getExpediente());
						registro.setTitulo(Utils.stripAccents(peliculaMultisesion.getTituloEs()));
						registro.setCodigoDistribuidora(peliculaMultisesion.getCodigoDistribuidora());
						registro.setNombreDistribuidora(Utils.stripAccents(peliculaMultisesion.getNombreDistribuidora()));
						registro.setVersionOriginal(peliculaMultisesion.getVo());
						registro.setVersionLinguistica(sesion.getVersionLinguistica());
						registro.setIdiomaSubtitulos(peliculaMultisesion.getSubtitulos());
						registro.setFormatoProyeccion(sesion.getFormato());
						registros.add(registro);
					}
				} else {
					RegistroPelicula registro = new RegistroPelicula();
					registro.setCodigoSala(sesion.getParSala().getCodigo());
					registro.setCodigoPelicula(idEvento.intValue());
					registro.setCodigoExpediente(sesion.getParEvento().getExpediente());
					registro.setTitulo(Utils.stripAccents(sesion.getParEvento().getTituloEs()));
					registro.setCodigoDistribuidora(sesion.getParEvento().getCodigoDistribuidora());
					registro.setNombreDistribuidora(Utils.stripAccents(sesion.getParEvento().getNombreDistribuidora()));
					registro.setVersionOriginal(sesion.getParEvento().getVo());
					registro.setVersionLinguistica(sesion.getVersionLinguistica());
					registro.setIdiomaSubtitulos(sesion.getParEvento().getSubtitulos());
					registro.setFormatoProyeccion(sesion.getFormato());
					registros.add(registro);
				}
			}
        }
        
        return registros;
    }

    @Transactional
    public List<SesionDTO> getSesionesOrdenadas(List<Sesion> sesiones)
    {
        QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;
        QSalaDTO qSalaDTO = QSalaDTO.salaDTO;
        
        List<Long> idsSesiones = Sesion.getIdsSesiones(sesiones);
        
        JPAQuery query = new JPAQuery(entityManager);
        
        List<SesionDTO> resultado = query
                .from(qSesionDTO)
                .join(qSesionDTO.parSala, qSalaDTO).fetch()
                .where(qSesionDTO.id.in(idsSesiones))
                .orderBy(qSalaDTO.id.asc(), qSesionDTO.fechaCelebracion.asc())
                .list(qSesionDTO);
        
        return resultado;
    }

    @Transactional
    public Sesion getSesionByRssId(String rssId)
    {
        JPAQuery query = new JPAQuery(entityManager);
        SesionDTO uniqueResult = query.from(QSesionDTO.sesionDTO)
                .where(QSesionDTO.sesionDTO.rssId.eq(rssId)).uniqueResult(QSesionDTO.sesionDTO);
        
        if (uniqueResult == null)
            return null;
        else
            return Sesion.SesionDTOToSesion(uniqueResult);    
    }
    
    @Transactional
	public List<SesionDTO> getSesionesCinePorFechas(Date dtInicio, Date dtFin, String sort) {
		QSalaDTO qSalaDTO = QSalaDTO.salaDTO;
		QEventoDTO qEventoDTO = QEventoDTO.eventoDTO;

        JPAQuery query = new JPAQuery(entityManager);
        query.from(qSesionDTO).join(qSesionDTO.parEvento, qEventoDTO).leftJoin(qSesionDTO.parSala, qSalaDTO).fetch();
        
        if (dtInicio != null || dtFin != null) {
        	BooleanBuilder condicion = new BooleanBuilder();
	        
        	if (dtInicio != null) 
	        	condicion = condicion.and(qSesionDTO.fechaCelebracion.goe(new Timestamp(dtInicio.getTime())));
	        
	        if (dtFin != null)
	        	condicion = condicion.and(qSesionDTO.fechaCelebracion.loe(new Timestamp(dtFin.getTime())));
	       
	       	query.where(condicion);
        }
        
        return query.orderBy(getSort(qSesionDTO, sort)).distinct().list(qSesionDTO);
	}
    
    @Transactional
	public List<SesionDTO> getSesiones(List<Long> ids) {
		QSalaDTO qSalaDTO = QSalaDTO.salaDTO;
		QEventoDTO qEventoDTO = QEventoDTO.eventoDTO;

        JPAQuery query = new JPAQuery(entityManager);
        return query.from(qSesionDTO).join(qSesionDTO.parEvento, qEventoDTO).leftJoin(qSesionDTO.parSala, qSalaDTO).fetch()
        .where(qSesionDTO.id.in(ids)).list(qSesionDTO);
	}
    
    @Transactional
	public List<SesionDTO> getSesionesICAAPorFechas(Date dtInicio, Date dtFin, String sort) {
		QSalaDTO qSalaDTO = QSalaDTO.salaDTO;
		QEventoDTO qEventoDTO = QEventoDTO.eventoDTO;
		QEnviosSesionDTO qEnviosSesion = QEnviosSesionDTO.enviosSesionDTO;
		QEnvioDTO qEnvioDTO = QEnvioDTO.envioDTO;

        JPAQuery query = new JPAQuery(entityManager);
        query.from(qSesionDTO).join(qSesionDTO.parEvento, qEventoDTO).leftJoin(qSesionDTO.parSala, qSalaDTO).fetch().
        	leftJoin(qSesionDTO.parEnviosSesion, qEnviosSesion).fetch().leftJoin(qEnviosSesion.parEnvio, qEnvioDTO).fetch();
        BooleanExpression condicion = qEventoDTO.parTiposEvento.exportarICAA.eq(true);
        
        if (dtInicio != null) 
        	condicion = condicion.and(qSesionDTO.fechaCelebracion.goe(new Timestamp(dtInicio.getTime())));
        
        if (dtFin != null)
        	condicion = condicion.and(qSesionDTO.fechaCelebracion.loe(new Timestamp(dtFin.getTime())));
       
       	query.where(condicion);
        
        return query.orderBy(getSort(qSesionDTO, sort)).distinct().list(qSesionDTO);
	}
    
    @Transactional
    public List<SesionDTO> getSesionesPorFechas(Date dtInicio, Date dtFin, String sort) {
    	QSalaDTO qSalaDTO = QSalaDTO.salaDTO;
		QEventoDTO qEventoDTO = QEventoDTO.eventoDTO;

        JPAQuery query = new JPAQuery(entityManager);
        query.from(qSesionDTO).join(qSesionDTO.parEvento, qEventoDTO).leftJoin(qSesionDTO.parSala, qSalaDTO).fetch();
        BooleanBuilder condicion = new BooleanBuilder();
        
        if (dtInicio != null) 
        	condicion = condicion.and(qSesionDTO.fechaCelebracion.goe(new Timestamp(dtInicio.getTime())));
        
        if (dtFin != null)
        	condicion = condicion.and(qSesionDTO.fechaCelebracion.loe(new Timestamp(dtFin.getTime())));
       
       	query.where(condicion);
        
        return query.orderBy(getSort(qSesionDTO, sort)).list(qSesionDTO);
	}

    @Transactional
	public List<TarifaDTO> getTarifasPreciosSesion(long sesionId) {
    	QTarifaDTO qTarifa = QTarifaDTO.tarifaDTO;
        JPAQuery query = new JPAQuery(entityManager);
        return query.from(qPreciosSesionDTO, qTarifa).where(qPreciosSesionDTO.parSesione.id.eq(sesionId).
        		and(qTarifa.id.eq(qPreciosSesionDTO.parTarifa.id))).distinct().list(qTarifa);
	}

    @Transactional
	public List<TarifaDTO> getTarifasPreciosPlantilla(long sesionId) {
		QTarifaDTO qTarifa = QTarifaDTO.tarifaDTO;
		QPreciosPlantillaDTO qPreciosPlantilla = QPreciosPlantillaDTO.preciosPlantillaDTO;
		
        JPAQuery query = new JPAQuery(entityManager);
        return query.from(qPreciosPlantilla, qTarifa).where(qPreciosPlantilla.parPlantilla.id.eq(
        		new JPASubQuery().from(qSesionDTO).where(qSesionDTO.id.eq(sesionId)).unique(qSesionDTO.parPlantilla.id)).
       		and(qTarifa.id.eq(qPreciosPlantilla.parTarifa.id))).distinct().list(qTarifa);
	}

    @Transactional
	public void setIncidencia(long sesionId, int incidenciaId) {
		JPAUpdateClause jpaUpdate = new JPAUpdateClause(entityManager, qSesionDTO);
		jpaUpdate.set(qSesionDTO.incidenciaId, incidenciaId).where(qSesionDTO.id.eq(sesionId)).execute();
	}
}
