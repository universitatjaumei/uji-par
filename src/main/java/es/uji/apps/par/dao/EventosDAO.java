package es.uji.apps.par.dao;

import java.util.List;

import es.uji.apps.par.model.ParEvento;

public interface EventosDAO
{
    List<ParEvento> getEventos();

    void removeEvento(long id);

    ParEvento addEvento(ParEvento evento);

    void updateEvento(ParEvento evento);
}