package es.uji.apps.par.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPASubQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.types.expr.BooleanExpression;

import es.uji.apps.par.IncidenciaNotFoundException;
import es.uji.apps.par.db.PreciosSesionDTO;
import es.uji.apps.par.db.QButacaDTO;
import es.uji.apps.par.db.QCompraDTO;
import es.uji.apps.par.db.QEnvioDTO;
import es.uji.apps.par.db.QEnviosSesionDTO;
import es.uji.apps.par.db.QEventoDTO;
import es.uji.apps.par.db.QPreciosPlantillaDTO;
import es.uji.apps.par.db.QPreciosSesionDTO;
import es.uji.apps.par.db.QSalaDTO;
import es.uji.apps.par.db.QSesionDTO;
import es.uji.apps.par.db.QTarifaDTO;
import es.uji.apps.par.db.SesionDTO;
import es.uji.apps.par.db.TarifaDTO;
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
    public List<Object[]> getSesionesConButacasVendidas(long eventoId, boolean activas, String sortParameter,
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
        queryVendidas.where(qSesionDTO.id.eq(qButacaDTO.parSesion.id).and(qButacaDTO.anulada.eq(false)).
        		and(qButacaDTO.parCompra.id.eq(qCompraDTO.id).and(qCompraDTO.reserva.eq(false))));
        
        JPASubQuery queryReservadas = new JPASubQuery();
        queryReservadas.from(qButacaDTO, qCompraDTO);
        queryReservadas.where(qSesionDTO.id.eq(qButacaDTO.parSesion.id).and(qButacaDTO.anulada.eq(false)).
        		and(qButacaDTO.parCompra.id.eq(qCompraDTO.id).and(qCompraDTO.reserva.eq(true))));

        List<Object[]> sesiones = query.orderBy(getSort(qSesionDTO, sortParameter))/*.offset(start).limit(limit)*/
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
        return sesion;
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
        
        //entityManager.merge(Sesion.SesionToSesionDTO(sesion));
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

        List<Long> idsSesiones = Sesion.getIdsSesiones(sesiones);
        JPAQuery query = new JPAQuery(entityManager);
        
        List<Object[]> resultado = query
        	.from(qSesionDTO)
        	.join(qSesionDTO.parSala, qSalaDTO)
        	.leftJoin(qSesionDTO.parCompras, qCompraDTO)
            .where(qSesionDTO.id.in(idsSesiones)
            .and(qCompraDTO.anulada.isNull().or(qCompraDTO.anulada.eq(false))))
            .distinct()
            .groupBy(qSesionDTO.id, qSalaDTO.codigo, qSesionDTO.fechaCelebracion)
            .list(qSesionDTO.id, qSalaDTO.codigo, qSesionDTO.fechaCelebracion, qCompraDTO.importe.sum(), qSesionDTO.incidenciaId);
        
        List<RegistroSesion> registros = new ArrayList<RegistroSesion>();
        
        for (Object[] row:resultado)
        {
        	long idSesion = (Long) row[0];
        	String codigoSala = (String) row[1];
        	Date fechaCelebracion = (Date) row[2];
            BigDecimal recaudacion = (BigDecimal) row[3];
            Long espectadores = getEspectadores(idSesion);
            
            RegistroSesion registro = new RegistroSesion();
            
            registro.setCodigoSala(codigoSala);
            registro.setEspectadores(espectadores.intValue());
            registro.setPeliculas(1);
            registro.setFecha(fechaCelebracion);
            registro.setHora(HOUR_FORMAT.format(fechaCelebracion));
            registro.setIncidencia(TipoIncidencia.intToTipoIncidencia(Utils.safeObjectToInt(row[4])));

            if (recaudacion == null)
                registro.setRecaudacion(BigDecimal.ZERO);
            else
                registro.setRecaudacion(recaudacion);
            
            registros.add(registro);
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

	@Transactional
    public List<RegistroSesionPelicula> getRegistrosSesionesPeliculas(List<Sesion> sesiones)
    {
        QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;
        QEventoDTO qEventoDTO = QEventoDTO.eventoDTO;
        
        List<Long> idsSesiones = Sesion.getIdsSesiones(sesiones);
        
        JPAQuery query = new JPAQuery(entityManager);

        List<Object[]> resultado = query
                .from(qSesionDTO)
                .join(qSesionDTO.parEvento, qEventoDTO)
                .join(qSesionDTO.parSala).fetch()
                .where(qSesionDTO.id.in(idsSesiones))
                .distinct()
                .list(qSesionDTO, qEventoDTO.id);
        
        List<RegistroSesionPelicula> registros = new ArrayList<RegistroSesionPelicula>();
        
        for (Object[] row:resultado)
        {
            SesionDTO sesion = (SesionDTO) row[0];
            Long idEvento = (Long) row[1];
            
            RegistroSesionPelicula registro = new RegistroSesionPelicula();
            registro.setCodigoSala(sesion.getParSala().getCodigo());
            registro.setCodigoPelicula(idEvento.intValue());
            registro.setFecha(sesion.getFechaCelebracion());
            registro.setHora(HOUR_FORMAT.format(sesion.getFechaCelebracion()));
            
            registros.add(registro);
        }
        
        return registros;
    }

    @Transactional
    public List<RegistroPelicula> getRegistrosPeliculas(List<Sesion> sesiones)
    {
        QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;
        QEventoDTO qEventoDTO = QEventoDTO.eventoDTO;
        
        List<Long> idsSesiones = Sesion.getIdsSesiones(sesiones);
        
        JPAQuery query = new JPAQuery(entityManager);

        List<Object[]> resultado = query
                .from(qSesionDTO)
                .join(qSesionDTO.parEvento, qEventoDTO)
                .join(qSesionDTO.parSala).fetch()
                .where(qSesionDTO.id.in(idsSesiones))
                .distinct()
                .list(qSesionDTO, qEventoDTO.id);
        
        List<RegistroPelicula> registros = new ArrayList<RegistroPelicula>();
        
        for (Object[] row:resultado)
        {
            SesionDTO sesion = (SesionDTO) row[0];
            Long idEvento = (Long) row[1];
            
            RegistroPelicula registro = new RegistroPelicula();
            registro.setCodigoSala(sesion.getParSala().getCodigo());
            registro.setCodigoPelicula(idEvento.intValue());
            registro.setCodigoExpediente(sesion.getParEvento().getExpediente());
            registro.setTitulo(sesion.getParEvento().getTituloEs());
            registro.setCodigoDistribuidora(sesion.getParEvento().getCodigoDistribuidora());
            registro.setNombreDistribuidora(sesion.getParEvento().getNombreDistribuidora());
            registro.setVersionOriginal(sesion.getParEvento().getVo());
            registro.setVersionLinguistica(sesion.getVersionLinguistica());
            registro.setIdiomaSubtitulos(sesion.getParEvento().getSubtitulos());
            registro.setFormatoProyeccion(sesion.getFormato());
            
            registros.add(registro);
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
        
        return query.orderBy(getSort(qSesionDTO, sort)).list(qSesionDTO);
	}

    @Transactional
	public List<TarifaDTO> getTarifasPreciosSesion(long sesionId) {
    	QTarifaDTO qTarifa = QTarifaDTO.tarifaDTO;
        JPAQuery query = new JPAQuery(entityManager);
        return query.from(qPreciosSesionDTO, qTarifa).where(qPreciosSesionDTO.parSesione.id.eq(sesionId).
        		and(qTarifa.id.eq(qPreciosSesionDTO.parTarifa.id))).listDistinct(qTarifa);
	}

    @Transactional
	public List<TarifaDTO> getTarifasPreciosPlantilla(long sesionId) {
		QTarifaDTO qTarifa = QTarifaDTO.tarifaDTO;
		QPreciosPlantillaDTO qPreciosPlantilla = QPreciosPlantillaDTO.preciosPlantillaDTO;
		
        JPAQuery query = new JPAQuery(entityManager);
        return query.from(qPreciosPlantilla, qTarifa).where(qPreciosPlantilla.parPlantilla.id.eq(
        		new JPASubQuery().from(qSesionDTO).where(qSesionDTO.id.eq(sesionId)).unique(qSesionDTO.parPlantilla.id)).
       		and(qTarifa.id.eq(qPreciosPlantilla.parTarifa.id))).listDistinct(qTarifa);
	}

    @Transactional
	public void setIncidencia(long sesionId, int incidenciaId) {
		JPAUpdateClause jpaUpdate = new JPAUpdateClause(entityManager, qSesionDTO);
		jpaUpdate.set(qSesionDTO.incidenciaId, incidenciaId).where(qSesionDTO.id.eq(sesionId)).execute();
	}
}
