package com.fourtic.paranimf.entradas.db;

import java.sql.SQLException;
import java.util.List;

import android.util.Log;

import com.fourtic.paranimf.entradas.constants.Constants;
import com.fourtic.paranimf.entradas.data.Sesion;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;

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
        return dao.queryForAll();
    }

    public Sesion getById(int id) throws SQLException
    {
        return dao.queryForId(id);
    }

    public void update(Sesion sesionDB) throws SQLException
    {
        dao.update(sesionDB);
    }

}
