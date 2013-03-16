package br.com.androidzin.pontopro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragment;

public class HistoryFragment extends SherlockFragment implements OnNavigationListener{

	private SpinnerAdapter mSpinnerAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mSpinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.history_options,
		          android.R.layout.simple_spinner_dropdown_item);
		return inflater.inflate(R.layout.history_fragment, container, false);
		
	}
	
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		String[] strings = getResources().getStringArray(R.array.history_options);
		Toast.makeText(getActivity(), strings[itemPosition], Toast.LENGTH_SHORT).show();
		return true;
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getSherlockActivity() != null)
		{
			ActionBar bar = getSherlockActivity().getSupportActionBar();
			if(isVisibleToUser)
			{
				bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
				bar.setListNavigationCallbacks(mSpinnerAdapter, this);
			} else {
				bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			}
		}
	}
	
}
