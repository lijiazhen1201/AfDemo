package cn.appoa.aframework.widget.pager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.appoa.aframework.R;
import cn.appoa.aframework.utils.AsyncRun;

/**
 * 轮播图
 */
public class RollViewPager extends ViewPager {

    public RollViewPager(Context context) {
        super(context);
        this.addOnPageChangeListener(pageChageListener);
    }

    public RollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.addOnPageChangeListener(pageChageListener);
    }

    /**
     * 轮播点的ImageView
     */
    private List<ImageView> pointsImageView;

    /**
     * 当前选中页面下标
     */
    private int currentPosition = 0;

    /**
     * 页面切换的监听
     */
    private OnPageChangeListener pageChageListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            if (pointsImageView != null && pointsImageView.size() > 0) {
                for (int i = 0; i < pointsImageView.size(); i++) {
                    pointsImageView.get(i).setImageResource(point_normal != 0 ? point_normal : R.drawable.point_normal);
                }
                pointsImageView.get(position)
                        .setImageResource(point_selected != 0 ? point_selected : R.drawable.point_selected);
            }
            currentPosition = position;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * 未选中点
     */
    private int point_normal;

    /**
     * 选中点
     */
    private int point_selected;

    /**
     * 设置轮播点 需在initPointList前调用
     *
     * @param point_normal
     * @param point_selected
     */
    public void setPointResource(int point_normal, int point_selected) {
        this.point_normal = point_normal;
        this.point_selected = point_selected;
    }

    /**
     * 此方法必须调用，设置ViewPager的适配器
     *
     * @param adapter ViewPager的适配器
     */
    public void setMyAdapter(PagerAdapter adapter) {
        RollViewPager.this.setAdapter(adapter);
        createHandler();
    }

    /**
     * 轮播图总数
     */
    private int totalSize;

    /**
     * 此方法必须调用，设置轮播点的布局
     *
     * @param totalSize 轮播图总数
     * @param points    显示轮播点的LinearLayout
     */
    public void initPointList(int totalSize, LinearLayout points) {
        this.totalSize = totalSize;
        float scale = getContext().getResources().getDisplayMetrics().density;
        int marginLeft = (int) (4 * scale + 0.5f);
        pointsImageView = new ArrayList<ImageView>();
        for (int i = 0; i < totalSize; i++) {
            ImageView point = new ImageView(getContext());
            if (i == 0) {
                // 默认选中第一个
                point.setImageResource(point_selected != 0 ? point_selected : R.drawable.point_selected);
            } else {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(marginLeft, 0, 0, 0);
                point.setLayoutParams(layoutParams);
                point.setImageResource(point_normal != 0 ? point_normal : R.drawable.point_normal);
            }
            pointsImageView.add(point);
        }
        points.removeAllViews();
        points.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < pointsImageView.size(); i++) {
            points.addView(pointsImageView.get(i));
        }
    }

    private int lastX;
    private int lastY;
    private boolean isFirst = true;
    @SuppressWarnings("unused")
    private boolean isMine;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int moveX = (int) ev.getX();
        int moveY = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) ev.getX();
                lastY = (int) ev.getY();
                cancel();
                break;
            case MotionEvent.ACTION_MOVE:
                int disX = Math.abs(moveX - lastX);
                int disY = Math.abs(moveY - lastY);
                if (isFirst) {
                    if (disX > disY - 2) {
                        isMine = true;
                        getParent().requestDisallowInterceptTouchEvent(true);
                    } else {
                        isMine = false;
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    isFirst = false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isFirst = true;
                isMine = false;
                go();
                lastX = 0;
                lastY = 0;
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private Handler handler;

    /**
     * Handler的what标记
     */
    private final int WHAT_GO = 1000;

    /**
     * 轮播时间
     */
    private int byTime = 5;

    /**
     * 设置轮播时间间隔，默认5秒
     *
     * @param byTime
     */
    public void setByTime(int byTime) {
        this.byTime = byTime;
    }

    private synchronized void createHandler() {
        if (handler == null) {
            AsyncRun.runInMain(new Runnable() {
                @Override
                public void run() {
                    handler = new MyHandler(RollViewPager.this);
                    go();
                }
            });
        }
    }

    static class MyHandler extends Handler {

        private WeakReference<RollViewPager> mOuter;

        public MyHandler(RollViewPager activity) {
            mOuter = new WeakReference<RollViewPager>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            RollViewPager outer = mOuter.get();
            if (outer != null) {// Do something with outer as your wish.
                if (msg.what == outer.WHAT_GO) {
                    if (outer.totalSize == 0) {
                        return;
                    }
                    if (outer != null) {
                        outer.currentPosition++;
                        outer.setCurrentItem(outer.currentPosition % outer.totalSize);
                    }
                    sendEmptyMessageDelayed(outer.WHAT_GO, outer.byTime * 1000);
                }
            }
        }
    }

    /**
     * 是否轮播
     */
    public boolean isGoing;

    /**
     * 开启轮播
     */
    public void go() {
        isGoing = true;
        if (handler == null) {
            createHandler();
        } else {
            handler.removeMessages(WHAT_GO);
            handler.sendEmptyMessageDelayed(WHAT_GO, byTime * 1000);
        }
    }

    /**
     * 取消轮播
     */
    public void cancel() {
        isGoing = false;
        if (handler != null) {
            handler.removeMessages(WHAT_GO);
        }
    }

}
