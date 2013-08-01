package es.uji.apps.par.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.dao.ButacasDAO;
import es.uji.apps.par.dao.LocalizacionesDAO;
import es.uji.apps.par.db.ButacaDTO;
import es.uji.apps.par.db.LocalizacionDTO;
import es.uji.apps.par.model.Butaca;
import es.uji.apps.par.model.DisponiblesLocalizacion;

@Service
public class ButacasService
{
    @Autowired
    private ButacasDAO butacasDAO;
    
    @Autowired
    private LocalizacionesDAO localizacionesDAO;

    public List<ButacaDTO> getButacas(long idSesion, String codigoLocalizacion)
    {
        return butacasDAO.getButacas(idSesion, codigoLocalizacion);
    }

    public boolean estaOcupada(long idSesion, String codigoLocalizacion, String fila, String numero)
    {
        return butacasDAO.estaOcupada(idSesion, codigoLocalizacion, fila, numero);
    }

    public List<Butaca> estanOcupadas(long sesionId, List<Butaca> butacas)
    {
        List<Butaca> ocupadas = new ArrayList<Butaca>();

        for (Butaca butaca : butacas)
        {
            if (estaOcupada(sesionId, butaca.getLocalizacion(), butaca.getFila(), butaca.getNumero()))
                ocupadas.add(butaca);
        }

        return ocupadas;
    }
    
    public List<DisponiblesLocalizacion> getDisponiblesNoNumerada(long idSesion)
    {
        List<DisponiblesLocalizacion> disponibles = new ArrayList<DisponiblesLocalizacion>();
        List<LocalizacionDTO> localizaciones = localizacionesDAO.get();
        
        for (LocalizacionDTO localizacion: localizaciones)
        {
            DisponiblesLocalizacion disponible = new DisponiblesLocalizacion(localizacion.getCodigo(), 
                    localizacion.getTotalEntradas().intValue() - butacasDAO.getOcupadas(idSesion, localizacion.getCodigo()));
            
            disponibles.add(disponible);
        }
            
        return disponibles;
    }
}
