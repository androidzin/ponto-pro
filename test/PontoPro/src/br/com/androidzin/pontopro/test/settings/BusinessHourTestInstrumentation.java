package br.com.androidzin.pontopro.test.settings;

import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.jayway.android.robotium.solo.Solo;

import br.com.androidzin.pontopro.R;
import br.com.androidzin.pontopro.settings.BusinessHourCommom;
import br.com.androidzin.pontopro.settings.SettingsActivity;
import br.com.androidzin.pontopro.util.Constants;

public class BusinessHourTestInstrumentation extends ActivityInstrumentationTestCase2<SettingsActivity>{

    private Solo solo;
    private SharedPreferences sharedPreferences;

    public BusinessHourTestInstrumentation() {
        super(SettingsActivity.class);
    }
    public BusinessHourTestInstrumentation(Class<SettingsActivity> activityClass) {
        super(SettingsActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
        sharedPreferences = getActivity().getPreferenceManager().getDefaultSharedPreferences(getActivity());
        populateSharedPreferences(sharedPreferences);
    }

    private void populateSharedPreferences(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(BusinessHourCommom.ENTERED_CHECKIN_KEY, Constants.hoursInMilis*8);
        editor.putLong(BusinessHourCommom.LUNCH_CHECKIN_KEY, Constants.hoursInMilis*12);
        editor.putLong(BusinessHourCommom.AFTER_LUNCH_CHECKIN_KEY, -100);
        editor.putLong(BusinessHourCommom.LEAVING_CHECKIN_KEY, Constants.hoursInMilis*17);

        editor.putBoolean(BusinessHourCommom.ENTERED_CHECKIN_ERROR, false);
        editor.putBoolean(BusinessHourCommom.LUNCH_CHECKIN_ERROR, false);
        editor.putBoolean(BusinessHourCommom.LEAVING_CHECKIN_ERROR, false);

        editor.putString(BusinessHourCommom.WORKING_TIME_KEY, getActivity().getString(R.string.eight_hour_value));
        editor.putString(BusinessHourCommom.EATING_TIME_KEY, getActivity().getString(R.string.one_and_half_hour_value));

        editor.apply();

    }
    /*
    public void testIncreaseEatingTimeInterval(){
        final SettingsActivity mActivity = getActivity();
        solo.clickOnText(mActivity.getString(R.string.pref_business_hour));
        solo.clickOnText(mActivity.getString(R.string.eating_time));
        solo.clickOnText(mActivity.getString(R.string.two_hours));

        solo.sleep(1000);

        Long lunchCheckin = sharedPreferences.getLong(BusinessHourCommom.LUNCH_CHECKIN_KEY, -1);
        Long afterLunchCheckin = sharedPreferences.getLong(BusinessHourCommom.AFTER_LUNCH_CHECKIN_KEY, -1);
        assertTrue(lunchCheckin != -1);
        assertTrue(afterLunchCheckin != -1);

        Long newInterval = Long.valueOf(mActivity.getString(R.string.two_hour_value));
        Long currentInterval = afterLunchCheckin - lunchCheckin;
        assertEquals(newInterval, currentInterval);

    }

    public void testDecreaseEatingTimeInterval(){
        final SettingsActivity mActivity = getActivity();
        solo.clickOnText(mActivity.getString(R.string.pref_business_hour));
        solo.clickOnText(mActivity.getString(R.string.eating_time));
        solo.clickOnText(mActivity.getString(R.string.one_hour));

        solo.sleep(1000);

        Long lunchCheckin = sharedPreferences.getLong(BusinessHourCommom.LUNCH_CHECKIN_KEY, -1);
        Long afterLunchCheckin = sharedPreferences.getLong(BusinessHourCommom.AFTER_LUNCH_CHECKIN_KEY, -1);
        assertTrue(lunchCheckin != -1);
        assertTrue(afterLunchCheckin != -1);

        Long newInterval = Long.valueOf(mActivity.getString(R.string.one_hour_value));
        Long currentInterval = afterLunchCheckin - lunchCheckin;
        assertEquals(newInterval, currentInterval);

    }
    */
    public void testInvalidEnteredCheckin(){
        final SettingsActivity mActivity = (SettingsActivity) solo.getCurrentActivity();
        solo.clickOnText(mActivity.getString(R.string.pref_business_hour));
        solo.clickOnText(mActivity.getString(R.string.entered_checkin));

        solo.setTimePicker(0, 16, 0);
        solo.clickOnText(mActivity.getString(android.R.string.ok));
        solo.sleep(1000);
        solo.goBack();
        assertEquals(true, solo.waitForText(mActivity.getString(R.string.generic_settings_error), 1, 3500));

        boolean hasError = sharedPreferences.getBoolean(BusinessHourCommom.ENTERED_CHECKIN_ERROR, false);
        assertEquals(true, hasError);

    }

    @Override
    protected void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
