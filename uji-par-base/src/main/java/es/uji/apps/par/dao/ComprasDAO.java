package es.uji.apps.par.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

import es.uji.apps.par.ButacaOcupadaAlActivarException;
import es.uji.apps.par.database.DatabaseHelper;
import es.uji.apps.par.database.DatabaseHelperFactory;
import es.uji.apps.par.db.ButacaDTO;
import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.db.QButacaDTO;
import es.uji.apps.par.db.QCompraDTO;
import es.uji.apps.par.db.QSesionDTO;
import es.uji.apps.par.db.SesionDTO;
import es.uji.apps.par.model.Sesion;
import es.uji.apps.par.utils.DateUtils;

@Repository
public class ComprasDAO extends BaseDAO
{
    private static final int ELIMINA_PENDIENTES_MINUTOS = 20;
    
    @Autowired
    private SesionesDAO sesionDAO;

    private DatabaseHelper dbHelper;
    
    public ComprasDAO()
    {
        dbHelper = DatabaseHelperFactory.newInstance();
    }

    @Transactional
    public CompraDTO insertaCompra(Long sesionId, Date fecha, boolean taquilla, BigDecimal importe)
    {
        SesionDTO sesion = sesionDAO.getSesion(sesionId);
        
        CompraDTO compraDTO = new CompraDTO(sesion, new Timestamp(fecha.getTime()), taquilla, importe, UUID.randomUUID().toString());

        entityManager.persist(compraDTO);

        return compraDTO;
    }

    @Transactional
    public CompraDTO reserva(Long sesionId, Date fecha, Date desde, Date hasta, String observaciones)
    {
        SesionDTO sesion = sesionDAO.getSesion(sesionId);
        
        CompraDTO compraDTO = new CompraDTO(sesion, new Timestamp(fecha.getTime()), true, BigDecimal.ZERO, UUID.randomUUID().toString());
        
        compraDTO.setReserva(true);
        compraDTO.setDesde(DateUtils.dateToTimestampSafe(desde));
        compraDTO.setHasta(DateUtils.dateToTimestampSafe(hasta));
        compraDTO.setObservacionesReserva(observaciones);

        entityManager.persist(compraDTO);

        return compraDTO;
    }
    
    @Transactional
    public CompraDTO guardaCompra(Long compraId, Long sesionId, Date fecha, boolean taquilla, BigDecimal importe)
    {
        CompraDTO compraDTO = getCompraById(compraId);
        
        if (compraDTO == null)
        {
            compraDTO = insertaCompra(sesionId, fecha, taquilla, importe);
        }
        else
        {
            SesionDTO sesion = sesionDAO.getSesion(sesionId);
            compraDTO.setParSesion(sesion);
            compraDTO.setFecha(new Timestamp(fecha.getTime()));
            compraDTO.setTaquilla(taquilla);
            compraDTO.setImporte(importe);
            
            entityManager.persist(compraDTO);
        }

        return compraDTO;
    }

    @Transactional
    public CompraDTO getCompraById(long id)
    {
        QCompraDTO qCompraDTO = QCompraDTO.compraDTO;
        QButacaDTO qButacaDTO = QButacaDTO.butacaDTO;
        
        JPAQuery query = new JPAQuery(entityManager);

         List<CompraDTO> compras = query
                .from(qCompraDTO, qButacaDTO)
                .join(qCompraDTO.parButacas, qButacaDTO).fetch()
                .where(qCompraDTO.id.eq(id))
                .distinct()
                .list(qCompraDTO);
         
         if (compras.size() == 0)
             return null;
         else
             return compras.get(0);
        
        //return entityManager.find(CompraDTO.class, id);
    }

    @Transactional
    public void guardarCodigoPagoTarjeta(long idCompra, String codigo)
    {
        CompraDTO compra = getCompraById(idCompra);
       
        compra.setCodigoPagoTarjeta(codigo);
        
        entityManager.persist(compra);
    }

    @Transactional
    public void marcarPagada(Long idCompra)
    {
        CompraDTO compra = getCompraById(idCompra);
        
        compra.setPagada(true);
        
        entityManager.persist(compra);        
    }
    
