package com.scwang.smartrefresh.layout.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.appoa.aframework.adapter.ZmAdapter;
import cn.appoa.aframework.utils.AtyUtils;

/**
 * AbsListView下拉刷新
 *
 * @param <T> 数据源
 * @param <V> AbsListView
 */
public abstract class PullToRefreshAdapterViewBaseFragment<T, V extends AbsListView>
        extends PullToRefreshBaseFragment<V> implements AdapterView.OnItemClickListener {

    @Override
    public void initRefreshLayout(Bundle savedInstanceState) {

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
     * AbsListView相关设置
     */
    public abstract void setRefreshView();

    /**
     * 数据源
     */
    protected List<T> dataList;

    /**
     * 适配器
     */
    protected ZmAdapter<T> adapter;

    /**
     * 初始化适配器
     *
     * @return
     */
    public abstract ZmAdapter<T> initAdapter();

    @Override
    public void initRefreshView() {
        emptyView = setEmptyView();
        if (emptyView != null) {
            refreshView.setEmptyView(emptyView);
        }
        setRefreshView();
        dataList = new ArrayList<T>();
        adapter = initAdapter();
        if (adapter != null) {
            adapter.setList(dataList);
            adapter.notifyDataSetChanged();
            refreshView.setAdapter(adapter);
        }
    }

    @Override
    protected void initListener() {
        refreshView.setOnItemClickListener(this);
        super.initListener();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean setRefreshMode() {
        return true;
    }

    @Override
    public void onRefresh() {
        if (adapter != null) {
            dataList.clear();
            adapter.setList(dataList);
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
            stopRefresh();
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
                adapter.setList(dataList);
                adapter.notifyDataSetChanged();
            }
            isMore = true;
        } else {
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
}
