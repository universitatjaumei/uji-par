package es.uji.apps.par.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.dao.ButacasDAO;
import es.uji.apps.par.db.ButacaDTO;

@Service
public class ButacasService
{
    @Autowired
    private ButacasDAO butacasDAO;

    public List<ButacaDTO> getButacas(long idSesion, String codigoLocalizacion)
    {
        return butacasDAO.getButacas(idSesion, codigoLocalizacion);
    }

    public boolean estaOcupada(int idSesion, String codigoLocalizacion, String fila, String numero)
    {
        return butacasDAO.estaOcupada(idSesion, codigoLocalizacion, fila, numero);
    }

}
