package com.fourtic.paranimf.entradas.activity.base;

public interface BaseActivity
{
    public void ocultaProgreso();

    public void muestraProgreso();

    public void showError(Throwable exception);

}