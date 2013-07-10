package br.com.androidzin.pontopro;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.com.androidzin.pontopro.data.provider.PontoProContract;

public class WorkdayListAdapter extends CursorAdapter {

	private LayoutInflater mInflater;
	
	private int idIndex ;
	private int workedHoursIndex;
	private int isClosedIndex;
	private int dailyIndex ;
	

	public WorkdayListAdapter(Context context, Cursor cursor) {
		super(context, cursor);
		mInflater = LayoutInflater.from(context);
		
		idIndex = cursor.getColumnIndex(PontoProContract.WORKDAY_ID);
		workedHoursIndex = cursor.getColumnIndex(PontoProContract.WORKDAY_WORKED_HOURS);
		isClosedIndex = cursor.getColumnIndex(PontoProContract.WORKDAY_IS_CLOSED);
		dailyIndex = cursor.getColumnIndex(PontoProContract.WORKDAY_DAILY_MARK);
	}

	static class ViewHolder {
		TextView dailyHour;
		TextView weekDay;
		View statusLabel;
	}

	@Override
	public void bindView(View convertView, Context context, Cursor cursor) {
		ViewHolder holder;
		holder = new ViewHolder();
		holder.dailyHour = (TextView) convertView.findViewById(R.id.workdayHours);
		holder.weekDay = (TextView) convertView.findViewById(R.id.workdayDate);
		holder.statusLabel = (View) convertView.findViewById(R.id.workdayStatusLabel);
		convertView.setTag(holder);
		
		holder.dailyHour.setText(cursor.getString(workedHoursIndex));
		holder.weekDay.setText(cursor.getString(dailyIndex));
		int value = Integer.valueOf((String) holder.dailyHour.getText());
		if (value > 480) {
			holder.statusLabel.setBackgroundColor(Color.parseColor("#8DBF41"));
		} else {
			holder.statusLabel.setBackgroundColor(Color.parseColor("#F25933"));
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		return mInflater.inflate(R.layout.workday_list_item, null);
	}
}
