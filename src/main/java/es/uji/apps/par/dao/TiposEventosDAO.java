package es.uji.apps.par.dao;

import java.util.List;

import es.uji.apps.par.model.ParTipoEvento;

public interface TiposEventosDAO
{
    List<ParTipoEvento> getTiposEventos();

    void removeTipoEvento(long id);

    ParTipoEvento addTipoEvento(ParTipoEvento tipoEvento);

    void updateTipoEvento(ParTipoEvento tipoEvento);
}