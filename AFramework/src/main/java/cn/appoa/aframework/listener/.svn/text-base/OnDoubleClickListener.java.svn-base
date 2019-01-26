package cn.appoa.aframework.listener;

import android.view.MotionEvent;
import android.view.View;

/**
 * 双击点击事件
 * https://blog.csdn.net/zuo_er_lyf/article/details/80068006
 */
public abstract class OnDoubleClickListener implements View.OnTouchListener {

    private int count = 0;//点击次数
    private long firstClick = 0;//第一次点击时间
    private long secondClick = 0;//第二次点击时间

    /**
     * 两次点击时间间隔，单位毫秒
     */
    private final int totalTime = 800;


    /**
     * 触摸事件处理
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getAction()) {//按下
            count++;
            if (1 == count) {
                firstClick = System.currentTimeMillis();//记录第一次点击时间
            } else if (2 == count) {
                secondClick = System.currentTimeMillis();//记录第二次点击时间
                if (secondClick - firstClick < totalTime) {//判断二次点击时间间隔是否在设定的间隔时间之内
                    count = 0;
                    firstClick = 0;
                    return onDoubleClick(v);
                } else {
                    count = 1;
                    firstClick = secondClick;
                }
                secondClick = 0;
            }
        }
        return false;
    }

    /**
     * 双击事件的回调
     *
     * @param v
     */
    public abstract boolean onDoubleClick(View v);
}
