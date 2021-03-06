package com.fourtic.paranimf.entradas.db;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.util.Log;

import com.fourtic.paranimf.entradas.constants.Constants;
import com.fourtic.paranimf.entradas.data.Sesion;
import com.fourtic.paranimf.entradas.exception.SesionNoEncontradaException;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

@Singleton
public class SesionDao
{
    DbHelperService dbHelper;

    Dao<Sesion, Integer> dao;

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

    public void inserta(Sesion sesion) throws SQLException
    {
        dao.create(sesion);
    }

    public List<Sesion> getSesiones(int idEvento) throws SQLException
    {
        QueryBuilder<Sesion, Integer> builder = dao.queryBuilder();
        builder.where().eq("evento_id", idEvento);
        builder.orderBy("fecha", false);

        List<Sesion> sesiones = builder.query();

        Set<Integer> idsModificados = getIdsSesionesModificadas();

        for (Sesion sesion : sesiones)
        {
            if (idsModificados.contains(sesion.getId()))
            {
                sesion.setModificado(true);
            }
        }

        return sesiones;
    }

    public Sesion getPorId(int id) throws SQLException
    {
        return dao.queryForId(id);
    }

    public void actualiza(Sesion sesionDB) throws SQLException
    {
        dao.update(sesionDB);
    }

    public void actualizaFechaSincronizacion(int sesionId, Date fechaSync) throws SQLException
    {
        UpdateBuilder<Sesion, Integer> builder = dao.updateBuilder();

        builder.updateColumnValue("fecha_sync", fechaSync);
        builder.where().eq("id", sesionId);

        builder.update();
    }

    public Date getFechaSincronizacion(int sesionId) throws SQLException, SesionNoEncontradaException
    {
        List<Sesion> sesiones = dao.queryForEq("id", sesionId);

        if (sesiones.size() == 0)
        {
            throw new SesionNoEncontradaException();
        }
        else
        {
            return sesiones.get(0).getFechaSync();
        }
    }

    public Dao<Sesion, Integer> getDaoInternal()
    {
        return dao;
    }

    private Set<Integer> getIdsSesionesModificadas() throws SQLException
    {
        Set<Integer> result = new HashSet<Integer>();

        GenericRawResults<String[]> queryRaw = dao.queryRaw("select s.id from sesion s, butaca b "
                + "where s.id=b.sesion_id and b.modificada=1");

        List<String[]> ids = queryRaw.getResults();

        for (String[] id : ids)
        {
            result.add(Integer.parseInt(id[0]));
        }

        return result;
    }

    public void deleteAll() throws SQLException
    {
        dao.deleteBuilder().delete();
    }

}
