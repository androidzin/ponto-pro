package br.com.androidzin.pontopro.test;

import android.test.AndroidTestCase;
import br.com.androidzin.pontopro.CountDownTimer;
import br.com.androidzin.pontopro.Countable;
import junit.framework.Test;

public class CountDownLogicTest extends AndroidTestCase{

    public static final int PERIOD = 2000;
    private CountDownTimer countDownTimer;
    private boolean success = false;
    private int count = 0;

    protected void setUp() throws Exception {
        super.setUp();

        countDownTimer = new CountDownTimer(1000, new Countable() {
            @Override
            public void onTick(long milisElapsedTime, long millisUntilFinished) {
                count++;
            }

            @Override
            public void onFinish() {
                success = true;
            }
        });
    }

    /*public void testCountingDown()
    {
        countDownTimer.start(PERIOD);
        try {
            Thread.currentThread().sleep(PERIOD*2);
        } catch (InterruptedException e) {
        }
        assertEquals(false, countDownTimer.isRunning());
    }*/

    public void testPlayWithoutTimeSet()
    {
        boolean passed = false;
        try {
            countDownTimer.play();
        } catch (NullPointerException e) {
            passed = true;
        }
        assertEquals(true, passed);
    }

    public void testPause()
    {
        countDownTimer.start(PERIOD);
        long t1 = countDownTimer.getRemainingTime();
        countDownTimer.pause();
        long t2 = countDownTimer.getRemainingTime();
        countDownTimer.play();
        long t3 = countDownTimer.getRemainingTime();

        assertTrue( (t2-t1) < 200);
        assertTrue( (t3-t1) < 200);

    }

    public void testStop()
    {
        countDownTimer.start(PERIOD);
        long t1 = countDownTimer.getRemainingTime();
        countDownTimer.stop();
        long t2 = countDownTimer.getRemainingTime();
        countDownTimer.start(PERIOD);
        long t3 = countDownTimer.getRemainingTime();

        assertTrue( (t2-t1) == -t1);
        assertTrue( (t3-t1) < 200);

    }
}
