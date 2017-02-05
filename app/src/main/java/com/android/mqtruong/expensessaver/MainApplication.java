package com.android.mqtruong.expensessaver;

import android.app.Application;

/**
 * Created by Mai on 2/4/2017.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppRater.app_launched(this);
    }
}
