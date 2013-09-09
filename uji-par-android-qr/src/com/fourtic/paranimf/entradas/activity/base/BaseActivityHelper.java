package com.fourtic.paranimf.entradas.activity.base;

import android.app.Activity;
import android.widget.Toast;

public class BaseActivityHelper
{
    public static void showError(Activity activity, Throwable exception)
    {
        showError(activity, exception.getMessage());
    }

    public static void showError(Activity activity, String errorMessage)
    {
        Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
    }

    public static void showMessage(BaseNormalActivity activity, String message)
    {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

}
