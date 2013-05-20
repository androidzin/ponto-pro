package br.com.androidzin.pontopro.settings;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.Toast;
import br.com.androidzin.pontopro.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BusinessHourFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String PREFS_PREFIX = "prefs_";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.business_hour_prefs);

        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(sharedPreferences.contains(key)){
                String enteredCheckin = sharedPreferences.getString(PREFS_PREFIX.concat(getString(R.string.entered_checkin)),
                        getString(R.string.zero_number));
                String lunchCheckin = sharedPreferences.getString(PREFS_PREFIX.concat(getString(R.string.lunch_checkin)),
                        getString(R.string.zero_number));
                String afterLunchCheckin = sharedPreferences.getString(PREFS_PREFIX.concat(getString(R.string.after_lunch_checkin)),
                        getString(R.string.zero_number));
                String leavingCheckin = sharedPreferences.getString(PREFS_PREFIX.concat(getString(R.string.leaving_checkin)),
                        getString(R.string.zero_number));

        }
    }

}
