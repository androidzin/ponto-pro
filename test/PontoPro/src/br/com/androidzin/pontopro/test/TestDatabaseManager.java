package br.com.androidzin.pontopro.test;

import android.content.ContentValues;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import br.com.androidzin.pontopro.database.DatabaseManager;
import br.com.androidzin.pontopro.model.Checkin;
import br.com.androidzin.pontopro.model.Workday;

public class TestDatabaseManager extends AndroidTestCase {

	private DatabaseManager databaseManager;
	private long workdayID;
	private long checkinID;
	
	protected void setUp() throws Exception {
		super.setUp();
		RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
		databaseManager = new DatabaseManager(context);
		workdayID = databaseManager.addWorkday();
		checkinID = databaseManager.addCheckin(workdayID);
	}
	
	public void testInsertEmptyWorkday(){
		long id = databaseManager.addWorkday();
		assertEquals(2,id);
	}
	
	public void testInsertFullWorkday(){
		long id = databaseManager.addWorkday(10, 20);
		assertEquals(2,id);
	}
	
	public void testSelectWorkdayNotNull(){
		long id = databaseManager.addWorkday();
		Workday w = databaseManager.getWorkdayData(id);
		assertNotNull(w);
	}
	
	public void testSelectEmptyWorkday(){
		long id = databaseManager.addWorkday();
		Workday w = databaseManager.getWorkdayData(id);
		
		assertEquals(false, w.hasOpenCheckin());
		assertEquals(0, w.getDailyMark());
		assertEquals(0, w.getWorkedTime());
	}
	
	public void testSelectFullWorkday(){
		long id = databaseManager.addWorkday(30, 30);
		Workday w = databaseManager.getWorkdayData(id);
		
		assertEquals(false, w.hasOpenCheckin());
		assertEquals(30, w.getDailyMark());
		assertEquals(30, w.getWorkedTime());
	}
	
	public void testWorkdayValuesCreation(){
		try {
			ContentValues content = databaseManager.createWorkdayValues(-10, -10, false);
			assertEquals(-10, (int) content.getAsInteger(databaseManager.WORKDAY_DAILY_MARK));
			assertEquals(-10, (int) content.getAsInteger(databaseManager.WORKDAY_WORKED_HOURS));
			assertEquals(false, (content.getAsBoolean(databaseManager.WORKDAY_IS_CLOSED)).booleanValue());
		} catch (NumberFormatException e) {
			
		}
	}

	public void testInsertCheckin(){
		long id = databaseManager.addCheckin(workdayID);
		assertEquals(2, id);
	}
	
	public void testInsertCheckinInvalidWorkday(){
		long id = databaseManager.addCheckin(10);
		assertEquals(-1, id);
	}
	
	public void testSelectCheckinNotNull(){
		long id = databaseManager.addCheckin(workdayID);
		Checkin c = databaseManager.getCheckinData(id);
		assertNotNull(c);
	}
	
	public void testSelectCorrectCheckin(){
		long id = databaseManager.addCheckin(workdayID);
		Checkin c = databaseManager.getCheckinData(id);
		
		assertEquals(workdayID, c.getWorkdayID());
	}
	
	public void testSelectCheckinFromWorkdayNotNull(){
		Checkin c = databaseManager.getCheckinFromWorkday(workdayID);
		assertNotNull(c);
	}
	
	public void testSelectCheckinFrowWorkdayCorrectValues(){
		Checkin c = databaseManager.getCheckinFromWorkday(workdayID);
		assertEquals(workdayID, c.getWorkdayID());
		assertEquals(checkinID, c.getCheckinID());
	}
	
	public void tearDown() throws Exception {
		databaseManager.close();
		super.tearDown();
	}
}
