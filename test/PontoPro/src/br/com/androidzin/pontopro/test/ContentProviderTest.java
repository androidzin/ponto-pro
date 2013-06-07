package br.com.androidzin.pontopro.test;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import br.com.androidzin.pontopro.data.provider.PontoProContentProvider;
import br.com.androidzin.pontopro.data.provider.PontoProContract;

public class ContentProviderTest extends
		ProviderTestCase2<PontoProContentProvider> {

	// A URI that the provider does not offer, for testing error handling.
	private static final Uri INVALID_URI = Uri.withAppendedPath(
			PontoProContract.CONTENT_URI, "invalid");
	// Contains a reference to the mocked content resolver for the provider
	// under test.
	private MockContentResolver mMockResolver;

	// Contains an SQLite database, used as test data
	private SQLiteDatabase databaseManager;

	private long workdayID;
	private long checkinID;

	public ContentProviderTest(Class<PontoProContentProvider> providerClass,
			String providerAuthority) {
		super(providerClass, providerAuthority);
	}

	public ContentProviderTest() {
		super(PontoProContentProvider.class, PontoProContract.AUTHORITY);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		mMockResolver = getMockContentResolver();
		databaseManager = getProvider().getHelperToTest().getWritableDatabase();
		insertWorkdayData();
	}

	public void insertWorkdayData() {
		workdayID = databaseManager.insert(PontoProContract.WORKDAY_TABLE,
				null, PontoProContract.createWorkdayValues(0, 0, false));
		databaseManager.insert(PontoProContract.WORKDAY_TABLE, null,
				PontoProContract.createWorkdayValues(0, 0, false));
		databaseManager.insert(PontoProContract.WORKDAY_TABLE, null,
				PontoProContract.createWorkdayValues(0, 0, false));
		databaseManager.insert(PontoProContract.WORKDAY_TABLE, null,
				PontoProContract.createWorkdayValues(0, 0, false));
		checkinID = databaseManager.insert(PontoProContract.CHECKINS_TABLE,
				null, PontoProContract.createCheckinValues(workdayID));
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testWorkdayInsertion() {
		ContentValues values = PontoProContract
				.createWorkdayValues(0, 0, false);
		Uri row = mMockResolver.insert(Uri.withAppendedPath(
				PontoProContract.CONTENT_URI, "workday/insert"), values);

		long id = ContentUris.parseId(row);

		// Does a full query on the table. Since insertData() hasn't yet been
		// called, the
		// table should only contain the record just inserted.
		Cursor cursor = mMockResolver.query(Uri.withAppendedPath(
				PontoProContract.CONTENT_URI, "workday/all"), // the main table
																// URI
				null, // no projection, return all the columns
				null, // no selection criteria, return all the rows in the model
				null, // no selection arguments
				null // default sort order
				);

		// We have now 5 records
		assertEquals(5, cursor.getCount());
		// Moves to the first (and only) record in the cursor and asserts that
		// this worked.
		assertTrue(cursor.moveToFirst());

		int idIndex = cursor.getColumnIndex(PontoProContract.WORKDAY_ID);
		int workedHoursIndex = cursor
				.getColumnIndex(PontoProContract.WORKDAY_WORKED_HOURS);
		int isClosedIndex = cursor
				.getColumnIndex(PontoProContract.WORKDAY_IS_CLOSED);
		int dailyIndex = cursor
				.getColumnIndex(PontoProContract.WORKDAY_DAILY_MARK);

		assertEquals(0, cursor.getInt(isClosedIndex));
		assertEquals(0, cursor.getInt(dailyIndex));
		assertEquals(0, cursor.getInt(workedHoursIndex));
		assertEquals(1, cursor.getLong(idIndex));

	}

	public void testWorkdayDeleteAll() {
		int deletions = mMockResolver.delete(Uri.withAppendedPath(
				PontoProContract.CONTENT_URI, "workday/all"), // the main table
																// URI
				null, null);

		// There was 4 record inserted before in setUp()
		assertEquals(4, deletions);
	}

	public void testWorkdayDeleteID() {
		int deletions = mMockResolver.delete(
				Uri.withAppendedPath(PontoProContract.CONTENT_URI, "workday/"
						+ workdayID), // the main table URI
				null, null);

		assertEquals(1, deletions);
	}

	public void testWorkdayDeleteWhere() {
		int deletions = mMockResolver.delete(
				Uri.withAppendedPath(PontoProContract.CONTENT_URI, "workday/"
						+ workdayID), // the main table URI
				PontoProContract.WORKDAY_WORKED_HOURS + " = ?",
				new String[] { "0" });

		// There was 1 record inserted before in setUp() matching the where
		// conditions
		assertEquals(1, deletions);
	}

	public void testWorkdayQueryID() {
		Cursor cursor = mMockResolver.query(
				Uri.withAppendedPath(PontoProContract.CONTENT_URI, "workday/"+ workdayID), 
				null, 
				null, 
				null, 
				null);

		assertNotNull(cursor);
		assertEquals(1, cursor.getCount());
		assertTrue(cursor.moveToFirst());
	}

	public void testWorkdayQueryAll() {
		Cursor cursor = mMockResolver.query(
				Uri.withAppendedPath(PontoProContract.CONTENT_URI, "workday/all"), 
				null, 
				null, 
				null,
				null);

		assertNotNull(cursor);
		assertEquals(4, cursor.getCount());
		assertTrue(cursor.moveToFirst());
	}

	public void testCheckinQueryAllFromWorkday() {
		Cursor cursor = mMockResolver.query(
				Uri.withAppendedPath(PontoProContract.CONTENT_URI, "workday/checkin/" + workdayID),
				null, 
				null, 
				null, 
				null);

		int idIndex = cursor.getColumnIndex(PontoProContract.CHECKINS_ID);
		int checkinWorkdayIDIndex = cursor.getColumnIndex(PontoProContract.CHECKINS_WORKDAY_ID);

		assertNotNull(cursor);
		assertEquals(1, cursor.getCount());
		assertTrue(cursor.moveToFirst());

		assertEquals(1, cursor.getLong(idIndex));
		assertEquals(1, cursor.getInt(checkinWorkdayIDIndex));
	}

	public void testWorkdayUpdateID() {
		ContentValues values = PontoProContract.createWorkdayValues(5, 8, true);
		int updated = mMockResolver.update(
				Uri.withAppendedPath(PontoProContract.CONTENT_URI, "workday/"+ workdayID), 
				values, 
				null, 
				null);

		assertEquals(1, updated);

		Cursor cursor = mMockResolver.query(
				Uri.withAppendedPath(PontoProContract.CONTENT_URI, "workday/"+ workdayID), 
				null, 
				null, 
				null, 
				null);

		assertNotNull(cursor);
		assertEquals(1, cursor.getCount());
		assertTrue(cursor.moveToFirst());

		int isClosedIndex = cursor.getColumnIndex(PontoProContract.WORKDAY_IS_CLOSED);
		int workedHoursIndex = cursor.getColumnIndex(PontoProContract.WORKDAY_WORKED_HOURS);
		int dailyMarkIndex = cursor.getColumnIndex(PontoProContract.WORKDAY_DAILY_MARK);

		assertEquals(5, cursor.getInt(dailyMarkIndex));
		assertEquals(8, cursor.getInt(workedHoursIndex));
		assertEquals(1, cursor.getInt(isClosedIndex));
	}

	public void testWorkdayUpdateWhere() {
		ContentValues values = PontoProContract.createWorkdayValues(0, 8, true);
		int updated = mMockResolver.update(
				Uri.withAppendedPath(PontoProContract.CONTENT_URI, "workday/"+ workdayID), 
				values,
				PontoProContract.WORKDAY_WORKED_HOURS + " = ?",
				new String[] { "0" });

		assertEquals(1, updated);

		Cursor cursor = mMockResolver.query(
				Uri.withAppendedPath(PontoProContract.CONTENT_URI, "workday/"+ workdayID), 
				null, 
				null, 
				null, 
				null);

		assertNotNull(cursor);
		assertEquals(1, cursor.getCount());
		assertTrue(cursor.moveToFirst());

		int isClosedIndex = cursor.getColumnIndex(PontoProContract.WORKDAY_IS_CLOSED);
		int workedHoursIndex = cursor.getColumnIndex(PontoProContract.WORKDAY_WORKED_HOURS);
		int dailyMarkIndex = cursor.getColumnIndex(PontoProContract.WORKDAY_DAILY_MARK);

		assertEquals(0, cursor.getInt(dailyMarkIndex));
		assertEquals(8, cursor.getInt(workedHoursIndex));
		assertEquals(1, cursor.getInt(isClosedIndex));
	}

	public void testCheckinInsertion() {
		ContentValues checkinValues = PontoProContract
				.createCheckinValues(workdayID);
		Uri rowCheckin = mMockResolver.insert(Uri.withAppendedPath(
				PontoProContract.CONTENT_URI, "checkin/insert"), checkinValues);

		long id = ContentUris.parseId(rowCheckin);
		// Does a full query on the table. Since insertData() hasn't yet been
		// called, the
		// table should only contain the record just inserted.
		Cursor cursor = mMockResolver.query(Uri.withAppendedPath(
				PontoProContract.CONTENT_URI, "checkin/all"), // the main table
																// URI
				null, // no projection, return all the columns
				null, // no selection criteria, return all the rows in the model
				null, // no selection arguments
				null // default sort order
				);

		// Asserts that there should be 2 record.
		assertEquals(2, cursor.getCount());
		// Moves to the first (and only) record in the cursor and asserts that
		// this worked.
		assertTrue(cursor.moveToFirst());

		int idIndex = cursor.getColumnIndex(PontoProContract.CHECKINS_ID);
		int workdayIndex = cursor.getColumnIndex(PontoProContract.CHECKINS_WORKDAY_ID);

		assertEquals(1, cursor.getInt(workdayIndex));
		assertEquals(1, cursor.getLong(idIndex));
	}

	public void testCheckinDeleteID() {
		int deletions = mMockResolver.delete(
				Uri.withAppendedPath(PontoProContract.CONTENT_URI, "checkin/"+ checkinID), // the main table URI
				null,
				null);

		// There was 1 record inserted before in setUp()
		assertEquals(1, deletions);
	}

	public void testCheckinDeleteAll() {
		int deletions = mMockResolver.delete(
				Uri.withAppendedPath(PontoProContract.CONTENT_URI, "checkin/all"), // the main table URI
				null, 
				null);

		// There was 1 record inserted before in setUp()
		assertEquals(1, deletions);
	}

	public void testCheckinDeleteWhere() {
		int deletions = mMockResolver.delete(
				Uri.withAppendedPath(PontoProContract.CONTENT_URI, "checkin/"
						+ workdayID), // the main table URI
				PontoProContract.CHECKINS_WORKDAY_ID + " = ? ",
				new String[] { String.valueOf(workdayID) });

		// There was 1 record inserted before in setUp()
		assertEquals(1, deletions);
	}

	public void testCheckinQueryID() {
		Cursor cursor = mMockResolver.query(Uri.withAppendedPath(
				PontoProContract.CONTENT_URI, "checkin/" + checkinID), // the main table URI
				null, // no projection, return all the columns
				null, // no selection criteria, return all the rows in the model
				null, // no selection arguments
				null // default sort order
				);

		assertNotNull(cursor);
		assertEquals(1, cursor.getCount());
		assertTrue(cursor.moveToFirst());
	}

	public void testCheckinQueryAll() {
		Cursor cursor = mMockResolver.query(Uri.withAppendedPath(
				PontoProContract.CONTENT_URI, "checkin/all"), // the main table URI
				null, // no projection, return all the columns
				null, // no selection criteria, return all the rows in the model
				null, // no selection arguments
				null // default sort order
				);

		assertNotNull(cursor);
		assertEquals(1, cursor.getCount());
		assertTrue(cursor.moveToFirst());
	}

	public void testCheckinUpdateID() {
		ContentValues values = new ContentValues();
		values.put(PontoProContract.CHECKINS_WORKDAY_ID, 2);
		int updated = mMockResolver.update(
				Uri.withAppendedPath(PontoProContract.CONTENT_URI, "checkin/"+ checkinID),
				values,
				null,
				null);

		assertEquals(1, updated);

		Cursor cursor = mMockResolver.query(
				Uri.withAppendedPath(PontoProContract.CONTENT_URI, "checkin/"+ checkinID), 
				null, 
				null, 
				null, 
				null);

		assertNotNull(cursor);
		assertEquals(1, cursor.getCount());
		assertTrue(cursor.moveToFirst());

		int workdayIndex = cursor.getColumnIndex(PontoProContract.CHECKINS_WORKDAY_ID);

		assertEquals(2, cursor.getInt(workdayIndex));
		
	}

	public void testCheckinUpdateWhere() {
		
	}

	public void testCheckinUpdateAll() {

	}

}
