package es.uji.apps.par.ficheros.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.dao.CinesDAO;
import es.uji.apps.par.dao.ComprasDAO;
import es.uji.apps.par.dao.SalasDAO;
import es.uji.apps.par.db.CineDTO;
import es.uji.apps.par.ficheros.registros.RegistroBuzon;
import es.uji.apps.par.ficheros.registros.RegistroSala;
import es.uji.apps.par.model.Sala;
import es.uji.apps.par.model.Sesion;

@Service
public class FicherosService
{
    @Autowired
    private CinesDAO cinesDao;

    @Autowired
    private SalasDAO salasDAO;

    @Autowired
    private ComprasDAO comprasDAO;

    public RegistroBuzon generaRegistroBuzon(Date fechaEnvio, String tipo, List<Sesion> sesiones)
    {
        List<CineDTO> cines = cinesDao.getCines();
        CineDTO cine = cines.get(0);

        RegistroBuzon registroBuzon = new RegistroBuzon();

        registroBuzon.setCodigo(cine.getCodigo());
        registroBuzon.setFechaEnvio(fechaEnvio);
        registroBuzon.setTipo(tipo);
        registroBuzon.setSesiones(sesiones.size());
        registroBuzon.setRecaudacion(comprasDAO.getRecaudacionSesiones(sesiones));

        // int lineasRegistroSala = salasDAO.getSalas(sesiones).size(); 

        return registroBuzon;
    }

    public List<RegistroSala> generaRegistrosSala(List<Sesion> sesiones)
    {
        List<RegistroSala> registrosSala = new ArrayList<RegistroSala>();
        
        List<Sala> salas = salasDAO.getSalas(sesiones);
        
        for (Sala sala:salas)
        {
            RegistroSala registroSala = new RegistroSala();
            
            registroSala.setCodigo(sala.getCodigo());
            registroSala.setNombre(sala.getNombre());
            
            registrosSala.add(registroSala);
        }

        return registrosSala;
    }
}
