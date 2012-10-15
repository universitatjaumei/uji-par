package es.uji.apps.par.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.dao.SesionesDAO;
import es.uji.apps.par.model.ParSesion;

@Service
public class SesionesService
{
    @Autowired
    private SesionesDAO sesionDAO;
    
    public List<ParSesion> getSesiones(Integer eventoId)
    {
        return sesionDAO.getSesiones(eventoId);
    }

    public void removeSesion(Integer id)
    {
        sesionDAO.removeSesion(id);
    }

    public ParSesion addSesion(long eventoId, ParSesion sesion)
    {
        return sesionDAO.addSesion(eventoId, sesion);
    }

    public void updateSesion(long eventoId, ParSesion sesion)
    {
        sesionDAO.updateSesion(eventoId, sesion);
    }
}
