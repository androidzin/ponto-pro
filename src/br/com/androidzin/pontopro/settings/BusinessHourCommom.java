package br.com.androidzin.pontopro.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.widget.Toast;

import br.com.androidzin.pontopro.R;

public class BusinessHourCommom {

    static final String WORKING_TIME_KEY = SettingsActivity.PREFS_PREFIX.concat("working_time");
    static final String EATING_TIME_KEY = SettingsActivity.PREFS_PREFIX.concat("eating_time");
    static final String LEAVING_CHECKIN_KEY = SettingsActivity.PREFS_PREFIX.concat("leaving_checkin");
    static final String AFTER_LUNCH_CHECKIN_KEY = SettingsActivity.PREFS_PREFIX.concat("after_lunch_checkin");
    static final String LUNCH_CHECKIN_KEY = SettingsActivity.PREFS_PREFIX.concat("lunch_checkin");
    static final String ENTERED_CHECKIN_KEY = SettingsActivity.PREFS_PREFIX.concat("entered_checkin");

    public static void verifyAndNotifyWorkingTimeViolation(SharedPreferences sharedPreferences, Context context, String key) {
        if(!key.equals(AFTER_LUNCH_CHECKIN_KEY))
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
        Long eatingInterval = Long.parseLong(sharedPreferences.getString(EATING_TIME_KEY, "0"));
        Long lunchCheckin = sharedPreferences.getLong(LUNCH_CHECKIN_KEY, 0);
        Long afterLunchCheckin = sharedPreferences.getLong(AFTER_LUNCH_CHECKIN_KEY, 0);

        if(preference.getKey().equals(LUNCH_CHECKIN_KEY)){
            lunchCheckin = Long.parseLong(newValue.toString());
        } else if (preference.getKey().equals(EATING_TIME_KEY)) {
            eatingInterval = Long.parseLong(newValue.toString());
        }
        afterLunchCheckin = lunchCheckin + eatingInterval;
        sharedPreferences.edit().putLong(AFTER_LUNCH_CHECKIN_KEY, afterLunchCheckin).commit();
        return true;
    }
}
