package com.example.androidmouse.Common.DependencyInjection;

import android.app.Activity;
import android.content.Context;
import android.view.GestureDetector;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ViewSwitcher;

import com.example.androidmouse.Common.Constants;
import com.example.androidmouse.UI.Common.Factories.ViewFactory;
import com.example.androidmouse.UI.ScreenNavigator.ScreenNavigator;
import com.example.androidmouse.DataModel.BTDeviceModel;
import com.example.androidmouse.UI.BTDeviceListActivity.DeviceListActivityController;
import com.example.androidmouse.UI.MainActivity.MainActivityController;
import com.example.androidmouse.UseCase.FetchBondedBTDevicesUseCase;
import com.example.androidmouse.UseCase.ReadAccelometerAndSendBTUseCase;
import com.google.zxing.integration.android.IntentIntegrator;

import androidx.core.view.GestureDetectorCompat;

public class ControllerRootComposition {

    private final RootComposition mRootComposition;
    private final Activity mActivity;

    public ControllerRootComposition(Activity activity, RootComposition rootComposition) {

        mActivity = activity;
        mRootComposition = rootComposition;
    }

    public RootComposition getRootComposition()
    {
        return mRootComposition;
    }

    public Context getContext()
    {
        return mActivity;
    }

    public Activity getActivity() {
        return mActivity;
    }

    public MainActivityController getMainActivityController()
    {
        return new MainActivityController(getReadAccelometerAndSendBTUseCase(),
                getFetchBondedBTDevicesUseCase(),
                getInputMethodManager(),
                getViewFactory());
    }

    public DeviceListActivityController getDeviceListActivityController() {
        return new DeviceListActivityController(
                getFetchBondedBTDevicesUseCase(),
                getIntentIntegrator(false, false)
        );
    }

    public BTDeviceModel getBTDeviceModel() {
        return new BTDeviceModel();
    }

    public ReadAccelometerAndSendBTUseCase getReadAccelometerAndSendBTUseCase() {
        return new ReadAccelometerAndSendBTUseCase(
                getRootComposition().getSensorManager(),
                getRootComposition().getLinearAccelerationSensor(),
                getRootComposition().getGyroscopeSensor()
        );
    }

    public IntentIntegrator getIntentIntegrator(boolean isBeepEnabled, boolean isOrientationLocked) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(getActivity());
        intentIntegrator.setRequestCode(Constants.INTENT_INTEGRATOR_REQUEST_CODE);
        intentIntegrator.setBeepEnabled(isBeepEnabled);
        intentIntegrator.setOrientationLocked(isOrientationLocked);

        return intentIntegrator;
    }

    public ViewFactory getViewFactory() {
        return new ViewFactory(getActivity().getLayoutInflater(), getContext());
    }

    public FetchBondedBTDevicesUseCase getFetchBondedBTDevicesUseCase() {
        return new FetchBondedBTDevicesUseCase(new BTDeviceModel());
    }

    public GestureDetectorCompat getGestureDetectorCompat(GestureDetector.OnGestureListener onGestureListener, GestureDetector.OnDoubleTapListener onDoubleTapListener) {
        GestureDetectorCompat gestureDetectorCompat = new GestureDetectorCompat(getActivity(), onGestureListener);
        gestureDetectorCompat.setOnDoubleTapListener(onDoubleTapListener);

        return gestureDetectorCompat;
    }

    public ScreenNavigator getScreenNavigator() {
        return new ScreenNavigator(getActivity());
    }

    public InputMethodManager getInputMethodManager() {
        return (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
    }

}
