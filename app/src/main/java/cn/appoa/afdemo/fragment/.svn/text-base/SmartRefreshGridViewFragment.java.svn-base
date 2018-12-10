package cn.appoa.afdemo.fragment;

import com.scwang.smartrefresh.layout.fragment.PullToRefreshGridViewFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.adapter.RefreshBeanAdapter;
import cn.appoa.afdemo.bean.RefreshBean;
import cn.appoa.afdemo.net.API;
import cn.appoa.aframework.adapter.ZmAdapter;

public class SmartRefreshGridViewFragment extends PullToRefreshGridViewFragment<RefreshBean> {

    @Override
    public float initHorizontalSpacing() {
        return 4.0f;
    }

    @Override
    public float initVerticalSpacing() {
        return 4.0f;
    }

    @Override
    public int initNumColumns() {
        return 2;
    }

    @Override
    public ZmAdapter<RefreshBean> initAdapter() {
        return new RefreshBeanAdapter(getActivity(), dataList, R.layout.item_refresh_bean_grid);
    }

    @Override
    public String setUrl() {
        return API.faq_list;
    }

    @Override
    public Map<String, String> setParams() {
        Map<String, String> params = new HashMap<>();
        params.put("pageindex", pageindex + "");
        params.put("pagesize", 10 + "");
        return params;
    }

    @Override
    public List<RefreshBean> filterResponse(String response) {
        if (API.filterJson(response)) {
            List<RefreshBean> datas = new ArrayList<>();
            // 模拟多数据
            for (int i = 0; i < 50; i++) {
                datas.addAll(API.parseJson(response, RefreshBean.class));
            }
            return datas;
        }
        return null;
    }

}
