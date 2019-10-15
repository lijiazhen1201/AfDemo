package cn.appoa.afdemo.fragment;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.SparseIntArray;

import java.io.IOException;

import cn.appoa.afdemo.base.BaseFragment;
import cn.appoa.afdemo.listener.ZmShakeListener;
import cn.appoa.aframework.utils.AsyncRun;
import cn.appoa.aframework.utils.AtyUtils;

/**
 * 摇一摇页面封装
 */
public abstract class ZmShakeFragment extends BaseFragment
        implements ZmShakeListener.OnShakeListener {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 初始化声音
        loadSound(getActivity());
        // 初始化监听
        initListener(getActivity());
    }

    protected SoundPool sndPool = null;
    protected SparseIntArray soundPoolMap = null;

    /**
     * 初始化声音
     *
     * @param context
     */
    protected void loadSound(final Context context) {
        sndPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 5);
        soundPoolMap = new SparseIntArray();
        sndPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {

            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                AtyUtils.i("SoundPool", "音频加载完毕");
            }
        });
        AsyncRun.runInBack(new Runnable() {
            @Override
            public void run() {
                try {
                    soundPoolMap.put(0, sndPool.load(context.getAssets().
                            openFd("sound/shake_sound_male.mp3"), 1));
                    soundPoolMap.put(1, sndPool.load(context.getAssets().
                            openFd("sound/shake_match.mp3"), 1));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected ZmShakeListener mShakeListener = null;

    /**
     * 初始化监听
     */
    protected void initListener(Context context) {
        mShakeListener = new ZmShakeListener(context);
        // 监听到手机摇动
        mShakeListener.setOnShakeListener(this);
    }

    @Override
    public void onShake() {
        // 摇一摇开始
        onShakeStart();
        // 开启动画
        initAnim();
    }

    /**
     * 摇一摇开始（某些初始化操作）
     */
    protected abstract void onShakeStart();

    /**
     * 开启动画（初始化动画）
     */
    protected abstract void initAnim();

    /**
     * 动画开始（在setAnimationListener中的onAnimationStart中调用）
     */
    protected void animStart() {
        // 动画开始，取消监听
        mShakeListener.stop();
        // 播放摇一摇声音
        sndPool.play(soundPoolMap.get(0), (float) 0.2, (float) 0.2, 0, 0, (float) 0.6);
    }

    /**
     * 动画结束（在setAnimationListener中的onAnimationEnd中调用）
     */
    protected void animEnd() {
        // 动画结束

        // 摇一摇结束
        onShakeFinish();
    }

    /**
     * 摇一摇结束（结束后掉接口，查看附近的人）
     */
    protected abstract void onShakeFinish();

    /**
     * 开始加载（开始掉接口之前调用）
     */
    protected void beforeLoading(CharSequence text) {
    }

    /**
     * 加载结束（接口访问完毕时候调用）
     */
    protected void afterLoading() {
        // 播放结束声音
        sndPool.play(soundPoolMap.get(1), (float) 0.2, (float) 0.2, 0, 0, (float) 0.6);
        // 重启监听
        mShakeListener.start();
    }

    @Override
    public void onDestroy() {
        if (mShakeListener != null) {
            // 取消监听
            mShakeListener.stop();
        }
        super.onDestroy();
    }

}
