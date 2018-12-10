package cn.appoa.aframework.widget.noscroll;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 不能滚动的RecyclerView
 */
public class NoScrollRecyclerView extends RecyclerView {

    public NoScrollRecyclerView(Context context) {
        super(context);
    }

    public NoScrollRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置不滚动
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }

}
