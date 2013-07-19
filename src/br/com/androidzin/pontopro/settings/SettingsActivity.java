package br.com.androidzin.pontopro.settings;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

import br.com.androidzin.pontopro.R;
import static br.com.androidzin.pontopro.settings.BusinessHourCommom.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceChangeListener{

    static final String ACTION_PREFS_NOTIFICATION = "br.com.androidzin.pontopro.settings.NOTIFICATION";
	static final String ACTION_PREFS_BUSSINESSHOUR = "br.com.androidzin.pontopro.settings.BUSSINER_HOUR";
    static final String PREFS_PREFIX = "pref_";
    static final String PREF_KEY_NOTIFICATION_ENABLED_KEY = PREFS_PREFIX.concat("key_notification_enabled");
    static final String PREF_CATEGORY_BUSINESS_KEY = PREFS_PREFIX.concat("category_business");
    private HashMap<String, Boolean> errors;
    private int attempts = 0;

    @Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    String action = getIntent().getAction();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            if (action != null && action.equals(ACTION_PREFS_NOTIFICATION)) {
                PreferenceManager.setDefaultValues(this, R.xml.notifications_prefs, false);
                addPreferencesFromResource(R.xml.notifications_prefs);
            } else if (action != null && action.equals(ACTION_PREFS_BUSSINESSHOUR)){
                addPreferencesFromResource(R.xml.business_hour_prefs);
                PreferenceManager.setDefaultValues(this, R.xml.business_hour_prefs, false);
                findPreference(ENTERED_CHECKIN_KEY).setOnPreferenceChangeListener(this);
                findPreference(LUNCH_CHECKIN_KEY).setOnPreferenceChangeListener(this);
                findPreference(LEAVING_CHECKIN_KEY).setOnPreferenceChangeListener(this);
                findPreference(EATING_TIME_KEY).setOnPreferenceChangeListener(this);
                errors = new HashMap<String, Boolean>();
            } else {
                addPreferencesFromResource(R.xml.general_preferences_headers_legacy);
            }
        }

        errors = new HashMap<String, Boolean>();

	}

    @Override
    protected void onResume() {
        super.onResume();
        if(findPreference(WORKING_TIME_KEY) != null || findPreference(PREF_KEY_NOTIFICATION_ENABLED_KEY) != null)
        {
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(findPreference(WORKING_TIME_KEY) != null || findPreference(PREF_KEY_NOTIFICATION_ENABLED_KEY) != null)
        {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
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
        if(findPreference(PREF_CATEGORY_BUSINESS_KEY) != null)
        {
            verifyAndNotifyWorkingTimeViolation(sharedPreferences, this, key);
        }

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        boolean result = verifyTimeSettings(preference, newValue, sharedPreferences);
        setHasError(sharedPreferences, preference, !result);
        return  result;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

   void setHasError(SharedPreferences sharedPreferences, Preference preference, Boolean hasError) {
       errors.put(preference.getKey(), hasError);
       sharedPreferences.edit().putBoolean(preference.getKey().concat(BusinessHourCommom.ERROR_SUFFIX), hasError);
    }

    boolean hasErrors(){
        Iterator<String> keys = errors.keySet().iterator();
        while(keys.hasNext()){
            String key = keys.next();
            if(errors.get(key) == true){
                return true;
            }
        }
        return false;
    }

    private void saveErrors() {

    }


    public int getAttempts() {
        return  attempts;
    }

    @Override
    public void onBackPressed() {
        if(hasErrors() && attempts == 0){
            Toast.makeText(this, R.string.generic_settings_error, Toast.LENGTH_LONG).show();
            attempts++;
        } else {
            super.onBackPressed();
        }
    }
}
