package es.uji.apps.par.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPAUpdateClause;

import es.uji.apps.par.db.EnvioDTO;
import es.uji.apps.par.db.EnviosSesionDTO;
import es.uji.apps.par.db.QEnvioDTO;
import es.uji.apps.par.db.SesionDTO;

@Repository
public class ComunicacionesICAADAO extends BaseDAO
{
	@Transactional
	public void addNewEnvio(List<Integer> ids, Date fechaEnvio, String tipoEnvio) {
		EnvioDTO envioDTO = addNewFicheroGenerado(fechaEnvio);
		addSesionesEnvio(ids, envioDTO, tipoEnvio);
	}

	private void addSesionesEnvio(List<Integer> ids, EnvioDTO envioDTO, String tipoEnvio) {
		for (Integer id: ids) {
			EnviosSesionDTO envioSesionDTO = new EnviosSesionDTO();
			envioSesionDTO.setParEnvio(envioDTO);
			envioSesionDTO.setParSesion(new SesionDTO(id));
			envioSesionDTO.setTipoEnvio(tipoEnvio);
			entityManager.persist(envioSesionDTO);
		}
	}

	private EnvioDTO addNewFicheroGenerado(Date fechaEnvio) {
		EnvioDTO envioDTO = new EnvioDTO();
		envioDTO.setFechaGeneracionFichero(new Timestamp(fechaEnvio.getTime()));
		
		entityManager.persist(envioDTO);
		return envioDTO;
	}

	@Transactional
	public void marcaEnviosComoEnviados(List<Long> ids, Date fechaEnvio) {
		QEnvioDTO qEnvioDTO = QEnvioDTO.envioDTO;
		JPAUpdateClause updateClause = new JPAUpdateClause(entityManager, qEnvioDTO);
		updateClause.set(qEnvioDTO.fechaEnvioFichero, new Timestamp(fechaEnvio.getTime())).
		where(qEnvioDTO.id.in(ids)).execute();
	}
}
