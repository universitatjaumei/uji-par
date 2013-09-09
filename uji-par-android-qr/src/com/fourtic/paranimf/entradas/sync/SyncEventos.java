package com.fourtic.paranimf.entradas.sync;

import java.sql.SQLException;
import java.util.List;

import android.util.Log;

import com.fourtic.paranimf.entradas.constants.Constants;
import com.fourtic.paranimf.entradas.data.Evento;
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
                    syncEventosToDB(eventos);
                    callback.onSuccess();
                }
                catch (SQLException e)
                {
                    String errorMessage = "Error insertando eventos en BD";
                    
                    Log.e(Constants.TAG, errorMessage, e);
                    callback.onError(e, errorMessage);
                }
            }

            @Override
            public void onError(Throwable e, String errorMessage)
            {
                callback.onError(e, errorMessage);
            }
        });
    }

    private void syncEventosToDB(List<Evento> eventos) throws SQLException
    {
        eventoDao.persist(eventos);
    }
}
