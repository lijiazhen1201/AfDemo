package cn.appoa.afdemo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.scwang.smartrefresh.layout.fragment.PullToRefreshScrollViewFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.adapter.RefreshBeanAdapter;
import cn.appoa.afdemo.bean.RefreshBean;
import cn.appoa.afdemo.net.API;
import cn.appoa.aframework.utils.AtyUtils;
import zm.http.volley.ZmVolley;
import zm.http.volley.request.ZmStringRequest;

public class SmartRefreshScrollViewFragment extends PullToRefreshScrollViewFragment {

    @Override
    public void initRefreshLayout(Bundle savedInstanceState) {
        setRefreshContentView(R.layout.fragment_refresh_scrollview);
    }

    private ListView lv_refresh;
    private List<RefreshBean> dataList;
    private RefreshBeanAdapter adapter;

    @Override
    public void initRefreshContentView(View view) {
        if (view == null)
            return;
        lv_refresh = (ListView) view.findViewById(R.id.lv_refresh);
        dataList = new ArrayList<>();
        adapter = new RefreshBeanAdapter(getActivity(), dataList, R.layout.item_refresh_bean_list);
        lv_refresh.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onLoadMore() {
        getDataList();
    }

    @Override
    public void initData() {
        getBannerData();
        getCategoryData();
        getData1();
        getData2();
        getData3();
        getDataList();
    }

    /**
     * 轮播
     */
    private void getBannerData() {
    }

    /**
     * 分类
     */
    private void getCategoryData() {
    }

    /**
     * 推荐1
     */
    private void getData1() {
    }

    /**
     * 推荐2
     */
    private void getData2() {
    }

    /**
     * 推荐3
     */
    private void getData3() {
    }

    /**
     * 模拟数据
     */
    private void getDataList() {
        if (pageindex == 1) {
            dataList.clear();
            adapter.setList(dataList);
        }
        if (ZmVolley.isNetworkConnect(mActivity)) {
            Map<String, String> params = new HashMap<>();
            params.put("pageindex", pageindex + "");
            params.put("pagesize", 10 + "");
            showLoading("获取中...");
            ZmVolley.request(new ZmStringRequest(API.faq_list, params, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    stopRefresh();
                    stopLoadMore();
                    dismissLoading();
                    AtyUtils.i("RefreshScrollViewFragment", response);
                    if (API.filterJson(response)) {
                        List<RefreshBean> datas = API.parseJson(response, RefreshBean.class);
                        if (datas.size() > 0) {
                            isMore = true;
                            dataList.addAll(datas);
                            adapter.setList(dataList);
                        }
                    } else {
                        API.showErrorMsg(mActivity, response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    stopRefresh();
                    stopLoadMore();
                    dismissLoading();
                    AtyUtils.e("RefreshScrollViewFragment", error);
                }
            }));
        } else {
            stopRefresh();
            stopLoadMore();
        }
    }

}
