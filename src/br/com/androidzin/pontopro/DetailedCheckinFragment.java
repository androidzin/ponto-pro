package br.com.androidzin.pontopro;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.ListView;
import br.com.androidzin.pontopro.database.CheckinLoader;
import br.com.androidzin.pontopro.model.Checkin;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class DetailedCheckinFragment extends SherlockFragmentActivity implements  LoaderCallbacks<List<Checkin>> {

	private ListView mCheckinListView;
	// The Loader's id (this id is specific to the ListFragment's LoaderManager)
	private static final int LOADER_ID = 1;
	private static final String TAG = DetailedCheckinFragment.class.getCanonicalName();
	private CheckinListAdapter mAdapter;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
	
		setContentView(R.layout.detailed_checkin_fragment);
		mCheckinListView = (ListView) findViewById(android.R.id.list);
		bundle = new Bundle();
		bundle.putLong("workdayID", getIntent().getLongExtra("workdayID", -1));
		getSupportLoaderManager().initLoader(LOADER_ID, bundle, this);
		
		
		mAdapter = new CheckinListAdapter(getApplicationContext());
		mCheckinListView.setAdapter(mAdapter);
	}

	@Override
	public Loader<List<Checkin>> onCreateLoader(int id, Bundle args) {
		Log.i(TAG, "+++ onCreateLoader() called! +++");
		return new CheckinLoader(getApplicationContext(), args.getLong("workdayID"));
	}

	@Override
	public void onLoadFinished(Loader<List<Checkin>> loader, List<Checkin> data) {
		Log.i(TAG, "+++ onLoadFinished() called! +++");
		mAdapter.setData(data);
	}

	@Override
	public void onLoaderReset(Loader<List<Checkin>> arg0) {
		mAdapter.setData(null);
	}
	
}
