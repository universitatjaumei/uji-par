package es.uji.apps.par.dao;

import java.util.List;

import es.uji.apps.par.model.ParSesion;

public interface SesionesDAO
{
    List<ParSesion> getSesiones(long eventoId);

    void removeSesion(long id);

    ParSesion addSesion(long id, ParSesion sesion);

    void updateSesion(long eventoId, ParSesion sesion);
}