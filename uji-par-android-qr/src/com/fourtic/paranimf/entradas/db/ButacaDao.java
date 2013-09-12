package com.fourtic.paranimf.entradas.db;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import android.util.Log;

import com.fourtic.paranimf.entradas.constants.Constants;
import com.fourtic.paranimf.entradas.data.Butaca;
import com.fourtic.paranimf.entradas.data.Sesion;
import com.fourtic.paranimf.entradas.exception.ButacaFromAnotherSesionException;
import com.fourtic.paranimf.entradas.exception.ButacaNotFoundException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

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

    public List<Butaca> getButacasModificadas(int sesionId) throws SQLException
    {
        QueryBuilder<Butaca, Integer> builder = dao.queryBuilder();
        builder.where().eq("sesion_id", sesionId).and().eq("modificada", true);

        return builder.query();
    }

    public boolean butacasModified(int sesionId) throws SQLException
    {
        return getButacasModificadas(sesionId).size() > 0;
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

    public long getButacasModificadasCount(int sesionId) throws SQLException
    {
        QueryBuilder<Butaca, Integer> builder = dao.queryBuilder();

        return builder.where().eq("sesion_id", sesionId).and().eq("modificada", true).countOf();
    }

    public Date getFechaPresentada(int sesionId, String uuid) throws SQLException, ButacaNotFoundException,
            ButacaFromAnotherSesionException
    {
        List<Butaca> butacas = dao.queryForEq("uuid", uuid);

        if (butacas.size() == 0)
        {
            throw new ButacaNotFoundException();
        }
        else
        {
            Butaca butaca = butacas.get(0);

            if (butaca.getSesion().getId() != sesionId)
            {
                throw new ButacaFromAnotherSesionException();
            }
            else
            {
                return butacas.get(0).getFechaPresentada();
            }
        }
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

    public void updateFechaPresentada(String uuid, Date date) throws SQLException
    {
        UpdateBuilder<Butaca, Integer> builder = dao.updateBuilder();
        builder.where().eq("uuid", uuid);
        builder.updateColumnValue("presentada", date);
        builder.updateColumnValue("modificada", true);
        builder.update();
    }

    public Dao<Butaca, Integer> getDaoInternal()
    {
        return dao;
    }

    public List<Butaca> getButacasNoPresentadasByUuid(int sesionId, String uuid) throws SQLException
    {
        QueryBuilder<Butaca, Integer> builder = dao.queryBuilder();

        builder.where().eq("sesion_id", sesionId).and().isNull("presentada").and().like("uuid", "%-%-%-%-%-%" + uuid + "%");

        return builder.query();
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
