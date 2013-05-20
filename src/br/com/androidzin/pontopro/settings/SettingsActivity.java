package br.com.androidzin.pontopro.settings;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import br.com.androidzin.pontopro.R;
import br.com.androidzin.pontopro.util.Constants;

import java.util.List;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceChangeListener{

	static final String ACTION_PREFS_NOTIFICATION = "br.com.androidzin.pontopro.settings.NOTIFICATION";
	static final String ACTION_PREFS_BUSSINESSHOUR = "br.com.androidzin.pontopro.settings.BUSSINER_HOUR";
    static final String PREFS_PREFIX = "prefs_";
    static final String WORKING_TIME_KEY = PREFS_PREFIX.concat("working_time");
    static final String EATING_TIME_KEY = PREFS_PREFIX.concat("eating_time");
    static final String LEAVING_CHECKIN_KEY = PREFS_PREFIX.concat("leaving_checkin");
    static final String AFTER_LUNCH_CHECKIN_KEY = PREFS_PREFIX.concat("after_lunch_checkin");
    static final String LUNCH_CHECKIN_KEY = PREFS_PREFIX.concat("lunch_checkin");
    static final String ENTERED_CHECKIN_KEY = PREFS_PREFIX.concat("entered_checkin");

    boolean business = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);



	    String action = getIntent().getAction();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
            if (action != null && action.equals(ACTION_PREFS_NOTIFICATION)) {
                addPreferencesFromResource(R.xml.notifications_prefs);
            } else if (action != null && action.equals(ACTION_PREFS_BUSSINESSHOUR)){
                addPreferencesFromResource(R.xml.business_hour_prefs);
                findPreference(LUNCH_CHECKIN_KEY).setOnPreferenceChangeListener(this);
                findPreference(AFTER_LUNCH_CHECKIN_KEY).setOnPreferenceChangeListener(this);
                business = true;
            } else {
                addPreferencesFromResource(R.xml.general_preferences_headers_legacy);
            }
        }

	}

    @Override
    protected void onResume() {
        super.onResume();
        if(business){
            findPreference(WORKING_TIME_KEY).setSummary(
                    getPreferenceManager().getSharedPreferences().getString(WORKING_TIME_KEY, ""));
            findPreference(EATING_TIME_KEY).setSummary(
                    getPreferenceManager().getSharedPreferences().getString(EATING_TIME_KEY, ""));
        }
    }

    // Called only on Honeycomb and later
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onBuildHeaders(List<Header> target) {
	   loadHeadersFromResource(R.xml.general_preferences_headers, target);
	}

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(business) {
            if(sharedPreferences.contains(key)){
                if(key.equals(WORKING_TIME_KEY) || key.equals(EATING_TIME_KEY)) {
                    String currentValue = sharedPreferences.getString(key, "");
                    Long value = Long.parseLong(currentValue)/ Constants.hoursInMilis;
                    findPreference(key).setSummary(value.toString().concat(" "+getString(R.string.hours)));
                }
            }
        }
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
            SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
            Long enteredCheckin = sharedPreferences.getLong(ENTERED_CHECKIN_KEY, 0);
            Long lunchCheckin = sharedPreferences.getLong(LUNCH_CHECKIN_KEY, 0);
            Long afterLunchCheckin = sharedPreferences.getLong(AFTER_LUNCH_CHECKIN_KEY, 0);
            Long leavingCheckin = sharedPreferences.getLong(LEAVING_CHECKIN_KEY, 0);
            Long eatingInterval = Long.parseLong(sharedPreferences.getString(EATING_TIME_KEY, "0"));

            if(preference.getKey().equals(LUNCH_CHECKIN_KEY)){
                afterLunchCheckin += eatingInterval;
                sharedPreferences.edit().putLong(AFTER_LUNCH_CHECKIN_KEY, afterLunchCheckin).commit();
                findPreference(AFTER_LUNCH_CHECKIN_KEY).setSummary(afterLunchCheckin.toString());
            } else if (preference.getKey().equals(AFTER_LUNCH_CHECKIN_KEY)) {
                lunchCheckin -= eatingInterval;
                sharedPreferences.edit().putLong(LUNCH_CHECKIN_KEY, lunchCheckin).commit();
            }
            return true;
    }
}
