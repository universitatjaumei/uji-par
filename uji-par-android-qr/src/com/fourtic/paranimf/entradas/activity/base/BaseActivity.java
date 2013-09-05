package com.fourtic.paranimf.entradas.activity.base;

public interface BaseActivity
{
    public void hideProgress();

    public void showProgress();

    public void showError(Throwable exception);

}