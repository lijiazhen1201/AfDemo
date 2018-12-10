package cn.appoa.afdemo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.fragment.PullToRefreshListViewFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.adapter.RefreshBeanAdapter;
import cn.appoa.afdemo.bean.RefreshBean;
import cn.appoa.afdemo.net.API;
import cn.appoa.afdemo.widget.LoadMoreHandLayout;
import cn.appoa.afdemo.widget.RefreshHandLayout;
import cn.appoa.aframework.adapter.ZmAdapter;
import cn.appoa.aframework.utils.AtyUtils;

public class SmartRefreshListViewFragment extends PullToRefreshListViewFragment<RefreshBean> {

    @Override
    public void initRefreshLayout(Bundle savedInstanceState) {
        super.initRefreshLayout(savedInstanceState);
        // 自定义刷新布局
        RefreshHandLayout header = new RefreshHandLayout(getActivity());
        refreshLayout.setRefreshHeader(header);
        LoadMoreHandLayout footer = new LoadMoreHandLayout(getActivity());
        refreshLayout.setRefreshFooter(footer);
    }

    @Override
    public int setDividerColor() {
        return ContextCompat.getColor(mActivity, R.color.colorDivider);
    }

    @Override
    public float setDividerHeight() {
        return 0.5f;
    }

    @Override
    public ZmAdapter<RefreshBean> initAdapter() {
        return new RefreshBeanAdapter(getActivity(), dataList, R.layout.item_refresh_bean_list);
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

    @Override
    public View setEmptyView() {
        View view = View.inflate(getActivity(), R.layout.empty_refresh_bean, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRefresh();
            }
        });
        return view;
    }

    View topView;

    @Override
    public void initTopView() {
        if (topView != null) {
            topLayout.removeView(topView);
            topView = null;
        }
        topView = View.inflate(mActivity, R.layout.fragment_refresh_test, null);
        TextView tv_refresh_test = (TextView) topView.findViewById(R.id.tv_refresh_test);
        tv_refresh_test.setText("Top");
        tv_refresh_test.setBackgroundColor(Color.RED);
        tv_refresh_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AtyUtils.showShort(mActivity, "Top", true);
            }
        });
        topLayout.addView(topView);
    }

    View headerView;

    @Override
    public void initHeaderView() {
        if (headerView != null) {
            refreshView.removeHeaderView(headerView);
            headerView = null;
        }
        headerView = View.inflate(mActivity, R.layout.fragment_refresh_test, null);
        TextView tv_refresh_test = (TextView) headerView.findViewById(R.id.tv_refresh_test);
        tv_refresh_test.setText("Header");
        tv_refresh_test.setBackgroundColor(Color.YELLOW);
        tv_refresh_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AtyUtils.showShort(mActivity, "Header", true);
            }
        });
        refreshView.addHeaderView(headerView);
    }

    View footerView;

    @Override
    public void initFooterView() {
        if (footerView != null) {
            refreshView.removeFooterView(footerView);
            footerView = null;
        }
        footerView = View.inflate(mActivity, R.layout.fragment_refresh_test, null);
        TextView tv_refresh_test = (TextView) footerView.findViewById(R.id.tv_refresh_test);
        tv_refresh_test.setText("Footer");
        tv_refresh_test.setBackgroundColor(Color.BLUE);
        tv_refresh_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AtyUtils.showShort(mActivity, "Footer", true);
            }
        });
        refreshView.addFooterView(footerView);
    }

    View bottomView;

    @Override
    public void initBottomView() {
        if (bottomView != null) {
            bottomLayout.removeView(bottomView);
            bottomView = null;
        }
        bottomView = View.inflate(mActivity, R.layout.fragment_refresh_test, null);
        TextView tv_refresh_test = (TextView) bottomView.findViewById(R.id.tv_refresh_test);
        tv_refresh_test.setText("Bottom");
        tv_refresh_test.setBackgroundColor(Color.GREEN);
        tv_refresh_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AtyUtils.showShort(mActivity, "Bottom", true);
            }
        });
        bottomLayout.addView(bottomView);
    }

    ImageView endView;

    @Override
    public void initEndView() {
        if (endView != null) {
            endLayout.removeView(endView);
            endView = null;
        }
        endView = new ImageView(mActivity);
        endView.setImageResource(R.drawable.to_top);
        endView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toScrollTop();
            }
        });
        endLayout.addView(endView);
    }
}
