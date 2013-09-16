package es.uji.apps.par.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.CampoRequeridoException;
import es.uji.apps.par.FechasInvalidasException;
import es.uji.apps.par.dao.EventosDAO;
import es.uji.apps.par.dao.LocalizacionesDAO;
import es.uji.apps.par.dao.SesionesDAO;
import es.uji.apps.par.db.PreciosSesionDTO;
import es.uji.apps.par.db.SesionDTO;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.Localizacion;
import es.uji.apps.par.model.PreciosPlantilla;
import es.uji.apps.par.model.PreciosSesion;
import es.uji.apps.par.model.Sesion;
import es.uji.apps.par.utils.DateUtils;

@Service
public class SesionesService
{
    @Autowired
    private SesionesDAO sesionDAO;
    
    @Autowired
    private EventosDAO eventosDAO;
    
    @Autowired
    private LocalizacionesDAO localizacionesDAO;
    
    @Autowired
    private PreciosPlantillaService preciosPlantillaService;
    
    public List<Sesion> getSesiones(Integer eventoId, String sortParameter, int start, int limit)
    {
    	return getSesiones(eventoId, false, sortParameter, start, limit);
    }
    
    public List<Sesion> getSesiones(Integer eventoId) {
		return getSesiones(eventoId, false, "", 0, 100);
	}
    
    public List<Sesion> getSesionesActivas(Integer eventoId, String sortParameter, int start, int limit)
    {
        return getSesiones(eventoId, true, sortParameter, start, limit);
    }
    
    private List<Sesion> getSesiones(Integer eventoId, boolean activos, String sortParameter, int start, int limit)
    {
        List<Sesion> listaSesiones = new ArrayList<Sesion>();
        
        List<SesionDTO> sesiones;
        
        if (activos)
            sesiones = sesionDAO.getSesionesActivas(eventoId, sortParameter, start, limit);
        else
            sesiones = sesionDAO.getSesiones(eventoId, sortParameter, start, limit);
        
        for (SesionDTO sesionDB: sesiones) {
            listaSesiones.add(new Sesion(sesionDB));
        }
        return listaSesiones;
    }
    
    // Para el Ext que espera recibir segundos en vez de milisegundos
    public List<Sesion> getSesionesDateEnSegundos(Integer eventoId, String sortParameter, int start, int limit)
    {
      return getSesionesDateEnSegundos(eventoId, false, sortParameter, start, limit);
    }
    
    // Para el Ext que espera recibir segundos en vez de milisegundos
    public List<Sesion> getSesionesActivasDateEnSegundos(Integer eventoId, String sortParameter, int start, int limit)
    {
    	return getSesionesDateEnSegundos(eventoId, true, sortParameter, start, limit);
    }    
    
    public List<Sesion> getSesionesDateEnSegundos(Integer eventoId, boolean activos, String sortParameter, int start, int limit)
    {
        List<Sesion> sesiones;
        
        if (activos)
            sesiones = getSesionesActivas(eventoId, sortParameter, start, limit);
        else
            sesiones = getSesiones(eventoId, sortParameter, start, limit);
        
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
    	checkSesionAndSetTimesToDates(sesion);

    	sesion.setEvento(Evento.eventoDTOtoEvento(eventosDAO.getEventoById(eventoId)));
    	List<PreciosSesion> listaPreciosSesion = new ArrayList<PreciosSesion>();
    	if (sesion.getPreciosSesion() !=  null) {
        	for (PreciosSesion preciosSesion: sesion.getPreciosSesion()) {
        		preciosSesion.setLocalizacion(Localizacion.localizacionDTOtoLocalizacion(localizacionesDAO.getLocalizacionById(preciosSesion.getLocalizacion().getId())));
        		preciosSesion.setSesion(sesion);
        		listaPreciosSesion.add(preciosSesion);
        	}
        	sesion.setPreciosSesion(listaPreciosSesion);
        }
    	
    	SesionDTO sesionDTO = sesionDAO.persistSesion(Sesion.SesionToSesionDTO(sesion));
		
    	sesion.setId(sesionDTO.getId());
        return sesion;
    }

	private void addPreciosSesion(SesionDTO sesionDTO) {
		if (sesionDTO.getParPreciosSesions() != null) {
			for (PreciosSesionDTO precioSesionDTO : sesionDTO.getParPreciosSesions()) {
    			precioSesionDTO.setParSesione(sesionDTO);
    			sesionDAO.addPrecioSesion(precioSesionDTO);
    		}
    	}
	}
    
    private Evento createParEventoWithId(long eventoId)
    {
        Evento evento = new Evento();
        evento.setId(eventoId);
        return evento;
    }

