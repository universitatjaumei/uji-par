package com.fourtic.paranimf.entradas.activity.base;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.view.MenuItem;
import com.fourtic.paranimf.entradas.activity.EventosActivity;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockPreferenceActivity;

public class BasePreferenceActivity extends RoboSherlockPreferenceActivity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeButtonEnabled(true);
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case android.R.id.home:
            goToHome();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    private void goToHome()
    {
        Intent intent = new Intent(this, EventosActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }
}
