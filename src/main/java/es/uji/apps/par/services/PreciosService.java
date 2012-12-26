package es.uji.apps.par.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.CampoRequeridoException;
import es.uji.apps.par.dao.PreciosDAO;
import es.uji.apps.par.db.PrecioDTO;
import es.uji.apps.par.model.Precio;

@Service
public class PreciosService {
	@Autowired
    private PreciosDAO preciosDAO;

	public List<Precio> getPreciosOfPlantilla(long plantillaPreciosId) {
		List<Precio> listaPrecios = new ArrayList<Precio>();
		for (PrecioDTO precioDB : preciosDAO.getPreciosOfPlantilla(plantillaPreciosId))
        {
            listaPrecios.add(new Precio(precioDB));
        }
        return listaPrecios;
	}

	public void remove(int plantillaId, int precioId) {
		preciosDAO.remove(plantillaId, precioId);
	}

	public Precio add(Precio precio) throws CampoRequeridoException {
		checkRequiredFields(precio);
		return preciosDAO.add(precio);
	}

	private void checkRequiredFields(Precio precio) throws CampoRequeridoException {
		if (precio.getPlantillaPrecios() == null)
			throw new CampoRequeridoException("Plantilla de precios");
		
		if (precio.getLocalizacion() == null)
			throw new CampoRequeridoException("Localización");
	}

	public void update(Precio precio) throws CampoRequeridoException {
		checkRequiredFields(precio);
		preciosDAO.update(precio);
	}
}
