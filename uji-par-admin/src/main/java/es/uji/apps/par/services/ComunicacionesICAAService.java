package es.uji.apps.par.services;

import java.io.IOException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.CampoRequeridoException;
import es.uji.apps.par.IncidenciaNotFoundException;
import es.uji.apps.par.RegistroSerializaException;
import es.uji.apps.par.TipoEnvioInvalidoException;
import es.uji.apps.par.dao.ComunicacionesICAADAO;
import es.uji.apps.par.ficheros.registros.FicheroRegistros;
import es.uji.apps.par.ficheros.service.FicherosService;
import es.uji.apps.par.model.Sesion;
import es.uji.apps.par.utils.DateUtils;

@Service
public class ComunicacionesICAAService {
	
	@Autowired
    FicherosService ficherosService;
	
	@Autowired
	ComunicacionesICAADAO comunicacionesICAADAO;

	public byte[] generaFicheroICAA(List<Integer> ids, String fechaEnvioHabitualAnterior, String tipoEnvio) 
			throws TipoEnvioInvalidoException, CampoRequeridoException, RegistroSerializaException, 
			IncidenciaNotFoundException, NoSuchProviderException, IOException, InterruptedException {
		checkTipoEnvio(tipoEnvio);
		checkIds(ids);
		Date fechaEnvioAnterior = getDateEnvio(fechaEnvioHabitualAnterior);
		List<Sesion> sesiones = getSesionesFromIDs(ids);
		FicheroRegistros ficheroRegistros = ficherosService.generaFicheroRegistros(fechaEnvioAnterior, tipoEnvio, sesiones);
		byte[] contenido = ficheroRegistros.toByteArray();
		byte[] contenidoCifrado = ficherosService.encryptData(contenido);
		
		comunicacionesICAADAO.addNewEnvio(ids, ficheroRegistros.getRegistroBuzon().getFechaEnvio(), tipoEnvio);
		
		return contenidoCifrado;
	}

	private List<Sesion> getSesionesFromIDs(List<Integer> ids) {
		List<Sesion> sesiones = new ArrayList<Sesion>();
		for (Integer id: ids) {
			sesiones.add(new Sesion(id));
		}
		return sesiones;
	}

	private Date getDateEnvio(String fechaEnvioHabitualAnterior) {
		return DateUtils.spanishStringToDate(fechaEnvioHabitualAnterior);
	}

	private void checkIds(List<Integer> ids) throws CampoRequeridoException {
		if (ids == null || ids.size() == 0)
			throw new CampoRequeridoException("identificadores de sesión");
	}

	private void checkTipoEnvio(String tipoEnvio) throws TipoEnvioInvalidoException {
		if (tipoEnvio == null || (!tipoEnvio.equals("FL") && !tipoEnvio.equals("AT")))
			throw new TipoEnvioInvalidoException();
	}

	public void marcaEnviosComoEnviados(List<Long> ids) {
		Calendar cal = Calendar.getInstance();
		Date fechaEnvio = cal.getTime();
		comunicacionesICAADAO.marcaEnviosComoEnviados(ids, fechaEnvio);
	}

}
