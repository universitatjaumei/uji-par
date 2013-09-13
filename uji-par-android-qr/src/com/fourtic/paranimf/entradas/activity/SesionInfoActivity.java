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
import com.fourtic.paranimf.entradas.exception.ButacaDeOtraSesionException;
import com.fourtic.paranimf.entradas.exception.ButacaNoEncontradaException;
import com.fourtic.paranimf.entradas.network.EstadoRed;
import com.fourtic.paranimf.entradas.sync.SincronizadorButacas;
import com.fourtic.paranimf.entradas.sync.SincronizadorButacas.SyncCallback;
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
    private SincronizadorButacas sync;

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
    private EstadoRed red;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sesion_info_activity);
        setSupportProgressBarIndeterminateVisibility(false);

        iniciaBotones();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        actualizaInfo();
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

            if (red.estaActiva())
            {
                sincroniza();
            }
            else
            {
                muestraError(getString(R.string.conexion_red_no_disponible));
            }

            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void sincroniza()
    {
        try
        {
            if (red.estaActiva())
                sincronizaButacas();
            else
                muestraError(getString(R.string.conexion_red_no_disponible));
        }
        catch (SQLException e)
        {
            gestionaError(getString(R.string.error_sincronizando_entradas), e);
        }
    }

    private void iniciaBotones()
    {

        escanearBoton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                abreActividadEscanear();
            }
        });

        manualBoton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                abreActividadManual();
            }
        });
    }

    private void sincronizaButacas() throws SQLException
    {
        muestraProgreso();

        sync.sincronizaButacasDesdeRest(sesionId, new SyncCallback()
        {
            @Override
            public void onSuccess()
            {
                actualizaInfo();
                muestraMensaje(getString(R.string.sincronizado));
                ocultaProgreso();
            }

            @Override
            public void onError(Throwable e, String errorMessage)
            {
                gestionaError(getString(R.string.error_sincronizando_butacas), e);
                ocultaProgreso();
            }
        });
    }

    protected void abreActividadEscanear()
    {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "DATA_MATRIX_MODE");
        startActivityForResult(intent, REQUEST_CODE);
    }

    protected void abreActividadManual()
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
                procesCodigoBarras(data);
            }
            catch (Exception e)
            {
                gestionaError(getString(R.string.error_escaneando_entrada), e);
            }
        }
    };

    private void procesCodigoBarras(Intent data) throws SQLException
    {
        abreActividadEscanear();

        String uuid = data.getStringExtra("SCAN_RESULT");

        try
        {
            Date fechaPresentada = butacaDao.getFechaPresentada(sesionId, uuid);

            if (fechaPresentada == null)
            {
                butacaDao.actualizaFechaPresentada(uuid, new Date());
                muestraDialogoResultadoScan(getString(R.string.entrada_ok), false);
            }
            else
            {
                muestraDialogoResultadoScan(getString(R.string.ya_presentada) + Utils.formatDateWithTime(fechaPresentada),
                        true);
            }
        }
        catch (ButacaNoEncontradaException e)
        {
            muestraDialogoResultadoScan(getString(R.string.entrada_no_sesion), true);
        }
        catch (ButacaDeOtraSesionException e)
        {
            muestraDialogoResultadoScan(getString(R.string.entrada_otra_sesion), true);
        }
    }

    private void muestraDialogoResultadoScan(String message, boolean error)
    {
        Intent intent = new Intent(this, ResultadoScanActivity.class);
        intent.putExtra(Constants.DIALOG_MESSAGE, message);
        intent.putExtra(Constants.DIALOG_ERROR, error);

        startActivity(intent);
    }

    private void actualizaInfo()
    {
        try
        {
            textEventoTitulo.setText(eventoTitulo);
            textSesionFecha.setText(Utils.formatDate(new Date(sesionFechaEpoch)) + " " + sesionHora);

            textNumeroVendidas.setText(Long.toString(butacaDao.getNumeroButacas(sesionId)));
            textNumeroPresentadas.setText(Long.toString(butacaDao.getNumeroButacasPresentadas(sesionId)));

            long modificadas = butacaDao.getNumeroButacasModificadas(sesionId);

            textFaltanSubir.setVisibility(modificadas == 0 ? View.INVISIBLE : View.VISIBLE);

            Date lastSync = sesionDao.getFechaSincronizacion(sesionId);

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
            gestionaError(getString(R.string.error_recuperando_datos_sesion), e);
        }
    }
}
