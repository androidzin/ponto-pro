package br.com.androidzin.pontopro.test;

import android.test.ActivityInstrumentationTestCase2;
import br.com.androidzin.pontopro.MainActivity;
import br.com.androidzin.pontopro.R;

import com.jayway.android.robotium.solo.Solo;


public class CheckinLogicTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private Solo solo;
	
	public CheckinLogicTest(){
		super(MainActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void testCheckinFragmentButton() throws InterruptedException{
		solo.clickOnText("Checkin");
		Thread.sleep(1000);
		assertTrue(solo.waitForText("text"));
	}
	
	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

}
