package com.wzy.floattext;

import android.app.Application;
import android.provider.Settings;

public class FloatApplication extends Application {
    public static FloatApplication instance;

    public static FloatApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
