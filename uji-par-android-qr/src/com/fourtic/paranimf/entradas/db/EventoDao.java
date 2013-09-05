package com.fourtic.paranimf.entradas.db;

import java.sql.SQLException;
import java.util.List;

import android.util.Log;

import com.fourtic.paranimf.entradas.constants.Constants;
import com.fourtic.paranimf.entradas.data.Evento;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;

@Singleton
public class EventoDao
{
    DbHelperService dbHelper;

    private Dao<Evento, Integer> dao;

    @Inject
    public EventoDao(DbHelperService dbHelper)
    {
        this.dbHelper = dbHelper;

        try
        {
            dao = dbHelper.getHelper().getEventoDao();
        }
        catch (SQLException e)
        {
            Log.e(Constants.TAG, "Error iniciando dao Evento", e);
        }
    }

    public void insert(List<Evento> eventos) throws SQLException
    {
        for (Evento evento : eventos)
        {
            dao.create(evento);
        }
    }

    public List<Evento> getEventos() throws SQLException
    {
        return dao.queryForAll();
    }

}
