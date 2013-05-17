package br.com.androidzin.pontopro;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import br.com.androidzin.pontopro.model.Workday;

public class WorkdayListAdapter extends ArrayAdapter<Workday> {

	private LayoutInflater mInflater;

	public WorkdayListAdapter(Context context) {
		super(context, android.R.layout.simple_list_item_2);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView == null) {
			view = mInflater.inflate(R.layout.checkin_item_list, parent, false);
		} else {
			view = convertView;
		}
		Workday workday = getItem(position);
		((TextView) view.findViewById(R.id.checkinText)).setText(String.valueOf(workday.getWorkdayID()));
		return view;
	}

	public void setData(List<Workday> data) {
		clear();
		if (data != null) {
			for (int i = 0; i < data.size(); i++) {
				add(data.get(i));
			}
		}
	}
}
