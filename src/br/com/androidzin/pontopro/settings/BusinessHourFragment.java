package br.com.androidzin.pontopro.settings;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;
import br.com.androidzin.pontopro.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BusinessHourFragment extends PreferenceFragment{

    private static final String PREFS_PREFIX = "prefs_";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.business_hour_prefs);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.business_hour_prefs, false);
    }



}
