package br.com.androidzin.pontopro.settings;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import br.com.androidzin.pontopro.R;

import static br.com.androidzin.pontopro.settings.BusinessHourCommom.EATING_TIME_KEY;
import static br.com.androidzin.pontopro.settings.BusinessHourCommom.ENTERED_CHECKIN_KEY;
import static br.com.androidzin.pontopro.settings.BusinessHourCommom.LEAVING_CHECKIN_KEY;
import static br.com.androidzin.pontopro.settings.BusinessHourCommom.LUNCH_CHECKIN_KEY;
import static br.com.androidzin.pontopro.settings.BusinessHourCommom.verifyAndNotifyWorkingTimeViolation;
import static br.com.androidzin.pontopro.settings.BusinessHourCommom.verifyTimeSettings;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BusinessHourFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceChangeListener{

    private SettingsActivity mSettingsActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.business_hour_prefs);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.business_hour_prefs, false);
        findPreference(ENTERED_CHECKIN_KEY).setOnPreferenceChangeListener(this);
        findPreference(LUNCH_CHECKIN_KEY).setOnPreferenceChangeListener(this);
        findPreference(LEAVING_CHECKIN_KEY).setOnPreferenceChangeListener(this);
        findPreference(EATING_TIME_KEY).setOnPreferenceChangeListener(this);

        mSettingsActivity = (SettingsActivity) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
          verifyAndNotifyWorkingTimeViolation(sharedPreferences, getActivity(), key);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        boolean result = verifyTimeSettings(preference, newValue, sharedPreferences);
        mSettingsActivity.setHasError(sharedPreferences, preference, !result);
        return  result;
    }

}
