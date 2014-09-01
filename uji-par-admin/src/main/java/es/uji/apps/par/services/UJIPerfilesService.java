package es.uji.apps.par.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.commons.sso.dao.ApaDAO;

@Service
public class UJIPerfilesService {
	private ApaDAO apaDAO;
	private final String CODIGO_APLICACION = "PAR";
	
	@Autowired
    public void setApaDAO(ApaDAO apaDAO)
    {
        this.apaDAO = apaDAO;
    }

	public boolean hasPerfil(String perfil, Long personaId) {
		return apaDAO.hasPerfil(CODIGO_APLICACION, perfil, personaId);
	}
	
	
}
