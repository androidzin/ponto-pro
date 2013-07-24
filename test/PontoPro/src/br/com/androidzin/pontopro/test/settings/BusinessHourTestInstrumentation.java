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

    private static final int enteredInitial = Constants.hoursInMilis * 8;
    private static final int lunchInitial = Constants.hoursInMilis * 12;
    private static final int leavingInitial = Constants.hoursInMilis * 17;

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

        editor.putLong(BusinessHourCommom.ENTERED_CHECKIN_KEY, enteredInitial);
        editor.putLong(BusinessHourCommom.LUNCH_CHECKIN_KEY, lunchInitial);
        editor.putLong(BusinessHourCommom.AFTER_LUNCH_CHECKIN_KEY, -100);
        editor.putLong(BusinessHourCommom.LEAVING_CHECKIN_KEY, leavingInitial);

        editor.putBoolean(BusinessHourCommom.ENTERED_CHECKIN_ERROR, false);
        editor.putBoolean(BusinessHourCommom.LUNCH_CHECKIN_ERROR, false);
        editor.putBoolean(BusinessHourCommom.LEAVING_CHECKIN_ERROR, false);

        editor.putString(BusinessHourCommom.WORKING_TIME_KEY, getActivity().getString(R.string.eight_hour_value));
        editor.putString(BusinessHourCommom.EATING_TIME_KEY, getActivity().getString(R.string.one_and_half_hour_value));

        editor.apply();

    }

    public void testIncreaseEatingTimeInterval(){

        solo.clickOnText(solo.getString(R.string.pref_business_hour));
        solo.clickOnText(solo.getString(R.string.eating_time));
        solo.clickOnText(solo.getString(R.string.two_hours));

        solo.sleep(1000);

        Long lunchCheckin = sharedPreferences.getLong(BusinessHourCommom.LUNCH_CHECKIN_KEY, -1);
        Long afterLunchCheckin = sharedPreferences.getLong(BusinessHourCommom.AFTER_LUNCH_CHECKIN_KEY, -1);
        assertTrue(lunchCheckin != -1);
        assertTrue(afterLunchCheckin != -1);

        Long newInterval = Long.valueOf(solo.getString(R.string.two_hour_value));
        Long currentInterval = afterLunchCheckin - lunchCheckin;
        assertEquals(newInterval, currentInterval);

    }

    public void testDecreaseEatingTimeInterval(){
        solo.clickOnText(solo.getString(R.string.pref_business_hour));
        solo.clickOnText(solo.getString(R.string.eating_time));
        solo.clickOnText(solo.getString(R.string.one_hour));

        solo.sleep(1000);

        Long lunchCheckin = sharedPreferences.getLong(BusinessHourCommom.LUNCH_CHECKIN_KEY, -1);
        Long afterLunchCheckin = sharedPreferences.getLong(BusinessHourCommom.AFTER_LUNCH_CHECKIN_KEY, -1);
        assertTrue(lunchCheckin != -1);
        assertTrue(afterLunchCheckin != -1);

        Long newInterval = Long.valueOf(solo.getString(R.string.one_hour_value));
        Long currentInterval = afterLunchCheckin - lunchCheckin;
        assertEquals(newInterval, currentInterval);

    }

    private void setCheckin(String checkinLabel, int hour, int minutes, int match) {
        solo.clickOnText(checkinLabel, match);
        solo.setTimePicker(0, hour, minutes);
        solo.clickOnText(solo.getString(android.R.string.ok));
    }

    private void setEnteredCheckin(int hour, int minutes) {
        setCheckin(solo.getString(R.string.entered_checkin), hour, minutes, 1);
    }

    private void setLunchCheckin(int hour, int minutes) {
        setCheckin(solo.getString(R.string.lunch_checkin), hour, minutes, 1);
    }

    private void setLeavingCheckin(int hour, int minutes) {
        setCheckin(solo.getString(R.string.leaving_checkin), hour, minutes, 2);
    }

    public void testInvalidEnteredCheckin(){
        solo.clickOnText(solo.getString(R.string.pref_business_hour));
        setLunchCheckin(12, 0);
        setLeavingCheckin(17, 0);
        setEnteredCheckin(13, 0);

        solo.sleep(1000);
        solo.goBack();
        assertEquals(true, solo.waitForText(solo.getString(R.string.generic_settings_error), 1, 3500));

        boolean hasError = sharedPreferences.getBoolean(BusinessHourCommom.ENTERED_CHECKIN_ERROR, false);
        assertEquals(true, hasError);

    }


    public void testInvalidLunchCheckin(){
        solo.clickOnText(solo.getString(R.string.pref_business_hour));
        setEnteredCheckin(8, 0);
        setLeavingCheckin(17, 0);
        setLunchCheckin(18, 0);

        solo.sleep(1000);
        solo.goBack();
        assertEquals(true, solo.waitForText(solo.getString(R.string.generic_settings_error), 1, 3500));

        boolean hasError = sharedPreferences.getBoolean(BusinessHourCommom.LUNCH_CHECKIN_ERROR, false);
        assertEquals(true, hasError);

    }

    public void testInvalidLeavingCheckin(){
        solo.clickOnText(solo.getString(R.string.pref_business_hour));
        solo.clickOnText(solo.getString(R.string.eating_time));
        solo.clickOnText(solo.getString(R.string.one_hour));
        setEnteredCheckin(8, 0);
        setLunchCheckin(12, 0);
        setLeavingCheckin(9, 0);

        solo.sleep(1000);
        solo.goBack();
        assertEquals(true, solo.waitForText(solo.getString(R.string.generic_settings_error), 1, 3500));

        boolean hasError = sharedPreferences.getBoolean(BusinessHourCommom.LEAVING_CHECKIN_ERROR, false);
        assertEquals(true, hasError);

    }



    public void testInvalidCheckinFixed(){
        solo.clickOnText(solo.getString(R.string.pref_business_hour));
        setLunchCheckin(12, 0);
        setLeavingCheckin(17, 0);
        setEnteredCheckin(13, 0);

        solo.sleep(1000);

        setEnteredCheckin(8, 0);

        solo.goBack();
        assertEquals(false, solo.waitForText(solo.getString(R.string.generic_settings_error), 1, 3500));

        boolean hasError = sharedPreferences.getBoolean(BusinessHourCommom.ENTERED_CHECKIN_ERROR, true);
        assertEquals(false, hasError);

    }

    public void testValidCheckin(){
        solo.clickOnText(solo.getString(R.string.pref_business_hour));
        solo.clickOnText(solo.getString(R.string.eating_time));
        solo.clickOnText(solo.getString(R.string.one_hour));
        setEnteredCheckin(8, 0);
        setLunchCheckin(12, 0);
        setLeavingCheckin(17, 0);

        assertEquals(false, solo.waitForText(solo.getString(R.string.working_time_violation), 1, 3500));
        solo.goBack();
        assertEquals(false, solo.waitForText(solo.getString(R.string.generic_settings_error), 1, 3500));

    }

    public void testWorkingTimeViolation(){
        solo.clickOnText(solo.getString(R.string.pref_business_hour));
        solo.clickOnText(solo.getString(R.string.eating_time));
        solo.clickOnText(solo.getString(R.string.two_hours));
        setEnteredCheckin(8, 0);
        setLunchCheckin(12, 0);
        setLeavingCheckin(17, 0);

        assertEquals(true, solo.waitForText(solo.getString(R.string.working_time_violation), 1, 3500));
        solo.goBack();
        assertEquals(false, solo.waitForText(solo.getString(R.string.generic_settings_error), 1, 3500));

    }


    @Override
    protected void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
