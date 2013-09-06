package com.fourtic.paranimf.entradas.activity;

import java.sql.SQLException;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.fourtic.paranimf.entradas.R;
import com.fourtic.paranimf.entradas.activity.base.BaseNormalActivity;
import com.fourtic.paranimf.entradas.adapter.SesionesListAdapter;
import com.fourtic.paranimf.entradas.constants.Constants;
import com.fourtic.paranimf.entradas.db.SesionDao;
import com.google.inject.Inject;

public class SesionesActivity extends BaseNormalActivity
{
    @InjectView(R.id.sesiones)
    private ListView listSesiones;

    @Inject
    private SesionDao sesionDao;

    private SesionesListAdapter adapter;

    @InjectExtra(value = Constants.ID_EVENTO)
    private int idEvento;

    @InjectExtra(value = Constants.TITULO_EVENTO)
    private String tituloEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sesiones_activity);
        setSupportProgressBarIndeterminateVisibility(false);
        
        getSupportActionBar().setTitle(tituloEvento);

        initList();
    }

    private void initList()
    {
        adapter = new SesionesListAdapter(this);
        listSesiones.setAdapter(adapter);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        loadSesionesFromDB();
    }

    private void loadSesionesFromDB()
    {
        try
        {
            adapter.update(sesionDao.getSesiones(idEvento));
        }
        catch (SQLException e)
        {
            Log.e(Constants.TAG, "Error recuperando sesiones de BD", e);
        }
    }

}
