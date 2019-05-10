package cn.appoa.aframework.widget.gridpassword;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class GridPasswordLayout4 extends RelativeLayout {

    public GridPasswordLayout4(Context context) {
        super(context);
    }

    public GridPasswordLayout4(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridPasswordLayout4(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        int childHeightSize = getMeasuredHeight();
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize * 4, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
