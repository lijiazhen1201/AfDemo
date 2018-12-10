package com.cjt2325.cameralibrary.local;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class LocalVideoLayout extends RelativeLayout {

    public LocalVideoLayout(Context context) {
        super(context);
    }

    public LocalVideoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LocalVideoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        int childHeightSize = getMeasuredHeight();
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize * 1 / 1, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
