package com.fourtic.paranimf.entradas.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.fourtic.paranimf.entradas.constants.Constants;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;

public abstract class BaseNormalActivity extends RoboSherlockFragmentActivity implements BaseActivity
{
    public void setContentView(int layoutRes)
    {
        super.setContentView(layoutRes);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setSupportProgressBarIndeterminateVisibility(false);

        getSupportActionBar().setHomeButtonEnabled(true);

        //        getSupportActionBar().setDisplayShowCustomEnabled(true);
        //        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void hideProgress()
    {
        setSupportProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void showProgress()
    {
        setSupportProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void showError(Throwable exception)
    {
        BaseActivityHelper.showError(this, exception);
    }

    public void showError(String errorMessage)
    {
        BaseActivityHelper.showError(this, errorMessage);
    }

    private void goToActivity(Class<?> activityClass)
    {
        Intent intent = new Intent(this, activityClass);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }

    protected void handleError(String message, Throwable e)
    {
        Log.e(Constants.TAG, message, e);
        showError(message);
    }
}
