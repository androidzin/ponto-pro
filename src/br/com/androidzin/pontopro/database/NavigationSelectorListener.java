package br.com.androidzin.pontopro.database;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class NavigationSelectorListener extends BroadcastReceiver {

	//private static final String TAG = NavigationSelectorListener.class.getCanonicalName();
	private static final String TAG = "LOADER";
	private WorkdayLoader mLoader;
	
	public NavigationSelectorListener(WorkdayLoader loader) {
		mLoader = loader;
		IntentFilter filter = new IntentFilter(CheckinLoader.ACTION_SELECTOR_CHANGED);
	    mLoader.getContext().registerReceiver(this, filter);
	}
	
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		Log.i(TAG, "+++ The observer has detected a selector change!" +
	    		" Notifying Loader... +++");
	    // Tell the loader about the change.
	    mLoader.setIntervalToGet(arg1.getStringExtra("choice"));
		mLoader.onContentChanged();
	}

}
