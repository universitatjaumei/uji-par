package es.uji.apps.par.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.CampoRequeridoException;
import es.uji.apps.par.dao.LocalizacionesDAO;
import es.uji.apps.par.model.Localizacion;

@Service
public class LocalizacionesService
{

    @Autowired
    private LocalizacionesDAO localizacionesDAO;

    public List<Localizacion> get()
    {
        return localizacionesDAO.get();
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
}
