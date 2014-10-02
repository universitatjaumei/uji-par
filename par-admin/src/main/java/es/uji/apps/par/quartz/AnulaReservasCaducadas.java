package es.uji.apps.par.quartz;

import es.uji.apps.par.IncidenciaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.services.ComprasService;

@Service
public class AnulaReservasCaducadas
{
    @Autowired
    ComprasService comprasService;

    public void ejecuta() throws IncidenciaNotFoundException {
        comprasService.anulaReservasCaducadas();
    }
}
