package com.fourtic.paranimf.entradas.db;

import android.app.Application;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.j256.ormlite.android.apptools.OpenHelperManager;

@Singleton
public class DbHelperService
{
    private DBHelper helper;

    @Inject
    public DbHelperService(Application application)
    {
        helper = OpenHelperManager.getHelper(application, DBHelper.class);
    }

    public DBHelper getHelper()
    {
        return helper;
    }
}
