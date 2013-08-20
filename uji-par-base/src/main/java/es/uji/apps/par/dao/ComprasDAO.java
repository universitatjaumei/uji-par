package es.uji.apps.par.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;

import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.db.QButacaDTO;
import es.uji.apps.par.db.QCompraDTO;
import es.uji.apps.par.db.QTipoEventoDTO;
import es.uji.apps.par.db.SesionDTO;

@Repository
public class ComprasDAO
{
    private static final int ELIMINA_PENDIENTES_MINUTOS = 10;

    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private SesionesDAO sesionDAO;

    @Transactional
    public CompraDTO insertaCompra(Long sesionId, String nombre, String apellidos, String telefono, String email, Date fecha, boolean taquilla, BigDecimal importe)
    {
        SesionDTO sesion = sesionDAO.getSesion(sesionId);
        
        CompraDTO compraDTO = new CompraDTO(sesion, nombre, apellidos, telefono, email, new Timestamp(fecha.getTime()), taquilla, importe, UUID.randomUUID().toString());

        entityManager.persist(compraDTO);

        return compraDTO;
    }
    
    @Transactional
    public CompraDTO guardaCompra(Long compraId, Long sesionId, String nombre, String apellidos, String telefono, String email, Date fecha, boolean taquilla, BigDecimal importe)
    {
        CompraDTO compraDTO = getCompraById(compraId);
        
        if (compraDTO == null)
        {
            compraDTO = insertaCompra(sesionId, nombre, apellidos, telefono, email, fecha, taquilla, importe);
        }
        else
        {
            SesionDTO sesion = sesionDAO.getSesion(sesionId);
            compraDTO.setParSesion(sesion);
            compraDTO.setNombre(nombre);
            compraDTO.setApellidos(apellidos);
            compraDTO.setTelefono(telefono);
            compraDTO.setEmail(email);
            compraDTO.setFecha(new Timestamp(fecha.getTime()));
            compraDTO.setTaquilla(taquilla);
            compraDTO.setImporte(importe);
            
            entityManager.persist(compraDTO);
        }

        return compraDTO;
    }

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
        
        new JPADeleteClause(entityManager, qComprasDTO).where(qComprasDTO.pagada.eq(false).and(qComprasDTO.fecha.lt(limite))).execute();          
    }

}