	private void checkSesionAndSetTimesToDates(Sesion sesion) throws CampoRequeridoException, FechasInvalidasException {
		checkRequiredFields(sesion);
        
    	sesion.setFechaCelebracionWithDate(DateUtils.addTimeToDate(sesion.getFechaCelebracion(),
                sesion.getHoraCelebracion()));
        sesion.setFechaInicioVentaOnlineWithDate(DateUtils.addTimeToDate(sesion.getFechaInicioVentaOnline(), 
        		sesion.getHoraInicioVentaOnline()));
        sesion.setFechaFinVentaOnlineWithDate(DateUtils.addTimeToDate(sesion.getFechaFinVentaOnline(), 
        		sesion.getHoraFinVentaOnline()));
        
        checkIfDatesAreValid(sesion);
	}
    
	@Transactional
    public void updateSesion(long eventoId, Sesion sesion) throws CampoRequeridoException, FechasInvalidasException
    {
		checkSesionAndSetTimesToDates(sesion);
		sesion.setEvento(createParEventoWithId(eventoId));
        
        sesionDAO.updateSesion(sesion);
        sesionDAO.deleteExistingPreciosSesion(sesion.getId());
        addPreciosSesion(Sesion.SesionToSesionDTO(sesion));
    }

    private void checkIfDatesAreValid(Sesion sesion) throws FechasInvalidasException {
		if (DateUtils.millisecondsToSeconds(sesion.getFechaFinVentaOnline().getTime()) < 
				DateUtils.millisecondsToSeconds(sesion.getFechaInicioVentaOnline().getTime()))
			throw new FechasInvalidasException(FechasInvalidasException.FECHA_INICIO_VENTA_POSTERIOR_FECHA_FIN_VENTA, 
			        sesion.getFechaInicioVentaOnline(), sesion.getFechaFinVentaOnline());
		
		if (DateUtils.millisecondsToSeconds(sesion.getFechaFinVentaOnline().getTime()) > 
				DateUtils.millisecondsToSeconds(sesion.getFechaCelebracion().getTime()))
			throw new FechasInvalidasException(FechasInvalidasException.FECHA_FIN_VENTA_POSTERIOR_FECHA_CELEBRACION,
			        sesion.getFechaCelebracion(), sesion.getFechaFinVentaOnline());
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
		if (sesion.getHoraInicioVentaOnline() == null)
			throw new CampoRequeridoException("Hora de inicio de la venta online");
		if (sesion.getHoraFinVentaOnline() == null)
			throw new CampoRequeridoException("Hora de fin de la venta online");
	}

	public List<PreciosSesion> getPreciosSesion(Long sesionId, String sortParameter, int start, int limit) {
		List<PreciosSesion> listaPreciosSesion = new ArrayList<PreciosSesion>();
    	
		Sesion sesion = getSesion(sesionId);
		
		if (sesion.getPlantillaPrecios().getId() == -1)
		{
        	for (PreciosSesionDTO precioSesionDB: sesionDAO.getPreciosSesion(sesionId, sortParameter, start, limit))
        		listaPreciosSesion.add(new PreciosSesion(precioSesionDB));
		}
		else
		{
		    List<PreciosPlantilla> preciosPlantilla = preciosPlantillaService.getPreciosOfPlantilla(sesion.getPlantillaPrecios().getId(), sortParameter, start, limit);
            
            for(PreciosPlantilla precioPlantilla: preciosPlantilla)
                listaPreciosSesion.add(new PreciosSesion(precioPlantilla));
		}
		
        return listaPreciosSesion;
	}
	
	public List<PreciosSesion> getPreciosSesion(Long sesionId) {
		return getPreciosSesion(sesionId, "", 0, 100);
	}
	
	public Map<String, PreciosSesion> getPreciosSesionPorLocalizacion(Long sesionId)
	{
	    Map<String, PreciosSesion> resultado = new HashMap<String, PreciosSesion>();
	    
	    for (PreciosSesion precio: getPreciosSesion(sesionId))
	        resultado.put(precio.getLocalizacion().getCodigo(), precio);
	    
        return resultado;
	}
	
	public Sesion getSesion(long id)
	{
	    return new Sesion(sesionDAO.getSesion(id));
	}

	public int getTotalSesionesActivas(Integer eventoId) {
		return sesionDAO.getTotalSesionesActivas(eventoId);
	}

	public int getTotalSesiones(Integer eventoId) {
		return sesionDAO.getTotalSesiones(eventoId);
	}

	public int getTotalPreciosSesion(Long sesionId) {
		return sesionDAO.getTotalPreciosSesion(sesionId);
	}
}
