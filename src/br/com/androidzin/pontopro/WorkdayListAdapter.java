package br.com.androidzin.pontopro;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.com.androidzin.pontopro.data.provider.PontoProContract;

public class WorkdayListAdapter extends SimpleCursorAdapter {

	private LayoutInflater mInflater;
	private int workedHoursIndex;
	private int workDayString;
	
	public WorkdayListAdapter(Context context, Cursor cursor,  int layout, String[] from, int[] to) {
		super(context, layout, cursor, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		mInflater = LayoutInflater.from(context);
	}

	static class ViewHolder {
		TextView dailyHour;
		TextView weekDay;
		View statusLabel;
	}

	@Override
	public void bindView(View convertView, Context context, Cursor cursor) {
		ViewHolder holder = new ViewHolder();
		if ( convertView != null ) {
			holder = (ViewHolder) convertView.getTag();
			holder.dailyHour.setText(cursor.getString(workedHoursIndex));
			holder.weekDay.setText(cursor.getString(workDayString));
			int workedTime = Integer.valueOf((String) holder.dailyHour.getText());
			
			if (workedTime > 480) {
				holder.statusLabel.setBackgroundColor(Color.parseColor("#8DBF41"));
			} else {
				holder.statusLabel.setBackgroundColor(Color.parseColor("#F25933"));
			}
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		if ( cursor != null ) {
			workedHoursIndex = cursor.getColumnIndex(PontoProContract.WORKDAY_WORKED_HOURS);
			workDayString = cursor.getColumnIndex(PontoProContract.WORKDAY_WORK_DATE);
		}
		View view = mInflater.inflate(R.layout.workday_list_item, null) ; 
		ViewHolder holder = new ViewHolder();
		holder.dailyHour = (TextView) view.findViewById(R.id.workdayHours);
		holder.weekDay = (TextView) view.findViewById(R.id.workdayDate);
		holder.statusLabel = (View) view.findViewById(R.id.workdayStatusLabel);
		view.setTag(holder);
		return view;
	}
}
