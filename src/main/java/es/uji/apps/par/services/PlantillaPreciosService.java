package es.uji.apps.par.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.CampoRequeridoException;
import es.uji.apps.par.dao.PlantillasPreciosDAO;
import es.uji.apps.par.db.PlantillaPreciosDTO;
import es.uji.apps.par.model.PlantillaPrecios;

@Service
public class PlantillaPreciosService {
	
	@Autowired
    private PlantillasPreciosDAO plantillasPreciosDAO;

	public List<PlantillaPrecios> get() {
		List<PlantillaPrecios> plantillaPrecios = new ArrayList<PlantillaPrecios>();
		
		for (PlantillaPreciosDTO plantillaDTO: plantillasPreciosDAO.get()) {
			plantillaPrecios.add(new PlantillaPrecios(plantillaDTO));
		}
		return plantillaPrecios;
	}

	public void remove(int id) {
		plantillasPreciosDAO.remove(id);
	}

	public PlantillaPrecios add(PlantillaPrecios plantillaPrecios) throws CampoRequeridoException {
		checkRequiredFields(plantillaPrecios);
		return plantillasPreciosDAO.add(plantillaPrecios);
	}

	private void checkRequiredFields(PlantillaPrecios plantillaPrecios) throws CampoRequeridoException {
		if (plantillaPrecios.getNombre() == null || plantillaPrecios.getNombre().isEmpty())
			throw new CampoRequeridoException("Nombre");
	}

	public void update(PlantillaPrecios plantillaPrecios) throws CampoRequeridoException {
		checkRequiredFields(plantillaPrecios);
		plantillasPreciosDAO.update(plantillaPrecios);
	}
}
