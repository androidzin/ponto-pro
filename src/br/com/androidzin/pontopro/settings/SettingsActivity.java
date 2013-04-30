package br.com.androidzin.pontopro.settings;

import java.util.List;

import br.com.androidzin.pontopro.R;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {

	final static String ACTION_PREFS_NOTIFICATION = "br.com.androidzin.pontopro.settings.NOTIFICATION";
	final static String ACTION_PREFS_BUSSINESSHOUR = "br.com.androidzin.pontopro.settings.BUSSINER_HOUR";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    String action = getIntent().getAction();
	    if (action != null && action.equals(ACTION_PREFS_NOTIFICATION)) {
	        addPreferencesFromResource(R.xml.notifications_prefs);
	    } else if (action != null && action.equals(ACTION_PREFS_BUSSINESSHOUR)){
	    	addPreferencesFromResource(R.xml.notifications_prefs);
		} else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
	        addPreferencesFromResource(R.xml.general_preferences_headers_legacy);
	    }
	}

	// Called only on Honeycomb and later
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onBuildHeaders(List<Header> target) {
	   loadHeadersFromResource(R.xml.general_preferences_headers, target);
	}
}
