package com.example.androidmouse.Common.DependencyInjection;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.provider.Settings;

import com.example.androidmouse.Common.Constants;
import com.example.androidmouse.UI.AndroidMouseApplication;
import com.example.androidmouse.UI.Common.Factories.ViewFactory;

import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

public class RootComposition {

    private final AndroidMouseApplication mAndroidMouseApplication;

    public RootComposition(AndroidMouseApplication androidMouseApplication) {

        this.mAndroidMouseApplication = androidMouseApplication;
    }

    public Context getContext() {
        return mAndroidMouseApplication;
    }

    public Application getApplication() {
        return mAndroidMouseApplication;
    }

    public SensorManager getSensorManager()
    {
        return (SensorManager) getContext().getSystemService(getContext().SENSOR_SERVICE);
    }

    public SharedPreferences getSharedPreferences() {
        return getContext().getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
    }

    public Sensor getLinearAccelerationSensor()
    {
        return getSensorManager().getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    public Sensor getGyroscopeSensor()
    {
        return getSensorManager().getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    public UUID getDeviceUUID()
    {
        return UUID.fromString(Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID));
    }

}
