package com.fourtic.paranimf.entradas.network;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.inject.Inject;

public class EstadoRed
{
    @Inject
    private ConnectivityManager connectivityManager;

    public boolean estaActiva()
    {
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
