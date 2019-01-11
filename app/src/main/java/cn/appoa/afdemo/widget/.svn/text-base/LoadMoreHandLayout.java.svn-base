package cn.appoa.afdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

import cn.appoa.afdemo.R;

public class LoadMoreHandLayout extends FrameLayout implements RefreshFooter {

    public LoadMoreHandLayout(Context context) {
        this(context, null);
    }

    public LoadMoreHandLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreHandLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private boolean isRefresh = false;
    private RelativeLayout rl_refresh_hand;
    private ImageView iv_refresh_hand;
    private ProgressBar pb_refresh_hand;
    private TextView tv_refresh_hand;
    private CharSequence mPullLabel;
    private CharSequence mRefreshingLabel;
    private CharSequence mReleaseLabel;

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        View.inflate(context, R.layout.layout_refresh_hand, this);
        rl_refresh_hand = (RelativeLayout) findViewById(R.id.rl_refresh_hand);
        iv_refresh_hand = (ImageView) findViewById(R.id.iv_refresh_hand);
        pb_refresh_hand = (ProgressBar) findViewById(R.id.pb_refresh_hand);
        tv_refresh_hand = (TextView) findViewById(R.id.tv_refresh_hand);
        mPullLabel = isRefresh ? "下拉刷新" : "上滑加载更多";
        mRefreshingLabel = isRefresh ? "正在刷新" : "正在加载更多";
        mReleaseLabel = isRefresh ? "松开刷新" : "松开加载更多";
        LayoutParams lp = (LayoutParams) rl_refresh_hand.getLayoutParams();
        lp.gravity = isRefresh ? Gravity.BOTTOM : Gravity.TOP;
    }

    /**
     * 获取实体视图（必须返回，不能为null）
     *
     * @return 实体视图
     */
    @Override
    public View getView() {
        return this;// 真实的视图就是自己，不能返回null
    }

    /**
     * 获取变换方式 {@link SpinnerStyle} 必须返回 非空 （必须指定一个：平移、拉伸、固定、全屏）
     *
     * @return 变换方式
     */
    @Override
    public SpinnerStyle getSpinnerStyle() {
//		return SpinnerStyle.FixedBehind;// 固定在背后     特点：HeaderView高度不会改变
//		return SpinnerStyle.FixedFront;//  固定在前面     特点：HeaderView高度不会改变
//		return SpinnerStyle.MatchLayout;// 填满布局         特点：HeaderView高度不会改变，尺寸充满 RefreshLayout
//		return SpinnerStyle.Scale;//       拉伸形变         特点：在下拉和上弹（HeaderView高度改变）时候，会自动触发OnDraw事件
        return SpinnerStyle.Translate;//   平行移动         特点: HeaderView高度不会改变
    }

    /**
     * 设置主题颜色 （如果自定义的Header没有注意颜色，本方法可以什么都不处理）
     *
     * @param colors 对应Xml中配置的 srlPrimaryColor srlAccentColor
     */
    @Override
    public void setPrimaryColors(int... colors) {
    }

    /**
     * 尺寸定义完成 （如果高度不改变（代码修改：setHeader），只调用一次, 在RefreshLayout#onMeasure中调用）
     *
     * @param kernel       RefreshKernel 核心接口（用于完成高级Header功能）
     * @param height       HeaderHeight or FooterHeight
     * @param extendHeight extendHeaderHeight or extendFooterHeight
     */
    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {
    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    /**
     * 手指拖动下拉（会连续多次调用，用于实时控制动画关键帧）
     *
     * @param percent      下拉的百分比 值 = offset/footerHeight (0 - percent -
     *                     (footerHeight+extendHeight) / footerHeight )
     * @param offset       下拉的像素偏移量 0 - offset - (footerHeight+extendHeight)
     * @param height       高度 HeaderHeight or FooterHeight
     * @param extendHeight 扩展高度 extendHeaderHeight or extendFooterHeight
     */
//    @Override
//    public void onPulling(float percent, int offset, int height, int extendHeight) {
//    }

    /**
     * 手指释放之后的持续动画（会连续多次调用，用于实时控制动画关键帧）
     *
     * @param percent      下拉的百分比 值 = offset/footerHeight (0 - percent -
     *                     (footerHeight+extendHeight) / footerHeight )
     * @param offset       下拉的像素偏移量 0 - offset - (footerHeight+extendHeight)
     * @param height       高度 HeaderHeight or FooterHeight
     * @param extendHeight 扩展高度 extendHeaderHeight or extendFooterHeight
     */
//    @Override
//    public void onReleasing(float percent, int offset, int height, int extendHeight) {
//
//    }

    /**
     * 释放时刻（调用一次，将会触发加载）
     *
     * @param refreshLayout RefreshLayout
     * @param height        高度 HeaderHeight or FooterHeight
     * @param extendHeight  扩展高度 extendHeaderHeight or extendFooterHeight
     */
    @Override
    public void onReleased(RefreshLayout refreshLayout, int height, int extendHeight) {
    }

    /**
     * 开始动画 （开始刷新或者开始加载动画）
     *
     * @param refreshLayout RefreshLayout
     * @param height        HeaderHeight or FooterHeight
     * @param extendHeight  extendHeaderHeight or extendFooterHeight
     */
    @Override
    public void onStartAnimator(RefreshLayout refreshLayout, int height, int extendHeight) {

    }

    /**
     * 动画结束
     *
     * @param refreshLayout RefreshLayout
     * @param success       数据是否成功刷新或加载
     * @return 完成动画所需时间 如果返回 Integer.MAX_VALUE 将取消本次完成事件，继续保持原有状态
     */
    @Override
    public int onFinish(RefreshLayout refreshLayout, boolean success) {
        return 500;// 延迟500毫秒之后再弹回
    }

    /**
     * 水平方向的拖动
     *
     * @param percentX  下拉时，手指水平坐标对屏幕的占比（0 - percentX - 1）
     * @param offsetX   下拉时，手指水平坐标对屏幕的偏移（0 - offsetX - LayoutWidth）
     * @param offsetMax 最大的偏移量
     */
    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {
    }

    /**
     * 是否支持水平方向的拖动（将会影响到onHorizontalDrag的调用）
     *
     * @return 水平拖动需要消耗更多的时间和资源，所以如果不支持请返回false
     */
    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    /**
     * 状态改变事件 {@link RefreshState}
     *
     * @param refreshLayout RefreshLayout
     * @param oldState      改变之前的状态
     * @param newState      改变之后的状态
     */
    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:// 默认状态
            case PullUpToLoad:// 开始上拉
                iv_refresh_hand.setVisibility(View.VISIBLE);
                iv_refresh_hand.setRotation(isRefresh ? 180 : 0);
                pb_refresh_hand.setVisibility(View.GONE);
                tv_refresh_hand.setText(mPullLabel);
                break;
            case Loading:// 正在加载
                iv_refresh_hand.setVisibility(View.GONE);
                pb_refresh_hand.setVisibility(View.VISIBLE);
                tv_refresh_hand.setText(mRefreshingLabel);
                break;
            case ReleaseToLoad:// 释放立即加载
                iv_refresh_hand.setRotation(isRefresh ? 0 : 180);
                tv_refresh_hand.setText(mReleaseLabel);
                break;
            default:
                break;
        }
    }

    /**
     * 设置数据全部加载完成，将不能再次触发加载功能
     *
     * @param noMoreData 是否有更多数据
     * @return true 支持全部加载完成的状态显示 false 不支持
     */
    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        return false;
    }


}
