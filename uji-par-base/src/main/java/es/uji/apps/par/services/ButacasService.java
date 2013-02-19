package es.uji.apps.par.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.par.dao.ButacasDAO;

@Service
public class ButacasService
{
    @Autowired
    private ButacasDAO butacasDAO;

    public boolean estaOcupada(int idSesion, String codigoLocalizacion, String fila, String numero)
    {
        return butacasDAO.estaOcupada(idSesion, codigoLocalizacion, fila, numero);
    }

}
