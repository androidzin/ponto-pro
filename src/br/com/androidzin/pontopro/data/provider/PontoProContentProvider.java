package br.com.androidzin.pontopro.data.provider;

import static br.com.androidzin.pontopro.data.provider.PontoProContract.AUTHORITY;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import br.com.androidzin.pontopro.data.DatabaseHelper;

public class PontoProContentProvider extends ContentProvider {

	static final String TAG = "ProviderDemo";
	private DatabaseHelper mHelper;

	private static final int WORKDAY_ID = 1;
	private static final int WORKDAY_ALL = 2;
	private static final int WORKDAY_CHECKINS = 3;
	
	private static final int WORKDAY_INTERVAL= 12;
	
	private static final int CHECKIN_ID = 8;
	private static final int CHECKIN_ALL = 9;
	
	private static final int WORKDAY_INSERT = 10;
	private static final int CHECKIN_INSERT = 11;
	
	public static final String WORKDAY_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "workday";
	public static final String WORKDAY_INTERVAL_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "workday-interval"; 
	public static final String CHECKIN_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "checkin";
	
	public static final String CHECKINS_ID = "_id";
	public static final String CHECKINS_TABLE = "checkins";
	public static final String CHECKINS_WORKDAY_ID = "workdayID";
	public static final String CHECKINS_CHECKIN_HOUR = "checkinHour";

	public static final String WORKDAY_TABLE = "workday";
	public static final String WORKDAY_ID_COLUMN = "_id";
	public static final String WORKDAY_WORK_DATE = "workDate";
	public static final String WORKDAY_WORKED_HOURS = "workedHours";
	public static final String WORKDAY_IS_CLOSED = "isClosed";
	public static final String WORKDAY_DAILY_MARK = "dailyMark";
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	

    static {
        sURIMatcher.addURI(AUTHORITY, "workday/#", WORKDAY_ID);
        sURIMatcher.addURI(AUTHORITY, "workday/all", WORKDAY_ALL);
        sURIMatcher.addURI(AUTHORITY, "workday/checkin/#", WORKDAY_CHECKINS);
        sURIMatcher.addURI(AUTHORITY, "workday/interval", WORKDAY_INTERVAL);
        
        sURIMatcher.addURI(AUTHORITY, "workday/insert", WORKDAY_INSERT);
        //sURIMatcher.addURI(AUTHORITY, "workday/update", WORKDAY_INTERVAL_OTHER);
        
        sURIMatcher.addURI(AUTHORITY, "checkin/#", CHECKIN_ID);
        sURIMatcher.addURI(AUTHORITY, "checkin/all", CHECKIN_ALL);
        
        sURIMatcher.addURI(AUTHORITY, "checkin/insert", CHECKIN_INSERT);
    }
    
    public DatabaseHelper getHelperToTest(){
    	return mHelper;
    }

	@Override
	public boolean onCreate() {
		this.mHelper = new DatabaseHelper(getContext());
		return (mHelper == null) ? false : true;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowID = 0;
		Uri result = null;
		switch (sURIMatcher.match(uri)) {
			/**
			 * @param dailyMark
			 * @param workedHours
			 * @param isClosed
			 */
			case WORKDAY_INSERT:
				rowID = mHelper.getWritableDatabase().insert(WORKDAY_TABLE, null, values);
				break;
				
			/**
			 * 
			 */
			case CHECKIN_INSERT:
				rowID = mHelper.getWritableDatabase().insert(CHECKINS_TABLE, null, values);
				break;
			default:
				break;
		}
		if ( rowID > 0 ) {
			result = Uri.withAppendedPath(uri, String.valueOf(rowID));
			getContext().getContentResolver().notifyChange(result, null);
		}
		return result;
	}
	
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		int count = 0;
		String whereClause ;
		switch (sURIMatcher.match(uri)) {
			case WORKDAY_ID:
				whereClause =  PontoProContract.WORKDAY_ID + "=" + uri.getLastPathSegment();
				if ( where != null ){
					whereClause = whereClause + " AND " + where ;
				}
				count = mHelper.getWritableDatabase().delete(PontoProContract.WORKDAY_TABLE, whereClause, whereArgs);
				break;

			case WORKDAY_ALL:
				count = mHelper.getWritableDatabase().delete(PontoProContract.WORKDAY_TABLE, where, whereArgs);
				break;
				
				
			case CHECKIN_ID:
				whereClause =  PontoProContract.CHECKINS_ID + "=" + uri.getLastPathSegment();
				if ( where != null ){
					whereClause = whereClause + " AND " + where ;
				}
				count = mHelper.getWritableDatabase().delete(PontoProContract.CHECKINS_TABLE, whereClause, whereArgs);
				break;
				
			case CHECKIN_ALL:
				count = mHelper.getWritableDatabase().delete(PontoProContract.CHECKINS_TABLE, where, whereArgs);
				break;
				
			case WORKDAY_CHECKINS:
				whereClause = PontoProContract.CHECKINS_WORKDAY_ID + "=" + uri.getLastPathSegment() ;
				if ( where != null ){
					whereClause = whereClause + " AND " + where ;
               	}
 				count = mHelper.getWritableDatabase().delete(PontoProContract.CHECKINS_TABLE, whereClause, whereArgs);
				break;
			default:
				break;
		}
		
