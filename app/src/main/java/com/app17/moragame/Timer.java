package com.app17.moragame;

import android.util.Log;

public class Timer implements Runnable {

    private static final String TAG = "Timer";

    private boolean alive;
    private boolean stop;

    private long milliseconds;
    private long targetMilliseconds;
    private long stepMilliseconds;

    //是否到達時間
    private boolean onTime;
    //倒數時間到
    private boolean countDown;

    private OnTimerListener onTimerListener;


    public Timer(long targetMilliseconds, boolean countDown,OnTimerListener onTimerListener) {
        this.targetMilliseconds = targetMilliseconds;
        this.countDown = countDown;
        stepMilliseconds = 100;
        if (countDown) {
            milliseconds = targetMilliseconds;
        }
        this.onTimerListener=onTimerListener;
    }

    public void setCountDown(boolean countDown) {
        this.countDown = countDown;
        milliseconds = targetMilliseconds;
    }

    public boolean isOnTime() {
        return onTime;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public void start() {
        setAlive(true);
        new Thread(this).start();
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public long getTargetMilliseconds() {
        return targetMilliseconds;
    }


    public void setTargetMilliseconds(long targetMilliseconds) {
        this.targetMilliseconds = targetMilliseconds;
    }

    public void setStepMilliseconds(long stepMilliseconds) {
        this.stepMilliseconds = stepMilliseconds;
    }

    public void reset() {
        milliseconds = 0;
        if (countDown) {
            milliseconds = targetMilliseconds;
        }
        stop = false;
        alive = true;
        onTime = false;
    }

    @Override
    public void run() {
        while (true) {
            if (!alive) {
                break;
            }
            if (stop) {
                continue;
            }

            onTimerListener.onTick(milliseconds);
            //Log.d(TAG, "run: " + milliseconds);

            try {
                Thread.sleep(stepMilliseconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //倒數計時
            if (countDown) {
                milliseconds -= stepMilliseconds;
                if (milliseconds <= 0) {
                    milliseconds = 0;
                    onTime = true;
                }
            } else {
                milliseconds += stepMilliseconds;
                if (milliseconds >= targetMilliseconds) {
                    milliseconds = targetMilliseconds;
                    onTime = true;
                }
            }

            if (onTime) {
                stop = true;
                onTimerListener.onTime(milliseconds);
                //Log.d(TAG, "time's up: " + milliseconds);
            }
        }
    }

    public interface OnTimerListener{
        void onTick(long milliseconds);
        void onTime(long milliseconds);
    }
}
