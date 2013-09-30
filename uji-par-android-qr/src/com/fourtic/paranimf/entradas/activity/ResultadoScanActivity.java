package com.fourtic.paranimf.entradas.activity;

import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.Window;
import com.fourtic.paranimf.entradas.R;
import com.fourtic.paranimf.entradas.activity.base.BaseNormalActivity;
import com.fourtic.paranimf.entradas.constants.Constants;
import com.fourtic.paranimf.entradas.scan.ResultadoScan;

public class ResultadoScanActivity extends BaseNormalActivity
{
    private static final long RETARDO_OK = 1000;
    private static final long RETARDO_ERROR = 2000;

    @InjectView(R.id.dialogRoot)
    private RelativeLayout rootLayout;

    @InjectView(R.id.texto)
    private TextView textView;

    @InjectExtra(value = Constants.DIALOG_MESSAGE)
    private String message;

    @InjectExtra(value = Constants.SCAN_RESULT)
    private int resultadoScan;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultado_scan_activity);

        handler = new Handler();

        textView.setText(message);
        cambiaColorFondo();

        cerrarAlTocar();
        programaCierreActivity();
    }

    private void cerrarAlTocar()
    {
        rootLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    private void programaCierreActivity()
    {
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    finish();
                }
                catch (Exception e)
                {
                }
            }
        }, getRetardoPorResultado());
    }

    private long getRetardoPorResultado()
    {
        if (resultadoScan == ResultadoScan.ERROR.ordinal())
        {
            return RETARDO_ERROR;
        }
        else
        {
            return RETARDO_OK;
        }
    }

    private void cambiaColorFondo()
    {
        int colorFondo;

        if (resultadoScan == ResultadoScan.OK.ordinal())
            colorFondo = R.color.dialog_scan_ok;
        else if (resultadoScan == ResultadoScan.DESCUENTO.ordinal())
            colorFondo = R.color.dialog_scan_descuento;
        else
            colorFondo = R.color.dialog_scan_error;

        rootLayout.setBackgroundColor(getResources().getColor(colorFondo));
    }
}
