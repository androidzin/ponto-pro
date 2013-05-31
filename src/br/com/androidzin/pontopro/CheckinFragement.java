package br.com.androidzin.pontopro;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import br.com.androidzin.pontopro.data.DatabaseManager;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;

public class CheckinFragement extends SherlockFragment implements OnTimeSetListener, Countable{
	
	private static final int ZERO = 0;
	private static final int hoursInMilis = 3600000;
	private static final int minutesInMilis = 60000;
	
	private boolean hasDailyGoal;
	private TextView mDailyGoal, mWorktimeRemaining;
	private ProgressBar mDailyGoalBar;
	private CountDownTimer timer;
	private Button doCheckin;
	private DatabaseManager databaseManager;
	long workdayID;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		databaseManager = new DatabaseManager(getActivity());
		return inflater.inflate(R.layout.checkin_fragement, container, false);
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState) {	
		mDailyGoal = (TextView) view.findViewById(R.id.dailyGoalText);
		mWorktimeRemaining = (TextView) view.findViewById(R.id.workTimeRemaining);
		mDailyGoalBar = (ProgressBar) view.findViewById(R.id.dailyGoal);
		doCheckin = (Button) view.findViewById(R.id.doCheckin);
		
		workdayID = databaseManager.addWorkday();
		doCheckin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				databaseManager.addCheckin(workdayID);
			}
		});
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

	private void startCountDownTimer(long remainingTime) {
		timer = new CountDownTimer(CountDownTimer.ONE_SECOND, remainingTime, this);
		timer.start();
	}

	@Override
	public void onTick(long milisElapsedTime, long millisUntilFinished) {
		mWorktimeRemaining.setText(String.format("%02d:%02d:%02d",
				millisUntilFinished/hoursInMilis, 
				(millisUntilFinished%hoursInMilis)/minutesInMilis,
				(millisUntilFinished%minutesInMilis)/CountDownTimer.ONE_SECOND));
	}

	@Override
	public void onFinish() {
		Toast.makeText(getSherlockActivity(), "Terminou", Toast.LENGTH_SHORT).show();
	}
}
