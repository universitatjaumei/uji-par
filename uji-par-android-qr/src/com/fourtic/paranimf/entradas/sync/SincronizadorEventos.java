package com.fourtic.paranimf.entradas.sync;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.fourtic.paranimf.entradas.R;
import com.fourtic.paranimf.entradas.data.Evento;
import com.fourtic.paranimf.entradas.db.EventoDao;
import com.fourtic.paranimf.entradas.rest.RestService;
import com.fourtic.paranimf.entradas.rest.RestService.ResultCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SincronizadorEventos
{
    @Inject
    private RestService rest;

    @Inject
    Context context;

    @Inject
    private EventoDao eventoDao;

    public interface SyncCallback
    {
        public void onSuccess();

        public void onError(Throwable e, String errorMessage);
    }

    public void actualizaEventosDesdeRest(final SyncCallback callback)
    {
        rest.getEventos(new ResultCallback<List<Evento>>()
        {
            @Override
            public void onSuccess(List<Evento> eventos)
            {
                try
                {
                    eventoDao.actualizaEventos(eventos);
                    callback.onSuccess();
                }
                catch (SQLException e)
                {
                    callback.onError(e, context.getString(R.string.error_insertando_eventos_bd));
                }
            }

            @Override
            public void onError(Throwable e, String errorMessage)
            {
                callback.onError(e, context.getString(R.string.error_actualizando_eventos_rest));
            }
        });
    }
}
