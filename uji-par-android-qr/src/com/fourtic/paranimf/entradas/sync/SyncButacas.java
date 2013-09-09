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

    public boolean needsSync(int sesionId) throws SQLException
    {
        return butacaDao.butacasModified(sesionId);
    }

    public void syncButacasFromRest(final int sesionId, final SyncCallback callback)
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
