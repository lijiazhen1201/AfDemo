package com.scwang.smartrefresh.layout.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import cn.appoa.aframework.fragment.AfFragment;
import cn.appoa.aframework.presenter.PullToRefreshOkGoPresenter;
import cn.appoa.aframework.presenter.PullToRefreshPresenter;
import cn.appoa.aframework.presenter.PullToRefreshVolleyPresenter;
import cn.appoa.aframework.utils.AtyUtils;
import cn.appoa.aframework.view.IPullToRefreshView;

/**
 * 下拉刷新Fragment
 *
 * @param <V> View
 */
public abstract class PullToRefreshBaseFragment<V extends View> extends AfFragment<PullToRefreshPresenter>
        implements View.OnTouchListener, OnRefreshListener, OnLoadMoreListener, IPullToRefreshView {

    /**
     * 根布局
     */
    public RelativeLayout rootLayout;

    /**
     * 头布局
     */
    public FrameLayout topLayout;

    /**
     * 刷新布局
     */
    public SmartRefreshLayout refreshLayout;

    /**
     * 刷新布局
     */
    public abstract void initRefreshLayout(Bundle savedInstanceState);

    /**
     * 刷新控件
     */
    public V refreshView;

    /**
     * 初始化刷新控件
     *
     * @return
     */
    public abstract V onCreateRefreshView();

    /**
     * 底布局
     */
    public FrameLayout bottomLayout;

    /**
     * 置顶布局
     */
    public FrameLayout endLayout;

    @Override
    public View initFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootLayout = new RelativeLayout(getActivity());

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        topLayout = new FrameLayout(getActivity());
        layout.addView(topLayout, new LinearLayout.LayoutParams(-1, -2));

        refreshLayout = new SmartRefreshLayout(getActivity());
        // 取消自动加载更多
        refreshLayout.setEnableAutoLoadMore(false);
        setRefreshLayout();
        setRefreshLayoutHeader();
        setRefreshLayoutFooter();
        refreshView = onCreateRefreshView();
        initRefreshLayout(savedInstanceState);
        if (refreshView != null) {
            refreshView.setOnTouchListener(this);
            refreshLayout.setRefreshContent(refreshView, -1, -1);
            // refreshLayout.addView(refreshView, new ViewGroup.LayoutParams(-1, -1));
            layout.addView(refreshLayout, new LinearLayout.LayoutParams(-1, 0, 1.0f));
        }

        bottomLayout = new FrameLayout(getActivity());
        layout.addView(bottomLayout, new LinearLayout.LayoutParams(-1, -2));

        rootLayout.addView(layout, new RelativeLayout.LayoutParams(-1, -1));

        endLayout = new FrameLayout(getActivity());
        int padding = AtyUtils.dip2px(getActivity(), 16.0f);
        endLayout.setPadding(padding, padding, padding, padding);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -2);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rootLayout.addView(endLayout, params);

        return rootLayout;
    }

    /**
     * 下拉刷新相关设置
     */
    private void setRefreshLayout() {
        //下面示例中的值等于默认值
//        refreshLayout.setPrimaryColorsId(R.color.colorTheme, android.R.color.white);
//        refreshLayout.setDragRate(0.5f);//显示下拉高度/手指真实下拉高度=阻尼效果
//        refreshLayout.setReboundDuration(300);//回弹动画时长（毫秒）
//
//        refreshLayout.setHeaderHeight(100);//Header标准高度（显示下拉高度>=标准高度 触发刷新）
//        refreshLayout.setHeaderHeightPx(100);//同上-像素为单位 （V1.1.0删除）
//        refreshLayout.setFooterHeight(100);//Footer标准高度（显示上拉高度>=标准高度 触发加载）
//        refreshLayout.setFooterHeightPx(100);//同上-像素为单位 （V1.1.0删除）
//
//        refreshLayout.setFooterHeaderInsetStart(0);//设置 Header 起始位置偏移量 1.0.5
//        refreshLayout.setFooterHeaderInsetStartPx(0);//同上-像素为单位 1.0.5 （V1.1.0删除）
//        refreshLayout.setFooterFooterInsetStart(0);//设置 Footer 起始位置偏移量 1.0.5
//        refreshLayout.setFooterFooterInsetStartPx(0);//同上-像素为单位 1.0.5 （V1.1.0删除）
//
//        refreshLayout.setHeaderMaxDragRate(2);//最大显示下拉高度/Header标准高度
//        refreshLayout.setFooterMaxDragRate(2);//最大显示下拉高度/Footer标准高度
//        refreshLayout.setHeaderTriggerRate(1);//触发刷新距离 与 HeaderHeight 的比率1.0.4
//        refreshLayout.setFooterTriggerRate(1);//触发加载距离 与 FooterHeight 的比率1.0.4
//
//        refreshLayout.setEnableRefresh(true);//是否启用下拉刷新功能
//        refreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
//        refreshLayout.setEnableAutoLoadMore(true);//是否启用列表惯性滑动到底部时自动加载更多
//        refreshLayout.setEnablePureScrollMode(false);//是否启用纯滚动模式
//        refreshLayout.setEnableNestedScroll(false);//是否启用嵌套滚动
//        refreshLayout.setEnableOverScrollBounce(true);//是否启用越界回弹
//        refreshLayout.setEnableScrollContentWhenLoaded(true);//是否在加载完成时滚动列表显示新的内容
//        refreshLayout.setEnableHeaderTranslationContent(true);//是否下拉Header的时候向下平移列表或者内容
//        refreshLayout.setEnableFooterTranslationContent(true);//是否上拉Footer的时候向上平移列表或者内容
//        refreshLayout.setEnableLoadMoreWhenContentNotFull(true);//是否在列表不满一页时候开启上拉加载功能
//        refreshLayout.setEnableFooterFollowWhenLoadFinished(false);//是否在全部加载结束之后Footer跟随内容1.0.4
//        refreshLayout.setEnableOverScrollDrag(false);//是否启用越界拖动（仿苹果效果）1.0.4
//
//        refreshLayout.setEnableScrollContentWhenRefreshed(true);//是否在刷新完成时滚动列表显示新的内容 1.0.5
//        refreshLayout.srlEnableClipHeaderWhenFixedBehind(true);//是否剪裁Header当时样式为FixedBehind时1.0.5
//        refreshLayout.srlEnableClipFooterWhenFixedBehind(true);//是否剪裁Footer当时样式为FixedBehind时1.0.5
//
//        refreshLayout.setDisableContentWhenRefresh(false);//是否在刷新的时候禁止列表的操作
//        refreshLayout.setDisableContentWhenLoading(false);//是否在加载的时候禁止列表的操作
//
//        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener());//设置多功能监听器
//        refreshLayout.setScrollBoundaryDecider(new ScrollBoundaryDecider());//设置滚动边界判断
//        refreshLayout.setScrollBoundaryDecider(new ScrollBoundaryDeciderAdapter());//自定义滚动边界
//
//        refreshLayout.setRefreshHeader(new ClassicsHeader(context));//设置Header
//        refreshLayout.setRefreshFooter(new ClassicsFooter(context));//设置Footer
//        refreshLayout.setRefreshContent(new View(context));//设置刷新Content（用于非xml布局代替addView）1.0.4
//
//        refreshLayout.autoRefresh();//自动刷新
//        refreshLayout.autoLoadMore();//自动加载
//        refreshLayout.autoRefresh(400);//延迟400毫秒后自动刷新
//        refreshLayout.autoLoadMore(400);//延迟400毫秒后自动加载
//        refreshLayout.finishRefresh();//结束刷新
//        refreshLayout.finishLoadMore();//结束加载
//        refreshLayout.finishRefresh(3000);//延迟3000毫秒后结束刷新
//        refreshLayout.finishLoadMore(3000);//延迟3000毫秒后结束加载
//        refreshLayout.finishRefresh(false);//结束刷新（刷新失败）
//        refreshLayout.finishLoadMore(false);//结束加载（加载失败）
//        refreshLayout.finishLoadMoreWithNoMoreData();//完成加载并标记没有更多数据 1.0.4
//        refreshLayout.closeHeaderOrFooter();//关闭正在打开状态的 Header 或者 Footer（1.1.0）
//        refreshLayout.resetNoMoreData();//恢复没有更多数据的原始状态 1.0.4（1.1.0删除）
//        refreshLayout.setNoMoreData(false);//恢复没有更多数据的原始状态 1.0.5
    }

    /**
     * 下拉刷新头相关设置
     */
    private void setRefreshLayoutHeader() {
        ClassicsHeader header = new ClassicsHeader(getActivity());
        // 下面示例中的值等于默认值
//        header.setAccentColor(android.R.color.white);//设置强调颜色
//        header.setPrimaryColor(R.color.colorTheme);//设置主题颜色
//        header.setTextSizeTitle(16);//设置标题文字大小（sp单位）
//        header.setTextSizeTitle(16, TypedValue.COMPLEX_UNIT_SP);//同上（1.1.0版本删除）
//        header.setTextSizeTime(10);//设置时间文字大小（sp单位）
//        header.setTextSizeTime(10, TypedValue.COMPLEX_UNIT_SP);//同上（1.1.0版本删除）
//        header.setTextTimeMarginTop(10);//设置时间文字的上边距（dp单位）
//        header.setTextTimeMarginTopPx(10);//同上-像素单位（1.1.0版本删除）
//        header.setEnableLastTime(true);//是否显示时间
//        header.setFinishDuration(500);//设置刷新完成显示的停留时间（设为0可以关闭停留功能）
//        header.setDrawableSize(20);//同时设置箭头和图片的大小（dp单位）
//        header.setDrawableArrowSize(20);//设置箭头的大小（dp单位）
//        header.setDrawableProgressSize(20);//设置图片的大小（dp单位）
//        header.setDrawableMarginRight(20);//设置图片和箭头和文字的间距（dp单位）
//        header.setDrawableSizePx(20);//同上-像素单位
//        header.setDrawableArrowSizePx(20);//同上-像素单位（1.1.0版本删除）
//        header.setDrawableProgressSizePx(20);//同上-像素单位（1.1.0版本删除）
//        header.setDrawableMarginRightPx(20);//同上-像素单位（1.1.0版本删除）
//        header.setArrowBitmap(bitmap);//设置箭头位图（1.1.0版本删除）
//        header.setArrowDrawable(drawable);//设置箭头图片
//        header.setArrowResource(R.drawable.ic_arrow);//设置箭头资源
//        header.setProgressBitmap(bitmap);//设置图片位图（1.1.0版本删除）
//        header.setProgressDrawable(drawable);//设置图片
//        header.setProgressResource(R.drawable.ic_progress);//设置图片资源
//        header.setTimeFormat(new DynamicTimeFormat("上次更新 %s"));//设置时间格式化（时间会自动更新）
//        header.setLastUpdateText("上次更新 3秒前");//手动更新时间文字设置（将不会自动更新时间）
//        header.setSpinnerStyle(SpinnerStyle.Translate);//设置移动样式（不支持：MatchLayout）
        // 设置刷新头
        refreshLayout.setRefreshHeader(header);
    }

    /**
     * 下拉刷新脚相关设置
     */
    private void setRefreshLayoutFooter() {
        ClassicsFooter footer = new ClassicsFooter(getActivity());
        // 下面示例中的值等于默认值
//        footer.setAccentColor(android.R.color.white);//设置强调颜色
//        footer.setPrimaryColor(R.color.colorTheme);//设置主题颜色
//        footer.setTextSizeTitle(16);//设置标题文字大小（sp单位）
//        footer.setTextSizeTitle(16, TypedValue.COMPLEX_UNIT_SP);//同上
//        footer.setFinishDuration(500);//设置刷新完成显示的停留时间
//        footer.setDrawableSize(20);//同时设置箭头和图片的大小（dp单位）
//        footer.setDrawableArrowSize(20);//设置箭头的大小（dp单位）
//        footer.setDrawableProgressSize(20);//设置图片的大小（dp单位）
//        footer.setDrawableMarginRight(20);//设置图片和箭头和文字的间距（dp单位）
//        footer.setDrawableSizePx(20);//同上-像素单位（1.1.0版本删除）
//        footer.setDrawableArrowSizePx(20);//同上-像素单位（1.1.0版本删除）
//        footer.setDrawableProgressSizePx(20);//同上-像素单位（1.1.0版本删除）
//        footer.setDrawableMarginRightPx(20);//同上-像素单位（1.1.0版本删除）
//        footer.setArrowBitmap(bitmap);//设置箭头位图（1.1.0版本删除）
//        footer.setArrowDrawable(drawable);//设置箭头图片
//        footer.setArrowResource(R.drawable.ic_arrow);//设置箭头资源
//        footer.setProgressBitmap(bitmap);//设置图片位图（1.1.0版本删除）
//        footer.setProgressDrawable(drawable);//设置图片
//        footer.setProgressResource(R.drawable.ic_progress);//设置图片资源
//        footer.setSpinnerStyle(SpinnerStyle.Translate);//设置移动样式（不支持：MatchLayout）
        // 设置刷新脚
        refreshLayout.setRefreshFooter(footer);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        hideSoftKeyboard();
        return false;
    }

    /**
     * 分页
     */
    protected int pageindex = 1;

    /**
     * 是否是首次
     */
    protected boolean isFirst = true;

    /**
     * 是否能加载更多
     */
    protected boolean isMore = false;

    /**
     * 是否正在网络访问
     */
    protected boolean isRequest = false;

    /**
     * 初始化顶布局
     */
    public void initTopView() {

    }

    /**
     * 初始化头布局
     */
    public void initHeaderView() {

    }

    /**
     * 初始化脚部局
     */
    public void initFooterView() {

    }

    /**
     * 初始化底布局
     */
    public void initBottomView() {

    }

    /**
     * 初始化置顶布局
     */
    public void initEndView() {

    }

    /**
     * 刷新控件
     */
    public abstract void initRefreshView();

    /**
     * 刷新模式(true：下拉刷新和上拉加载更多false：下拉刷新)
     */
    protected boolean isBoth = false;

    /**
     * 设置刷新模式
     *
     * @return true：下拉刷新和上拉加载更多false：下拉刷新
     */
    public abstract boolean setRefreshMode();

    /**
     * 监听
     */
    protected void initListener() {
        if (isBoth) {
            // 下拉刷新和上拉加载
            setEnableRefreshLoadMore(true, true);
            refreshLayout.setOnRefreshListener(this);
            refreshLayout.setOnLoadMoreListener(this);
        } else {
            // 下拉刷新
            setEnableRefreshLoadMore(true, false);
            refreshLayout.setOnRefreshListener(this);
        }
    }

    /**
     * 设置是否可下拉刷新和上拉加载更多
     *
     * @param enableRefresh  是否可下拉刷新
     * @param enableLoadMore 是否可上拉加载更多
     */
    public void setEnableRefreshLoadMore(boolean enableRefresh, boolean enableLoadMore) {
        if (refreshLayout != null) {
            refreshLayout.setEnableRefresh(enableRefresh);
            refreshLayout.setEnableLoadMore(enableLoadMore);
        }
    }

    /**
     * 是否初始化完毕
     */
    protected boolean isInit = false;

    @Override
    public void initView(View view) {
        if (refreshView != null) {
            pageindex = 1;
            isFirst = true;
            isMore = false;
            isRequest = false;
            initTopView();
            initHeaderView();
            initFooterView();
            initBottomView();
            initEndView();
            initRefreshView();
            isBoth = setRefreshMode();
            initListener();
            isInit = true;
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        if (isInit) {
            pageindex = 1;
            isFirst = true;
            isMore = false;
            onRefresh();
        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        if (isInit) {
            if (isMore) {
                pageindex++;
                isFirst = false;
                isMore = false;
                onLoadMore();
            } else {
                stopLoadMore();
            }
        }
    }

    /**
     * 下拉刷新
     */
    public abstract void onRefresh();

    /**
     * 上拉加载更多
     */
    public abstract void onLoadMore();

    /**
     * 开始刷新
     */
    public void startRefresh() {
        if (refreshLayout != null) {
            refreshLayout.autoRefresh(500);
        }
    }

    /**
     * 停止刷新
     */
    public void stopRefresh() {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh(500);
        }
    }

    /**
     * 开始加载更多
     */
    public void startLoadMore() {
        if (refreshLayout != null) {
            refreshLayout.autoLoadMore(500);
        }
    }

    /**
     * 停止加载更多
     */
    public void stopLoadMore() {
        if (refreshLayout != null) {
            refreshLayout.finishLoadMore(500);
        }
    }

    /**
     * 是否使用OkGo网络访问
     *
     * @return
     */
    protected boolean isOkGoRequest() {
        return false;
    }

    @Override
    public PullToRefreshPresenter initPresenter() {
        return isOkGoRequest() ? new PullToRefreshOkGoPresenter() : new PullToRefreshVolleyPresenter();
    }

    @Override
    public void onAttachView() {
        mPresenter.onAttach(this);
    }

    @Override
    public void onSuccessResponse(String response) {
        isRequest = false;
        if (pageindex == 1) {
            stopRefresh();
        } else {
            stopLoadMore();
        }
    }

    @Override
    public void onFailedResponse(String message) {
        isRequest = false;
        if (pageindex == 1) {
            stopRefresh();
        } else {
            stopLoadMore();
        }
    }

    /**
     * 置顶
     */
    public abstract void toScrollTop();

    /**
     * 刷新
     */
    public void refresh() {
        onRefresh(refreshLayout);
    }
}