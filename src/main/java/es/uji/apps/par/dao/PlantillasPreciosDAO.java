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

import es.uji.apps.par.db.PlantillaPreciosDTO;
import es.uji.apps.par.db.QPlantillaPreciosDTO;
import es.uji.apps.par.model.PlantillaPrecios;

@Repository
public class PlantillasPreciosDAO {

	@PersistenceContext
    private EntityManager entityManager;
	private QPlantillaPreciosDTO qPlantillaPreciosDTO = QPlantillaPreciosDTO.plantillaPreciosDTO;
	
	
	@Transactional
	public List<PlantillaPreciosDTO> get() {
		JPAQuery query = new JPAQuery(entityManager);

        List<PlantillaPreciosDTO> plantillaPrecios = new ArrayList<PlantillaPreciosDTO>();

        for (PlantillaPreciosDTO plantillaPreciosDB : query.from(qPlantillaPreciosDTO).orderBy(qPlantillaPreciosDTO.nombre.asc()).list(
                qPlantillaPreciosDTO))
        {
            plantillaPrecios.add(plantillaPreciosDB);
        }

        return plantillaPrecios;
	}
	
	@Transactional
	public long remove(long id) {
		JPADeleteClause delete = new JPADeleteClause(entityManager, qPlantillaPreciosDTO);
        return delete.where(qPlantillaPreciosDTO.id.eq(id)).execute();
	}
		
	@Transactional
	public PlantillaPrecios add(PlantillaPrecios plantillaPrecios) {
		PlantillaPreciosDTO plantillaPreciosDTO = new PlantillaPreciosDTO();
		plantillaPreciosDTO.setNombre(plantillaPrecios.getNombre());
		
		entityManager.persist(plantillaPreciosDTO);
		plantillaPrecios.setId(plantillaPreciosDTO.getId());
		
		return plantillaPrecios;
	}
	
	@Transactional
	public PlantillaPrecios update(PlantillaPrecios plantillaPrecios) {
		JPAUpdateClause update = new JPAUpdateClause(entityManager, qPlantillaPreciosDTO);
        update.set(qPlantillaPreciosDTO.nombre, plantillaPrecios.getNombre())
                .where(qPlantillaPreciosDTO.id.eq(plantillaPrecios.getId())).execute();

        return plantillaPrecios;
	}
}
