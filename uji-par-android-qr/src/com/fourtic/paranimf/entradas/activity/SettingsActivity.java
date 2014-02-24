package com.fourtic.paranimf.entradas.activity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;

import com.fourtic.paranimf.entradas.R;
import com.fourtic.paranimf.entradas.rest.RestService;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockPreferenceActivity;
import com.google.inject.Inject;

public class SettingsActivity extends RoboSherlockPreferenceActivity implements OnSharedPreferenceChangeListener {

	public static final String PREF_HOST = "pref_host";
	public static final String PREF_PORT = "pref_port";
	public static final String PREF_APIKEY = "pref_apikey";
	
    @Inject
    private RestService rest;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
	
    @Override
    protected void onResume()
    {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
	
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
		rest.setURLFromPreferences(this);
		rest.setAPIKeyFromPreferences(this);
    }
}
