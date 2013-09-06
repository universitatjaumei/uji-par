package com.fourtic.paranimf.entradas.db;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import android.util.Log;

import com.fourtic.paranimf.entradas.constants.Constants;
import com.fourtic.paranimf.entradas.data.Evento;
import com.fourtic.paranimf.entradas.data.Sesion;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;

@Singleton
public class EventoDao
{
    DbHelperService dbHelper;

    @Inject
    private SesionDao sesionDao;

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

    public void persist(final List<Evento> eventos) throws SQLException
    {
        TransactionManager.callInTransaction(dao.getConnectionSource(), new Callable<Void>()
        {
            public Void call() throws Exception
            {
                for (Evento evento : eventos)
                {
                    updateDatosEvento(evento);
                    updateSesionesEvento(evento);
                }

                return null;
            }

        });
    }

    private void updateDatosEvento(Evento evento) throws SQLException
    {
        Evento eventoDB = dao.queryForId(evento.getId());

        if (eventoDB == null)
        {
            dao.create(evento);
        }
        else
        {
            eventoDB.setTitulo(evento.getTitulo());
            dao.update(eventoDB);
        }
    }

    private void updateSesionesEvento(Evento evento) throws SQLException
    {
        for (Sesion sesion : evento.getSesiones())
        {
            Sesion sesionDB = sesionDao.getById(sesion.getId());

            if (sesionDB == null)
            {
                sesion.setEvento(evento);
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

    public List<Evento> getEventos() throws SQLException
    {
        return dao.queryForAll();
    }

}
