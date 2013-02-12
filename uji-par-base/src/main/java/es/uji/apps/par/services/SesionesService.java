package es.uji.apps.par.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.CampoRequeridoException;
import es.uji.apps.par.DateUtils;
import es.uji.apps.par.FechasInvalidasException;
import es.uji.apps.par.dao.EventosDAO;
import es.uji.apps.par.dao.LocalizacionesDAO;
import es.uji.apps.par.dao.SesionesDAO;
import es.uji.apps.par.db.LocalizacionDTO;
import es.uji.apps.par.db.PreciosSesionDTO;
import es.uji.apps.par.db.SesionDTO;
import es.uji.apps.par.model.Evento;
import es.uji.apps.par.model.Localizacion;
import es.uji.apps.par.model.PreciosSesion;
import es.uji.apps.par.model.Sesion;

@Service
public class SesionesService
{
    @Autowired
    private SesionesDAO sesionDAO;
    
    @Autowired
    private EventosDAO eventosDAO;
    
    @Autowired
    private LocalizacionesDAO localizacionesDAO;
    
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
    	checkSesionAndSetTimesToDates(sesion);

    	
    	sesion.setEvento(Evento.eventoDTOtoEvento(eventosDAO.getEventoById(eventoId)));
    	/*sesion.setPlantillaPrecios(Plantilla.plantillaPreciosDTOtoPlantillaPrecios(
    			plantillasDAO.getPlantillaById(sesion.getPlantillaPrecios().getId())));*/
    	//sesion.setEvento(createParEventoWithId(eventoId));
    	
    	List<PreciosSesion> listaPreciosSesion = new ArrayList<PreciosSesion>();
    	if (sesion.getPreciosSesion() !=  null) {
        	for (PreciosSesion preciosSesion: sesion.getPreciosSesion()) {
        		preciosSesion.setLocalizacion(Localizacion.localizacionDTOtoLocalizacion(localizacionesDAO.getLocalizacionById(preciosSesion.getLocalizacion().getId())));
        		preciosSesion.setSesion(sesion);
        		listaPreciosSesion.add(preciosSesion);
        		//PreciosSesionDTO preciosSesionDTO = PreciosSesion.precioSesionToPrecioSesionDTO(preciosSesion);
        		//sesionDAO.persistPreciosSesion(preciosSesionDTO);
        	}
        	sesion.setPreciosSesion(listaPreciosSesion);
        }
    	
    	
    	SesionDTO sesionDTO = sesionDAO.persistSesion(Sesion.SesionToSesionDTO(sesion));
    	sesion.setId(sesionDTO.getId());
    	/*sesionDTO = sesionDAO.persistSesion(sesionDTO);
    	
    	
    	//setLocalizacionesToPreciosSesion(sesion.getPreciosSesion());
        */
    	//sesion = sesionDAO.testMethod(sesionDTO, sesion);
        return sesion;
    }

	private void setLocalizacionesToPreciosSesion(List<PreciosSesion> listaPreciosSesion) {
		if (listaPreciosSesion != null) {
			for (PreciosSesion preciosSesion: listaPreciosSesion) {
				LocalizacionDTO localizacionDTO = localizacionesDAO.getLocalizacionById(preciosSesion.getLocalizacion().getId());
				preciosSesion.setLocalizacion(Localizacion.localizacionDTOtoLocalizacion(localizacionDTO));
			}
		}
	}

	private void addPreciosSesion(SesionDTO sesionDTO) {
    	//List<PreciosSesionDTO> listaPreciosSesionDTO = PreciosSesion.listaPreciosSesionToListaPreciosSesionDTO(sesionDTO.getPreciosSesion());
		if (sesionDTO.getParPreciosSesions() != null) {
    		//for (PreciosSesionDTO precioSesionDTO : listaPreciosSesionDTO) {
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

	public List<PreciosSesion> getPreciosSesion(Integer sesionId) {
		List<PreciosSesion> listaPreciosSesion = new ArrayList<PreciosSesion>();
    	
    	for (PreciosSesionDTO precioSesionDB: sesionDAO.getPreciosSesion(sesionId)) {
    		listaPreciosSesion.add(new PreciosSesion(precioSesionDB));
    	}
        return listaPreciosSesion;
	}
	
	public Sesion getSesion(long id)
	{
	    return new Sesion(sesionDAO.getSesion(id));
	}
}
