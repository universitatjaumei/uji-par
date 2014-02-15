package es.uji.apps.par.dao;

import java.util.ArrayList;
import java.util.List;

import es.uji.apps.par.PrecioRepetidoException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

import es.uji.apps.par.db.PreciosPlantillaDTO;
import es.uji.apps.par.db.QLocalizacionDTO;
import es.uji.apps.par.db.QPreciosPlantillaDTO;
import es.uji.apps.par.db.QTarifaDTO;
import es.uji.apps.par.model.Localizacion;
import es.uji.apps.par.model.Plantilla;
import es.uji.apps.par.model.PreciosPlantilla;
import es.uji.apps.par.model.Tarifa;

@Repository
public class PreciosPlantillaDAO extends BaseDAO {
	private QPreciosPlantillaDTO qPreciosPlantillaDTO = QPreciosPlantillaDTO.preciosPlantillaDTO;
	
	@Transactional
	public List<PreciosPlantillaDTO> getPreciosOfPlantilla(long plantillaPreciosId, String sortParameter, int start, int limit) {
        List<PreciosPlantillaDTO> precios = new ArrayList<PreciosPlantillaDTO>();
        List<PreciosPlantillaDTO> preciosPlantillaDTO = getQueryPreciosPlantilla(plantillaPreciosId).
        		orderBy(getSort(qPreciosPlantillaDTO, sortParameter)).offset(start).limit(limit).
        		list(qPreciosPlantillaDTO);

        for (PreciosPlantillaDTO precioDB : preciosPlantillaDTO)
        {
            precios.add(precioDB);
        }

        return precios;
	}

	@Transactional
	private JPAQuery getQueryPreciosPlantilla(long plantillaPreciosId) {
		JPAQuery query = new JPAQuery(entityManager);
		QLocalizacionDTO qLocalizacionDTO = QLocalizacionDTO.localizacionDTO;
		QTarifaDTO qTarifa = QTarifaDTO.tarifaDTO;
		return query.from(qPreciosPlantillaDTO, qLocalizacionDTO, qTarifa).
				where(qPreciosPlantillaDTO.parPlantilla.id.eq(plantillaPreciosId).
				and(qPreciosPlantillaDTO.parTarifa.id.eq(qTarifa.id)).
        		and(qLocalizacionDTO.id.eq(qPreciosPlantillaDTO.parLocalizacione.id)));
	}
	
	@Transactional
	public long remove(long plantillaId, long precioId) {
		JPADeleteClause delete = new JPADeleteClause(entityManager, qPreciosPlantillaDTO);
        return delete.where(qPreciosPlantillaDTO.id.eq(precioId).and(qPreciosPlantillaDTO.parPlantilla.id.eq(plantillaId))).execute();
	}
		
	@Transactional
	public PreciosPlantilla add(PreciosPlantilla precio) {
        PreciosPlantillaDTO preciosPlantillaDTO = new PreciosPlantillaDTO();
        preciosPlantillaDTO.setParLocalizacione(Localizacion.localizacionToLocalizacionDTO(precio.getLocalizacion()));
        preciosPlantillaDTO.setParPlantilla(Plantilla.plantillaPreciosToPlantillaPreciosDTO(precio.getPlantillaPrecios()));
        preciosPlantillaDTO.setPrecio(precio.getPrecio());
        preciosPlantillaDTO.setParTarifa(Tarifa.toDTO(precio.getTarifa()));

        entityManager.persist(preciosPlantillaDTO);
        precio.setId(preciosPlantillaDTO.getId());

        return precio;
	}
	
	@Transactional
	public PreciosPlantilla update(PreciosPlantilla precio) {
		JPAUpdateClause update = new JPAUpdateClause(entityManager, qPreciosPlantillaDTO);
        update.set(qPreciosPlantillaDTO.precio, precio.getPrecio())
        		.set(qPreciosPlantillaDTO.parLocalizacione, Localizacion.localizacionToLocalizacionDTO(precio.getLocalizacion()))
        		.set(qPreciosPlantillaDTO.parPlantilla, Plantilla.plantillaPreciosToPlantillaPreciosDTO(precio.getPlantillaPrecios()))
                .where(qPreciosPlantillaDTO.id.eq(precio.getId())).execute();

        return precio;
	}

	@Transactional
	public int getTotalPreciosOfPlantilla(long plantillaPreciosId) {
		return (int) getQueryPreciosPlantilla(plantillaPreciosId).count();
	}
}
