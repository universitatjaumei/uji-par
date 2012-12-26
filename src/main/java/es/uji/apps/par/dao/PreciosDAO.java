package es.uji.apps.par.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

import es.uji.apps.par.db.PrecioDTO;
import es.uji.apps.par.db.QLocalizacionDTO;
import es.uji.apps.par.db.QPrecioDTO;
import es.uji.apps.par.model.Localizacion;
import es.uji.apps.par.model.PlantillaPrecios;
import es.uji.apps.par.model.Precio;

@Repository
public class PreciosDAO {

	@PersistenceContext
    private EntityManager entityManager;
	private QPrecioDTO qPrecioDTO = QPrecioDTO.precioDTO;
	
	
	@Transactional
	public List<PrecioDTO> getPreciosOfPlantilla(long plantillaPreciosId) {
		JPAQuery query = new JPAQuery(entityManager);
		QLocalizacionDTO qLocalizacionDTO = QLocalizacionDTO.localizacionDTO;

        List<PrecioDTO> precios = new ArrayList<PrecioDTO>();

        for (PrecioDTO precioDB : query.from(qPrecioDTO, qLocalizacionDTO).where(qPrecioDTO.parPlantillasPrecio.id.eq(plantillaPreciosId).
        		and(qLocalizacionDTO.id.eq(qPrecioDTO.parLocalizacione.id))).list(qPrecioDTO))
        {
            precios.add(precioDB);
        }

        return precios;
	}
	
	@Transactional
	public long remove(long plantillaId, long precioId) {
		JPADeleteClause delete = new JPADeleteClause(entityManager, qPrecioDTO);
        return delete.where(qPrecioDTO.id.eq(precioId).and(qPrecioDTO.parPlantillasPrecio.id.eq(plantillaId))).execute();
	}
		
	@Transactional
	public Precio add(Precio precio) {
		PrecioDTO precioDTO = new PrecioDTO();
		precioDTO.setDescuento(precio.getDescuento());
		precioDTO.setInvitacion(precio.getInvitacion());
		precioDTO.setParLocalizacione(Localizacion.localizacionToLocalizacionDTO(precio.getLocalizacion()));
		//precioDTO.setParLocalizacione(precio.getLocalizacion());
		precioDTO.setParPlantillasPrecio(PlantillaPrecios.plantillaPreciosToPlantillaPreciosDTO(precio.getPlantillaPrecios()));
		precioDTO.setPrecio(precio.getPrecio());
		
		entityManager.persist(precioDTO);
		precio.setId(precioDTO.getId());
		
		return precio;
	}
	
	@Transactional
	public Precio update(Precio precio) {
		JPAUpdateClause update = new JPAUpdateClause(entityManager, qPrecioDTO);
        update.set(qPrecioDTO.descuento, precio.getDescuento())
        		.set(qPrecioDTO.invitacion, precio.getInvitacion())
        		.set(qPrecioDTO.precio, precio.getPrecio())
        		//.set(qPrecioDTO.parLocalizacione, Localizacion.localizacionToLocalizacionDTO(precio.getLocalizacion()))
        		.set(qPrecioDTO.parLocalizacione, Localizacion.localizacionToLocalizacionDTO(precio.getLocalizacion()))
        		.set(qPrecioDTO.parPlantillasPrecio, PlantillaPrecios.plantillaPreciosToPlantillaPreciosDTO(precio.getPlantillaPrecios()))
                .where(qPrecioDTO.id.eq(precio.getId())).execute();

        return precio;
	}
}
