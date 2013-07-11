package br.com.androidzin.pontopro;

import org.joda.time.DateTime;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
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
	private String baseDate;
	private String todayDate; 
	private DateTime today = new DateTime();
	private static final int LOADER_ID = 1;

	public static enum RANGE {
		TODAY("Hoje"), WEEKLY("Semanal"), MONTHLY("Mensal"), OTHER("Outros");
		private String value;
		private RANGE(String string) {
			value = string;
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mActivity = (MainActivity) getSherlockActivity();

		isTablet = mActivity.getResources().getBoolean(R.bool.is_tablet);
		
		baseDate = PontoProContract.parser.print(today.minusHours(23));
		todayDate = PontoProContract.parser.print(today);
		
		mSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.history_options,
				android.R.layout.simple_spinner_dropdown_item);
		
		getLoaderManager().initLoader(LOADER_ID, null, this);
		mAdapter = new WorkdayListAdapter(getActivity(), 
				null, 
				R.layout.workday_list_item,
				new String[]{PontoProContract.WORKDAY_WORKED_HOURS, PontoProContract.WORKDAY_WORK_DATE}, 
				new int[]{R.id.workdayHours, R.id.workdayDate} );
		setListAdapter(mAdapter);
		
		return inflater.inflate(R.layout.history_fragment, container, false);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(getActivity(), DetailedCheckinFragment.class);
		intent.putExtra(WORKDAY_ID, mAdapter.getItemId(position));
		startActivity(intent);
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
		String[] strings = getResources().getStringArray(R.array.history_options);
		updateBaseDate(strings[itemPosition]);
		Toast.makeText(getActivity(), strings[itemPosition], Toast.LENGTH_SHORT).show();
		getLoaderManager().restartLoader(LOADER_ID, null, this);
		return true;
	}
	
	private void updateBaseDate(String choice) {
		DateTime date = new DateTime();
		DateTime newDate = null;
		if (choice.equalsIgnoreCase(RANGE.TODAY.value)) {
			newDate = date.minusHours(23);
		} else if (choice.equalsIgnoreCase(RANGE.MONTHLY.value)) {
			newDate = date.minusMonths(1);
		} else if (choice.equalsIgnoreCase(RANGE.WEEKLY.value)) {
			newDate = date.minusWeeks(1);
		}
		baseDate = PontoProContract.parser.print(newDate);
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
		if ( BuildConfig.DEBUG) {
			Log.i(TAG, "+++ onCreateLoader() called! +++");
		}
		return new CursorLoader(getActivity(),
				Uri.withAppendedPath(PontoProContract.CONTENT_URI , "workday/interval"),
				null,
				null,
				new String[]{baseDate, todayDate},
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
