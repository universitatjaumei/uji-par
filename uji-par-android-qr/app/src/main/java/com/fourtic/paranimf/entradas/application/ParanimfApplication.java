package com.fourtic.paranimf.entradas.application;

import java.util.Locale;

import android.app.Application;
import android.content.res.Configuration;

public class ParanimfApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        idiomaCa();
    }

    private void idiomaCa()
    {
        Configuration config = getBaseContext().getResources().getConfiguration();

        Locale.setDefault(new Locale("ca"));
        getBaseContext().getResources()
                .updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

}
