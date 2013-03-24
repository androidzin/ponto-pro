package br.com.androidzin.pontopro;

import android.os.Handler;

public class CountDownTimer implements Runnable {

	private final long PERIOD;
	private final long TIME;
	private long milisElapsed;
	private Countable callback;
	private Handler mHandler;
	private boolean isRunning;
	private long remainingTime;
	private Object lock = new Object();
	
	public static final long ONE_SECOND = 1000;
	
	public CountDownTimer(long period, long time, Countable callback) {
		this.callback = callback;
		this.PERIOD = period;
		this.TIME = time;
		milisElapsed = 0;
		remainingTime = 0;
		isRunning = false;
		mHandler = new Handler();
	}
	
	public void start()
	{
		if(isRunning == false)
		{
			isRunning = true;
			mHandler.post(this);
		}
		
	}
	
	@Override
	public void run() {
		milisElapsed += PERIOD;
		synchronized (lock) {
			remainingTime = TIME - milisElapsed;
		}
		callback.onTick(milisElapsed, remainingTime);
		if(remainingTime == 0)
		{
			callback.onFinish();
		} else {
			mHandler.postDelayed(this, ONE_SECOND);
		}
		
	}
	
	public long pause()
	{
		isRunning = false;
		mHandler.removeCallbacks(this);
		synchronized (lock) {
			return remainingTime;
		}
	}

}
