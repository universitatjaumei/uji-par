package com.fourtic.paranimf.entradas.activity;

import java.sql.SQLException;
import java.util.List;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.fourtic.paranimf.entradas.R;
import com.fourtic.paranimf.entradas.activity.base.BaseNormalActivity;
import com.fourtic.paranimf.entradas.adapter.SesionesListAdapter;
import com.fourtic.paranimf.entradas.constants.Constants;
import com.fourtic.paranimf.entradas.data.Sesion;
import com.fourtic.paranimf.entradas.db.SesionDao;
import com.google.inject.Inject;

public class SesionesActivity extends BaseNormalActivity
{
    @InjectView(R.id.sesiones)
    private ListView sesionesList;

    @Inject
    private SesionDao sesionDao;

    private SesionesListAdapter adapter;

    @InjectExtra(value = Constants.EVENTO_ID)
    private int idEvento;

    @InjectExtra(value = Constants.EVENTO_TITULO)
    private String tituloEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sesiones_activity);
        setSupportProgressBarIndeterminateVisibility(false);

        getSupportActionBar().setTitle(tituloEvento);

        iniciaList();
    }

    private void iniciaList()
    {
        adapter = new SesionesListAdapter(this);
        sesionesList.setAdapter(adapter);

        sesionesList.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (position < sesionesList.getAdapter().getCount())
                {
                    openInfoSesion((Sesion)sesionesList.getAdapter().getItem(position));
                }
            }
        });
    }

    protected void openInfoSesion(Sesion sesion)
    {
        Intent intent = new Intent(this, SesionInfoActivity.class);
        intent.putExtra(Constants.SESION_ID, sesion.getId());
        intent.putExtra(Constants.EVENTO_TITULO, tituloEvento);
        intent.putExtra(Constants.SESION_FECHA, sesion.getFecha().getTime());
        intent.putExtra(Constants.SESION_HORA, sesion.getHoraCelebracion());
        
        startActivity(intent);        
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        cargaSesionesDesdeBd();
    }

    private void cargaSesionesDesdeBd()
    {
        try
        {
            List<Sesion> sesiones = sesionDao.getSesiones(idEvento);
            adapter.update(sesiones);
        }
        catch (SQLException e)
        {
            Log.e(Constants.TAG, getString(R.string.error_recuperando_sesiones_bd), e);
        }
    }

}
