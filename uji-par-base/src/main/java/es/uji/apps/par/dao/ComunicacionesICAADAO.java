package es.uji.apps.par.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.par.db.EnvioDTO;
import es.uji.apps.par.db.EnviosSesionDTO;
import es.uji.apps.par.db.SesionDTO;

@Repository
public class ComunicacionesICAADAO extends BaseDAO
{
	@Transactional
	public void addNewEnvio(List<Integer> ids, Date fechaEnvio, String tipoEnvio) {
		EnvioDTO envioDTO = addNewFicheroGenerado(fechaEnvio, tipoEnvio);
		addSesionesEnvio(ids, envioDTO);
	}

	private void addSesionesEnvio(List<Integer> ids, EnvioDTO envioDTO) {
		for (Integer id: ids) {
			EnviosSesionDTO envioSesionDTO = new EnviosSesionDTO();
			envioSesionDTO.setParEnvio(envioDTO);
			envioSesionDTO.setParSesion(new SesionDTO(id));
			entityManager.persist(envioSesionDTO);
		}
	}

	private EnvioDTO addNewFicheroGenerado(Date fechaEnvio, String tipoEnvio) {
		EnvioDTO envioDTO = new EnvioDTO();
		envioDTO.setFechaGeneracionFichero(new Timestamp(fechaEnvio.getTime()));
		envioDTO.setTipoEnvio(tipoEnvio);
		
		entityManager.persist(envioDTO);
		return envioDTO;
	}
}
