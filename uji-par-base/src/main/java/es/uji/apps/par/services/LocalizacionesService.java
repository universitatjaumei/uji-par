package es.uji.apps.par.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.CampoRequeridoException;
import es.uji.apps.par.dao.LocalizacionesDAO;
import es.uji.apps.par.db.LocalizacionDTO;
import es.uji.apps.par.model.Localizacion;

@Service
public class LocalizacionesService
{

    @Autowired
    private LocalizacionesDAO localizacionesDAO;

    public List<Localizacion> get(String sortParameter, int start, int limit)
    {
    	List<Localizacion> listaLocalizaciones = new ArrayList<Localizacion>();
    	
    	for(LocalizacionDTO localizacionDB: localizacionesDAO.get(sortParameter, start, limit))
    		listaLocalizaciones.add(new Localizacion(localizacionDB));
        
    	return listaLocalizaciones;
    }

    public void remove(Integer id)
    {
        localizacionesDAO.remove(id);
    }

    public Localizacion add(Localizacion localizacion) throws CampoRequeridoException
    {
        checkRequiredFields(localizacion);
        return localizacionesDAO.add(localizacion);
    }

    public void update(Localizacion localizacion) throws CampoRequeridoException
    {
        checkRequiredFields(localizacion);
        localizacionesDAO.update(localizacion);
    }

    private void checkRequiredFields(Localizacion localizacion) throws CampoRequeridoException
    {
        if (localizacion.getNombreEs() == null || localizacion.getNombreEs().isEmpty())
            throw new CampoRequeridoException("Nombre");
    }

	public int getTotalLocalizaciones() {
		return localizacionesDAO.getTotalLocalizaciones();
	}
}
