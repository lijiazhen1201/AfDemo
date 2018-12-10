package com.scwang.smartrefresh.layout.fragment;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ScrollView;

/**
 * ScrollView下拉刷新
 */
public abstract class PullToRefreshScrollViewFragment extends PullToRefreshBaseFragment<ScrollView> {

    @Override
    public ScrollView onCreateRefreshView() {
        return new ScrollView(getActivity());
    }

    @Override
    public boolean setRefreshMode() {
        return true;
    }

    @Override
    public void initRefreshView() {
        initRefreshContentView(mView);
    }

    @Override
    public void toScrollTop() {
        if (refreshView != null)
            refreshView.scrollTo(0, 0);
    }

    /**
     * 设置内容
     *
     * @param layoutResID
     */
    public void setRefreshContentView(@LayoutRes int layoutResID) {
        View view = View.inflate(getActivity(), layoutResID, null);
        setRefreshContentView(view);
    }

    /**
     * 内容
     */
    protected View mView;

    /**
     * 设置内容
     *
     * @param view
     */
    public void setRefreshContentView(@Nullable View view) {
        if (view == null || refreshView == null)
            return;
        mView = view;
        refreshView.removeAllViews();
        refreshView.addView(view, new ScrollView.LayoutParams(-1, -1));
    }

    /**
     * 初始化控件
     *
     * @param view
     */
    public abstract void initRefreshContentView(View view);
}
