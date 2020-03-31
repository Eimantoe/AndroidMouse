package com.example.androidmouse.Common;

public class ThreadWithAbort extends Thread {

    private boolean mIsRunning;

    @Override
    public synchronized void start() {
        setRunning();
        super.start();
    }

    public boolean getIsRunning()
    {
        return mIsRunning;
    }

    public void setRunning()
    {
        mIsRunning = true;
    }

    public void abort()
    {
        mIsRunning = false;
    }
}
