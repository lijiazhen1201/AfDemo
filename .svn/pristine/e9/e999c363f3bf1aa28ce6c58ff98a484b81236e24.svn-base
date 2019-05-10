package cn.appoa.afdemo.app;

import android.content.Context;
import android.support.multidex.MultiDex;

import cn.appoa.aframework.app.AfApplication;

public class MyApplication extends AfApplication {

    @Override
    public void initApplication() {
        // 分包
        MultiDex.install(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 分包
        MultiDex.install(base);
    }
}
