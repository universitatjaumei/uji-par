package com.fourtic.paranimf.entradas.activity;

import java.sql.SQLException;
import java.util.List;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.fourtic.paranimf.entradas.R;
import com.fourtic.paranimf.entradas.activity.base.BaseNormalActivity;
import com.fourtic.paranimf.entradas.adapter.EventosListAdapter;
import com.fourtic.paranimf.entradas.constants.Constants;
import com.fourtic.paranimf.entradas.data.Evento;
import com.fourtic.paranimf.entradas.db.EventoDao;
import com.fourtic.paranimf.entradas.rest.RestService;
import com.fourtic.paranimf.entradas.rest.RestService.ResultCallback;
import com.google.inject.Inject;

public class EventosActivity extends BaseNormalActivity
{
    @InjectView(R.id.eventos)
    private ListView listEventos;

    @Inject
    private EventoDao eventoDao;

    private EventosListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventos_activity);

        initList();
    }

    private void initList()
    {
        adapter = new EventosListAdapter(this);
        listEventos.setAdapter(adapter);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        // loadEventosFromRest();
        loadEventosFromDB();
    }

    private void loadEventosFromDB()
    {
        try
        {
            adapter.update(eventoDao.getEventos());
        }
        catch (SQLException e)
        {
            Log.e(Constants.TAG, "Error recuperando eventos de BD", e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getSupportMenuInflater().inflate(R.menu.eventos, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case R.id.action_sync:
            sincronize();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void sincronize()
    {

    }

    private void loadEventosFromRest()
    {
        new RestService(this).getEventos(new ResultCallback<List<Evento>>()
        {
            @Override
            public void onSuccess(List<Evento> eventos)
            {
                try
                {
                    eventoDao.insert(eventos);
                }
                catch (SQLException e)
                {
                    Log.e(Constants.TAG, "Error insertando eventos en BD", e);
                }
                adapter.update(eventos);
            }

            @Override
            public void onError(Throwable e, String errorMessage)
            {
                handleError(errorMessage, e);
            }
        });
    }
}
