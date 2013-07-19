package br.com.androidzin.pontopro.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.widget.Toast;

import br.com.androidzin.pontopro.R;


public class BusinessHourCommom {

    public static final String WORKING_TIME_KEY = SettingsActivity.PREFS_PREFIX.concat("working_time");
    public static final String EATING_TIME_KEY = SettingsActivity.PREFS_PREFIX.concat("eating_time");
    public static final String AFTER_LUNCH_CHECKIN_KEY = SettingsActivity.PREFS_PREFIX.concat("after_lunch_checkin");

    public static final String ERROR_SUFFIX = "-error";

    public static final String ENTERED_CHECKIN_KEY = SettingsActivity.PREFS_PREFIX.concat("entered_checkin");
    public static final String ENTERED_CHECKIN_ERROR = SettingsActivity.PREFS_PREFIX.concat("entered_checkin").concat(ERROR_SUFFIX);

    public static final String LUNCH_CHECKIN_KEY = SettingsActivity.PREFS_PREFIX.concat("lunch_checkin");
    public static final String LUNCH_CHECKIN_ERROR = SettingsActivity.PREFS_PREFIX.concat("lunch_checkin").concat(ERROR_SUFFIX);

    public static final String LEAVING_CHECKIN_KEY = SettingsActivity.PREFS_PREFIX.concat("leaving_checkin");
    public static final String LEAVING_CHECKIN_ERROR = SettingsActivity.PREFS_PREFIX.concat("leaving_checkin").concat(ERROR_SUFFIX);

    public static boolean verifyAndNotifyWorkingTimeViolation(SharedPreferences sharedPreferences, Context context, String key) {
        if(!key.equals(AFTER_LUNCH_CHECKIN_KEY))
        {
            Long workingTime = Long.parseLong(sharedPreferences.getString(WORKING_TIME_KEY, "0"));
            Long eatingInterval = Long.parseLong(sharedPreferences.getString(EATING_TIME_KEY, "0"));
            Long enteredCheckin = sharedPreferences.getLong(ENTERED_CHECKIN_KEY, 0);
            Long leavingCheckin = sharedPreferences.getLong(LEAVING_CHECKIN_KEY, 0);

            if(hasWorkingTimeViolation(enteredCheckin, leavingCheckin, workingTime, eatingInterval)){
                Toast.makeText(context, R.string.working_time_violation, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private static boolean hasWorkingTimeViolation(Long enteredCheckin, Long leavingCheckin,
                                                   Long workingTime, Long eatingInterval) {
        long workedHours = (leavingCheckin - enteredCheckin) - eatingInterval;
        return ((workedHours - workingTime) < 10000);
    }

    public static boolean verifyTimeSettings(Preference preference, Object newValue, SharedPreferences sharedPreferences) {
        Long enteredCheckin = sharedPreferences.getLong(ENTERED_CHECKIN_KEY, 0);
        Long lunchCheckin = sharedPreferences.getLong(LUNCH_CHECKIN_KEY, 0);
        Long leavingCheckin = sharedPreferences.getLong(LEAVING_CHECKIN_KEY, 0);
        Long newLongValue = Long.valueOf(newValue.toString());

        if ((preference.getKey().equals(ENTERED_CHECKIN_KEY) &&
                isValidEnteredTime(newLongValue, lunchCheckin,
                        leavingCheckin)) ||
            (preference.getKey().equals(LUNCH_CHECKIN_KEY) &&
                isValidLunchTime(newLongValue, enteredCheckin,
                        leavingCheckin)) ||
            (preference.getKey().equals(LEAVING_CHECKIN_KEY) &&
                isValidLeavingTime(newLongValue, enteredCheckin,
                    lunchCheckin)))
        {

            sharedPreferences.edit().putLong(preference.getKey(), newLongValue);
            if(preference.getKey().equals(LUNCH_CHECKIN_KEY) || preference.getKey().equals(EATING_TIME_KEY)){
                adjustAndSaveAfterLunchTime(preference, newValue, sharedPreferences);
            }
            return true;
        }
        return false;
    }
        
    private static boolean isValidEnteredTime(Long newValue,
    		Long lunchCheckin, Long leavingCheckin) {
    	
    	return newValue < lunchCheckin && newValue < leavingCheckin;
    }
    
    private static boolean isValidLunchTime(Long newValue,
    		Long enteredCheckin, Long leavingCheckin) {
    	return newValue > enteredCheckin && newValue < leavingCheckin;
    }
    
	private static boolean isValidLeavingTime(Long newValue,
			Long enteredCheckin, Long lunchCheckin) {
		return newValue > enteredCheckin && newValue > lunchCheckin;
	}

	private static boolean adjustAndSaveAfterLunchTime(Preference preference, Object newValue, SharedPreferences sharedPreferences) {
        Long eatingInterval = Long.parseLong(sharedPreferences.getString(EATING_TIME_KEY, "0"));
        Long lunchCheckin = sharedPreferences.getLong(LUNCH_CHECKIN_KEY, 0);

        if(preference.getKey().equals(LUNCH_CHECKIN_KEY)){
            lunchCheckin = Long.parseLong(newValue.toString());
        } else if (preference.getKey().equals(EATING_TIME_KEY)) {
            eatingInterval = Long.parseLong(newValue.toString());
        }

        return sharedPreferences.edit().putLong(AFTER_LUNCH_CHECKIN_KEY, getNewAfterLunchTime(eatingInterval, lunchCheckin)).commit();
    }

    private static Long getNewAfterLunchTime(Long eatingInterval, Long lunchCheckin) {
        return lunchCheckin + eatingInterval;
    }
}
