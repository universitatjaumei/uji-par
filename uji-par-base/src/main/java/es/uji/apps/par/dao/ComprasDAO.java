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
import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.db.QButacaDTO;
import es.uji.apps.par.db.QCompraDTO;
import es.uji.apps.par.db.SesionDTO;
import es.uji.apps.par.utils.DateUtils;

@Repository
public class ComprasDAO extends BaseDAO
{
    private static final int ELIMINA_PENDIENTES_MINUTOS = 10;
    
    @Autowired
    private SesionesDAO sesionDAO;

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
        QCompraDTO qComprasDTO = QCompraDTO.compraDTO;
        
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, -ELIMINA_PENDIENTES_MINUTOS);
        
        Timestamp limite = new Timestamp(calendar.getTimeInMillis());
        
        new JPADeleteClause(entityManager, qComprasDTO).where(qComprasDTO.pagada.eq(false).and(qComprasDTO.reserva.eq(false)).and(qComprasDTO.fecha.lt(limite))).execute();          
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
    public List<CompraDTO> getComprasBySesion(long sesionId, int showAnuladas, String sortParameter, int start, int limit)
    {
    	QCompraDTO qCompraDTO = QCompraDTO.compraDTO;
    	return getQueryComprasBySesion(sesionId, showAnuladas).orderBy(getSort(qCompraDTO, sortParameter)).
    			offset(start).limit(limit).list(qCompraDTO);
    }

    @Transactional
	private JPAQuery getQueryComprasBySesion(long sesionId, int showAnuladas) {
		JPAQuery query = new JPAQuery(entityManager);
		QCompraDTO qCompraDTO = QCompraDTO.compraDTO;
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(qCompraDTO.parSesion.id.eq(sesionId));
		
		if (showAnuladas == 0)
			builder.and(qCompraDTO.anulada.isNull().or(qCompraDTO.anulada.eq(false)));
		return query.from(qCompraDTO).where(builder);
	}

    @Transactional
	public int getTotalComprasBySesion(Long sesionId, int showAnuladas) {
		return (int) getQueryComprasBySesion(sesionId, showAnuladas).count();
	}

    @Transactional
	public void anularCompraReserva(Long sesionId, Long idCompraReserva) {
    	QCompraDTO qCompraDTO = QCompraDTO.compraDTO;
    	QButacaDTO qButacaDTO = QButacaDTO.butacaDTO;
		JPAUpdateClause updateCompra = new JPAUpdateClause(entityManager, qCompraDTO);
		updateCompra.set(qCompraDTO.anulada, true).
			where(qCompraDTO.id.eq(idCompraReserva).and(qCompraDTO.parSesion.id.eq(sesionId))).execute();
		
		JPAUpdateClause updateButacas = new JPAUpdateClause(entityManager, qButacaDTO);
		updateButacas.set(qButacaDTO.anulada, true).
			where(qButacaDTO.parCompra.id.eq(idCompraReserva)).execute();
	}
}
