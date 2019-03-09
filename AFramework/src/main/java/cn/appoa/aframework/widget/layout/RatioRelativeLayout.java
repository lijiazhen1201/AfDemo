package cn.appoa.aframework.widget.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import cn.appoa.aframework.R;

public class RatioRelativeLayout extends RelativeLayout {

    public RatioRelativeLayout(Context context) {
        super(context);
        init(context, null);
    }

    public RatioRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RatioRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    // 宽高比
    private float ratio;

    /**
     * 设置宽高比
     *
     * @param ratio
     */
    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    /**
     * 设置宽高比
     *
     * @param ratio_width
     * @param ratio_height
     */
    public void setRatio(int ratio_width, int ratio_height) {
        if (ratio_width > 0 && ratio_height >= 0) {
            setRatio((ratio_height * 1.0f) / (ratio_width * 1.0f));
        }
    }

    private void init(Context context, AttributeSet attrs) {
        ratio = 0.0f;
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RatioRelativeLayout);
            ratio = array.getFloat(R.styleable.RatioRelativeLayout_ratio, 0.0f);
            if (ratio <= 0.0f) {
                int ratio_width = array.getInteger(R.styleable.RatioRelativeLayout_ratio_width, 1);
                int ratio_height = array.getInteger(R.styleable.RatioRelativeLayout_ratio_height, 0);
                if (ratio_width > 0 && ratio_height >= 0) {
                    ratio = (ratio_height * 1.0f) / (ratio_width * 1.0f);
                }
            }
            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        if (ratio > 0) {
//            // 获取宽度的模式和尺寸
//            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//            // 获取高度的模式和尺寸
//            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//            // 宽确定，高不确定
//            if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY && ratio != 0) {
//                heightSize = (int) (widthSize * ratio + 0.5f);// 根据宽度和比例计算高度
//                heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
//            } else if (widthMode != MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY & ratio != 0) {
//                widthSize = (int) (heightSize / ratio + 0.5f);
//                widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
//            } else {
//                throw new RuntimeException("无法设定宽高比");
//            }
//            // 必须调用下面的两个方法之一完成onMeasure方法的重写，否则会报错
//            // super.onMeasure(widthMeasureSpec,heightMeasureSpec);
//            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
//        } else {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        }

        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        int childHeightSize = getMeasuredHeight();
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(ratio > 0 ? (int) (childWidthSize * ratio)
                : childHeightSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
