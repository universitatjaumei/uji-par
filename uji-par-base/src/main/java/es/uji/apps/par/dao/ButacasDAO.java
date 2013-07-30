package es.uji.apps.par.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.mysema.query.jpa.impl.JPAQuery;

import es.uji.apps.par.ButacaOcupadaException;
import es.uji.apps.par.NoHayButacasLibresException;
import es.uji.apps.par.db.ButacaDTO;
import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.db.LocalizacionDTO;
import es.uji.apps.par.db.PreciosSesionDTO;
import es.uji.apps.par.db.QButacaDTO;
import es.uji.apps.par.db.QLocalizacionDTO;
import es.uji.apps.par.db.QSesionDTO;
import es.uji.apps.par.db.SesionDTO;
import es.uji.apps.par.model.Butaca;

@Repository
public class ButacasDAO
{
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private LocalizacionesDAO localizacionesDAO;

    @Autowired
    private SesionesDAO sesionesDAO;

    private QButacaDTO qButacaDTO = QButacaDTO.butacaDTO;

    @Transactional
    public List<ButacaDTO> getButacas(long idSesion, String codigoLocalizacion)
    {
        QLocalizacionDTO qLocalizacionDTO = QLocalizacionDTO.localizacionDTO;
        QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;
        JPAQuery query = new JPAQuery(entityManager);

        List<ButacaDTO> list = query
                .from(qButacaDTO, qSesionDTO, qLocalizacionDTO)
                .where(qButacaDTO.parSesion.id.eq(qSesionDTO.id).and(qSesionDTO.id.eq(idSesion))
                        .and(qLocalizacionDTO.codigo.eq(codigoLocalizacion))
                        .and(qButacaDTO.parLocalizacion.id.eq(qLocalizacionDTO.id))).list(qButacaDTO);

        return list;
    }

    @Transactional
    public void addButaca(ButacaDTO butacaDTO)
    {
        entityManager.persist(butacaDTO);
    }

    public boolean estaOcupada(long idSesion, String codigoLocalizacion, String fila, String numero)
    {
        QLocalizacionDTO qLocalizacionDTO = QLocalizacionDTO.localizacionDTO;
        QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;

        JPAQuery query = new JPAQuery(entityManager);

        List<ButacaDTO> list = query
                .from(qButacaDTO, qSesionDTO, qLocalizacionDTO)
                .where(qButacaDTO.parSesion.id.eq(qSesionDTO.id).and(qSesionDTO.id.eq(idSesion))
                        .and(qLocalizacionDTO.codigo.eq(codigoLocalizacion))
                        .and(qButacaDTO.parLocalizacion.id.eq(qLocalizacionDTO.id)).and(qButacaDTO.fila.eq(fila))
                        .and(qButacaDTO.numero.eq(numero))).list(qButacaDTO);

        return list.size() > 0;
    }

    @Transactional
    public void reservaButacas(Long sesionId, CompraDTO compraDTO, List<Butaca> butacas) throws NoHayButacasLibresException, ButacaOcupadaException
    {
        reservaButacas(sesionId, compraDTO, butacas, 0);
    }
    
    @Transactional(rollbackForClassName={"NoHayButacasLibresException","ButacaOcupadaException"})
    public synchronized void reservaButacas(Long sesionId, CompraDTO compraDTO, List<Butaca> butacas, int sleep) throws NoHayButacasLibresException, ButacaOcupadaException
    {
        Butaca butacaActual = null;
        
        try
        {
            SesionDTO sesionDTO = sesionesDAO.getSesion(sesionId);
            
            for (Butaca butaca : butacas)
            {
                butacaActual = butaca;
                
                if (butaca.getFila()==null && butaca.getNumero()==null)
                {
                    int libres = numeroButacasLibres(sesionId, butaca.getLocalizacion());
                    
                    if (libres == 0)
                        throw new NoHayButacasLibresException(sesionId, butaca.getLocalizacion());
                }
                
                LocalizacionDTO localizacionDTO = localizacionesDAO.getLocalizacionByCodigo(butaca.getLocalizacion());
    
                ButacaDTO butacaDTO = Butaca.butacaToButacaDTO(butaca);
                butacaDTO.setParSesion(sesionDTO);
                butacaDTO.setParCompra(compraDTO);
                butacaDTO.setParLocalizacion(localizacionDTO);
    
                for (PreciosSesionDTO precioSesion : sesionDTO.getParPreciosSesions())
                {
                    if (precioSesion.getParLocalizacione().getCodigo().equals(butaca.getLocalizacion()))
                    {
                        if (butaca.getTipo().equals("normal"))
                            butacaDTO.setPrecio(precioSesion.getPrecio());
                        else if (butaca.getTipo().equals("descuento"))
                            butacaDTO.setPrecio(precioSesion.getDescuento());
                    }
                }
                
                entityManager.persist(butacaDTO);
                entityManager.flush();
            }
        }
        catch (JpaSystemException e)
        {
            if (butacaActual != null && e.getCause().getCause() instanceof ConstraintViolationException)
                throw new ButacaOcupadaException(sesionId, butacaActual.getLocalizacion(), butacaActual.getFila(), butacaActual.getNumero());
            else
                throw e;
        }
    }

    private int numeroButacasLibres(Long idSesion, String codigoLocalizacion)
    {
        QLocalizacionDTO qLocalizacionDTO = QLocalizacionDTO.localizacionDTO;
        QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;
        JPAQuery query = new JPAQuery(entityManager);
        
        
        LocalizacionDTO localizacionDTO = localizacionesDAO.getLocalizacionByCodigo(codigoLocalizacion);
        
        long ocupadas = query
                .from(qButacaDTO, qSesionDTO, qLocalizacionDTO)
                .where(qButacaDTO.parSesion.id.eq(qSesionDTO.id).and(qSesionDTO.id.eq(idSesion))
                        .and(qLocalizacionDTO.codigo.eq(codigoLocalizacion))
                        .and(qButacaDTO.parLocalizacion.id.eq(qLocalizacionDTO.id))).count();

        return (int) (localizacionDTO.getTotalEntradas().intValue() - ocupadas);
    }
}
