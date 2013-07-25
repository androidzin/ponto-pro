package br.com.androidzin.pontopro;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import br.com.androidzin.pontopro.data.provider.PontoProContract;
import br.com.androidzin.pontopro.model.Checkin;
import br.com.androidzin.pontopro.model.Workday;
import br.com.androidzin.pontopro.util.Constants;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;

public class CheckinFragment extends SherlockFragment implements OnTimeSetListener, Countable, View.OnClickListener{
	
	private static final int ZERO = 0;
	private static final int hoursInMilis = 3600000;
	private static final int minutesInMilis = 60000;
	
	private boolean hasDailyGoal;
	private TextView mDailyGoal, mWorktimeRemaining;
	private ProgressBar mDailyGoalBar;
	private CountDownTimer timer;
	private Button doCheckin;
	
	private Workday today = new Workday();
	private SharedPreferences mSharedPreferences;
	private String workdayPrefFile = "pontopro.workday_file";
	private String workdayPrefID = "workdayID";
	private String workdayPrefOpenCheckin  = "workdayOpenCheckin";
	private String workdayPrefWorkedTime = "workdayWorkedTime";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSharedPreferences = getActivity().getSharedPreferences(workdayPrefFile, Context.MODE_PRIVATE);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mSharedPreferences.edit().putLong(workdayPrefID, today.getWorkdayID());
		mSharedPreferences.edit().putBoolean(workdayPrefOpenCheckin, today.hasOpenCheckin());
		mSharedPreferences.edit().putInt(workdayPrefWorkedTime, today.getWorkedTime());
		
		mSharedPreferences.edit().commit();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		long workdayID = mSharedPreferences.getLong(workdayPrefID, 0);
		today.setWorkdayID(workdayID);
		today.setHasOpenCheckin(mSharedPreferences.getBoolean(workdayPrefOpenCheckin, true));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.checkin_fragment, container, false);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.doCheckin:
			if (today.started()) {
				// retrieve workday data from sharedPref
				// create checkin data
				// append to workday
				Checkin checkin = new Checkin(mSharedPreferences.getLong(workdayPrefID, 0));
				checkin.setTimeStamp(String.valueOf(System.currentTimeMillis()));
				today.addCheckin(checkin);
				
			} else {
				// insert workday into database
				ContentValues values = PontoProContract.createWorkdayValues(0, 0, false);
				getActivity().getContentResolver().insert(Uri.withAppendedPath(PontoProContract.CONTENT_URI, "workday/insert"), values);
			}
			break;

		default:
			break;
		}
	}
	
	public void onViewCreated(View view, Bundle savedInstanceState) {	
		mDailyGoal = (TextView) view.findViewById(R.id.dailyGoalText);
		mWorktimeRemaining = (TextView) view.findViewById(R.id.workTimeRemaining);
		mDailyGoalBar = (ProgressBar) view.findViewById(R.id.dailyGoal);
        timer = new CountDownTimer(CountDownTimer.ONE_SECOND, this);
		doCheckin = (Button) view.findViewById(R.id.doCheckin);
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
				new TimePickerDialog(getActivity(), this, Constants.ZERO, Constants.ZERO, true);
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
        mWorktimeRemaining.setText(getFormatedString(0));
        timer.stop();

		
	}

	@Override
	public void onTimeSet(TimePicker view, int hour, int minute) {
		mWorktimeRemaining.setText(String.format("%02d:%02d:00", hour, minute));
		long remainingTime = hour*Constants.hoursInMilis + minute * Constants.minutesInMilis;
		startCountDownTimer(remainingTime);
	}

	private void startCountDownTimer(long remainingTime) {
		if (timer.isRunning()) {
            timer.stop();
        }
        try {
            timer.start(remainingTime);
        } catch (NullPointerException e){
            Toast.makeText(getSherlockActivity(), R.string.no_time_selected, Toast.LENGTH_SHORT).show();
        }
	}

	@Override
	public void onTick(long millisElapsedTime, long millisUntilFinished) {
		mWorktimeRemaining.setText(getFormatedString(millisUntilFinished));
	}

    private String getFormatedString(long millisUntilFinished) {
        return String.format("%02d:%02d:%02d",
                millisUntilFinished/ Constants.hoursInMilis,
                (millisUntilFinished%Constants.hoursInMilis)/Constants.minutesInMilis,
                (millisUntilFinished%Constants.minutesInMilis)/ CountDownTimer.ONE_SECOND);
    }

    @Override
	public void onFinish() {
		Toast.makeText(getSherlockActivity(), R.string.test_timefinished, Toast.LENGTH_SHORT).show();
	}

	
}
