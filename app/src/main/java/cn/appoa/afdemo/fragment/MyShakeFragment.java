package cn.appoa.afdemo.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.appoa.afdemo.R;
import cn.appoa.aframework.utils.AsyncRun;
import cn.appoa.aframework.utils.AtyUtils;

/**
 * 摇一摇实现类
 */
public class MyShakeFragment extends ZmShakeFragment {

    @Override
    public View initFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_shake, container,false);
    }

    private ImageView shakeBg;
    private LinearLayout shakeContent;
    private RelativeLayout shakeImgUp;
    private RelativeLayout shakeImgDown;
    private LinearLayout shakeLoading;
    private View shakeLineUp;
    private View shakeLineDown;
    private TextView shakeText;

    @Override
    public void initView(View view) {
        shakeBg = (ImageView) view.findViewById(R.id.shakeBg);
        shakeContent = (LinearLayout) view.findViewById(R.id.shake_content);
        shakeImgUp = (RelativeLayout) view.findViewById(R.id.shakeImgUp);
        shakeImgDown = (RelativeLayout) view.findViewById(R.id.shakeImgDown);
        shakeLoading = (LinearLayout) view.findViewById(R.id.shake_loading);
        shakeLineUp = view.findViewById(R.id.shakeLineUp);
        shakeLineDown = view.findViewById(R.id.shakeLineDown);
        shakeText = (TextView) view.findViewById(R.id.shakeText);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onShakeStart() {

    }

    /**
     * 动画时间
     */
    private final int DURATION_TIME = 600;

    @Override
    protected void initAnim() {
        AnimationSet animup = new AnimationSet(true);
        TranslateAnimation mytranslateanimup0 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -0.5f);
        mytranslateanimup0.setDuration(DURATION_TIME);
        TranslateAnimation mytranslateanimup1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, +0.5f);
        mytranslateanimup1.setDuration(DURATION_TIME);
        mytranslateanimup1.setStartOffset(DURATION_TIME);
        animup.addAnimation(mytranslateanimup0);
        animup.addAnimation(mytranslateanimup1);
        shakeImgUp.startAnimation(animup);

        AnimationSet animdn = new AnimationSet(true);
        TranslateAnimation mytranslateanimdn0 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, +0.5f);
        mytranslateanimdn0.setDuration(DURATION_TIME);
        TranslateAnimation mytranslateanimdn1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -0.5f);
        mytranslateanimdn1.setDuration(DURATION_TIME);
        mytranslateanimdn1.setStartOffset(DURATION_TIME);
        animdn.addAnimation(mytranslateanimdn0);
        animdn.addAnimation(mytranslateanimdn1);
        shakeImgDown.startAnimation(animdn);

        // 动画监听，开始时显示加载状态，
        mytranslateanimdn0.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                animStart();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }
        });

        mytranslateanimdn1.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animEnd();
            }
        });
    }

    @Override
    protected void animStart() {
        super.animStart();
        // 隐藏线出现
        shakeLineUp.setVisibility(View.VISIBLE);
        shakeLineDown.setVisibility(View.VISIBLE);
    }

    @Override
    protected void animEnd() {
        super.animEnd();
        // 再次隐藏线
        shakeLineUp.setVisibility(View.GONE);
        shakeLineDown.setVisibility(View.GONE);
    }

    @Override
    protected void beforeLoading(CharSequence text) {
        super.beforeLoading(text);
        // 显示进度
        shakeLoading.setVisibility(View.VISIBLE);
        shakeText.setText(text);
    }

    @Override
    protected void afterLoading() {
        super.afterLoading();
        // 隐藏进度
        shakeLoading.setVisibility(View.GONE);
    }

    @Override
    protected void onShakeFinish() {
        //TODO 模拟掉接口
        beforeLoading("正在搜寻同一时刻摇晃手机的人");
        AsyncRun.runInBack(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                AsyncRun.runInMain(new Runnable() {//切换主线程
                    @Override
                    public void run() {
                        afterLoading();
                        AtyUtils.showShort(mActivity, "同一时刻没有用户摇晃手机", false);
                    }
                });
            }
        });
    }
}
