package es.uji.apps.par.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPAQuery;

import es.uji.apps.par.ButacaOcupadaException;
import es.uji.apps.par.NoHayButacasLibresException;
import es.uji.apps.par.db.ButacaDTO;
import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.db.LocalizacionDTO;
import es.uji.apps.par.db.LocalizacionOcupadasDTO;
import es.uji.apps.par.db.PreciosSesionDTO;
import es.uji.apps.par.db.QButacaDTO;
import es.uji.apps.par.db.QLocalizacionDTO;
import es.uji.apps.par.db.QLocalizacionOcupadasDTO;
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

    
    @Transactional(rollbackForClassName={"NoHayButacasLibresException","ButacaOcupadaException"})
    public synchronized void reservaButacas(Long sesionId, CompraDTO compraDTO, List<Butaca> butacas) throws NoHayButacasLibresException, ButacaOcupadaException
    {
        SesionDTO sesionDTO = sesionesDAO.getSesion(sesionId);
        
        if (sesionDTO.getParEvento().getAsientosNumerados().intValue() == 0)
            reservaButacasNoNumeradas(sesionId, compraDTO, butacas);
        else
            reservaButacasNumeradas(sesionId, compraDTO, butacas);
    }
    
    private void reservaButacasNoNumeradas(Long sesionId, CompraDTO compraDTO, List<Butaca> butacas) throws NoHayButacasLibresException
    {
        for (Butaca butaca : butacas)
        {
            if (hayButacasNoNumeradasLibres(sesionId, butaca.getLocalizacion()))
            {
                ocupaButacaNoNumerada(sesionId, butaca.getLocalizacion());
            }
            else
            {
                throw new NoHayButacasLibresException(sesionId, butaca.getLocalizacion());
            }
        }
    }
    
    private boolean hayButacasNoNumeradasLibres(Long idSesion, String codigoLocalizacion)
    {
        LocalizacionDTO localizacionDTO = localizacionesDAO.getLocalizacionByCodigo(codigoLocalizacion);
        LocalizacionOcupadasDTO ocupadas = getOcupadas(idSesion, codigoLocalizacion);
        
        if (ocupadas == null)
            return true;
        else
            return ocupadas.getOcupadas() < localizacionDTO.getTotalEntradas().intValue();
    }
    
    private LocalizacionOcupadasDTO getOcupadas(Long idSesion, String codigoLocalizacion)
    {
        QLocalizacionDTO qLocalizacionDTO = QLocalizacionDTO.localizacionDTO;
        QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;
        QLocalizacionOcupadasDTO qOcupadasDTO = QLocalizacionOcupadasDTO.localizacionOcupadasDTO;
        
        JPAQuery query = new JPAQuery(entityManager);
        
         List<LocalizacionOcupadasDTO> ocupadas = query
                .from(qOcupadasDTO, qSesionDTO, qLocalizacionDTO)
                .where(qOcupadasDTO.parSesion.id.eq(qSesionDTO.id).and(qSesionDTO.id.eq(idSesion))
                        .and(qLocalizacionDTO.codigo.eq(codigoLocalizacion))
                        .and(qOcupadasDTO.parLocalizacion.id.eq(qLocalizacionDTO.id))).list(qOcupadasDTO);

        if (ocupadas.size() == 0)
            return null;
        else
            return ocupadas.get(0);
    }
    
    private void ocupaButacaNoNumerada(Long idSesion, String codigoLocalizacion)
    {
        LocalizacionOcupadasDTO ocupadasLocalizacion = getOcupadas(idSesion, codigoLocalizacion);
        
        if (ocupadasLocalizacion == null)
        {
            ocupadasLocalizacion = new LocalizacionOcupadasDTO(sesionesDAO.getSesion(idSesion), localizacionesDAO.getLocalizacionByCodigo(codigoLocalizacion));
            ocupadasLocalizacion.setOcupadas(0);
        }
        
        ocupadasLocalizacion.setOcupadas(ocupadasLocalizacion.getOcupadas()+1);
        
        entityManager.persist(ocupadasLocalizacion);
    }
    
    private void reservaButacasNumeradas(Long sesionId, CompraDTO compraDTO, List<Butaca> butacas) throws ButacaOcupadaException
    {
        Butaca butacaActual = null;
        
        try
        {
            SesionDTO sesionDTO = sesionesDAO.getSesion(sesionId);
            List<PreciosSesionDTO> parPreciosSesions = sesionDTO.getParPreciosSesions();
            
            for (Butaca butaca : butacas)
            {
                butacaActual = butaca;
                
                LocalizacionDTO localizacionDTO = localizacionesDAO.getLocalizacionByCodigo(butaca.getLocalizacion());
    
                ButacaDTO butacaDTO = Butaca.butacaToButacaDTO(butaca);
                butacaDTO.setParSesion(sesionDTO);
                butacaDTO.setParCompra(compraDTO);
                butacaDTO.setParLocalizacion(localizacionDTO);
    
                for (PreciosSesionDTO precioSesion : parPreciosSesions)
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

    public List<LocalizacionOcupadasDTO> getDisponiblesSesion(long idSesion)
    {
        QLocalizacionDTO qLocalizacionDTO = QLocalizacionDTO.localizacionDTO;
        QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;
        QLocalizacionOcupadasDTO qOcupadasDTO = QLocalizacionOcupadasDTO.localizacionOcupadasDTO;
        
        JPAQuery query = new JPAQuery(entityManager);
        
          List<LocalizacionOcupadasDTO> ocupadas = query
        .from(qOcupadasDTO, qSesionDTO, qLocalizacionDTO)
        .where(qOcupadasDTO.parSesion.id.eq(qSesionDTO.id).and(qSesionDTO.id.eq(idSesion))
                .and(qOcupadasDTO.parLocalizacion.id.eq(qLocalizacionDTO.id))).list(qOcupadasDTO);
         
         return ocupadas;
    }

}
