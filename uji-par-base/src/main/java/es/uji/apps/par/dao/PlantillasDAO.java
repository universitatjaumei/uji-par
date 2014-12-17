package es.uji.apps.par.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

import es.uji.apps.par.db.PlantillaDTO;
import es.uji.apps.par.db.QPlantillaDTO;
import es.uji.apps.par.db.QSalaDTO;
import es.uji.apps.par.db.SalaDTO;
import es.uji.apps.par.model.Plantilla;

@Repository
public class PlantillasDAO extends BaseDAO {

	private QPlantillaDTO qPlantillaDTO = QPlantillaDTO.plantillaDTO;
	
	@Transactional
	public List<PlantillaDTO> get(boolean filtrarEditables, String sortParameter, int start, int limit) {
        List<PlantillaDTO> plantillaPrecios = new ArrayList<PlantillaDTO>();
        List<PlantillaDTO> listaPlantillaPreciosDTO = new ArrayList<PlantillaDTO>();
        
        if (filtrarEditables)
        	listaPlantillaPreciosDTO = getQueryPlantillasEditables().orderBy(getSort(qPlantillaDTO, sortParameter)).
        		offset(start).limit(limit).list(qPlantillaDTO);
        else
        	listaPlantillaPreciosDTO = getQueryPlantillas().orderBy(getSort(qPlantillaDTO, sortParameter)).
        		offset(start).limit(limit).list(qPlantillaDTO);

        for (PlantillaDTO plantillaPreciosDB : listaPlantillaPreciosDTO)
        {
            plantillaPrecios.add(plantillaPreciosDB);
        }

        return plantillaPrecios;
	}

	@Transactional
	private JPAQuery getQueryPlantillasEditables() {
		QSalaDTO qSalaDTO = new QSalaDTO("qSalaDTO");
		JPAQuery query = new JPAQuery(entityManager);
		return query.from(qPlantillaDTO).leftJoin(qPlantillaDTO.sala, qSalaDTO).fetch().where(qPlantillaDTO.id.ne(Long.valueOf("-1")));
	}
	
	@Transactional
	private JPAQuery getQueryPlantillas() {
		QSalaDTO qSalaDTO = new QSalaDTO("qSalaDTO");
		JPAQuery query = new JPAQuery(entityManager);
		return query.from(qPlantillaDTO).leftJoin(qPlantillaDTO.sala, qSalaDTO).fetch();
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
		plantillaDTO.setSala(new SalaDTO(plantillaPrecios.getSala().getId()));
		
		entityManager.persist(plantillaDTO);
		plantillaPrecios.setId(plantillaDTO.getId());
		
		return plantillaPrecios;
	}
	
	@Transactional
	public Plantilla update(Plantilla plantillaPrecios) {
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qPlantillaDTO);
        update.set(qPlantillaDTO.nombre, plantillaPrecios.getNombre())
        	.set(qPlantillaDTO.sala, new SalaDTO(plantillaPrecios.getSala().getId()))
            .where(qPlantillaDTO.id.eq(plantillaPrecios.getId())).execute();

        return plantillaPrecios;
	}

	@Transactional
	public PlantillaDTO getPlantillaById(long id) {
		return entityManager.find(PlantillaDTO.class, id); 
	}

	@Transactional
	public int getTotalPlantillaPrecios() {
		return (int) getQueryPlantillas().count();
	}

	@Transactional
	public int getTotalPlantillasEditables() {
		return (int) getQueryPlantillasEditables().count();
	}
}
