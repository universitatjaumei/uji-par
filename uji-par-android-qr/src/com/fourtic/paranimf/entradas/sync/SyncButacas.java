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
public class SyncButacas
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

    public void syncButacasFromRest(final int sesionId, final SyncCallback callback) throws SQLException
    {
        if (butacasModified(sesionId))
        {
            uploadButacas(sesionId, new SyncCallback()
            {
                @Override
                public void onSuccess()
                {
                    downloadButacas(sesionId, callback);
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
            downloadButacas(sesionId, callback);
        }
    }

    private void uploadButacas(int sesionId, final SyncCallback callback)
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
        
        updateFechaPresentadaEpoch(butacas);

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

    private void updateFechaPresentadaEpoch(List<Butaca> butacas)
    {
        for (Butaca butaca : butacas)
        {
            if (butaca.getFechaPresentada() != null)
                butaca.setFechaPresentadaEpoch(butaca.getFechaPresentada().getTime());
        }
    }

    private boolean butacasModified(int sesionId) throws SQLException
    {
        return butacaDao.butacasModified(sesionId);
    }

    private void downloadButacas(final int sesionId, final SyncCallback callback)
    {
        rest.getButacas(sesionId, new ResultCallback<List<Butaca>>()
        {
            @Override
            public void onSuccess(List<Butaca> butacas)
            {
                try
                {
                    syncButacasToDB(sesionId, butacas);

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

    private void syncButacasToDB(int sesionId, List<Butaca> butacas) throws SQLException
    {
        butacaDao.persist(sesionId, butacas);
    }
}
