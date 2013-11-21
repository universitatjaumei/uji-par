package es.uji.apps.par.dao;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

import es.uji.apps.par.ButacaOcupadaException;
import es.uji.apps.par.NoHayButacasLibresException;
import es.uji.apps.par.db.ButacaDTO;
import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.db.LocalizacionDTO;
import es.uji.apps.par.db.PreciosSesionDTO;
import es.uji.apps.par.db.QButacaDTO;
import es.uji.apps.par.db.QCompraDTO;
import es.uji.apps.par.db.QLocalizacionDTO;
import es.uji.apps.par.db.QSesionDTO;
import es.uji.apps.par.db.SesionDTO;
import es.uji.apps.par.model.Butaca;

@Repository
public class ButacasDAO extends BaseDAO
{
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
        QCompraDTO qCompraDTO = QCompraDTO.compraDTO;
        
        JPAQuery query = new JPAQuery(entityManager);

        List<ButacaDTO> list = query
                .from(qButacaDTO)
                .join(qButacaDTO.parSesion, qSesionDTO)
                .join(qButacaDTO.parLocalizacion, qLocalizacionDTO)
                .leftJoin(qButacaDTO.parCompra, qCompraDTO).fetch()
                .where(qSesionDTO.id.eq(idSesion)
                        .and(qLocalizacionDTO.codigo.eq(codigoLocalizacion)))
                        .list(qButacaDTO);

