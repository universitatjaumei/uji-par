package com.fourtic.paranimf.entradas.activity.base;

import android.app.Activity;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class BaseActivityHelper
{
    public static void showError(Activity activity, Throwable exception)
    {
        showError(activity, exception.getMessage());
    }

    public static void showError(Activity activity, String errorMessage)
    {
        Crouton.makeText(activity, errorMessage, Style.ALERT).show();
    }

    public static void showMessage(BaseNormalActivity activity, String message)
    {
        Crouton.makeText(activity, message, Style.CONFIRM).show();
    }

}
