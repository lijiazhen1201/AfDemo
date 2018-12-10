package cn.appoa.aframework.widget.pager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import java.util.List;

import cn.appoa.aframework.R;

/**
 * 淘宝头条
 */
public class UPMarqueeView extends ViewFlipper {

    public UPMarqueeView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public UPMarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private Context mContext;
    private boolean isSetAnimDuration = false;
    private int interval = 5000;// 滚动时间

    /**
     * 动画时间
     */
    private int animDuration = 1000;

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        setFlipInterval(interval);
        Animation animIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_in);
        if (isSetAnimDuration)
            animIn.setDuration(animDuration);
        setInAnimation(animIn);
        Animation animOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_out);
        if (isSetAnimDuration)
            animOut.setDuration(animDuration);
        setOutAnimation(animOut);
    }

    /**
     * 设置循环滚动的View数组
     *
     * @param views
     */
    public void setViews(List<View> views) {
        if (views == null || views.size() == 0)
            return;
        removeAllViews();
        for (int i = 0; i < views.size(); i++) {
            addView(views.get(i));
        }
        startFlipping();
    }
}
