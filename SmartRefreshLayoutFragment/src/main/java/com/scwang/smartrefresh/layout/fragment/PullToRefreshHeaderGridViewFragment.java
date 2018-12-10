package com.scwang.smartrefresh.layout.fragment;

import android.widget.GridView;

import cn.appoa.aframework.widget.adapterview.HeaderGridView;

/**
 * HeaderGridView下拉刷新
 *
 * @param <T> 数据源
 */
public abstract class PullToRefreshHeaderGridViewFragment<T> extends PullToRefreshGridViewFragment<T> {

    @Override
    public GridView onCreateRefreshView() {
        return new HeaderGridView(getActivity());
    }

}
