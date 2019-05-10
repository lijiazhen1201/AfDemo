package cn.appoa.aframework.widget.layout;

/**
 * Created by Carbs.Wang on 2016/5/12.
 * https://github.com/Carbs0126/MaxHeightView
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.FrameLayout;
import cn.appoa.aframework.R;

/**
 *
 * A ViewGroup which can limit the max height of its child view, used on android platform
 *
 *  How to use:
 *  you can add MaxHeightLayout in xml layout file,and add attr app:max_height_ratio="float in[0,1]" or app:max_height_dimen="dimen".
 *  max_height_ratio refers to the max height ratio compare to the device screen height.
 *  max_height_dimen refers to the max height dimension.
 *
 *  <cn.appoa.aframework.widget.layout.MaxHeightLayout
 *      android:layout_width="match_parent"
 *      android:layout_height="wrap_content"
 *      app:max_height_ratio="0.3">
 *
 *      <ScrollView
 *          android:layout_width="match_parent"
 *          android:layout_height="wrap_content">
 *
 *          <LinearLayout
 *              android:layout_width="match_parent"
 *              android:layout_height="wrap_content"
 *              android:orientation="vertical">
 *
 *              // add your content view here
 *
 *          </LinearLayout>
 *      </ScrollView>
 *  </cn.appoa.aframework.widget.layout.MaxHeightLayout>
 *
 *  Analytical procedure:
 *  1.if neighter max_height_dimen nor max_height_ratio set, the max height will be
 *    DEFAULT_MAX_RATIO_WITHOUT_ARGU * device screen height.
 *  2.if you just set max_height_ratio, then the max height will be max_height_ratio * device screen height.
 *  3.if you just set max_height_dimen, then the max height will be max_height_dimen.
 *  4.if you set both max_height_dimen and max_height_ratio, then the max height will be the minume size.
 *
 * @author Carbs.Wang
 */
public class MaxHeightLayout extends FrameLayout {

    private static final float DEFAULT_MAX_RATIO_WITHOUT_ARGU = 0.6f;
    private static final float DEFAULT_MAX_RATIO = 0f;
    private static final float DEFAULT_MAX_DIMEN = 0f;

    private float mMaxRatio = DEFAULT_MAX_RATIO;
    private float mMaxDimen = DEFAULT_MAX_DIMEN;
    private float mMaxHeight = 0;

    public MaxHeightLayout(Context context) {
        super(context);
        init();
    }

    public MaxHeightLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init();
    }

    public MaxHeightLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MaxHeightLayout);
        final int count = a.getIndexCount();
        for (int i = 0; i < count; ++i) {
            int attr = a.getIndex(i);
            if(attr == R.styleable.MaxHeightLayout_max_height_ratio){
                mMaxRatio = a.getFloat(attr, DEFAULT_MAX_RATIO);
            }else if(attr == R.styleable.MaxHeightLayout_max_height_dimen){
                mMaxDimen = a.getDimension(attr, DEFAULT_MAX_DIMEN);
            }
        }
        a.recycle();
    }

    private void init(){
        if(mMaxDimen <= 0 && mMaxRatio <= 0){
            mMaxHeight = DEFAULT_MAX_RATIO_WITHOUT_ARGU * (float) getScreenHeight(getContext());
        } else if (mMaxDimen <= 0 && mMaxRatio > 0) {
            mMaxHeight = mMaxRatio * (float) getScreenHeight(getContext());
        } else if(mMaxDimen > 0 && mMaxRatio <= 0) {
            mMaxHeight = mMaxDimen;
        } else{
            mMaxHeight = Math.min(mMaxDimen, mMaxRatio * (float) getScreenHeight(getContext()));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
            heightSize = heightSize <= mMaxHeight ? heightSize
                    : (int) mMaxHeight;
        }

        if (heightMode == MeasureSpec.UNSPECIFIED) {
            heightSize = heightSize <= mMaxHeight ? heightSize
                    : (int) mMaxHeight;
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = heightSize <= mMaxHeight ? heightSize
                    : (int) mMaxHeight;
        }
        int maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize,
                heightMode);
        super.onMeasure(widthMeasureSpec, maxHeightMeasureSpec);
    }

    @SuppressWarnings("deprecation")
	private int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

}