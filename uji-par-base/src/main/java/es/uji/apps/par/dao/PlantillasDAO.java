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

import es.uji.apps.par.db.PlantillaDTO;
import es.uji.apps.par.db.QPlantillaDTO;
import es.uji.apps.par.model.Plantilla;

@Repository
public class PlantillasDAO {

	@PersistenceContext
    private EntityManager entityManager;
	private QPlantillaDTO qPlantillaDTO = QPlantillaDTO.plantillaDTO;
	
	
	@Transactional
	public List<PlantillaDTO> get() {
		JPAQuery query = new JPAQuery(entityManager);

        List<PlantillaDTO> plantillaPrecios = new ArrayList<PlantillaDTO>();

        for (PlantillaDTO plantillaPreciosDB : query.from(qPlantillaDTO).orderBy(qPlantillaDTO.nombre.asc()).list(qPlantillaDTO))
        {
            plantillaPrecios.add(plantillaPreciosDB);
        }

        return plantillaPrecios;
	}
	
	@Transactional
	public long remove(long id) {
		JPADeleteClause delete = new JPADeleteClause(entityManager, qPlantillaDTO);
        return delete.where(qPlantillaDTO.id.eq(id)).execute();
	}
		
	@Transactional
	public Plantilla add(Plantilla plantillaPrecios) {
		PlantillaDTO plantillaDTO = new PlantillaDTO();
		plantillaDTO.setNombre(plantillaPrecios.getNombre());
		
		entityManager.persist(plantillaDTO);
		plantillaPrecios.setId(plantillaDTO.getId());
		
		return plantillaPrecios;
	}
	
	@Transactional
	public Plantilla update(Plantilla plantillaPrecios) {
		JPAUpdateClause update = new JPAUpdateClause(entityManager, qPlantillaDTO);
        update.set(qPlantillaDTO.nombre, plantillaPrecios.getNombre())
                .where(qPlantillaDTO.id.eq(plantillaPrecios.getId())).execute();

        return plantillaPrecios;
	}
}
