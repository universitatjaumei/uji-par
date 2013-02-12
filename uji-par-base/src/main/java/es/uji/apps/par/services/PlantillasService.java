package es.uji.apps.par.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.CampoRequeridoException;
import es.uji.apps.par.dao.PlantillasDAO;
import es.uji.apps.par.db.PlantillaDTO;
import es.uji.apps.par.model.Plantilla;

@Service
public class PlantillasService {
	
	@Autowired
    private PlantillasDAO plantillasPreciosDAO;

	private List<Plantilla> get(boolean filtrarEditables) {
		List<Plantilla> plantillaPrecios = new ArrayList<Plantilla>();
		
		for (PlantillaDTO plantillaDTO: plantillasPreciosDAO.get(filtrarEditables)) {
			plantillaPrecios.add(new Plantilla(plantillaDTO));
		}
		return plantillaPrecios;
	}

	public void remove(int id) {
		plantillasPreciosDAO.remove(id);
	}

	public Plantilla add(Plantilla plantillaPrecios) throws CampoRequeridoException {
		checkRequiredFields(plantillaPrecios);
		return plantillasPreciosDAO.add(plantillaPrecios);
	}

	private void checkRequiredFields(Plantilla plantillaPrecios) throws CampoRequeridoException {
		if (plantillaPrecios.getNombre() == null || plantillaPrecios.getNombre().isEmpty())
			throw new CampoRequeridoException("Nombre");
	}

	public void update(Plantilla plantillaPrecios) throws CampoRequeridoException {
		checkRequiredFields(plantillaPrecios);
		plantillasPreciosDAO.update(plantillaPrecios);
	}

	public List<Plantilla> getEditables() {
		boolean filtrarEditables = true;
		return get(filtrarEditables);
	}
	
	public List<Plantilla> getAll() {
		boolean filtrarEditables = false;
		return get(filtrarEditables);
	}
}
