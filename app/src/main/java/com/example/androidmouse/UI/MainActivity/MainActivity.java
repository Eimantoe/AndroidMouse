package com.example.androidmouse.UI.MainActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.androidmouse.Common.MouseAction;
import com.example.androidmouse.UI.Common.Views.BaseActivity;
import com.example.androidmouse.UI.ScreenNavigator.ScreenNavigator;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

public class MainActivity extends BaseActivity
        implements MainActivityController.Listener, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    public final static String DEVICE_NAME = "DEVICE_NAME";
    public final static String DEVICE_ADDRESS = "DEVICE_ADDRESS";

    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;

    private MainActivityController mMainActivityController;
    private MainActivityView mMainActivityView;
    private ScreenNavigator mScreenNavigator;

    public String getIntentExtra(String key)
    {
        return getIntent().getStringExtra(key);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScreenNavigator = getControllerRootComposition().getScreenNavigator();
        mDetector = getControllerRootComposition().getGestureDetectorCompat(this, this);

        mMainActivityView = getControllerRootComposition().getViewFactory().getMainActivityView(getIntentExtra(DEVICE_NAME), null);

        mMainActivityController = getControllerRootComposition().getMainActivityController();
        mMainActivityController.bindView(mMainActivityView);
        mMainActivityController.bindIntent(getIntent());

        setContentView(mMainActivityView.getRootView());
    }

    @Override
    protected void onStart() {
        super.onStart();

        mMainActivityController.registerListener(this);
        mMainActivityController.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mMainActivityController.unregisterListener(this);
        mMainActivityController.onStop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.mDetector.onTouchEvent(event)) {
            return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void onBackButtonPressed() {
        mScreenNavigator.backButtonPressed();
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
                            float distanceY) {
        if (distanceY > 0) {
            mMainActivityController.onTouchEvent(MouseAction.ACTION_UP);
        } else {
            mMainActivityController.onTouchEvent(MouseAction.ACTION_DOWN);
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        mMainActivityController.onTouchEvent(MouseAction.ACTION_R_CLICK);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        mMainActivityController.onTouchEvent(MouseAction.ACTION_L_CLICK);
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        mMainActivityController.onTouchEvent(MouseAction.ACTION_L_RELEASE);
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

}
