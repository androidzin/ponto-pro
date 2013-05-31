package br.com.androidzin.pontopro.data.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import br.com.androidzin.pontopro.data.DatabaseHelper;
import br.com.androidzin.pontopro.data.PontoProContract;

public class PontoProContentProvider extends ContentProvider {

	static final String TAG = "ProviderDemo";

	static final String AUTHORITY = "br.com.androidzin.pontopro.data.provider";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
	
	private SQLiteDatabase mDataBase;
	private DatabaseHelper mHelper;

	private static final int WORKDAY_ID = 1;
	private static final int WORKDAY_ALL = 2;
	private static final int WORKDAY_CHECKINS = 3;
	
	private static final int WORKDAY_INTERVAL_TODAY = 4;
	private static final int WORKDAY_INTERVAL_WEEK = 5;
	private static final int WORKDAY_INTERVAL_MONTH = 6;
	private static final int WORKDAY_INTERVAL_OTHER = 7;
	
	private static final int CHECKIN_ID = 8;
	private static final int CHECKIN_ALL = 9;
	
	public static final String WORKDAY_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "workday";
	public static final String WORKDAY_INTERVAL = ContentResolver.CURSOR_DIR_BASE_TYPE + "workday-interval"; 
	public static final String CHECKIN_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "checkin";
	
	public static final String CHECKINS_ID = "_id";
	public static final String CHECKINS_TABLE = "checkins";
	public static final String CHECKINS_WORKDAY_ID = "workdayID";
	public static final String CHECKINS_CHECKIN_HOUR = "checkinHour";

	public static final String WORKDAY_TABLE = "workday";
	public static final String WORKDAY_WORK_DATE = "workDate";
	public static final String WORKDAY_WORKED_HOURS = "workedHours";
	public static final String WORKDAY_IS_CLOSED = "isClosed";
	public static final String WORKDAY_DAILY_MARK = "dailyMark";
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static
    {
        sURIMatcher.addURI(AUTHORITY, "workday/#", WORKDAY_ID);
        sURIMatcher.addURI(AUTHORITY, "workday/*", WORKDAY_ALL);
        sURIMatcher.addURI(AUTHORITY, "workday/#/checkin", WORKDAY_CHECKINS);
        sURIMatcher.addURI(AUTHORITY, "workday/interval/today", WORKDAY_INTERVAL_TODAY);
        sURIMatcher.addURI(AUTHORITY, "workday/interval/week", WORKDAY_INTERVAL_WEEK);
        sURIMatcher.addURI(AUTHORITY, "workday/interval/month", WORKDAY_INTERVAL_MONTH);
        sURIMatcher.addURI(AUTHORITY, "workday/interval/other", WORKDAY_INTERVAL_OTHER);
        
        sURIMatcher.addURI(AUTHORITY, "checkin/#", CHECKIN_ID);
        sURIMatcher.addURI(AUTHORITY, "checkin/*", CHECKIN_ALL);
    }
	
	@Override
	public int delete(Uri uri, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case WORKDAY_ID:
		case WORKDAY_ALL:
			return WORKDAY_TYPE;
			
		case WORKDAY_INTERVAL_TODAY:
		case WORKDAY_INTERVAL_WEEK:
		case WORKDAY_INTERVAL_MONTH:
		case WORKDAY_INTERVAL_OTHER:
			return WORKDAY_INTERVAL;
			
		case CHECKIN_ID:
		case CHECKIN_ALL:
			return CHECKIN_TYPE;
			
		default:
			return null;
		}

	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		this.mHelper = new DatabaseHelper(getContext());
		return (mHelper == null) ? false : true;
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
		case WORKDAY_CHECKINS:
			queryBuilder.setTables(CHECKINS_TABLE);
			queryBuilder.appendWhere(PontoProContract.CHECKINS_WORKDAY_ID + "=" + uri.getQueryParameter("#"));
			break;

		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		
		Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

}
