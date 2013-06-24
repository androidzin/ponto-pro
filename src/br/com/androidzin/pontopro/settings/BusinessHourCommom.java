package br.com.androidzin.pontopro.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.widget.Toast;

import br.com.androidzin.pontopro.R;


public class BusinessHourCommom {

    public static final String WORKING_TIME_KEY = SettingsActivity.PREFS_PREFIX.concat("working_time");
    public static final String EATING_TIME_KEY = SettingsActivity.PREFS_PREFIX.concat("eating_time");
    public static final String LEAVING_CHECKIN_KEY = SettingsActivity.PREFS_PREFIX.concat("leaving_checkin");
    public static final String AFTER_LUNCH_CHECKIN_KEY = SettingsActivity.PREFS_PREFIX.concat("after_lunch_checkin");
    public static final String LUNCH_CHECKIN_KEY = SettingsActivity.PREFS_PREFIX.concat("lunch_checkin");
    public static final String ENTERED_CHECKIN_KEY = SettingsActivity.PREFS_PREFIX.concat("entered_checkin");

    public static void verifyAndNotifyWorkingTimeViolation(SharedPreferences sharedPreferences, Context context, String key) {
        if(!key.equals(AFTER_LUNCH_CHECKIN_KEY))
        {
            Long workingTime = Long.parseLong(sharedPreferences.getString(WORKING_TIME_KEY, "0"));
            Long eatingInterval = Long.parseLong(sharedPreferences.getString(EATING_TIME_KEY, "0"));
            Long enteredCheckin = sharedPreferences.getLong(ENTERED_CHECKIN_KEY, 0);
            Long leavingCheckin = sharedPreferences.getLong(LEAVING_CHECKIN_KEY, 0);

            if(verifyWorkingTimeViolation(enteredCheckin, leavingCheckin, workingTime, eatingInterval)){
                Toast.makeText(context, R.string.working_time_violation, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static boolean verifyWorkingTimeViolation(Long enteredCheckin, Long leavingCheckin,
                                                      Long workingTime, Long eatingInterval) {
        return ((leavingCheckin - enteredCheckin) - eatingInterval) - workingTime < 10000;
    }

    public static boolean adjustAfterLunchTime(Preference preference, Object newValue, SharedPreferences sharedPreferences) {
        Long eatingInterval = Long.parseLong(sharedPreferences.getString(EATING_TIME_KEY, "0"));
        Long lunchCheckin = sharedPreferences.getLong(LUNCH_CHECKIN_KEY, 0);

        if(preference.getKey().equals(LUNCH_CHECKIN_KEY)){
            lunchCheckin = Long.parseLong(newValue.toString());
        } else if (preference.getKey().equals(EATING_TIME_KEY)) {
            eatingInterval = Long.parseLong(newValue.toString());
        }
        Long afterLunchCheckin = lunchCheckin + eatingInterval;
        return sharedPreferences.edit().putLong(AFTER_LUNCH_CHECKIN_KEY, afterLunchCheckin).commit();
    }
}
