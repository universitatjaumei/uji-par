package com.fourtic.paranimf.entradas.sync;

import java.sql.SQLException;
import java.util.List;

import android.util.Log;

import com.fourtic.paranimf.entradas.constants.Constants;
import com.fourtic.paranimf.entradas.data.Butaca;
import com.fourtic.paranimf.entradas.db.ButacaDao;
import com.fourtic.paranimf.entradas.rest.RestService;
import com.fourtic.paranimf.entradas.rest.RestService.ResultCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SincronizadorButacas
{
    @Inject
    private RestService rest;

    @Inject
    private ButacaDao butacaDao;

    public interface SyncCallback
    {
        public void onSuccess();

        public void onError(Throwable e, String errorMessage);
    }

    public void sincronizaButacasDesdeRest(final int sesionId, final SyncCallback callback) throws SQLException
    {
        if (butacaDao.hayButacasModificadas(sesionId))
        {
            subeButacas(sesionId, new SyncCallback()
            {
                @Override
                public void onSuccess()
                {
                    descargaButacas(sesionId, callback);
                }

                @Override
                public void onError(Throwable e, String errorMessage)
                {
                    callback.onError(e, errorMessage);
                }
            });
        }
        else
        {
            descargaButacas(sesionId, callback);
        }
    }

    private void subeButacas(int sesionId, final SyncCallback callback)
    {
        List<Butaca> butacas = null;

        try
        {
            butacas = butacaDao.getButacasModificadas(sesionId);
        }
        catch (SQLException e)
        {
            callback.onError(e, "Error consultando butacas de móvil");
        }

        actualizaFechaPresentadaEpoch(butacas);

        rest.updatePresentadas(sesionId, butacas, new ResultCallback<Void>()
        {
            @Override
            public void onSuccess(Void successData)
            {
                callback.onSuccess();
            }

            @Override
            public void onError(Throwable throwable, String errorMessage)
            {
                callback.onError(throwable, errorMessage);
            }
        });
    }

    private void actualizaFechaPresentadaEpoch(List<Butaca> butacas)
    {
        for (Butaca butaca : butacas)
        {
            if (butaca.getFechaPresentada() != null)
                butaca.setFechaPresentadaEpoch(butaca.getFechaPresentada().getTime());
        }
    }

    private void descargaButacas(final int sesionId, final SyncCallback callback)
    {
        rest.getButacas(sesionId, new ResultCallback<List<Butaca>>()
        {
            @Override
            public void onSuccess(List<Butaca> butacas)
            {
                try
                {
                    butacaDao.actualizaButacas(sesionId, butacas);

                    callback.onSuccess();
                }
                catch (SQLException e)
                {
                    String errorMessage = "Error actualizando butacas desde REST";

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

}
