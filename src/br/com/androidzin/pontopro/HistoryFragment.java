package br.com.androidzin.pontopro;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
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
import br.com.androidzin.pontopro.data.loader.WorkdayLoader;
import br.com.androidzin.pontopro.model.Workday;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockListFragment;

public class HistoryFragment extends SherlockListFragment implements
		OnNavigationListener, LoaderCallbacks<List<Workday>> {

    private static final String TAG = HistoryFragment.class.getCanonicalName();
    public static final String WORKDAY_ID = "workdayID";
    public static final String CHOICE = "choice";
    private SpinnerAdapter mSpinnerAdapter;
	private MainActivity mActivity;
    private boolean isTablet;

	private WorkdayListAdapter mAdapter;
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
		mAdapter = new WorkdayListAdapter(getActivity());
		setListAdapter(mAdapter);
		return inflater.inflate(R.layout.history_fragment, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
				Intent intent = new Intent(getActivity(), DetailedCheckinFragment.class);
				intent.putExtra(WORKDAY_ID, ((Workday) mAdapter.getItem(pos)).getWorkdayID());
				startActivity(intent);
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
	public Loader<List<Workday>> onCreateLoader(int id, Bundle args) {
		Log.i(TAG, "+++ onCreateLoader() called! +++");
		return new WorkdayLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<Workday>> loader, List<Workday> data) {
		// show data on screen
		Log.i(TAG, "+++ onLoadFinished() called! +++");
		mAdapter.setData(data);
	}

	@Override
	public void onLoaderReset(Loader<List<Workday>> loader) {
		mAdapter.setData(null);
	}

}
