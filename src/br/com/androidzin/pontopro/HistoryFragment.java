package br.com.androidzin.pontopro;

import org.joda.time.DateTime;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import br.com.androidzin.pontopro.data.loader.CheckinLoader;
import br.com.androidzin.pontopro.data.provider.PontoProContract;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockListFragment;

public class HistoryFragment extends SherlockListFragment implements
		OnNavigationListener, LoaderCallbacks<Cursor> {

    private static final String TAG = HistoryFragment.class.getCanonicalName();
    public static final String WORKDAY_ID = "workdayID";
    public static final String CHOICE = "choice";
    private SpinnerAdapter mSpinnerAdapter;
	private MainActivity mActivity;
    private boolean isTablet;

	private WorkdayListAdapter mAdapter;
	private SimpleCursorAdapter mCursorAdapter;
	// The Loader's id (this id is specific to the ListFragment's LoaderManager)
	private static final int LOADER_ID = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.history_options,
				android.R.layout.simple_spinner_dropdown_item);
		mActivity = (MainActivity) getSherlockActivity();

        isTablet = mActivity.getResources().getBoolean(R.bool.is_tablet);

		getLoaderManager().initLoader(LOADER_ID, null, this);
		mCursorAdapter = new SimpleCursorAdapter(getActivity(), 
				R.layout.workday_list_item, 
				null,
				new String[]{PontoProContract.WORKDAY_WORKED_HOURS, PontoProContract.WORKDAY_WORK_DATE}, 
				new int[]{R.id.workdayHours, R.id.workdayDate}, 
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		
		mCursorAdapter.setViewBinder(new CustomBinder());
		//mAdapter = new WorkdayListAdapter(getActivity());
		//setListAdapter(mAdapter);
		setListAdapter(mCursorAdapter);
		return inflater.inflate(R.layout.history_fragment, container, false);
	}
	
	class CustomBinder implements ViewBinder {

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if ( columnIndex == cursor.getColumnIndex(PontoProContract.WORKDAY_WORKED_HOURS)) {
				int workedHours = cursor.getInt(columnIndex);
				if (workedHours > 640 ) {
					view.setBackgroundColor(Color.parseColor("#8DBF41"));
				} else{ 
					view.setBackgroundColor(Color.parseColor("#F25933"));
				}
				return true;
			}
			return false;
		}
		 	
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
				//Intent intent = new Intent(getActivity(), DetailedCheckinFragment.class);
				//intent.putExtra(WORKDAY_ID, ((Workday) mAdapter.getItem(pos)).getWorkdayID());
				//startActivity(intent);
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mActivity != null && isTablet) {
			ActionBar bar = getSherlockActivity().getSupportActionBar();
			bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			bar.setListNavigationCallbacks(mSpinnerAdapter, this);
		}
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		String[] strings = getResources().getStringArray(
				R.array.history_options);
		Toast.makeText(getActivity(), strings[itemPosition], Toast.LENGTH_SHORT)
				.show();
		fireBroadcast(strings[itemPosition]);
		return true;
	}

	private void fireBroadcast(String choice) {
		Intent intent = new Intent(CheckinLoader.ACTION_SELECTOR_CHANGED);
		intent.putExtra(CHOICE, choice);
		getActivity().sendBroadcast(intent);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (mActivity != null && !isTablet) {
			ActionBar bar = mActivity.getSupportActionBar();
			if (isVisibleToUser) {
				bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
				bar.setListNavigationCallbacks(mSpinnerAdapter, this);
			} else {
				bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			}
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mActivity != null && isTablet) {
			ActionBar bar = mActivity.getSupportActionBar();
			bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Log.i(TAG, "+++ onCreateLoader() called! +++");
		//return new WorkdayLoader(getActivity());
		DateTime date = new DateTime();
		String t = PontoProContract.parser.print(date);
		return new CursorLoader(getActivity(),
				Uri.withAppendedPath(PontoProContract.CONTENT_URI , "workday/all"),
				null,
				null,
				null,
				null);
		
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		// show data on screen
		Log.i(TAG, "+++ onLoadFinished() called! +++");
		//mAdapter.setData(data);
		mCursorAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		//mAdapter.setData(null);
		mCursorAdapter.swapCursor(null);
	}

}
