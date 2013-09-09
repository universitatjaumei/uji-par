package com.fourtic.paranimf.entradas.db;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import android.util.Log;

import com.fourtic.paranimf.entradas.constants.Constants;
import com.fourtic.paranimf.entradas.data.Butaca;
import com.fourtic.paranimf.entradas.data.Sesion;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

@Singleton
public class ButacaDao
{
    DbHelperService dbHelper;

    private Dao<Butaca, Integer> dao;

    @Inject
    private SesionDao sesionDao;

    @Inject
    public ButacaDao(DbHelperService dbHelper)
    {
        this.dbHelper = dbHelper;

        try
        {
            dao = dbHelper.getHelper().getButacaDao();
        }
        catch (SQLException e)
        {
            Log.e(Constants.TAG, "Error iniciando dao Butaca", e);
        }
    }

    public List<Butaca> getButacas(int sesionId) throws SQLException
    {
        return dao.queryForEq("sesion_id", sesionId);
    }

    public boolean butacasModified(int sesionId) throws SQLException
    {
        QueryBuilder<Butaca, Integer> builder = dao.queryBuilder();

        long numModified = builder.where().eq("sesion_id", sesionId).and().eq("modificada", true).countOf();

        return numModified > 0;
    }

    public long getButacasCount(int sesionId) throws SQLException
    {
        QueryBuilder<Butaca, Integer> builder = dao.queryBuilder();

        return builder.where().eq("sesion_id", sesionId).countOf();
    }

    public long getButacasPresentadasCount(int sesionId) throws SQLException
    {
        QueryBuilder<Butaca, Integer> builder = dao.queryBuilder();

        return builder.where().eq("sesion_id", sesionId).and().isNotNull("presentada").countOf();
    }

    public void persist(final int sesionId, final List<Butaca> butacas) throws SQLException
    {
        TransactionManager.callInTransaction(dao.getConnectionSource(), new Callable<Void>()
        {
            public Void call() throws Exception
            {
                updateButacas(sesionId, butacas);
                sesionDao.updateFechaSync(sesionId, new Date());

                return null;
            }
        });
    }

    private void updateButacas(final int sesionId, final List<Butaca> butacas) throws SQLException
    {
        Sesion sesion = sesionDao.getById(sesionId);

        DeleteBuilder<Butaca, Integer> builder = dao.deleteBuilder();
        builder.where().eq("sesion_id", sesionId);
        builder.delete();

        for (Butaca butaca : butacas)
        {
            insertButaca(sesion, butaca);
        }
    }

    private void insertButaca(Sesion sesion, Butaca butaca) throws SQLException
    {
        butaca.setSesion(sesion);

        if (butaca.getFechaPresentadaEpoch() != 0)
            butaca.setFechaPresentada(new Date(butaca.getFechaPresentadaEpoch()));

        butaca.setModificada(false);

        dao.create(butaca);
    }

    /*
    private void updateSesionesButaca(Butaca butaca) throws SQLException
    {
        for (Sesion sesion : butaca.getSesiones())
        {
            Sesion sesionDB = sesionDao.getById(sesion.getId());

            if (sesionDB == null)
            {
                sesion.setButaca(butaca);
                sesion.setFecha(new Date(sesion.getFechaCelebracionEpoch()));
                
                sesionDao.insert(sesion);
            }
            else
            {
                sesionDB.setFecha(new Date(sesion.getFechaCelebracionEpoch()));
                
                sesionDao.update(sesionDB);
            }
        }
    }
    */

}
