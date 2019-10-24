package cn.appoa.aframework.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.lzy.okgo.OkGo;
import com.megabox.android.slide.SlideBackActivity;

import cn.appoa.aframework.R;
import cn.appoa.aframework.dialog.DefaultLoadingDialog;
import cn.appoa.aframework.manager.Foreground;
import cn.appoa.aframework.presenter.BasePresenter;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.utils.AtyUtils;
import cn.appoa.aframework.view.IBaseView;
import zm.bus.event.BusProvider;
import zm.http.volley.ZmVolley;

/**
 * Activity基类
 */
public abstract class AfActivity<P extends BasePresenter> extends SlideBackActivity
        implements IBaseView, Foreground.ForegroundListener {

    /**
     * Tag
     */
    protected String REQUEST_TAG;
    /**
     * Activity
     */
    protected Activity mActivity = null;
    /**
     * Fragment管理
     */
    protected FragmentManager mFragmentManager = null;
    /**
     * 输入法管理
     */
    protected InputMethodManager mInputMethodManager = null;
    /**
     * 根布局
     */
    protected RelativeLayout rootlayout;
    /**
     * 内容根布局
     */
    protected LinearLayout layout;
    /**
     * 标题
     */
    protected BaseTitlebar titlebar;
    /**
     * 内容
     */
    protected FrameLayout content;
    /**
     * 底部
     */
    protected View bottom;
    /**
     * Presenter
     */
    protected P mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        REQUEST_TAG = String.valueOf(System.currentTimeMillis());
        // 去标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 横竖屏设置
        setRequestedOrientation(isScreenOrientationLandscape() ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 防止启动页重复初始化 https://blog.csdn.net/love100628/article/details/43238135
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) &&
                    TextUtils.equals(action, Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        // 滑动
        setSlideable(enableSliding());
        // 初始化
        mActivity = this;
        mFragmentManager = getSupportFragmentManager();
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 注册事件总线
        BusProvider.getInstance().register(this);
        // 传递数据
        initIntent(getIntent());
        // 加载布局
        initContentView(savedInstanceState);
        bindButterKnife();
        Foreground.get(this).addForegroundListener(this);
        initView();
        mPresenter = initPresenter();
        if (mPresenter != null) {
            mPresenter.onCreate(savedInstanceState);
            onAttachView();
        }
        initData();
    }

    /**
     * 加载布局
     *
     * @param savedInstanceState
     */
    protected void initContentView(Bundle savedInstanceState) {
        rootlayout = new RelativeLayout(this);
        rootlayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorBgLighterGray));
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        titlebar = initTitlebar();
        if (titlebar != null) {
            layout.addView(titlebar, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
        content = new FrameLayout(this);
        content.setId(R.id.fl_fragment);
        initContent(savedInstanceState);
        layout.addView(content, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        bottom = initBottomView();
        if (bottom != null) {
            layout.addView(bottom, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
        rootlayout.addView(layout, new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        setContentView(rootlayout);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPresenter != null) {
            mPresenter.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unBindButterKnife();
        BusProvider.getInstance().unregister(this);
        if (mPresenter != null) {
            mPresenter.onDetach();
            mPresenter.onDestroy();
            mPresenter = null;
        }
        ZmVolley.cancelRequest(REQUEST_TAG);
        //取消全局默认的OKHttpClient中标识为tag的请求
        OkGo.getInstance().cancelTag(REQUEST_TAG);
        Foreground.get(this).removeForegroundListener(this);
    }

    /**
     * 是否为横屏
     */
    public boolean isScreenOrientationLandscape() {
        return false;
    }

    /**
     * 初始化Titlebar
     */
    public BaseTitlebar initTitlebar() {
        return null;
    }

    /**
     * 初始化底部
     */
    public View initBottomView() {
        return null;
    }

    /**
     * 获取Intent传递的数据
     */
    public void initIntent(Intent intent) {

    }

    /**
     * 初始化Presenter
     */
    public P initPresenter() {
        return null;
    }

    /**
     * 绑定view
     */
    public void onAttachView() {
        if (mPresenter != null) {
            mPresenter.onAttach(this);
        }
    }

    /**
     * 初始化内容布局
     */
    public abstract void initContent(Bundle savedInstanceState);

    /**
     * 初始化控件
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 设置内容
     */
    public void setContent(@LayoutRes int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID, null);
        setContent(view);
    }

    /**
     * 设置内容
     */
    public void setContent(@Nullable View view) {
        if (view == null || content == null)
            return;
        content.addView(view, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 显示加载框
     */
    @Override
    public void showLoading(CharSequence message) {
        DefaultLoadingDialog.getInstance().showLoading(this, message);
    }

    /**
     * 隐藏加载框
     */
    @Override
    public void dismissLoading() {
        DefaultLoadingDialog.getInstance().dismissLoading();
    }

    /**
     * 显示软键盘
     */
    @Override
    public void showSoftKeyboard() {
        if (mInputMethodManager != null) {
            mInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 隐藏软键盘
     */
    @Override
    public void hideSoftKeyboard() {
        if (mInputMethodManager != null && getCurrentFocus() != null) {
            mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public String getRequestTag() {
        return REQUEST_TAG;
    }

    @Override
    public void onErrorCodeResponse(String message) {
        //TODO 强制下线
    }

    /**
     * 返回键
     */
    public void back(View view) {
        onBackPressed();
    }

    /**
     * 回到桌面
     */
    public void goBackHome() {
        Intent backHome = new Intent(Intent.ACTION_MAIN);
        backHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        backHome.addCategory(Intent.CATEGORY_HOME);
        startActivity(backHome);
    }

    /**
     * 退出间隔时间
     */
    private long intervalTime = 0;

    /**
     * 再按一次返回键退出
     */
    protected void doubleClickExit(long exitTime, CharSequence exitMessage) {
        if ((System.currentTimeMillis() - intervalTime) > exitTime) {
            AtyUtils.showShort(mActivity, exitMessage, false);
            intervalTime = System.currentTimeMillis();
        } else {
            // AtyManager.getInstance().exitApp();
            finish();
        }
    }

    @TargetApi(23)
    public void requestPermissions(String[] permissions, PermissionsResultAction action) {
        // 请求获取权限
        if (permissions != null && permissions.length > 0) {
            PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this, permissions, action);
        }
    }

    @TargetApi(23)
    public void requestAllPermissions() {
        // 请求获取全部权限，调用权限管理类，然后放进去需要申请的权限，通过接口回调的方法获得权限获取结果
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                // All permissions have been granted
            }

            @Override
            public void onDenied(String permission) {
                // "Permission " + permission + " has been denied"
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // 因为权限管理类无法监听系统，所以需要重写onRequestPermissionResult方法，更新权限管理类，并回调结果。这个是必须要有的
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    @Override
    public void onBecameForeground() {
        // 切换为前台

    }

    @Override
    public void onBecameBackground() {
        // 切换为后台

    }

    public void bindButterKnife() {
        // 绑定注解
        // ButterKnife.bind(this);
    }

    public void unBindButterKnife() {
        // 解绑注解
        //ButterKnife.unbind(this);
    }

    /**
     * 是否允许滑动
     */
    public boolean enableSliding() {
        return false;
    }

}
