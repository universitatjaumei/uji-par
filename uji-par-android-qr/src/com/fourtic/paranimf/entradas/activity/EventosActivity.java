package com.fourtic.paranimf.entradas.activity;

import java.sql.SQLException;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.fourtic.paranimf.entradas.R;
import com.fourtic.paranimf.entradas.activity.base.BaseNormalActivity;
import com.fourtic.paranimf.entradas.adapter.EventosListAdapter;
import com.fourtic.paranimf.entradas.constants.Constants;
import com.fourtic.paranimf.entradas.db.EventoDao;
import com.fourtic.paranimf.entradas.sync.SyncEventos;
import com.fourtic.paranimf.entradas.sync.SyncEventos.SyncCallback;
import com.google.inject.Inject;

public class EventosActivity extends BaseNormalActivity
{
    @InjectView(R.id.eventos)
    private ListView listEventos;

    @Inject
    private EventoDao eventoDao;

    @Inject
    private SyncEventos sync;

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
        sync.loadEventosFromRest(new SyncCallback()
        {
            @Override
            public void onSuccess()
            {
                Toast.makeText(EventosActivity.this, "Actualizado!", Toast.LENGTH_SHORT).show();
                loadEventosFromDB();
            }

            @Override
            public void onError(Throwable e, String errorMessage)
            {
                handleError(errorMessage, e);
            }
        });
    }

}
