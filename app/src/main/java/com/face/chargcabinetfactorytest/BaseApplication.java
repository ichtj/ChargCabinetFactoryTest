package com.face.chargcabinetfactorytest;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.face_chtj.base_iotutils.BaseIotUtils;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BaseIotUtils.instance().create(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
