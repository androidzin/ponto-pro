package br.com.androidzin.pontopro.test.settings;

import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

import br.com.androidzin.pontopro.settings.BusinessHourCommom;
import br.com.androidzin.pontopro.settings.SettingsActivity;
import br.com.androidzin.pontopro.R;
import br.com.androidzin.pontopro.util.Constants;

public class BusinessHourTest extends ActivityInstrumentationTestCase2<SettingsActivity>{

    private Solo solo;
    private SharedPreferences sharedPreferences;

    public  BusinessHourTest() {
        super(SettingsActivity.class);
    }
    public BusinessHourTest(Class<SettingsActivity> activityClass) {
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
        editor.putString(BusinessHourCommom.WORKING_TIME_KEY, getActivity().getString(R.string.eight_number));
        editor.putString(BusinessHourCommom.EATING_TIME_KEY, getActivity().getString(R.string.one_and_half_hour_value));
        editor.apply();

    }

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

    @Override
    protected void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
