package cn.appoa.afdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class UpgradeLayout02 extends RelativeLayout {

    public UpgradeLayout02(Context context) {
        super(context);
    }

    public UpgradeLayout02(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UpgradeLayout02(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        int childHeightSize = getMeasuredHeight();
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize * 410 / 276, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
