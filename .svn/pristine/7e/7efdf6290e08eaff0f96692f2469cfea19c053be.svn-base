package com.scwang.smartrefresh.layout.fragment;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.appoa.aframework.utils.AtyUtils;

/**
 * 推上置顶的RecyclerView下拉刷新
 *
 * @param <T> 数据源
 */
public abstract class PullToRefreshCoordinatorLayoutFragment<T> extends PullToRefreshBaseFragment<CoordinatorLayout> {

    @Override
    public void initRefreshLayout(Bundle savedInstanceState) {

    }

    public AppBarLayout appBarLayout;
    public FrameLayout scrollLayout;
    public View scrollView;
    public FrameLayout fixedLayout;
    public View fixedView;
    public View line;
    public RecyclerView recyclerView;

    @Override
    public CoordinatorLayout onCreateRefreshView() {
        View view = View.inflate(mActivity, R.layout.fragment_pull_to_refresh_coordinator_layout, null);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.appBarLayout);
        scrollLayout = (FrameLayout) view.findViewById(R.id.scrollLayout);
        scrollView = initScrollView();
        if (scrollView != null) {
            scrollLayout.addView(scrollView);
        }
        fixedLayout = (FrameLayout) view.findViewById(R.id.fixedLayout);
        fixedView = initFixedView();
        if (fixedView != null) {
            fixedLayout.addView(fixedView);
        }
        line = (View) view.findViewById(R.id.line);
        appBarLayout.setVisibility(scrollView == null || fixedView == null ? View.GONE : View.VISIBLE);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        return (CoordinatorLayout) view;
    }

    /**
     * 滚动布局
     *
     * @return
     */
    public View initScrollView() {
        return null;
    }

    /**
     * 固定布局
     *
     * @return
     */
    public View initFixedView() {
        return null;
    }

    /**
     * 数据为空的View
     */
    protected View emptyView;

    /**
     * 设置数据为空的View
     *
     * @return
     */
    public View setEmptyView() {
        return null;
    }

    /**
     * RecyclerView相关设置
     */
    public void setRefreshView() {

    }

    /**
     * 布局管理
     */
    protected RecyclerView.LayoutManager layoutManager;

    /**
     * 初始化布局管理
     *
     * @return
     */
    public abstract RecyclerView.LayoutManager initLayoutManager();

    /**
     * 设置布局管理
     *
     * @param layoutManager
     */
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (isInit) {
            this.layoutManager = layoutManager;
            recyclerView.setLayoutManager(this.layoutManager);
        }
    }

    /**
     * 分割线
     */
    protected RecyclerView.ItemDecoration decor;

    /**
     * 初始化分割线
     *
     * @return
     */
    public RecyclerView.ItemDecoration initItemDecoration() {
        return null;
    }

    /**
     * 设置分割线
     *
     * @param decor
     */
    public void setItemDecoration(RecyclerView.ItemDecoration decor) {
        if (isInit) {
            if (this.decor != null) {
                recyclerView.removeItemDecoration(this.decor);
            }
            this.decor = decor;
            recyclerView.addItemDecoration(this.decor);
        }
    }

    /**
     * 数据源
     */
    protected List<T> dataList;

    /**
     * 适配器
     */
    protected BaseQuickAdapter<T, BaseViewHolder> adapter;

    /**
     * 初始化适配器
     *
     * @return
     */
    public abstract BaseQuickAdapter<T, BaseViewHolder> initAdapter();

    @Override
    public void initRefreshView() {
        setRefreshView();
        dataList = new ArrayList<T>();
        adapter = initAdapter();
        layoutManager = initLayoutManager();
        if (layoutManager != null) {
            recyclerView.setLayoutManager(layoutManager);
        }
        decor = initItemDecoration();
        if (decor != null) {
            recyclerView.addItemDecoration(decor);
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (adapter != null) {
            setAdapter();
            // 设置头布局和脚布局可以和空布局共存（默认不共存）
            adapter.setHeaderFooterEmpty(true, true);
            emptyView = setEmptyView();
            if (emptyView != null) {
                adapter.setEmptyView(emptyView);
            }
            initHeaderView(adapter);
            initFooterView(adapter);
            adapter.setNewData(dataList);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }
    }

    /**
     * BaseQuickAdapter相关设置
     */
    protected void setAdapter() {

    }

    /**
     * 初始化头布局
     */
    public void initHeaderView(BaseQuickAdapter<T, BaseViewHolder> adapter) {

    }

    /**
     * 初始化脚部局
     */
    public void initFooterView(BaseQuickAdapter<T, BaseViewHolder> adapter) {

    }

    @Override
    public boolean setRefreshMode() {
        return true;
    }

    @Override
    public void onRefresh() {
        if (adapter != null) {
            dataList.clear();
            adapter.setNewData(dataList);
            adapter.notifyDataSetChanged();
        }
        initData();
    }

    @Override
    public void onLoadMore() {
        initData();
    }


    @Override
    public void initData() {
        if (isRequest) {
            // 如果正在网络访问，就不重复访问
            if (pageindex == 1) {
                stopRefresh();
            } else {
                stopLoadMore();
            }
            if (adapter != null) {
                adapter.loadMoreEnd();
            }
            return;
        }
        isRequest = true;
        mPresenter.getData(setUrl(), setParams());
    }

    /**
     * 网络访问地址
     *
     * @return
     */
    public abstract String setUrl();

    /**
     * 网络访问参数
     *
     * @return
     */
    public abstract Map<String, String> setParams();

    @Override
    public void onSuccessResponse(String response) {
        super.onSuccessResponse(response);
        if (pageindex == 1) {
            dataList.clear();
        }
        List<T> datas = filterResponse(response);
        if (datas != null && datas.size() > 0) {
            dataList.addAll(datas);
            if (adapter != null) {
                adapter.loadMoreComplete();
                adapter.setNewData(dataList);
                adapter.notifyDataSetChanged();
            }
            isMore = true;
        } else {
            if (adapter != null) {
                adapter.loadMoreEnd();
            }
            if (isShowToast()) {
                CharSequence message = null;
                if (pageindex == 1) {
                    message = TextUtils.isEmpty(setNoDataMsg()) ? "暂无任何信息" : setNoDataMsg();
                } else {
                    message = TextUtils.isEmpty(setAllDataMsg()) ? "已加载全部信息" : setAllDataMsg();
                }
                AtyUtils.showShort(getActivity(), message, false);
            }
        }
    }

    @Override
    public void onFailedResponse(String message) {
        super.onFailedResponse(message);
        if (adapter != null) {
            adapter.loadMoreFail();
        }
        if (isShowToast() && !TextUtils.isEmpty(message)) {
            AtyUtils.showShort(getActivity(), message, false);
        }
    }

    /**
     * 返回的结果过滤
     *
     * @param response
     * @return
     */
    public abstract List<T> filterResponse(String response);

    /**
     * 是否弹出提示
     *
     * @return
     */
    public boolean isShowToast() {
        return false;
    }

    /**
     * 数据为空的提示信息
     *
     * @return
     */
    public CharSequence setNoDataMsg() {
        return null;
    }

    /**
     * 全部数据加载完的提示信息
     *
     * @return
     */
    public CharSequence setAllDataMsg() {
        return null;
    }

    @Override
    public void toScrollTop() {
        //recyclerView置顶
        recyclerView.smoothScrollToPosition(0);
//        //CoordinatorLayout置顶
//        CoordinatorLayout.Behavior behavior =
//                ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).getBehavior();
//        if (behavior instanceof AppBarLayout.Behavior) {
//            AppBarLayout.Behavior appBarLayoutBehavior = (AppBarLayout.Behavior) behavior;
//            int topAndBottomOffset = appBarLayoutBehavior.getTopAndBottomOffset();
//            if (topAndBottomOffset != 0) {
//                appBarLayoutBehavior.setTopAndBottomOffset(0);
//            }
//        }
    }

}
