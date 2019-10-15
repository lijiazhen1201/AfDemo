package cn.appoa.afdemo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import java.util.Arrays;

import cn.appoa.afdemo.base.BaseActivity;
import cn.appoa.afdemo.dialog.RequestPermissionsDialog;
import cn.appoa.aframework.listener.OnCallbackListener;
import cn.appoa.aframework.permissions.PermissionUtils;


/**
 * 启动页
 */
public class StartActivity extends BaseActivity
        implements Animation.AnimationListener {

    @Override
    public void initContent(Bundle savedInstanceState) {
        setContent(R.layout.activity_start);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    private ImageView iv_start;

    @Override
    public void initView() {
        super.initView();
        iv_start = findViewById(R.id.iv_start);
    }

    @Override
    public void initData() {
        startAnim();
    }

    /**
     * 渐变展示启动屏
     */
    protected void startAnim() {
        AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
        aa.setDuration(3000);
        iv_start.startAnimation(aa);
        aa.setAnimationListener(this);
    }

    @Override
    public void onAnimationStart(Animation animation) {
        iv_start.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        String[] permissions = new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.RECORD_AUDIO,
        };
        if (PermissionUtils.hasPermission(StartActivity.this, Arrays.asList(permissions))) {
            endAnim();
        } else {
            new RequestPermissionsDialog(StartActivity.this, new OnCallbackListener() {
                @Override
                public void onCallback(int type, Object... obj) {
                    endAnim();
                }
            }).showRequestPermissionsDialog(StartActivity.this, permissions);
        }
    }

    /**
     * 动画结束
     */
    protected void endAnim() {
        startActivity(new Intent(StartActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(R.anim.start_alpha, R.anim.end_alpha);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //有极光推送添加以下代码
        //JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //有极光推送添加以下代码
        //JPushInterface.onPause(this);
    }

    @Override
    public boolean enableSliding() {
        //禁止侧滑返回
        return false;
    }

}
