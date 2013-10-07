package br.com.androidzin.pontopro.test.checkin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import br.com.androidzin.pontopro.MainActivity;

import com.jayway.android.robotium.solo.Solo;


public class CheckinLogicTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private Solo solo;
	private SharedPreferences sharedPreferences;
	
	private String workdayPrefFile = "pontopro.workday_file";
	private String workdayPrefID = "workday.ID";
	private String workdayPrefOpenCheckin  = "workday.openCheckin";
	private String workdayPrefWorkedTime = "workday.workedTime";
	private String workdayPrefDailyMark = "workday.DailyMark";
	private String workdayPrefCheckins ="workday.checkins";
	private String workdayPrefValid= "workday.valid";
	
	private final String checkinPrefID = "checkin.ID.";
	private final String checkinHour = "checkin.hour.";
	private final String checkinType = "checkin.type.";

	
	
	public CheckinLogicTest(){
		super(MainActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		populate();
	}
	
	public void populate(){
		SharedPreferences.Editor editor = sharedPreferences.edit(); 
		editor.putLong(workdayPrefID, 1);
		editor.putBoolean(workdayPrefOpenCheckin, true);
		editor.putInt(workdayPrefCheckins, 1);
		editor.putLong(workdayPrefWorkedTime, 0);
		editor.putInt(workdayPrefDailyMark, 0);
		//editor.putInt("checkin_id", 1);
		//editor.putInt("checkin_count", 1);
		//editor.putBoolean(checkinStatus, false);
		
		editor.commit();
	}
	
	/*public void testAssertWorkdayIsValid(){
		solo.clickOnText("Checkin");
		assertEquals(true, sharedPreferences.getBoolean(workdayPrefValid, false));
	}*/
	
	public void testAssertWorkdayInited() {
		solo.clickOnText("Checkin");
		assertTrue(sharedPreferences.getBoolean(workdayPrefOpenCheckin, false));
		assertEquals(0, sharedPreferences.getLong(workdayPrefWorkedTime, -1));
		assertEquals(0, sharedPreferences.getInt(workdayPrefDailyMark, -1));
		assertEquals(1, sharedPreferences.getInt(workdayPrefCheckins, -1));
	}
	
	public void testWorkdayValuesLoaded(){
		solo.goBack();
		//solo.sleep(5000);
		
		Intent intent = new Intent(getInstrumentation().getTargetContext(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getInstrumentation().getTargetContext().startActivity(intent);
		
		assertTrue(solo.waitForActivity("MainActivity", 5000));

		//assertTrue(sharedPreferences.getBoolean(workdayPrefOpenCheckin, false));
		assertEquals(0, sharedPreferences.getLong(workdayPrefWorkedTime, -1));
		assertEquals(0, sharedPreferences.getInt(workdayPrefDailyMark, -1));
		assertEquals(1, sharedPreferences.getInt(workdayPrefCheckins, -1));
	}
	
	/*public void testAssertCheckinInited(){
		solo.clickOnText("Checkin");
		//assertEquals(1, sharedPreferences.getInt(checkinPrefID, 0));
		//assertEquals(1, sharedPreferences.getInt(checkinCount, 0));
		assertEquals(true, sharedPreferences.getBoolean(checkinStatus, false));
	}
	
	/*public void testAssertListenerCalled(){
	}*/
	
	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
		sharedPreferences.edit().clear().commit();
	}

}
