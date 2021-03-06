package br.com.androidzin.pontopro.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.GenericSignatureFormatError;

import br.com.androidzin.pontopro.R;
import br.com.androidzin.pontopro.util.Constants;


public class BusinessHourCommom {

    public static final String WORKING_TIME_KEY = SettingsActivity.PREFS_PREFIX.concat("working_time");
    public static final String EATING_TIME_KEY = SettingsActivity.PREFS_PREFIX.concat("eating_time");
    public static final String AFTER_LUNCH_CHECKIN_KEY = SettingsActivity.PREFS_PREFIX.concat("after_lunch_checkin");

    public static final String ERROR_SUFFIX = "_error";

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

    public static boolean hasWorkingTimeViolation(Long enteredCheckin, Long leavingCheckin,
                                                   Long workingTime, Long eatingInterval) {
        long workedHours = (leavingCheckin - enteredCheckin) - eatingInterval;
        return (workingTime - workedHours > 10000);
    }

    public static boolean verifyTimeSettings(Preference preference, Object newValue, SharedPreferences sharedPreferences) {
        boolean shouldUpdate = verifyCheckinsTime(preference, newValue, sharedPreferences);
        if(shouldUpdate) {
            if(preference.getKey().equals(LUNCH_CHECKIN_KEY) || preference.getKey().equals(EATING_TIME_KEY)) {
                adjustAndSaveAfterLunchTime(preference, newValue, sharedPreferences);
            }
        }
        return shouldUpdate;
    }

    private static boolean verifyCheckinsTime(Preference preference, Object newValue, SharedPreferences sharedPreferences) {
        Long enteredCheckin = sharedPreferences.getLong(ENTERED_CHECKIN_KEY, 0);
        Long lunchCheckin = sharedPreferences.getLong(LUNCH_CHECKIN_KEY, 0);
        Long leavingCheckin = sharedPreferences.getLong(LEAVING_CHECKIN_KEY, 0);
        Long newLongValue = Long.valueOf(newValue.toString());

        return isNewCheckinValid(preference.getKey(), enteredCheckin, lunchCheckin, leavingCheckin, newLongValue);
    }

    public static boolean isNewCheckinValid(String preferenceKey, Long enteredCheckin, Long lunchCheckin, Long leavingCheckin, Long newLongValue) {
        if (preferenceKey.equals(ENTERED_CHECKIN_KEY)){
            return isValidEnteredTime(newLongValue, lunchCheckin,
                    leavingCheckin);
        } else if(preferenceKey.equals(LUNCH_CHECKIN_KEY)){
            return isValidLunchTime(newLongValue, enteredCheckin,
                    leavingCheckin);
        } else if(preferenceKey.equals(LEAVING_CHECKIN_KEY)){
            return isValidLeavingTime(newLongValue, enteredCheckin,
                                lunchCheckin);
        }
        return true;
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
