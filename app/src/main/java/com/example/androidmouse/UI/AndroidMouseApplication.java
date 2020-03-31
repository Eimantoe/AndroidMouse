package com.example.androidmouse.UI;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.androidmouse.Common.Constants;
import com.example.androidmouse.Common.DependencyInjection.RootComposition;

import static com.example.androidmouse.Common.Constants.FIRST_TIME_LOADING;

public class AndroidMouseApplication extends Application {

    private RootComposition mRootComposition;
    private boolean mFirstTimeLoading;

    @Override
    public void onCreate() {
        super.onCreate();

        mRootComposition = new RootComposition(this);

        mFirstTimeLoading = mRootComposition.getSharedPreferences().getBoolean(FIRST_TIME_LOADING, true);

        if (mFirstTimeLoading) {

            // show dialogs


            SharedPreferences.Editor sharedPreferencesEditor = mRootComposition.getSharedPreferences().edit();
            sharedPreferencesEditor.putBoolean("FIRST_TIME_LOADING", false);
            sharedPreferencesEditor.apply();
        }
    }

    public RootComposition getRootComposition() {
        return mRootComposition;
    }
}