    @Transactional
    public void marcarPagadaConRecibo(Long idCompra, String reciboPinpad)
    {
        CompraDTO compra = getCompraById(idCompra);
        
        compra.setPagada(true);
        compra.setReciboPinpad(reciboPinpad);
        
        entityManager.persist(compra);        
    }
    
    @Transactional
    public void marcarPagadaPasarela(Long idCompra, String codigoPago)
    {
        CompraDTO compra = getCompraById(idCompra);
        
        compra.setCodigoPagoPasarela(codigoPago);
        compra.setPagada(true);
        
        entityManager.persist(compra);        
    }

    @Transactional
    public void borrarCompraNoPagada(Long idCompra)
    {
        CompraDTO compra = getCompraById(idCompra);
        
        if (compra!=null && !compra.getPagada())
        {
            entityManager.remove(compra);
        }
    }
    
    @Transactional
    public void borrarCompraNoPagada(String uuidCompra)
    {
        CompraDTO compra = getCompraByUuid(uuidCompra);
        
        if (compra!=null && !compra.getPagada())
        {
            entityManager.remove(compra);
        }
    }
    
    @Transactional
    public CompraDTO getCompraByUuid(String uuid)
    {
        QCompraDTO qCompraDTO = QCompraDTO.compraDTO;
        QButacaDTO qButacaDTO = QButacaDTO.butacaDTO;
        
        JPAQuery query = new JPAQuery(entityManager);

         List<CompraDTO> compras = query
                .from(qCompraDTO, qButacaDTO)
                .join(qCompraDTO.parButacas, qButacaDTO).fetch()
                .where(qCompraDTO.uuid.eq(uuid))
                .distinct()
                .list(qCompraDTO);
         
         if (compras.size() == 0)
             return null;
         else
             return compras.get(0);
    }

    @Transactional
    public void eliminaComprasPendientes()
    {
        QCompraDTO qCompraDTO = QCompraDTO.compraDTO;
        
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -ELIMINA_PENDIENTES_MINUTOS);
        
        Timestamp limite = new Timestamp(calendar.getTimeInMillis());
        
        JPAQuery query = new JPAQuery(entityManager);
        
        List<CompraDTO> comprasACaducar = query
                .from(qCompraDTO)
                .where(  qCompraDTO.pagada.eq(false)
                        .and(qCompraDTO.anulada.eq(false))
                        .and(qCompraDTO.reserva.eq(false))
                        .and(qCompraDTO.fecha.lt(limite)))
                .distinct()
                .list(qCompraDTO);
        
