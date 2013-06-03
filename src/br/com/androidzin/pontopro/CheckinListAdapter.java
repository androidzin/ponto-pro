package br.com.androidzin.pontopro;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import br.com.androidzin.pontopro.model.Checkin;

public class CheckinListAdapter extends ArrayAdapter<Checkin> {

	private LayoutInflater mInflater;

	public CheckinListAdapter(Context context) {
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
		Checkin checkin = getItem(position);
		((TextView) view.findViewById(R.id.checkinText)).setText(checkin
				.toString());
		return view;
	}

	public void setData(List<Checkin> data) {
		clear();
		if (data != null) {
			for (int i = 0; i < data.size(); i++) {
				add(data.get(i));
			}
		}
	}

}
