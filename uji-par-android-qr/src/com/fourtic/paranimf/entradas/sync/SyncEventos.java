package com.fourtic.paranimf.entradas.sync;

import java.sql.SQLException;
import java.util.List;

import android.util.Log;

import com.fourtic.paranimf.entradas.constants.Constants;
import com.fourtic.paranimf.entradas.data.Evento;
import com.fourtic.paranimf.entradas.data.Sesion;
import com.fourtic.paranimf.entradas.db.EventoDao;
import com.fourtic.paranimf.entradas.rest.RestService;
import com.fourtic.paranimf.entradas.rest.RestService.ResultCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SyncEventos
{
    @Inject
    private RestService rest;

    @Inject
    private EventoDao eventoDao;

    public interface SyncCallback
    {
        public void onSuccess();

        public void onError(Throwable e, String errorMessage);
    }

    public void loadEventosFromRest(final SyncCallback callback)
    {
        rest.getEventos(new ResultCallback<List<Evento>>()
        {
            @Override
            public void onSuccess(List<Evento> eventos)
            {
                try
                {
                    setSesionEventoId(eventos);
                    
                    syncEventosToDB(eventos);
                    callback.onSuccess();
                }
                catch (SQLException e)
                {
                    Log.e(Constants.TAG, "Error insertando eventos en BD", e);
                }
            }

            @Override
            public void onError(Throwable e, String errorMessage)
            {
                callback.onError(e, errorMessage);
            }
        });
    }

    protected void setSesionEventoId(List<Evento> eventos)
    {
        for (Evento evento : eventos)
        {
            for (Sesion sesion:evento.getSesiones())
            {
                sesion.setEvento(evento);
            }
        }
    }

    private void syncEventosToDB(List<Evento> eventos) throws SQLException
    {
        eventoDao.persist(eventos);
    }
}