        for (CompraDTO compra: comprasACaducar)
        {
            compra.setCaducada(true);
            entityManager.persist(compra);
            
            anularCompraReserva(compra.getId());
        }
    }

    @Transactional
    public void rellenaDatosComprador(String uuidCompra, String nombre, String apellidos, String direccion, String poblacion, String cp, String provincia, String telefono, String email, Object infoPeriodica)
    {
        CompraDTO compra = getCompraByUuid(uuidCompra);
        
        compra.setNombre(nombre);
        compra.setApellidos(apellidos);
        compra.setDireccion(direccion);
        compra.setPoblacion(poblacion);
        compra.setCp(cp);
        compra.setProvincia(provincia);
        compra.setTelefono(telefono);
        compra.setEmail(email);
        compra.setInfoPeriodica(infoPeriodica!=null && infoPeriodica.equals("si"));
        
        entityManager.persist(compra);
    }
    
    @Transactional
    public List<CompraDTO> getComprasBySesion(long sesionId, int showAnuladas, String sortParameter, int start, int limit, int showOnline, String search)
    {
    	QCompraDTO qCompraDTO = QCompraDTO.compraDTO;
    	return getQueryComprasBySesion(sesionId, showAnuladas, showOnline, search).orderBy(getSort(qCompraDTO, sortParameter)).
    			offset(start).limit(limit).list(qCompraDTO);
    }

    @Transactional
	private JPAQuery getQueryComprasBySesion(long sesionId, int showAnuladas, int showOnline, String search) {
		JPAQuery query = new JPAQuery(entityManager);
		QCompraDTO qCompraDTO = QCompraDTO.compraDTO;
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(qCompraDTO.parSesion.id.eq(sesionId));
		
		if (showAnuladas == 0)
			builder.and(qCompraDTO.anulada.isNull().or(qCompraDTO.anulada.eq(false)));
		
		if (showOnline == 0)
			builder.and(qCompraDTO.taquilla.eq(true));
		
		if (!"".equals(search)) {
			String isNumeric = "^[\\d]+$";
			if (search.matches(isNumeric)) {
				long numericSearch = Integer.parseInt(search);
				builder.and(qCompraDTO.uuid.like('%' + search + '%').or(qCompraDTO.observacionesReserva.like('%' + search + '%').
						or(qCompraDTO.id.eq(numericSearch))));
			}
			else
				builder.and(qCompraDTO.uuid.like('%' + search + '%').or(qCompraDTO.observacionesReserva.like('%' + search + '%')));
		}
		
		return query.from(qCompraDTO).where(builder);
	}

    @Transactional
	public int getTotalComprasBySesion(Long sesionId, int showAnuladas, int showOnline, String search) {
		return (int) getQueryComprasBySesion(sesionId, showAnuladas, showOnline, search).count();
	}

    @Transactional
	public void anularCompraReserva(Long idCompraReserva) {
        QCompraDTO qCompraDTO = QCompraDTO.compraDTO;
        QButacaDTO qButacaDTO = QButacaDTO.butacaDTO;
        JPAUpdateClause updateCompra = new JPAUpdateClause(entityManager, qCompraDTO);
        updateCompra.set(qCompraDTO.anulada, true).
            where(qCompraDTO.id.eq(idCompraReserva)).execute();
        
        JPAUpdateClause updateButacas = new JPAUpdateClause(entityManager, qButacaDTO);
        updateButacas.set(qButacaDTO.anulada, true).
            where(qButacaDTO.parCompra.id.eq(idCompraReserva)).execute();
	}
    
    @Transactional
    public void desanularCompraReserva(Long idCompraReserva) throws ButacaOcupadaAlActivarException {
        
        try
        {
            QCompraDTO qCompraDTO = QCompraDTO.compraDTO;
            QButacaDTO qButacaDTO = QButacaDTO.butacaDTO;
            JPAUpdateClause updateCompra = new JPAUpdateClause(entityManager, qCompraDTO);
            updateCompra.set(qCompraDTO.anulada, false).
                         set(qCompraDTO.pagada, true).
                where(qCompraDTO.id.eq(idCompraReserva)).execute();
            
            JPAUpdateClause updateButacas = new JPAUpdateClause(entityManager, qButacaDTO);
            updateButacas.set(qButacaDTO.anulada, false).
                where(qButacaDTO.parCompra.id.eq(idCompraReserva)).execute();
        } 
        catch (Exception e)
        {
            for (ButacaDTO butaca:getCompraById(idCompraReserva).getParButacas())
            {
                if (butacaOcupada(butaca))
                {
                    String comprador = "";
                    
                    if (butaca.getParCompra().getNombre()!=null || butaca.getParCompra().getApellidos()!=null)
                        comprador = butaca.getParCompra().getNombre() + " " + butaca.getParCompra().getApellidos();
                        
                    throw new ButacaOcupadaAlActivarException(butaca.getParSesion().getId(), butaca.getParLocalizacion().getCodigo(), 
                            butaca.getFila(), butaca.getNumero(), comprador, butaca.getParCompra().getTaquilla());
                }
            }
        }
    } 

    private boolean butacaOcupada(ButacaDTO butaca)
    {
        QButacaDTO b = QButacaDTO.butacaDTO;
        
        JPAQuery query = new JPAQuery(entityManager);
        query.from(b)
            .where(b.parSesion.id.eq(butaca.getParSesion().getId())
                    .and(b.anulada.eq(false))
                    .and(b.parLocalizacion.codigo.eq(butaca.getParLocalizacion().getCodigo()))
                    .and(b.fila.eq(butaca.getFila()))
                    .and(b.numero.eq(butaca.getNumero())));
        
        return query.count() > 0;
    }

    public List<CompraDTO> getReservasACaducar(Date date)
    {
        QCompraDTO qCompraDTO = QCompraDTO.compraDTO;
        
        JPAQuery query = new JPAQuery(entityManager);

         List<CompraDTO> compras = query
                .from(qCompraDTO)
                .where(qCompraDTO.reserva.eq(true)
                        .and(qCompraDTO.anulada.eq(false))
                        .and(qCompraDTO.hasta.lt(DateUtils.dateToTimestampSafe(date))))
                .distinct()
                .list(qCompraDTO);
         
        return compras;
    }

    @Transactional
	public void anularButaca(Long idButaca) {
    	QButacaDTO qButacaDTO = QButacaDTO.butacaDTO;
		
		JPAUpdateClause updateButacas = new JPAUpdateClause(entityManager, qButacaDTO);
		updateButacas.set(qButacaDTO.anulada, true).
			where(qButacaDTO.id.eq(idButaca)).execute();
	}

    @SuppressWarnings("unchecked")
	@Transactional
	public List<Object[]> getComprasInFechas(String fechaInicio, String fechaFin) {
    	String sql = "select e.titulo_va, s.fecha_celebracion, b.tipo, l.codigo, count(b.id) as cantidad, sum(b.precio) as total, c.sesion_id " +
    			"from par_butacas b, par_compras c, par_sesiones s, par_eventos e, par_localizaciones l " +
    			"where b.compra_id = c.id and s.id = c.sesion_id and e.id = s.evento_id and l.id=b.localizacion_id " +
    			"and c.fecha >= TO_DATE('" + fechaInicio + "','YYYY-MM-DD') and c.fecha <= TO_DATE('" + fechaFin + " 23:59','YYYY-MM-DD HH24:MI') " +
    			"and c.pagada = " + dbHelper.trueString() + " and c.reserva = " + dbHelper.falseString() + " and c.taquilla = " + dbHelper.trueString() + " " +
    			"and b.anulada = " + dbHelper.falseString() + " " +
    			"and c.codigo_pago_tarjeta is null " +
    			"group by c.sesion_id, e.titulo_va, b.tipo, s.fecha_celebracion, l.codigo " +
    			"order by e.titulo_va";
    	
    	return entityManager.createNativeQuery(sql).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Object[]> getComprasPorEventoInFechas(String fechaInicio, String fechaFin) {
		String sql = "select e.id, e.titulo_va, b.tipo, count(b.id) as cantidad, sum(b.precio) as total, c.taquilla " +
    			"from par_butacas b, par_compras c, par_sesiones s, par_eventos e " +
    			"where b.compra_id = c.id and s.id = c.sesion_id and e.id = s.evento_id " +
    			"and c.fecha >= TO_DATE('" + fechaInicio + "','YYYY-MM-DD') and c.fecha <= TO_DATE('" + fechaFin + " 23:59','YYYY-MM-DD HH24:MI') " +
    			"and c.pagada = " + dbHelper.trueString() + " and c.reserva = " + dbHelper.falseString() + " " +
    			"and b.anulada = " + dbHelper.falseString() + " " +
    			"group by e.id, e.titulo_va, b.tipo, c.taquilla " +
    			"order by e.titulo_va";
		
    	return entityManager.createNativeQuery(sql).getResultList();
	}
	
    @SuppressWarnings("unchecked")
    @Transactional
    public List<Object[]> getComprasEfectivo(String fechaInicio, String fechaFin) {
        String sql = "select e.titulo_va, s.fecha_celebracion, b.tipo, count(b.id) as cantidad, sum(b.precio) as total, c.sesion_id, e.porcentaje_iva, " +  dbHelper.caseString("b.tipo", new String[]{"'normal'", "1", "'descuento'", "2", "'aulaTeatro'", "3", "'invitacion'", "4"}) + " as tipoOrden " +
                "from par_butacas b, par_compras c, par_sesiones s, par_eventos e " +
                "where b.compra_id = c.id and s.id = c.sesion_id and e.id = s.evento_id " +
                "and c.fecha >= TO_DATE('" + fechaInicio + "','YYYY-MM-DD') and c.fecha <= TO_DATE('" + fechaFin + " 23:59','YYYY-MM-DD HH24:MI') " +
                "and c.pagada = " + dbHelper.trueString() + " and c.reserva = " + dbHelper.falseString() + " and c.taquilla = " + dbHelper.trueString() + " " +
                "and b.anulada = " + dbHelper.falseString() + " " +
                "and c.codigo_pago_tarjeta is null " +
                "group by c.sesion_id, e.titulo_va, b.tipo, s.fecha_celebracion, e.porcentaje_iva " +
                "order by e.titulo_va, s.fecha_celebracion, tipoOrden";
        
        return entityManager.createNativeQuery(sql).getResultList();
    }	
    
    @SuppressWarnings("unchecked")
    @Transactional
    public List<Object[]> getComprasTpv(String fechaInicio, String fechaFin) {
    	String formato = "DD";
        String sql = "select e.titulo_va, s.fecha_celebracion, b.tipo, count(b.id) as cantidad, sum(b.precio) as total, c.sesion_id, e.porcentaje_iva, " +  dbHelper.caseString("b.tipo", new String[]{"'normal'", "1", "'descuento'", "2", "'aulaTeatro'", "3", "'invitacion'", "4"}) + " as tipoOrden, " + dbHelper.trunc("c.fecha", formato) + " " +
                "from par_butacas b, par_compras c, par_sesiones s, par_eventos e " +
                "where b.compra_id = c.id and s.id = c.sesion_id and e.id = s.evento_id " +
                "and c.fecha >= TO_DATE('" + fechaInicio + "','YYYY-MM-DD') and c.fecha <= TO_DATE('" + fechaFin + " 23:59','YYYY-MM-DD HH24:MI') " +
                "and c.reserva = " + dbHelper.falseString() + " " +
                "and (c.codigo_pago_tarjeta is not null or c.codigo_pago_pasarela is not null) " +
                "group by c.sesion_id, e.titulo_va, b.tipo, s.fecha_celebracion, e.porcentaje_iva, " + dbHelper.trunc("c.fecha", formato) + " " +
                "order by " + dbHelper.trunc("c.fecha", formato) + ", s.fecha_celebracion, tipoOrden";
        
        return entityManager.createNativeQuery(sql).getResultList();
    }   
    
    @SuppressWarnings("unchecked")
    @Transactional
    public List<Object[]> getComprasEventos(String fechaInicio, String fechaFin) {
        String sql = "select e.titulo_va, s.fecha_celebracion, b.tipo, count(b.id) as cantidad, sum(b.precio) as total, e.porcentaje_iva, " + dbHelper.caseString("b.tipo", new String[]{"'normal'", "1", "'descuento'", "2", "'aulaTeatro'", "3", "'invitacion'", "4"}) + " as tipoOrden, e.id as eventoId, s.id as sesionId " +
                "from par_butacas b, par_compras c, par_sesiones s, par_eventos e " +
                "where b.compra_id = c.id and s.id = c.sesion_id and e.id = s.evento_id " +
                "and s.fecha_celebracion >= TO_DATE('" + fechaInicio + "','YYYY-MM-DD') and s.fecha_celebracion <= TO_DATE('" + fechaFin + " 23:59','YYYY-MM-DD HH24:MI') " +
                "and c.pagada = " + dbHelper.trueString() + " " +
                "and b.anulada = " + dbHelper.falseString() + " " +
                "and c.reserva = " + dbHelper.falseString() + " " +
                "group by e.id, s.id, e.titulo_va, b.tipo, s.fecha_celebracion, e.porcentaje_iva " +
                "order by s.fecha_celebracion, tipoOrden";
        
        return entityManager.createNativeQuery(sql).getResultList();
    }

    public BigDecimal getTotalTaquillaTpv(String fechaInicio, String fechaFin)
    {
        String sql = "select sum(b.precio) " +
                "from par_butacas b, par_compras c, par_sesiones s, par_eventos e " +
                "where b.compra_id = c.id and s.id = c.sesion_id and e.id = s.evento_id " +
                "and c.fecha >= TO_DATE('" + fechaInicio + "','YYYY-MM-DD') and c.fecha <= TO_DATE('" + fechaFin + " 23:59','YYYY-MM-DD HH24:MI') " +
                "and c.pagada = " + dbHelper.trueString() + " and c.reserva = " + dbHelper.falseString() + " " +
                "and b.anulada = " + dbHelper.falseString() + " " +
                "and c.taquilla = " + dbHelper.trueString() + " " +
                "and c.codigo_pago_tarjeta is not null";
        
         Object result = entityManager.createNativeQuery(sql).getSingleResult();
         
         if (result == null)
             return BigDecimal.ZERO;
         else
             return (BigDecimal) result;
    }

    public BigDecimal getTotalTaquillaEfectivo(String fechaInicio, String fechaFin)
    {
        String sql = "select sum(b.precio) " +
                "from par_butacas b, par_compras c, par_sesiones s, par_eventos e " +
                "where b.compra_id = c.id and s.id = c.sesion_id and e.id = s.evento_id " +
                "and c.fecha >= TO_DATE('" + fechaInicio + "','YYYY-MM-DD') and c.fecha <= TO_DATE('" + fechaFin + " 23:59','YYYY-MM-DD HH24:MI') " +
                "and c.pagada = " + dbHelper.trueString() + " and c.reserva = " + dbHelper.falseString() + " " +
                "and b.anulada = " + dbHelper.falseString() + " " +
                "and c.taquilla = " + dbHelper.trueString() + " " +
                "and c.codigo_pago_tarjeta is null";
        
         Object result = entityManager.createNativeQuery(sql).getSingleResult();

         if (result == null)
             return BigDecimal.ZERO;
         else
             return dbHelper.castBigDecimal(result);
    }

    public BigDecimal getTotalOnline(String fechaInicio, String fechaFin)
    {
        String sql = "select sum(b.precio) " +
                "from par_butacas b, par_compras c, par_sesiones s, par_eventos e " +
                "where b.compra_id = c.id and s.id = c.sesion_id and e.id = s.evento_id " +
                "and c.fecha >= TO_DATE('" + fechaInicio + "','YYYY-MM-DD') and c.fecha <= TO_DATE('" + fechaFin + " 23:59','YYYY-MM-DD HH24:MI') " +
                "and c.pagada = " + dbHelper.trueString() + " and c.reserva = " + dbHelper.falseString() + " " +
                "and b.anulada = " + dbHelper.falseString() + " " +
                "and c.taquilla = " + dbHelper.falseString() + " ";
        
         Object result = entityManager.createNativeQuery(sql).getSingleResult();
         
         if (result == null)
             return BigDecimal.ZERO;
         else
             return dbHelper.castBigDecimal(result);
    }

    @Transactional
    public void rellenaCodigoPagoPasarela(long idCompra, String codigoPago)
    {
        CompraDTO compra = getCompraById(idCompra);
        compra.setCodigoPagoPasarela(codigoPago);
        
        entityManager.persist(compra);
    }

    public BigDecimal getRecaudacionSesiones(List<Sesion> sesiones)
    {
        QCompraDTO qCompraDTO = QCompraDTO.compraDTO;
        QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;
        
        List<Long> idsSesiones = Sesion.getIdsSesiones(sesiones);
        
        JPAQuery query = new JPAQuery(entityManager);

        BigDecimal total = query
                .from(qCompraDTO)
                .join(qCompraDTO.parSesion, qSesionDTO)
                .where(qSesionDTO.id.in(idsSesiones)
                        .and(qCompraDTO.reserva.eq(false))
                        .and(qCompraDTO.anulada.eq(false)))
                .distinct()        
                .uniqueResult(qCompraDTO.importe.sum());
        
        if (total == null)
            return BigDecimal.ZERO;
        else
            return total;
    }

    public int getEspectadores(List<Sesion> sesiones)
    {
        QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;
        QCompraDTO qCompraDTO = QCompraDTO.compraDTO;
        QButacaDTO qButacaDTO = QButacaDTO.butacaDTO;
        
        List<Long> idsSesiones = Sesion.getIdsSesiones(sesiones);
        
        JPAQuery query = new JPAQuery(entityManager);

        Long butacas = query
                .from(qCompraDTO)
                .join(qCompraDTO.parSesion, qSesionDTO)
                .join(qCompraDTO.parButacas, qButacaDTO)
                .where(qSesionDTO.id.in(idsSesiones)
                        .and(qCompraDTO.reserva.eq(false))
                        .and(qCompraDTO.anulada.eq(false)))
                .uniqueResult(qButacaDTO.count());
        
        if (butacas == null)
            return 0;
        else
            return butacas.intValue();
    }
}
