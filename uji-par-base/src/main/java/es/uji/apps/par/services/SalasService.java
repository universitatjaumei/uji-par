package es.uji.apps.par.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.dao.SalasDAO;
import es.uji.apps.par.db.SalaDTO;
import es.uji.apps.par.model.Sala;

@Service
public class SalasService
{
    @Autowired
    private SalasDAO salasDAO;
    
    public List<Sala> getSalas()
    {
        List<SalaDTO> salasDTO = salasDAO.getSalas();
        
        return Sala.salasDTOtoSalas(salasDTO);
    }
}
