package com.fourtic.paranimf.entradas.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.fourtic.paranimf.entradas.data.Evento;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DBHelper extends OrmLiteSqliteOpenHelper
{
    private static final String DATABASE_NAME = "paranimf.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Evento, Integer> eventoDao;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
    {
        try
        {
            TableUtils.createTable(connectionSource, Evento.class);
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion)
    {
        onCreate(db, connectionSource);
    }

    public Dao<Evento, Integer> getEventoDao() throws SQLException 
    {
        if (eventoDao == null)
        {
            eventoDao = getDao(Evento.class);
        }

        return eventoDao;
    }

    @Override
    public void close()
    {
        super.close();
        eventoDao = null;
    }

}