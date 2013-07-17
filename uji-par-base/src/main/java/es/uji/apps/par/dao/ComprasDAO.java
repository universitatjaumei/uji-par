package es.uji.apps.par.dao;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.db.CompraDTO;

@Repository
public class ComprasDAO
{
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public CompraDTO guardaCompra(String nombre, String apellidos, String telefono, String email, Date fecha)
    {
        CompraDTO compraDTO = new CompraDTO(nombre, apellidos, telefono, email, new Timestamp(fecha.getTime()));

        entityManager.persist(compraDTO);

        return compraDTO;
    }

}
