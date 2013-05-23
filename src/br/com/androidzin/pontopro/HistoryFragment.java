package br.com.androidzin.pontopro;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import br.com.androidzin.pontopro.database.CheckinLoader;
import br.com.androidzin.pontopro.database.WorkdayLoader;
import br.com.androidzin.pontopro.model.Workday;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockListFragment;
import com.ami.fundapter.BindDictionary;
import com.ami.fundapter.BooleanExtractor;
import com.ami.fundapter.FunDapter;
import com.ami.fundapter.ImageLoader;
import com.ami.fundapter.StringExtractor;

public class HistoryFragment extends SherlockListFragment implements
		OnNavigationListener, LoaderCallbacks<List<Workday>> {

	private static final String TAG = HistoryFragment.class.getCanonicalName();
	private SpinnerAdapter mSpinnerAdapter;
	private MainActivity mActivity;

	//private WorkdayListAdapter mAdapter;
	private FunDapter<Workday> mAdapter;
	private BindDictionary<Workday> prodDict;
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
		//mAdapter = new WorkdayListAdapter(getActivity());
		initProDict();
		mAdapter = new FunDapter<Workday>(getActivity().getApplicationContext(), null, R.layout.workday_list_item, prodDict);
		setListAdapter(mAdapter);
		return inflater.inflate(R.layout.history_fragment, container, false);

	}
	
	private void initProDict() {
		prodDict = new BindDictionary<Workday>();
		prodDict.addStringField(R.id.workdayHours, new StringExtractor<Workday>() {
			@Override
			public String getStringValue(Workday item, int position) {
				return String.valueOf(item.getWorkedTime());
			}
		});
		prodDict.addStringField(R.id.workdayDate, new StringExtractor<Workday>() {
			@Override
			public String getStringValue(Workday item, int position) {
				return item.getStringDate();
			}
		});
		/*prodDict.addConditionalVisibilityField(R.id.workdaySuccessTime, new BooleanExtractor<Workday>() {
			@Override
			public boolean getBooleanValue(Workday item, int position) {
				if ( item.getWorkedTime() > 480 ) {
					return true;
				}
				return false;
			}
		}, View.GONE);
		prodDict.addConditionalVisibilityField(R.id.workdayFailureTime, new BooleanExtractor<Workday>() {
			@Override
			public boolean getBooleanValue(Workday item, int position) {
				if ( item.getWorkedTime() == 0 ) {
					return true;
				}
				return false;
			}
		}, View.GONE);*/

		prodDict.addImageField(R.id.workdaySuccessTime, new StringExtractor<Workday>() {
			@Override
			public String getStringValue(Workday item, int position) {
				if ( item.getWorkedTime() > 480 ) {
					return "OK";
				}
				return "NO";
			}
		}, new ImageLoader() {
			@Override
			public void loadImage(String url, ImageView view) {
				Log.e("SYS", url);
				if ( url.equals("NO")) {
					view.setImageDrawable(getResources().getDrawable(R.drawable.error));
				} else {
					view.setImageDrawable(getResources().getDrawable(R.drawable.checkbox));
				}
			}
		});
		/*prodDict.addStringField(R.id.description,
		 * 
				new StringExtractor<Workday>() {

					@Override
					public String getStringValue(Product item, int position) {
						return item.description;
					}
				});
		prodDict.addStringField(R.id.price, new StringExtractor<Product>() {

			@Override
			public String getStringValue(Product item, int position) {
				return item.price + " $";
			}
		});
		prodDict.addImageField(R.id.image, new StringExtractor<Workday>() {

			@Override
			public String getStringValue(Workday item, int position) {
				return item.toString();
			}
		}, new ImageLoader() {
			@Override
			public void loadImage(String url, ImageView view) {
				// INSERT IMAGE LOADER LIBRARY HERE
			}
		}).onClick(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "clicked the image!",Toast.LENGTH_SHORT).show();
			}
		});*/
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
				Intent intent = new Intent(getActivity(), DetailedCheckinFragment.class);
				intent.putExtra("workdayID", ((Workday) mAdapter.getItem(pos)).getWorkdayID());
				startActivity(intent);
			}
		});
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
	public Loader<List<Workday>> onCreateLoader(int id, Bundle args) {
		Log.i(TAG, "+++ onCreateLoader() called! +++");
		return new WorkdayLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<Workday>> loader, List<Workday> data) {
		// show data on screen
		Log.i(TAG, "+++ onLoadFinished() called! +++");
		//mAdapter.setData(data);
		mAdapter.updateData((ArrayList<Workday>) data);
	}

	@Override
	public void onLoaderReset(Loader<List<Workday>> loader) {
		mAdapter.updateData(null);
	}

}
