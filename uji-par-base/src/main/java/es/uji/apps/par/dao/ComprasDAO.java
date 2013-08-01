package es.uji.apps.par.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.db.CompraDTO;
import es.uji.apps.par.db.SesionDTO;

@Repository
public class ComprasDAO
{
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private SesionesDAO sesionDAO;

    @Transactional
    public CompraDTO insertaCompra(Long sesionId, String nombre, String apellidos, String telefono, String email, Date fecha, boolean taquilla, BigDecimal importe)
    {
        SesionDTO sesion = sesionDAO.getSesion(sesionId);
        
        CompraDTO compraDTO = new CompraDTO(sesion, nombre, apellidos, telefono, email, new Timestamp(fecha.getTime()), taquilla, importe);

        entityManager.persist(compraDTO);

        return compraDTO;
    }
    
    @Transactional
    public CompraDTO guardaCompra(Long compraId, Long sesionId, String nombre, String apellidos, String telefono, String email, Date fecha, boolean taquilla, BigDecimal importe)
    {
        SesionDTO sesion = sesionDAO.getSesion(sesionId);
        
        CompraDTO compraDTO = getCompraById(compraId);
        
        compraDTO.setParSesion(sesion);
        compraDTO.setNombre(nombre);
        compraDTO.setApellidos(apellidos);
        compraDTO.setTelefono(telefono);
        compraDTO.setEmail(email);
        compraDTO.setFecha(new Timestamp(fecha.getTime()));
        compraDTO.setTaquilla(taquilla);
        compraDTO.setImporte(importe);
        
        entityManager.persist(compraDTO);

        return compraDTO;
    }

    public CompraDTO getCompraById(long id)
    {
        return entityManager.find(CompraDTO.class, id);
    }

}
