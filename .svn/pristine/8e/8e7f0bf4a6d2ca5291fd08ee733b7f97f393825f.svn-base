package com.megabox.android.slide;

import android.app.Application;
import android.os.Bundle;

import com.zhy.autolayout.AutoLayoutActivity;

/**
 * 这个类用来管理 activity 的栈
 *
 * @author lihong
 * @since 2016/10/28
 */
public class ActivityInterfaceImpl extends AutoLayoutActivity implements ActivityInterface {

    private Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStackManager.addToStack(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStackManager.removeFromStack(this);
        if (mActivityLifecycleCallbacks != null) {
            mActivityLifecycleCallbacks.onActivityDestroyed(this);
        }
    }

    @Override
    public void setActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callbacks) {
        mActivityLifecycleCallbacks = callbacks;
    }
}
