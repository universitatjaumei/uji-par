package com.fourtic.paranimf.entradas.activity;

import java.sql.SQLException;
import java.util.Date;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.fourtic.paranimf.entradas.R;
import com.fourtic.paranimf.entradas.activity.base.BaseNormalActivity;
import com.fourtic.paranimf.entradas.constants.Constants;
import com.fourtic.paranimf.entradas.db.ButacaDao;
import com.fourtic.paranimf.entradas.db.SesionDao;
import com.fourtic.paranimf.entradas.exception.ButacaFromAnotherSesionException;
import com.fourtic.paranimf.entradas.exception.ButacaNotFoundException;
import com.fourtic.paranimf.entradas.network.NetworkChecker;
import com.fourtic.paranimf.entradas.sync.SyncButacas;
import com.fourtic.paranimf.entradas.sync.SyncButacas.SyncCallback;
import com.fourtic.paranimf.utils.Utils;
import com.google.inject.Inject;

public class SesionInfoActivity extends BaseNormalActivity
{
    private static final int REQUEST_CODE = 1;

    @Inject
    private ButacaDao butacaDao;

    @Inject
    private SesionDao sesionDao;

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

    @InjectView(R.id.textFaltanSubir)
    private TextView textFaltanSubir;

    @InjectView(R.id.mensaje)
    private TextView textMensaje;

    @InjectView(R.id.escaneaButton)
    private Button escanearBoton;

    @InjectView(R.id.manualButton)
    private Button manualBoton;

    @InjectExtra(value = Constants.EVENTO_TITULO)
    private String eventoTitulo;

    @InjectExtra(value = Constants.SESION_HORA)
    private String sesionHora;

    @InjectExtra(value = Constants.SESION_FECHA)
    private long sesionFechaEpoch;

    @InjectExtra(value = Constants.SESION_ID)
    private int sesionId;

    @Inject
    private NetworkChecker network;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sesion_info_activity);
        setSupportProgressBarIndeterminateVisibility(false);

        initButtons();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        updateInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getSupportMenuInflater().inflate(R.menu.sesion_info, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case R.id.action_sync:

            syncSelected();

            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void syncSelected()
    {
        if (network.networkAvailable())
        {
            synchronize();
        }
        else
        {
            showError(getString(R.string.conexion_red_no_disponible));
        }
    }

    private void synchronize()
    {
        try
        {
            if (network.networkAvailable())
                syncButacas();
            else
                showError(getString(R.string.conexion_red_no_disponible));
        }
        catch (SQLException e)
        {
            handleError(getString(R.string.error_sincronizando_entradas), e);
        }
    }

    private void initButtons()
    {

        escanearBoton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                openScanActivity();
            }
        });

        manualBoton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                openManualActivity();
            }
        });
    }

    private void syncButacas() throws SQLException
    {
        showProgress();

        sync.syncButacasFromRest(sesionId, new SyncCallback()
        {
            @Override
            public void onSuccess()
            {
                updateInfo();
                showMessage(getString(R.string.sincronizado));
                hideProgress();
            }

            @Override
            public void onError(Throwable e, String errorMessage)
            {
                handleError(getString(R.string.error_sincronizando_butacas), e);
                hideProgress();
            }
        });
    }

    protected void openScanActivity()
    {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "DATA_MATRIX_MODE");
        startActivityForResult(intent, REQUEST_CODE);
    }

    protected void openManualActivity()
    {
        Intent intent = new Intent(this, EntradaManualActivity.class);
        intent.putExtra(Constants.SESION_ID, sesionId);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            try
            {
                processBarcode(data);
            }
            catch (Exception e)
            {
                handleError(getString(R.string.error_escaneando_entrada), e);
            }
        }
    };

    private void processBarcode(Intent data) throws SQLException
    {
        openScanActivity();

        String uuid = data.getStringExtra("SCAN_RESULT");

        try
        {
            Date fechaPresentada = butacaDao.getFechaPresentada(sesionId, uuid);

            if (fechaPresentada == null)
            {
                butacaDao.updateFechaPresentada(uuid, new Date());
                showScanResultDialog(getString(R.string.entrada_ok), false);
            }
            else
            {
                showScanResultDialog(getString(R.string.ya_presentada) + Utils.formatDateWithTime(fechaPresentada),
                        true);
            }
        }
        catch (ButacaNotFoundException e)
        {
            showScanResultDialog(getString(R.string.entrada_no_sesion), true);
        }
        catch (ButacaFromAnotherSesionException e)
        {
            showScanResultDialog(getString(R.string.entrada_otra_sesion), true);
        }
    }

    private void showScanResultDialog(String message, boolean error)
    {
        Intent intent = new Intent(this, ResultadoScanActivity.class);
        intent.putExtra(Constants.DIALOG_MESSAGE, message);
        intent.putExtra(Constants.DIALOG_ERROR, error);

        startActivity(intent);
    }

    private void updateInfo()
    {
        try
        {
            textEventoTitulo.setText(eventoTitulo);
            textSesionFecha.setText(Utils.formatDate(new Date(sesionFechaEpoch)) + " " + sesionHora);

            textNumeroVendidas.setText(Long.toString(butacaDao.getButacasCount(sesionId)));
            textNumeroPresentadas.setText(Long.toString(butacaDao.getButacasPresentadasCount(sesionId)));

            long modificadas = butacaDao.getButacasModificadasCount(sesionId);

            textFaltanSubir.setVisibility(modificadas == 0 ? View.INVISIBLE : View.VISIBLE);

            Date lastSync = sesionDao.getFechaSync(sesionId);

            if (lastSync == null)
            {
                textMensaje.setText(R.string.no_sincronizada);
            }
            else
            {
                textMensaje.setText(getString(R.string.ultima_sinc) + Utils.formatDateWithTime(lastSync));
            }
        }
        catch (Exception e)
        {
            handleError(getString(R.string.error_recuperando_datos_sesion), e);
        }
    }
}
