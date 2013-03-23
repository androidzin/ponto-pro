package br.com.androidzin.pontopro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

public class MainMenuFragment extends SherlockListFragment {

	private int lastAdded;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_menu_fragment, container, false);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lastAdded = 0;
		addFragment(new CheckinFragement(), false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setListAdapter(ArrayAdapter.createFromResource(getSherlockActivity(), R.array.menu_options,
		          android.R.layout.simple_list_item_1)); 
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Fragment newFragment = null;
		if(position != lastAdded) 
		{
			switch(position)
			{
				case 0:
					newFragment = new CheckinFragement();
				break;
				
				case 1:
					newFragment = new HistoryFragment();
				break;
				
				default:
				
				break;
			}
			lastAdded = position;
		}
		
		if(newFragment != null)
		{
			addFragment(newFragment, true);
		}
	}

	private void addFragment(Fragment newFragment, boolean canBack) {
		FragmentManager fragmentManager = getSherlockActivity().getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.fragment_container, newFragment);
		if(canBack)
		{
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}
	
	
}