		if ( count > 0) {
			// Notify the Context's ContentResolver of the change
			getContext().getContentResolver().notifyChange(uri, null);
		}
		
		return count;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteDatabase database = mHelper.getWritableDatabase();
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		
		switch (sURIMatcher.match(uri)) {
			case WORKDAY_ID:
				queryBuilder.setTables(WORKDAY_TABLE);
				queryBuilder.appendWhere(PontoProContract.WORKDAY_ID + "=" + uri.getLastPathSegment());
				break;
				
			case WORKDAY_ALL:
				queryBuilder.setTables(WORKDAY_TABLE);
				break;
				
			case WORKDAY_CHECKINS:
				queryBuilder.setTables(CHECKINS_TABLE);
				queryBuilder.appendWhere(PontoProContract.CHECKINS_WORKDAY_ID + "=" + uri.getLastPathSegment());
				break;
				
			case WORKDAY_INTERVAL:
				queryBuilder.setTables(WORKDAY_TABLE);
				queryBuilder.appendWhere(PontoProContract.WORKDAY_WORK_DATE + ">= ? AND ");
				queryBuilder.appendWhere(PontoProContract.WORKDAY_WORK_DATE + "<= ?");
				break;
				
			case CHECKIN_ALL:
				queryBuilder.setTables(CHECKINS_TABLE);
				break;
				
			case CHECKIN_ID:
				queryBuilder.setTables(CHECKINS_TABLE);
				queryBuilder.appendWhere(PontoProContract.CHECKINS_ID + "=" + uri.getLastPathSegment());
				break;
			
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		
		Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
		int count = 0;
		String whereClause ;
		switch (sURIMatcher.match(uri)) {
			case WORKDAY_ID:
				whereClause = PontoProContract.WORKDAY_ID + "=" + uri.getLastPathSegment();
				if ( where != null ){
					whereClause = whereClause + " AND " + where ;
               	}
				count = mHelper.getWritableDatabase().update(PontoProContract.WORKDAY_TABLE, values, whereClause, whereArgs);
				break;
	
			case WORKDAY_ALL:
				count = mHelper.getWritableDatabase().update(PontoProContract.WORKDAY_TABLE, values, where, whereArgs);
				break;
				
			case CHECKIN_ID:
				whereClause = PontoProContract.CHECKINS_ID + "=" + uri.getLastPathSegment();
				if ( where != null ){
					whereClause = whereClause + " AND " + where ;
               	}
				count = mHelper.getWritableDatabase().update(PontoProContract.CHECKINS_TABLE, values, whereClause, whereArgs);
				break;
				
			case CHECKIN_ALL:
				count = mHelper.getWritableDatabase().update(PontoProContract.CHECKINS_TABLE, values, where, whereArgs);
				break;
				
			case WORKDAY_CHECKINS:
				whereClause = PontoProContract.CHECKINS_WORKDAY_ID + "=" + uri.getLastPathSegment();
				if ( where != null ){
					whereClause = whereClause + " AND " + where ;
               	}
				count = mHelper.getWritableDatabase().update(PontoProContract.CHECKINS_TABLE, values, whereClause, whereArgs);
				break;
				
			default:
				break;
		}
		
		if ( count > 0) {
			// Notify the Context's ContentResolver of the change
			getContext().getContentResolver().notifyChange(uri, null);
		}
		
		return count;
	}
	
	@Override
	public String getType(Uri uri) {
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
			case WORKDAY_ID:
			case WORKDAY_ALL:
				return WORKDAY_TYPE;
				
			case WORKDAY_INTERVAL:
				return WORKDAY_INTERVAL_TYPE;
				
			case CHECKIN_ID:
			case CHECKIN_ALL:
				return CHECKIN_TYPE;
				
			default:
				return null;
		}
	}

}
