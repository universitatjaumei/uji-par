package com.fourtic.paranimf.entradas.activity;

import java.util.Date;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.fourtic.paranimf.entradas.R;
import com.fourtic.paranimf.entradas.activity.base.BaseNormalActivity;
import com.fourtic.paranimf.entradas.constants.Constants;
import com.fourtic.paranimf.entradas.db.ButacaDao;
import com.fourtic.paranimf.entradas.sync.SyncButacas;
import com.fourtic.paranimf.entradas.sync.SyncButacas.SyncCallback;
import com.fourtic.paranimf.utils.Utils;
import com.google.inject.Inject;

public class SesionInfoActivity extends BaseNormalActivity
{
    @Inject
    private ButacaDao butacaDao;

    @Inject
    private SyncButacas sync;

    @InjectView(R.id.eventoTitulo)
    private TextView textEventoTitulo;

    @InjectView(R.id.sesionFecha)
    private TextView textSesionFecha;

    @InjectView(R.id.numeroPresentadas)
    private TextView textNumeroPresentadas;

    @InjectView(R.id.numeroVendidas)
    private TextView textNumeroVendidas;

    @InjectView(R.id.sincronizaButton)
    private Button sincronizar;

    @InjectExtra(value = Constants.EVENTO_TITULO)
    private String eventoTitulo;

    @InjectExtra(value = Constants.SESION_HORA)
    private String sesionHora;

    @InjectExtra(value = Constants.SESION_FECHA)
    private long sesionFechaEpoch;

    @InjectExtra(value = Constants.SESION_ID)
    private int sesionId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sesion_info_activity);
        setSupportProgressBarIndeterminateVisibility(false);

        initInfo();
        initButtons();
    }

    private void initButtons()
    {
        sincronizar.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                sincronizar.setEnabled(false);
                showProgress();

                sync.syncButacasFromRest(sesionId, new SyncCallback()
                {
                    @Override
                    public void onSuccess()
                    {
                        initInfo();
                        showMessage("Sincronizado!");
                        hideProgress();
                        sincronizar.setEnabled(true);
                    }

                    @Override
                    public void onError(Throwable e, String errorMessage)
                    {
                        handleError("Error sincronizando butacas", e);
                        hideProgress();
                        sincronizar.setEnabled(true);
                    }
                });
            }
        });
    }

    private void initInfo()
    {
        try
        {
            textEventoTitulo.setText(eventoTitulo);
            textSesionFecha.setText(Utils.formatDate(new Date(sesionFechaEpoch)) + " " + sesionHora);

            textNumeroVendidas.setText(Long.toString(butacaDao.getButacasCount(sesionId)));
            textNumeroPresentadas.setText(Long.toString(butacaDao.getButacasPresentadasCount(sesionId)));
        }
        catch (Exception e)
        {
            handleError("Error recuperando datos de sesión", e);
        }
    }

}
