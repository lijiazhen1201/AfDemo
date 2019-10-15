package cn.appoa.aframework.manager;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Usage: Android 前台与后台监听
 * <p>
 * 1. Get the Foreground Singleton, passing a Context or Application object
 * unless you are sure that the Singleton has definitely already been
 * initialised elsewhere.
 * <p>
 * 2.a) Perform a direct, synchronous check: Foreground.isForeground() /
 * .isBackground()
 * <p>
 * or
 * <p>
 * 2.b) Register to be notified (useful in Service or other non-UI components):
 * <p>
 * Foreground.ForegroundListener myListener = new
 * Foreground.ForegroundListener(){ public void onBecameForeground(){ // ...
 * whatever you want to do } public void onBecameBackground(){ // ... whatever
 * you want to do } }
 *
 * @Override protected void onCreate(){ super.onCreate();
 * Foreground.get(this).addForegroundListener(listener); }
 * @Override protected void onDestroy(){ super.onDestroy();
 * Foreground.get(this).removeForegroundListener(listener); }
 */
public class Foreground implements Application.ActivityLifecycleCallbacks {

    public static final String TAG = Foreground.class.getSimpleName();
    public static final long CHECK_DELAY = 500;
    private static Foreground instance;
    private boolean foreground = false, paused = true;
    private Handler handler = new Handler();
    private List<ForegroundListener> listeners = new CopyOnWriteArrayList<ForegroundListener>();
    private Runnable check;
    private Class<Activity> clazz;//SplashActivity(启动页)

    /**
     * Its not strictly necessary to use this method - _usually_ invoking get with a
     * Context gives us a path to retrieve the Application and initialise, but
     * sometimes (e.g. in test harness) the ApplicationContext is != the
     * Application, and the docs make no guarantees.
     *
     * @param application
     * @return an initialised Foreground instance
     */
    public static Foreground init(Application application) {
        if (instance == null) {
            instance = new Foreground();
            application.registerActivityLifecycleCallbacks(instance);
        }
        return instance;
    }

    public static Foreground get(Application application) {
        if (instance == null) {
            init(application);
        }
        return instance;
    }

    public static Foreground get(Context ctx) {
        if (instance == null) {
            Context appCtx = ctx.getApplicationContext();
            if (appCtx instanceof Application) {
                init((Application) appCtx);
            }
            throw new IllegalStateException(
                    "Foreground is not initialised and " + "cannot obtain the Application object");
        }
        return instance;
    }

    public static Foreground get() {
        if (instance == null) {
            throw new IllegalStateException(
                    "Foreground is not initialised - invoke " + "at least once with parameterised init/get");
        }
        return instance;
    }

    public Class<Activity> getSplashClazz() {
        return clazz;
    }

    public void setSplashClazz(Class<Activity> clazz) {
        this.clazz = clazz;
    }

    public boolean isForeground() {
        return foreground;
    }

    public boolean isBackground() {
        return !foreground;
    }

    public void addForegroundListener(ForegroundListener listener) {
        listeners.add(listener);
    }

    public void removeForegroundListener(ForegroundListener listener) {
        listeners.remove(listener);
    }

    public interface ForegroundListener {

        void onBecameForeground();

        void onBecameBackground();
    }

    @Override
    public void onActivityResumed(Activity activity) {
        paused = false;
        boolean wasBackground = !foreground;
        foreground = true;
        if (check != null)
            handler.removeCallbacks(check);
        if (wasBackground) {
            Log.d(TAG, "went foreground");
            for (ForegroundListener l : listeners) {
                try {
                    l.onBecameForeground();
                } catch (Exception exc) {
                    Log.e(TAG, "Listener threw exception!:" + exc.toString());
                }
            }
        } else {
            Log.d(TAG, "still foreground");
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        paused = true;
        if (check != null)
            handler.removeCallbacks(check);
        handler.postDelayed(check = new Runnable() {
            @Override
            public void run() {
                if (foreground && paused) {
                    foreground = false;
                    Log.d(TAG, "went background");
                    for (ForegroundListener l : listeners) {
                        try {
                            l.onBecameBackground();
                        } catch (Exception exc) {
                            Log.e(TAG, "Listener threw exception!:" + exc.toString());
                        }
                    }
                } else {
                    Log.d(TAG, "still foreground");
                }
            }
        }, CHECK_DELAY);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        // 若bundle不为空则程序异常结束
        if (bundle != null) {
            // 重启整个程序
            if (clazz != null) {
                Intent intent = new Intent(activity, clazz);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        } else {
            AtyManager.getInstance().addActivity(activity);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        AtyManager.getInstance().removeActivity(activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    private int foregroundActivities = 0;
    private boolean isChangingConfiguration;

    @Override
    public void onActivityStarted(Activity activity) {
        foregroundActivities++;
        if (foregroundActivities == 1 && !isChangingConfiguration) {
            // 应用切到前台
            if (listener != null) {
                listener.onStarted(activity);
            }
        }
        isChangingConfiguration = false;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        foregroundActivities--;
        if (foregroundActivities == 0) {
            // 应用切到后台
            if (listener != null) {
                listener.onStopped(activity);
            }
        }
        isChangingConfiguration = activity.isChangingConfigurations();
    }

    private ConfigurationsListener listener;

    public ConfigurationsListener getConfigurationsListener() {
        return listener;
    }

    public void setConfigurationsListener(ConfigurationsListener listener) {
        this.listener = listener;
    }

    public interface ConfigurationsListener {

        void onStarted(Activity activity);

        void onStopped(Activity activity);

    }
}
