package es.uji.apps.par.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.CampoRequeridoException;
import es.uji.apps.par.dao.PreciosPlantillaDAO;
import es.uji.apps.par.db.PreciosPlantillaDTO;
import es.uji.apps.par.model.PreciosPlantilla;

@Service
public class PreciosPlantillaService {
	@Autowired
    private PreciosPlantillaDAO preciosDAO;

	public List<PreciosPlantilla> getPreciosOfPlantilla(long plantillaPreciosId) {
		List<PreciosPlantilla> listaPrecios = new ArrayList<PreciosPlantilla>();
		for (PreciosPlantillaDTO preciosPlantillaDB : preciosDAO.getPreciosOfPlantilla(plantillaPreciosId))
        {
            listaPrecios.add(new PreciosPlantilla(preciosPlantillaDB));
        }
        return listaPrecios;
	}

	public void remove(int plantillaId, int precioId) {
		preciosDAO.remove(plantillaId, precioId);
	}

	public PreciosPlantilla add(PreciosPlantilla precio) throws CampoRequeridoException {
		checkRequiredFields(precio);
		return preciosDAO.add(precio);
	}

	private void checkRequiredFields(PreciosPlantilla precio) throws CampoRequeridoException {
		if (precio.getPlantillaPrecios() == null)
			throw new CampoRequeridoException("Plantilla de precios");
		
		if (precio.getLocalizacion() == null)
			throw new CampoRequeridoException("Localización");
	}

	public void update(PreciosPlantilla precio) throws CampoRequeridoException {
		checkRequiredFields(precio);
		preciosDAO.update(precio);
	}
}
