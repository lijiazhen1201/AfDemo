package cn.appoa.aframework.widget.pager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.WindowManager;

/**
 * 自定义ViewPager做为父ViewPager控件
 */
public class ParentViewPager extends ViewPager {

    private int childVPHeight = 0;

    public ParentViewPager(Context context) {
        super(context);
        init(context);
    }

    public ParentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @SuppressWarnings({"static-access", "deprecation"})
    private void init(Context context) {
        // 获取屏幕宽高
        WindowManager windowManager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        int disWidth = windowManager.getDefaultDisplay().getWidth();
        // 根据屏幕的密度来过去dp值相应的px值
        childVPHeight = (int) (context.getResources().getDisplayMetrics().density * disWidth + 0.5f);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // 触摸在子ViewPager所在的页面和子ViewPager控件高度之内时
        // 返回false，此时将会将触摸的动作传给子ViewPager
        if (getCurrentItem() == 1 && event.getY() < childVPHeight) {
            return false;
        }
        return super.onInterceptTouchEvent(event);
    }

    // 此方法虽然简单可行，但是会出现，子ViewPager如果为ScrollView的时候，
    // 子ViewPager虽然已经滑动到看不到的地方，但是设定的高度内还是不能让父ViewPager左右滑动，
    // onTouch的动作透过了父Viewpager传递到了子控件
}