        return list;
    }

    @Transactional
    public void addButaca(ButacaDTO butacaDTO)
    {
        entityManager.persist(butacaDTO);
    }

    @Transactional
    public boolean estaOcupada(long idSesion, String codigoLocalizacion, String fila, String numero)
    {
        QLocalizacionDTO qLocalizacionDTO = QLocalizacionDTO.localizacionDTO;
        QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;

        JPAQuery query = new JPAQuery(entityManager);

        List<ButacaDTO> list = query
                .from(qButacaDTO, qSesionDTO, qLocalizacionDTO)
                .where(qButacaDTO.parSesion.id.eq(qSesionDTO.id).and(qSesionDTO.id.eq(idSesion))
                        .and(qLocalizacionDTO.codigo.eq(codigoLocalizacion))
                        .and(qButacaDTO.parLocalizacion.id.eq(qLocalizacionDTO.id))
                        .and(qButacaDTO.anulada.eq(false))
                        .and(qButacaDTO.fila.eq(fila))
                        .and(qButacaDTO.numero.eq(numero))).list(qButacaDTO);

        return list.size() > 0;
    }

    @Transactional
    public CompraDTO getCompra(long idSesion, String codigoLocalizacion, String fila, String numero)
    {
        QLocalizacionDTO qLocalizacionDTO = QLocalizacionDTO.localizacionDTO;
        QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;
        QCompraDTO qCompraDTO = QCompraDTO.compraDTO;

        JPAQuery query = new JPAQuery(entityManager);

        List<CompraDTO> list = query
                .from(qButacaDTO)
                .join(qButacaDTO.parSesion, qSesionDTO)
                .join(qButacaDTO.parLocalizacion, qLocalizacionDTO)
                .join(qButacaDTO.parCompra, qCompraDTO)
                .where(qSesionDTO.id.eq(idSesion)
                        .and(qLocalizacionDTO.codigo.eq(codigoLocalizacion))
                        .and(qButacaDTO.fila.eq(fila))
                        .and(qButacaDTO.numero.eq(numero))
                        .and(qButacaDTO.anulada.isNull().or(qButacaDTO.anulada.eq(false))))
                        .list(qCompraDTO);

        if (list.size() == 0)
            return null;
        else
            return list.get(0);
    }
    
    @Transactional
    private void deleteButacas(CompraDTO compraDTO)
    {
        QButacaDTO qButacaDTO = QButacaDTO.butacaDTO;
        
        new JPADeleteClause(entityManager, qButacaDTO).where(qButacaDTO.parCompra.id.eq(compraDTO.getId())).execute();  
    }

    @Transactional(rollbackForClassName={"NoHayButacasLibresException","ButacaOcupadaException"})
    public void reservaButacas(Long sesionId, CompraDTO compraDTO, List<Butaca> butacas) throws ButacaOcupadaException, NoHayButacasLibresException
    {
        Butaca butacaActual = null;
        
        try
        {
            deleteButacas(compraDTO);
            
            SesionDTO sesionDTO = sesionesDAO.getSesion(sesionId);
            List<PreciosSesionDTO> parPreciosSesions = sesionDTO.getParPreciosSesions();
            
            for (Butaca butaca : butacas)
            {
                butacaActual = butaca;
                
                LocalizacionDTO localizacionDTO = localizacionesDAO.getLocalizacionByCodigo(butaca.getLocalizacion());
                
                if (butaca.getFila()==null && butaca.getNumero()==null && 
                    noHayButacasLibres(sesionDTO, localizacionDTO))
                {
                    throw new NoHayButacasLibresException(sesionId, butaca.getLocalizacion());
                }
    
                ButacaDTO butacaDTO = Butaca.butacaToButacaDTO(butaca);
                butacaDTO.setParSesion(sesionDTO);
                butacaDTO.setParCompra(compraDTO);
                butacaDTO.setParLocalizacion(localizacionDTO);
                butacaDTO.setTipo(butaca.getTipo());
                butacaDTO.setAnulada(false);
    
                for (PreciosSesionDTO precioSesion : parPreciosSesions)
                {
                    if (precioSesion.getParLocalizacione().getCodigo().equals(butaca.getLocalizacion()))
                    {
                        if (butaca.getTipo().equals("normal"))
                            butacaDTO.setPrecio(precioSesion.getPrecio());
                        else if (butaca.getTipo().equals("descuento"))
                            butacaDTO.setPrecio(precioSesion.getDescuento());
                        else if (butaca.getTipo().equals("aulaTeatro"))
                            butacaDTO.setPrecio(precioSesion.getAulaTeatro());                        
                        else if (butaca.getTipo().equals("invitacion"))
                            butacaDTO.setPrecio(precioSesion.getInvitacion());                        
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

    @Transactional
    private boolean noHayButacasLibres(SesionDTO sesionDTO, LocalizacionDTO localizacionDTO)
    {
        return getOcupadas(sesionDTO.getId(), localizacionDTO.getCodigo()) >= localizacionDTO.getTotalEntradas().intValue();
    }

    @Transactional
    public int getOcupadas(long sesionId, String codigoLocalizacion)
    {
        QLocalizacionDTO qLocalizacionDTO = QLocalizacionDTO.localizacionDTO;
        QSesionDTO qSesionDTO = QSesionDTO.sesionDTO;
        QButacaDTO qButacaDTO = QButacaDTO.butacaDTO;
        
        JPAQuery query = new JPAQuery(entityManager);
        
        List<ButacaDTO> list = query.from(qButacaDTO)
                .innerJoin(qButacaDTO.parSesion, qSesionDTO)
                .innerJoin(qButacaDTO.parLocalizacion, qLocalizacionDTO)
                .where(qSesionDTO.id.eq(sesionId)
                        .and(qButacaDTO.anulada.eq(false))
                        .and(qLocalizacionDTO.codigo.eq(codigoLocalizacion))).listDistinct(qButacaDTO);
        
        long ocupadas = list.size();
        
        return (int) ocupadas;
    }

    @Transactional
	public List<ButacaDTO> getButacasCompra(Long idCompra, String sortParameter, int start, int limit) {
		return getQueryButacasCompra(idCompra).orderBy(getSort(qButacaDTO, sortParameter)).offset(start).limit(limit).list(qButacaDTO);
	}
    
    @Transactional
    private JPAQuery getQueryButacasCompra(Long idCompra) {
    	JPAQuery query = new JPAQuery(entityManager);
    	QCompraDTO qCompraDTO= QCompraDTO.compraDTO;
    	
    	return query.from(qButacaDTO)
    	        .join(qButacaDTO.parCompra, qCompraDTO).fetch()
    	        .where(qCompraDTO.id.eq(idCompra));
    }

    @Transactional
	public int getTotalButacasCompra(Long idCompra) {
		return (int) getQueryButacasCompra(idCompra).count();
	}

    public List<ButacaDTO> getButacasNoAnuladas(Long idSesion)
    {
        QCompraDTO qCompraDTO = QCompraDTO.compraDTO;
        
        JPAQuery query = new JPAQuery(entityManager);
        return query.from(qButacaDTO).join(qButacaDTO.parCompra, qCompraDTO).fetch()
                .where(qButacaDTO.parSesion.id.eq(idSesion).and(qButacaDTO.anulada.eq(false)))
                .distinct().list(qButacaDTO);
    }

    @Transactional
    public void updatePresentadas(Long sesionId, List<Butaca> butacas)
    {
        for (Butaca butaca: butacas)
        {
            new JPAUpdateClause(entityManager, qButacaDTO)
                .where(qButacaDTO.id.eq(butaca.getId()))
                .set(qButacaDTO.presentada, butaca.getPresentada()).execute();
        }
    }

}
