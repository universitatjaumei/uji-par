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

import es.uji.apps.par.db.PreciosPlantillaDTO;
import es.uji.apps.par.db.QLocalizacionDTO;
import es.uji.apps.par.db.QPreciosPlantillaDTO;
import es.uji.apps.par.model.Localizacion;
import es.uji.apps.par.model.Plantilla;
import es.uji.apps.par.model.PreciosPlantilla;

@Repository
public class PreciosPlantillaDAO {

	@PersistenceContext
    private EntityManager entityManager;
	private QPreciosPlantillaDTO qPreciosPlantillaDTO = QPreciosPlantillaDTO.preciosPlantillaDTO;
	
	
	@Transactional
	public List<PreciosPlantillaDTO> getPreciosOfPlantilla(long plantillaPreciosId) {
		JPAQuery query = new JPAQuery(entityManager);
		QLocalizacionDTO qLocalizacionDTO = QLocalizacionDTO.localizacionDTO;

        List<PreciosPlantillaDTO> precios = new ArrayList<PreciosPlantillaDTO>();

        for (PreciosPlantillaDTO precioDB : query.from(qPreciosPlantillaDTO, qLocalizacionDTO).where(qPreciosPlantillaDTO.parPlantilla.id.eq(plantillaPreciosId).
        		and(qLocalizacionDTO.id.eq(qPreciosPlantillaDTO.parLocalizacione.id))).list(qPreciosPlantillaDTO))
        {
            precios.add(precioDB);
        }

        return precios;
	}
	
	@Transactional
	public long remove(long plantillaId, long precioId) {
		JPADeleteClause delete = new JPADeleteClause(entityManager, qPreciosPlantillaDTO);
        return delete.where(qPreciosPlantillaDTO.id.eq(precioId).and(qPreciosPlantillaDTO.parPlantilla.id.eq(plantillaId))).execute();
	}
		
	@Transactional
	public PreciosPlantilla add(PreciosPlantilla precio) {
		PreciosPlantillaDTO preciosPlantillaDTO = new PreciosPlantillaDTO();
		preciosPlantillaDTO.setDescuento(precio.getDescuento());
		preciosPlantillaDTO.setInvitacion(precio.getInvitacion());
		preciosPlantillaDTO.setParLocalizacione(Localizacion.localizacionToLocalizacionDTO(precio.getLocalizacion()));
		//precioDTO.setParLocalizacione(precio.getLocalizacion());
		preciosPlantillaDTO.setParPlantilla(Plantilla.plantillaPreciosToPlantillaPreciosDTO(precio.getPlantillaPrecios()));
		preciosPlantillaDTO.setPrecio(precio.getPrecio());
		
		entityManager.persist(preciosPlantillaDTO);
		precio.setId(preciosPlantillaDTO.getId());
		
		return precio;
	}
	
	@Transactional
	public PreciosPlantilla update(PreciosPlantilla precio) {
		JPAUpdateClause update = new JPAUpdateClause(entityManager, qPreciosPlantillaDTO);
        update.set(qPreciosPlantillaDTO.descuento, precio.getDescuento())
        		.set(qPreciosPlantillaDTO.invitacion, precio.getInvitacion())
        		.set(qPreciosPlantillaDTO.precio, precio.getPrecio())
        		//.set(qPrecioDTO.parLocalizacione, Localizacion.localizacionToLocalizacionDTO(precio.getLocalizacion()))
        		.set(qPreciosPlantillaDTO.parLocalizacione, Localizacion.localizacionToLocalizacionDTO(precio.getLocalizacion()))
        		.set(qPreciosPlantillaDTO.parPlantilla, Plantilla.plantillaPreciosToPlantillaPreciosDTO(precio.getPlantillaPrecios()))
                .where(qPreciosPlantillaDTO.id.eq(precio.getId())).execute();

        return precio;
	}
}
