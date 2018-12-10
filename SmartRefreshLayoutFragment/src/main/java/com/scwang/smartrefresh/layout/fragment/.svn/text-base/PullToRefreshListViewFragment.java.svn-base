package com.scwang.smartrefresh.layout.fragment;

import android.graphics.drawable.ColorDrawable;
import android.widget.ListView;

import cn.appoa.aframework.utils.AtyUtils;

/**
 * ListView下拉刷新
 *
 * @param <T> 数据源
 */
public abstract class PullToRefreshListViewFragment<T> extends PullToRefreshAdapterViewBaseFragment<T, ListView> {

    @Override
    public ListView onCreateRefreshView() {
        return new ListView(getActivity());
    }

    @Override
    public void toScrollTop() {
        if (refreshView != null)
            refreshView.setSelection(0);
    }

    @Override
    public void setRefreshView() {
        try {
            refreshView.setDivider(new ColorDrawable(setDividerColor()));
            refreshView.setDividerHeight(AtyUtils.dip2px(getActivity(), setDividerHeight()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置线颜色
     *
     * @return
     */
    public abstract int setDividerColor();

    /**
     * 设置线宽
     *
     * @return 线宽(单位dp)
     */
    public abstract float setDividerHeight();
}
