package br.com.androidzin.pontopro;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import br.com.androidzin.pontopro.model.Workday;

public class WorkdayListAdapter extends ArrayAdapter<Workday> {

	private LayoutInflater mInflater;

	public WorkdayListAdapter(Context context) {
		super(context, R.layout.workday_list_item);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.workday_list_item, parent,false);
			holder = new ViewHolder();
			holder.dailyHour = (TextView) convertView.findViewById(R.id.workdayHours);
			holder.weekDay = (TextView) convertView.findViewById(R.id.workdayDate);
			holder.statusLabel = (View) convertView.findViewById(R.id.workdayStatusLabel);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Workday workday = getItem(position);
		holder.dailyHour.setText(String.valueOf(workday.getWorkedTime()));
		holder.weekDay.setText(workday.getStringDate());
		int value = Integer.valueOf((String) holder.dailyHour.getText());
		if (value > 480) {
			holder.statusLabel.setBackgroundColor(Color.parseColor("#8DBF41"));
		} else {
			holder.statusLabel.setBackgroundColor(Color.parseColor("#F25933"));
		}

		return convertView;
	}

	public void setData(List<Workday> data) {
		clear();
		if (data != null) {
			for (int i = 0; i < data.size(); i++) {
				add(data.get(i));
			}
		}
	}

	static class ViewHolder {
		TextView dailyHour;
		TextView weekDay;
		View statusLabel;
	}
}
