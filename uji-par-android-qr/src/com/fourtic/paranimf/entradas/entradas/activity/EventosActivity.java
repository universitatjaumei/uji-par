package com.fourtic.paranimf.entradas.entradas.activity;

import java.util.List;

import roboguice.inject.InjectView;
import android.os.Bundle;
import android.widget.ListView;

import com.fourtic.paranimf.entradas.R;
import com.fourtic.paranimf.entradas.activity.base.BaseNormalActivity;
import com.fourtic.paranimf.entradas.adapter.EventosListAdapter;
import com.fourtic.paranimf.entradas.data.DataService;
import com.fourtic.paranimf.entradas.data.DataService.ResultCallback;
import com.fourtic.paranimf.entradas.data.Evento;

public class EventosActivity extends BaseNormalActivity
{
    @InjectView(R.id.eventos)
    private ListView listEventos;

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
        loadEventos();
    }

    private void loadEventos()
    {
        new DataService(this).getEventos(new ResultCallback<List<Evento>>()
        {
            @Override
            public void onSuccess(List<Evento> eventos)
            {
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
