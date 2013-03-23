package br.com.androidzin.pontopro;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;

public class CheckinFragement extends SherlockFragment implements OnTimeSetListener{
	
	private static final int ZERO = 0;
	private static final long ONE_SECOND = 1000;
	private static final int hoursInMilis = 3600000;
	private static final int minutesInMilis = 60000;
	
	private boolean hasDailyGoal;
	private TextView mDailyGoal, mWorktimeRemaining;
	private ProgressBar mDailyGoalBar;
	private Handler mHandler;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mHandler = new Handler();
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.checkin_fragement, container, false);
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState) {	
		mDailyGoal = (TextView) view.findViewById(R.id.dailyGoalText);
		mWorktimeRemaining = (TextView) view.findViewById(R.id.workTimeRemaining);
		mDailyGoalBar = (ProgressBar) view.findViewById(R.id.dailyGoal);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
			case R.id.action_setdailygoal:
				if(hasDailyGoal == false)
				{
					showDailyGoalDialog();
				} else {
					forgetDailyGoal();
				}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showDailyGoalDialog() {
		hasDailyGoal = true;
		Dialog newFragment = 
				new TimePickerDialog(getActivity(), this, ZERO, ZERO, true);
		newFragment.show();
		showDailyGoalUI();
	}
	
	private void forgetDailyGoal() {
		hasDailyGoal = false;
		hideDailyGoalUI();		
	}

	private void showDailyGoalUI() {
		mDailyGoal.setVisibility(View.VISIBLE);
		mWorktimeRemaining.setVisibility(View.VISIBLE);
		mDailyGoalBar.setVisibility(View.VISIBLE);
	}


	private void hideDailyGoalUI() {
		mDailyGoal.setVisibility(View.INVISIBLE);
		mWorktimeRemaining.setVisibility(View.INVISIBLE);
		mDailyGoalBar.setVisibility(View.INVISIBLE);
		
	}

	@Override
	public void onTimeSet(TimePicker view, int hour, int minute) {
		mWorktimeRemaining.setText(String.format("%02d:%02d:00", hour, minute));
		long remainingTime = hour*hoursInMilis + minute * minutesInMilis;
		startCountDownTimer(remainingTime);
	}

	private void startCountDownTimer(final long remainingTime) {
		mHandler.post(coutingDown(remainingTime));
	}

	private Runnable coutingDown(final long remainingTime) {
		return new Runnable() {
			
			private long elapsedTime = 0;
			@Override
			public void run() {
				elapsedTime +=ONE_SECOND;
				long millisUntilFinished = remainingTime - elapsedTime;
				mWorktimeRemaining.setText(String.format("%02d:%02d:%02d",
						millisUntilFinished/hoursInMilis, 
						(millisUntilFinished%hoursInMilis)/minutesInMilis,
						(millisUntilFinished%minutesInMilis)/ONE_SECOND));
				if(millisUntilFinished > 0)
				{
					mHandler.postDelayed(this, ONE_SECOND);
				}
			}
		};
	}
}
