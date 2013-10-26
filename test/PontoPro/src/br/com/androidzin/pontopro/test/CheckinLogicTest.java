package br.com.androidzin.pontopro.test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;
import br.com.androidzin.pontopro.MainActivity;
import br.com.androidzin.pontopro.R;

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
	
	private final String checkinPrefID = "checkin.ID";
	private final String checkinStatus = "checkin.status";
	private final String checkinCount = "checkin.count";
	private final String checkinHour = "checkin.hour";

	
	
	public CheckinLogicTest(){
		super(MainActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
		sharedPreferences = getInstrumentation().getTargetContext().getSharedPreferences(workdayPrefFile, Context.MODE_PRIVATE);
		
	}
	
	public void populate(){
		SharedPreferences.Editor editor = sharedPreferences.edit(); 
		editor.putLong(workdayPrefID, 1);
		editor.putInt("checkin_id", 1);
		//editor.putInt("checkin_count", 1);
		editor.putBoolean(checkinStatus, false);
		
		editor.commit();
	}
	
	public void testAssertWorkdayIsValid(){
		solo.clickOnText("Checkin");
		assertEquals(true, sharedPreferences.getBoolean(workdayPrefValid, false));
	}
	
	public void testAssertWorkdayInited() {
		solo.clickOnText("Checkin");
		assertEquals(true, sharedPreferences.getBoolean(workdayPrefOpenCheckin, true));
		assertEquals(0, sharedPreferences.getInt(workdayPrefWorkedTime, -1));
		assertEquals(0, sharedPreferences.getInt(workdayPrefDailyMark, -1));
		assertEquals(1, sharedPreferences.getInt(workdayPrefCheckins, -1));
	}
	
	public void testWorkdayValuesLoaded(){
		solo.goBack();
			
		Intent intent = new Intent(getInstrumentation().getTargetContext(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getInstrumentation().getTargetContext().startActivity(intent);
		
		assertEquals(true, sharedPreferences.getBoolean(workdayPrefOpenCheckin, false));
		assertEquals(0, sharedPreferences.getInt(workdayPrefWorkedTime, -1));
		assertEquals(0, sharedPreferences.getInt(workdayPrefDailyMark, -1));
		assertEquals(0, sharedPreferences.getInt(workdayPrefCheckins, -1));
	}
	
	public void testAssertCheckinInited(){
		solo.clickOnText(solo.getString(R.id.doCheckin));
		//assertEquals(1, sharedPreferences.getInt(checkinPrefID, 0));
		assertEquals(1, sharedPreferences.getInt(checkinCount, 0));
		assertEquals(true, sharedPreferences.getBoolean(checkinStatus, false));
	}
	
	/*public void testAssertListenerCalled(){
	}*/
	
	
	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

}
