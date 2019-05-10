package cn.appoa.afdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class UpgradeLayout01 extends RelativeLayout {

    public UpgradeLayout01(Context context) {
        super(context);
    }

    public UpgradeLayout01(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UpgradeLayout01(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        int childHeightSize = getMeasuredHeight();
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize * 338 / 278, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
