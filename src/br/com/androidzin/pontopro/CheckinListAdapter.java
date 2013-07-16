package br.com.androidzin.pontopro;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.androidzin.pontopro.data.provider.PontoProContract;

public class CheckinListAdapter extends SimpleCursorAdapter{

	private LayoutInflater mInflater;
	private int checkinHour;
	
	public CheckinListAdapter(Context context, Cursor cursor, int layout, String[] from, int[] to) {
		super(context, layout, cursor, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	static class ViewHolder { 
		TextView checkinHour;
		ImageView checkinArrow;
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder = new ViewHolder();
		if ( view != null ) {
			holder = (ViewHolder) view.getTag();
			holder.checkinHour.setText(cursor.getString(checkinHour));
			if (cursor.getPosition() % 2 == 0) {
				holder.checkinArrow.setImageResource(R.drawable.check_out);
			} else {
				holder.checkinArrow.setImageResource(R.drawable.check_in);
			}
		}
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		if ( cursor != null ) {
			checkinHour = cursor.getColumnIndex(PontoProContract.CHECKINS_CHECKIN_HOUR);
		}
		ViewHolder holder = new ViewHolder();
		View view = mInflater.inflate(R.layout.checkin_item_list, null);
		holder.checkinHour = (TextView) view.findViewById(R.id.checkinText);
		holder.checkinArrow = (ImageView) view.findViewById(R.id.checkinArrow);
		view.setTag(holder);
		return view;
	}

}
