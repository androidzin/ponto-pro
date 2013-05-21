package br.com.androidzin.pontopro.settings;

import android.preference.PreferenceManager;
import br.com.androidzin.pontopro.R;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class NotificationFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.notifications_prefs);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.notifications_prefs, false);
	}
}
