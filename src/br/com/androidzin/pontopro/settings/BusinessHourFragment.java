package br.com.androidzin.pontopro.settings;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;
import br.com.androidzin.pontopro.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BusinessHourFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceChangeListener{

    static final String WORKING_TIME_KEY = SettingsActivity.PREFS_PREFIX.concat("working_time");
    static final String EATING_TIME_KEY = SettingsActivity.PREFS_PREFIX.concat("eating_time");
    static final String LEAVING_CHECKIN_KEY = SettingsActivity.PREFS_PREFIX.concat("leaving_checkin");
    static final String AFTER_LUNCH_CHECKIN_KEY = SettingsActivity.PREFS_PREFIX.concat("after_lunch_checkin");
    static final String LUNCH_CHECKIN_KEY = SettingsActivity.PREFS_PREFIX.concat("lunch_checkin");
    static final String ENTERED_CHECKIN_KEY = SettingsActivity.PREFS_PREFIX.concat("entered_checkin");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.business_hour_prefs);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.business_hour_prefs, false);
        findPreference(BusinessHourFragment.LUNCH_CHECKIN_KEY).setOnPreferenceChangeListener(this);
        findPreference(BusinessHourFragment.EATING_TIME_KEY).setOnPreferenceChangeListener(this);
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
        return adjustAfterLunchTime(preference, newValue, getPreferenceManager().getSharedPreferences());
    }

    public static void verifyAndNotifyWorkingTimeViolation(SharedPreferences sharedPreferences, Context context, String key) {
        if(!key.equals(BusinessHourFragment.AFTER_LUNCH_CHECKIN_KEY))
        {
            Long workingTime = Long.parseLong(sharedPreferences.getString(WORKING_TIME_KEY, "0"));
            Long eatingInterval = Long.parseLong(sharedPreferences.getString(EATING_TIME_KEY, "0"));
            Long enteredCheckin = sharedPreferences.getLong(ENTERED_CHECKIN_KEY, 0);
            Long leavingCheckin = sharedPreferences.getLong(LEAVING_CHECKIN_KEY, 0);

            if(((leavingCheckin - enteredCheckin) - eatingInterval) - workingTime < 10000){
                Toast.makeText(context, R.string.working_time_violation, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static boolean adjustAfterLunchTime(Preference preference, Object newValue, SharedPreferences sharedPreferences) {
        Long eatingInterval = Long.parseLong(sharedPreferences.getString(BusinessHourFragment.EATING_TIME_KEY, "0"));
        Long lunchCheckin = sharedPreferences.getLong(BusinessHourFragment.LUNCH_CHECKIN_KEY, 0);
        Long afterLunchCheckin = sharedPreferences.getLong(BusinessHourFragment.AFTER_LUNCH_CHECKIN_KEY, 0);

        if(preference.getKey().equals(BusinessHourFragment.LUNCH_CHECKIN_KEY)){
            lunchCheckin = Long.parseLong(newValue.toString());
        } else if (preference.getKey().equals(BusinessHourFragment.EATING_TIME_KEY)) {
            eatingInterval = Long.parseLong(newValue.toString());
        }
        afterLunchCheckin = lunchCheckin + eatingInterval;
        sharedPreferences.edit().putLong(BusinessHourFragment.AFTER_LUNCH_CHECKIN_KEY, afterLunchCheckin).commit();
        return true;
    }
}
