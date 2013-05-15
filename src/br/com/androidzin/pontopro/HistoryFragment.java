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
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import br.com.androidzin.pontopro.database.CheckinLoader;
import br.com.androidzin.pontopro.model.Checkin;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockListFragment;

public class HistoryFragment extends SherlockListFragment implements
		OnNavigationListener, LoaderCallbacks<List<Checkin>> {

	private static final String TAG = null;
	private SpinnerAdapter mSpinnerAdapter;
	private MainActivity mActivity;

	private CheckinListAdapter mAdapter;
	// The Loader's id (this id is specific to the ListFragment's LoaderManager)
	private static final int LOADER_ID = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.history_options,
				android.R.layout.simple_spinner_dropdown_item);
		mActivity = (MainActivity) getSherlockActivity();

		getLoaderManager().initLoader(LOADER_ID, null, this);
		mAdapter = new CheckinListAdapter(getActivity());
		setListAdapter(mAdapter);
		//setListShown(false);
		return inflater.inflate(R.layout.history_fragment, container, false);

	}

	@Override
	public void onResume() {
		super.onResume();
		if (mActivity != null && mActivity.isTablet()) {
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
		intent.putExtra("choice", choice);
		getActivity().sendBroadcast(intent);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (mActivity != null && !mActivity.isTablet()) {
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
		if (mActivity != null && mActivity.isTablet()) {
			ActionBar bar = mActivity.getSupportActionBar();
			bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		}
	}

	@Override
	public Loader<List<Checkin>> onCreateLoader(int id, Bundle args) {
		Log.i(TAG, "+++ onCreateLoader() called! +++");
		return new CheckinLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<Checkin>> loader, List<Checkin> data) {
		// TODO Auto-generated method stub
		// show data on screen
		Log.i(TAG, "+++ onLoadFinished() called! +++");
		mAdapter.setData(data);

		if (isResumed()) {
			//setListShown(true);
		} else {
			//setListShownNoAnimation(true);
		}
	}

	@Override
	public void onLoaderReset(Loader<List<Checkin>> loader) {
		// TODO Auto-generated method stub
		// adatper.setData(null)
		mAdapter.setData(null);
	}

}
