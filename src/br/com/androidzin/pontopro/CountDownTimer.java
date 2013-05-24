package br.com.androidzin.pontopro;

import android.os.Handler;

public class CountDownTimer{

	private final long PERIOD;
	private long totalTime;
	private long milisElapsed;
	private Countable callback;
	private Handler mHandler;
	private boolean isRunning;
	private long remainingTime;
	private Object lock = new Object();
	
	public static final long ONE_SECOND = 1000;
	
	public CountDownTimer(long period, Countable callback) {
		this.callback = callback;
		this.PERIOD = period;
		milisElapsed = 0;
		remainingTime = 0;
		isRunning = false;
		mHandler = new Handler();
	}
	
	public void play() throws NullPointerException
	{
        if(totalTime == 0) {
            throw new NullPointerException();
        }

        if(isRunning == false)
        {
            isRunning = true;
            mHandler.postDelayed(counting, PERIOD);
        }

    }

    public void start(long time) throws NullPointerException
    {
        synchronized (lock){
            this.totalTime = time;
            this.milisElapsed = 0;
        }
        play();
    }

    private Runnable counting = new Runnable() {
        @Override
        public void run() {
            milisElapsed += PERIOD;
            synchronized (lock) {
                remainingTime = totalTime - milisElapsed;
            }
            callback.onTick(milisElapsed, remainingTime);
            if(remainingTime <= 0)
            {
                isRunning = false;
                callback.onFinish();
            } else {
                mHandler.postDelayed(this, ONE_SECOND);
            }
        }
    };



	public long pause()
	{
		isRunning = false;
		mHandler.removeCallbacks(counting);
		synchronized (lock) {
			return remainingTime;
		}
	}

    public void stop()
    {
        isRunning = false;
        mHandler.removeCallbacks(counting);
        synchronized (lock) {
            totalTime = 0;
        }
    }

    public boolean isRunning()
    {
        return isRunning;
    }

    public long getRemainingTime() {
        return remainingTime;
    }
}
