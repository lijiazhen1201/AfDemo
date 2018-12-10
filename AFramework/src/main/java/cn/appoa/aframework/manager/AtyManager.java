package cn.appoa.aframework.manager;

import android.app.Activity;

import java.util.Stack;

/**
 * Activity管理
 */
public final class AtyManager {

    /**
     * Activity管理实例
     */
    private static AtyManager instance;

    /**
     * activity栈
     */
    private Stack<Activity> activityStack;

    /**
     * 保证只有一个Activity管理实例
     */
    private AtyManager() {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
    }

    /**
     * 获取Activity管理实例 ,单例模式
     */
    public static AtyManager getInstance() {
        if (instance == null)
            instance = new AtyManager();
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack != null && activity != null) {
            activityStack.add(activity);
        }
    }

    /**
     * 获取栈顶的Activity，先进后出原则
     */
    public Activity getLastActivity() {
        if (activityStack != null && activityStack.size() > 0) {
            return activityStack.lastElement();
        }
        return null;
    }

    /**
     * 得到指定activity前一个activity的实例
     */
    public Activity getPreviousActivity(Activity curActivity) {
        Activity preActivity = null;
        if (curActivity != null) {
            for (int i = activityStack.size() - 1; i >= 0; i--) {
                Activity activity = activityStack.get(i);
                if (activity == curActivity) {
                    int p = i - 1;
                    if (p >= 0) {
                        preActivity = activityStack.get(p);
                    }
                }
            }
        }
        return preActivity;
    }

    /**
     * 移除Activity到堆栈
     */
    public void removeActivity(Activity activity) {
        if (activityStack != null && activityStack.size() > 0 && activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 结束Activity
     */
    public void finishActivity(Activity activity) {
        if (activityStack != null && activityStack.size() > 0 && activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack != null && activityStack.size() > 0) {
            for (int i = 0; i < activityStack.size(); i++) {
                if (activityStack.get(i) != null) {
                    activityStack.get(i).finish();
                }
            }
            activityStack.clear();
        }
    }

    /**
     * 退出应用程序
     */
    public void exitApp() {
        try {
            finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.gc();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
