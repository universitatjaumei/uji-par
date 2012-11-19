package es.uji.apps.par.services;

import java.util.Calendar;
import java.util.Date;
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
    	sesion.setFechaCelebracionWithDate(addStartEventTimeToDate(sesion.getFechaCelebracion(), sesion.getHoraCelebracion()));
        return sesionDAO.addSesion(eventoId, sesion);
    }

    public void updateSesion(long eventoId, ParSesion sesion)
    {
    	sesion.setFechaCelebracionWithDate(addStartEventTimeToDate(sesion.getFechaCelebracion(), sesion.getHoraCelebracion()));
        sesionDAO.updateSesion(eventoId, sesion);
    }
    
    private Date addStartEventTimeToDate(Date startDate, String hour) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(startDate);
    	String[] arrHoraMinutos = hour.split(":");
    	
    	int hora = Integer.parseInt(arrHoraMinutos[0]);
    	int minutos = Integer.parseInt(arrHoraMinutos[1]);
    	
		cal.set(Calendar.HOUR_OF_DAY, hora);
    	cal.set(Calendar.MINUTE, minutos);
    	
    	return cal.getTime();
    }
}
