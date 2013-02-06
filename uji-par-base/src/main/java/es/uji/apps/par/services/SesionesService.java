package es.uji.apps.par.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.CampoRequeridoException;
import es.uji.apps.par.DateUtils;
import es.uji.apps.par.FechasInvalidasException;
import es.uji.apps.par.dao.SesionesDAO;
import es.uji.apps.par.db.SesionDTO;
import es.uji.apps.par.model.Sesion;

@Service
public class SesionesService
{
    @Autowired
    private SesionesDAO sesionDAO;

    public List<Sesion> getSesiones(Integer eventoId)
    {
    	List<Sesion> listaSesiones = new ArrayList<Sesion>();
    	
    	for (SesionDTO sesionDB: sesionDAO.getSesiones(eventoId)) {
    		listaSesiones.add(new Sesion(sesionDB));
    	}
        return listaSesiones;
    }
    
    
    // Para el Ext que espera recibir segundos en vez de milisegundos
    public List<Sesion> getSesionesDateEnSegundos(Integer eventoId)
    {
        List<Sesion> sesiones = getSesiones(eventoId);
        
        for (Sesion sesion : sesiones)
        {
            sesion.setFechaCelebracionWithDate(new Date(sesion.getFechaCelebracion().getTime()/1000));
            sesion.setFechaInicioVentaOnlineWithDate(new Date(sesion.getFechaInicioVentaOnline().getTime()/1000));
            sesion.setFechaFinVentaOnlineWithDate(new Date(sesion.getFechaFinVentaOnline().getTime()/1000));
        }
        
        return sesiones;
    }

    public void removeSesion(Integer id)
    {
        sesionDAO.removeSesion(id);
    }

    public Sesion addSesion(long eventoId, Sesion sesion) throws CampoRequeridoException, FechasInvalidasException
    {
    	checkRequiredFields(sesion);
    	checkIfDatesAreValid(sesion);
        sesion.setFechaCelebracionWithDate(DateUtils.addStartEventTimeToDate(sesion.getFechaCelebracion(),
                sesion.getHoraCelebracion()));
        return sesionDAO.addSesion(eventoId, sesion);
    }

    private void checkIfDatesAreValid(Sesion sesion) throws FechasInvalidasException {
		if (sesion.getFechaFinVentaOnline().getTime() < sesion.getFechaInicioVentaOnline().getTime())
			throw new FechasInvalidasException(FechasInvalidasException.FECHA_INICIO_VENTA_POSTERIOR_FECHA_FIN_VENTA);
		if (sesion.getFechaFinVentaOnline().getTime() > sesion.getFechaCelebracion().getTime())
			throw new FechasInvalidasException(FechasInvalidasException.FECHA_FIN_VENTA_POSTERIOR_FECHA_CELEBRACION);
	}

	private void checkRequiredFields(Sesion sesion) throws CampoRequeridoException {
		if (sesion.getFechaCelebracion() == null)
			throw new CampoRequeridoException("Fecha de celebración");
		if (sesion.getHoraCelebracion() == null)
			throw new CampoRequeridoException("Hora de celebración");
		if (sesion.getFechaInicioVentaOnline() == null)
			throw new CampoRequeridoException("Fecha de inicio de la venta online");
		if (sesion.getFechaFinVentaOnline() == null)
			throw new CampoRequeridoException("Fecha de fin de la venta online");
	}

	public void updateSesion(long eventoId, Sesion sesion) throws CampoRequeridoException, FechasInvalidasException
    {
		checkRequiredFields(sesion);
		checkIfDatesAreValid(sesion);
        sesion.setFechaCelebracionWithDate(DateUtils.addStartEventTimeToDate(sesion.getFechaCelebracion(),
                sesion.getHoraCelebracion()));
        sesionDAO.updateSesion(eventoId, sesion);
    }
	
	public Sesion getSesion(long id)
	{
	    return new Sesion(sesionDAO.getSesion(id));
	}
}
