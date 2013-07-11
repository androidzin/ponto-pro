package br.com.androidzin.pontopro;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.ListView;
import br.com.androidzin.pontopro.data.provider.PontoProContract;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class DetailedCheckinFragment extends SherlockFragmentActivity implements  LoaderCallbacks<Cursor> {

	private ListView mCheckinListView;
	private static final int LOADER_ID = 1;
	private static final String TAG = DetailedCheckinFragment.class.getCanonicalName();
	private CheckinListAdapter mAdapter;
	private long workdayID;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
	
		setContentView(R.layout.detailed_checkin_fragment);
		mCheckinListView = (ListView) findViewById(android.R.id.list);
		bundle = new Bundle();
		if ( getIntent() != null ) {
			workdayID = getIntent().getLongExtra("workdayID", -1);
		}
		bundle.putLong("workdayID", workdayID);
		getSupportLoaderManager().initLoader(LOADER_ID, bundle, this);
		
		mAdapter = new CheckinListAdapter(getApplicationContext(),
				null,
				R.layout.checkin_item_list,
				new String[]{},
				new int[]{});
		mCheckinListView.setAdapter(mAdapter);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		if ( BuildConfig.DEBUG) {
			Log.i(TAG, "+++ onCreateLoader() called! +++");
		}
		return new CursorLoader(getApplicationContext(), 
				Uri.withAppendedPath(PontoProContract.CONTENT_URI, "workday/checkin/" + workdayID), 
				null, 
				null, 
				null, 
				null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if ( BuildConfig.DEBUG) {
			Log.i(TAG, "+++ onLoadFinished() called! +++");
		}
		mAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}
	
}
