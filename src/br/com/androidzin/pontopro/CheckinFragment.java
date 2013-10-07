package br.com.androidzin.pontopro;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import br.com.androidzin.pontopro.model.Today;
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
	
	private Today mToday = new Today();
	private SharedPreferences mSharedPreferences;
	
	private Button doCheckin;
	private long ONE_DAY ;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		ONE_DAY = Long.valueOf(getResources().getString(R.string.twenty_four_hour_value));;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mToday.save();
	}

	@Override
	public void onResume() {
		super.onResume();
		mToday.load(mSharedPreferences);
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
			if ( sameDay() ) {
				if (!mToday.wasStarted()) {
					startToday();
				}
				// create checkin data
				// append to workday
				Checkin checkin = new Checkin();
				//checkin.setTimeStampSting(String.valueOf(System.currentTimeMillis()));
				checkin.setTimeStamp(System.currentTimeMillis());
				checkin.setWorkdayID(mToday.getWorkdayID());
				checkin.setType(mToday.getCheckinCounter());
				
				mToday.addCheckin(checkin);
				mToday.refreshData(checkin);
			} else {

				// Finish Day
				// Save data to DB
				// Calculate WorkedHours
				mToday.finish(getActivity());
				mToday = null;
				
				// Start new day
				mToday = new Today();
				startToday();
			}
			
			break;

		default:
			Toast.makeText(getActivity(), "text", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	

	private void startToday() {
		ContentValues values = PontoProContract.createWorkdayValues(0, 0, false);
		Uri created = getActivity().getContentResolver().insert(Uri.withAppendedPath(PontoProContract.CONTENT_URI, "workday/insert"), values);
		mToday.initData(mSharedPreferences, 
				Long.valueOf(created.getLastPathSegment()), 
				values.getAsInteger(PontoProContract.WORKDAY_WORKED_HOURS), 
				values.getAsInteger(PontoProContract.WORKDAY_DAILY_MARK));
	}
	
	private boolean sameDay() {
		if (mToday.getInitialTime() == 0) {
			return true;
		}
		if ( System.currentTimeMillis() - mToday.getInitialTime() < ONE_DAY ){
			return true;
		}
		return false;
	}

	public void onViewCreated(View view, Bundle savedInstanceState) {	
		mDailyGoal = (TextView) view.findViewById(R.id.dailyGoalText);
		mWorktimeRemaining = (TextView) view.findViewById(R.id.workTimeRemaining);
		mDailyGoalBar = (ProgressBar) view.findViewById(R.id.dailyGoal);
        timer = new CountDownTimer(CountDownTimer.ONE_SECOND, this);
        doCheckin = (Button) view.findViewById(R.id.doCheckin);
        doCheckin.setOnClickListener(this);
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
