package com.fourtic.paranimf.entradas.db;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import android.util.Log;

import com.fourtic.paranimf.entradas.constants.Constants;
import com.fourtic.paranimf.entradas.data.Sesion;
import com.fourtic.paranimf.entradas.exception.SesionNotFoundException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

@Singleton
public class SesionDao
{
    DbHelperService dbHelper;

    private Dao<Sesion, Integer> dao;

    @Inject
    public SesionDao(DbHelperService dbHelper)
    {
        this.dbHelper = dbHelper;

        try
        {
            dao = dbHelper.getHelper().getSesionDao();
        }
        catch (SQLException e)
        {
            Log.e(Constants.TAG, "Error iniciando dao Sesion", e);
        }
    }

    public void insert(Sesion sesion) throws SQLException
    {
        dao.create(sesion);
    }

    public List<Sesion> getSesiones(int idEvento) throws SQLException
    {
        return dao.queryForEq("evento_id", idEvento);
    }

    public Sesion getById(int id) throws SQLException
    {
        return dao.queryForId(id);
    }

    public void update(Sesion sesionDB) throws SQLException
    {
        dao.update(sesionDB);
    }

    public void updateFechaSync(int sesionId, Date fechaSync) throws SQLException
    {
        UpdateBuilder<Sesion, Integer> builder = dao.updateBuilder();

        builder.updateColumnValue("fecha_sync", fechaSync);
        builder.where().eq("id", sesionId);

        builder.update();
    }

    public Date getFechaSync(int sesionId) throws SQLException, SesionNotFoundException
    {
        List<Sesion> sesiones = dao.queryForEq("id", sesionId);

        if (sesiones.size() == 0)
        {
            throw new SesionNotFoundException();
        }
        else
        {
            return sesiones.get(0).getFechaSync();
        }
    }

}
