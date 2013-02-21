package es.uji.apps.par.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.dao.ButacasDAO;
import es.uji.apps.par.db.ButacaDTO;
import es.uji.apps.par.model.Butaca;

@Service
public class ButacasService
{
    @Autowired
    private ButacasDAO butacasDAO;

    public List<ButacaDTO> getButacas(long idSesion, String codigoLocalizacion)
    {
        return butacasDAO.getButacas(idSesion, codigoLocalizacion);
    }

    public boolean estaOcupada(long idSesion, String codigoLocalizacion, String fila, String numero)
    {
        return butacasDAO.estaOcupada(idSesion, codigoLocalizacion, fila, numero);
    }

    public List<Butaca> estanOcupadas(long sesionId, List<Butaca> butacas)
    {
        List<Butaca> ocupadas = new ArrayList<Butaca>();

        for (Butaca butaca : butacas)
        {
            if (estaOcupada(sesionId, butaca.getLocalizacion(), butaca.getFila(), butaca.getNumero()))
                ocupadas.add(butaca);
        }

        return ocupadas;
    }

}
