package br.com.androidzin.pontopro.settings;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;
import br.com.androidzin.pontopro.R;

public class ListPreferenceWithSummary extends ListPreference{

    public ListPreferenceWithSummary(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListPreferenceWithSummary(Context context) {
        super(context);
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
        setSummary(value);
    }

    @Override
    public void setSummary(CharSequence summary) {
        super.setSummary(getEntry());
    }
}
