package es.uji.apps.par.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.dao.LocalizacionesDAO;
import es.uji.apps.par.exceptions.ParCampoRequeridoException;
import es.uji.apps.par.model.ParLocalizacion;

@Service
public class LocalizacionesService {

	@Autowired
    private LocalizacionesDAO localizacionesDAO;

	public List<ParLocalizacion> get() {
		return localizacionesDAO.get();
	}

	public void remove(Integer id) {
		localizacionesDAO.remove(id);
	}

	public ParLocalizacion add(ParLocalizacion localizacion) throws ParCampoRequeridoException {
		checkRequiredFields(localizacion);
        return localizacionesDAO.add(localizacion);
	}

	public void update(ParLocalizacion localizacion) throws ParCampoRequeridoException {
		checkRequiredFields(localizacion);
		localizacionesDAO.update(localizacion);
	}
	
	private void checkRequiredFields(ParLocalizacion localizacion) throws ParCampoRequeridoException {
		if (localizacion.getNombre() == null || localizacion.getNombre().isEmpty())
    		throw new ParCampoRequeridoException("Nombre");
	}
}
