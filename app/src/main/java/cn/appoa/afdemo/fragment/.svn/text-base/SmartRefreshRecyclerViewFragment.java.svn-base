package cn.appoa.afdemo.fragment;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.divider.GridItemDecoration2;
import com.scwang.smartrefresh.layout.divider.ListItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.activity.SmartRefreshActivity;
import cn.appoa.afdemo.base.BaseRecyclerFragment;
import cn.appoa.afdemo.bean.RefreshBean;
import cn.appoa.afdemo.net.API;
import cn.appoa.aframework.utils.AtyUtils;

public class SmartRefreshRecyclerViewFragment extends BaseRecyclerFragment<RefreshBean> {

    @Override
    public RecyclerView.LayoutManager initLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    @Override
    public RecyclerView.ItemDecoration initItemDecoration() {
        return new ListItemDecoration(getActivity());
    }

    /**
     * 单布局Adapter
     */
    class QuickAdapter extends BaseQuickAdapter<RefreshBean, BaseViewHolder> {

        public QuickAdapter(int layoutResId, @Nullable List<RefreshBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, RefreshBean item) {
            SmartRefreshRecyclerViewFragment.this.convert(helper, item);
        }
    }

    /**
     * 多布局Adapter
     */
    class MultiItemQuickAdapter extends BaseMultiItemQuickAdapter<RefreshBean, BaseViewHolder> {

        public MultiItemQuickAdapter(@Nullable List<RefreshBean> data) {
            super(data);
            addItemType(0, R.layout.item_refresh_bean_list);
            addItemType(1, R.layout.item_refresh_bean_grid);
        }

        @Override
        protected void convert(BaseViewHolder helper, RefreshBean item) {
            SmartRefreshRecyclerViewFragment.this.convert(helper, item);
        }
    }

    /**
     * 设置适配器数据
     *
     * @param helper
     * @param t
     */
    @SuppressWarnings("unused")
    public void convert(BaseViewHolder helper, final RefreshBean t) {
        // 获取当前item的position
        int position = helper.getLayoutPosition();
        TextView tv_refresh_id = helper.getView(R.id.tv_refresh_id);
        TextView tv_refresh_title = helper.getView(R.id.tv_refresh_title);
        if (t != null) {
            tv_refresh_id.setText("ID:" + t.ID);
            tv_refresh_title.setText(t.Title);
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AtyUtils.showShort(mActivity, t.Title, true);
                }
            });
        }
    }

    @Override
    public BaseQuickAdapter<RefreshBean, BaseViewHolder> initAdapter() {
        // return new QuickAdapter(R.layout.item_refresh_bean_list, dataList);
        return new MultiItemQuickAdapter(dataList);
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
            // 模拟多条数据
            for (int i = 0; i < 50; i++) {
                datas.addAll(API.parseJson(response, RefreshBean.class));
            }
            // 设置类型
            for (int i = 0; i < datas.size(); i++) {
                datas.get(i).setItemType(isGrid ? 1 : 0);
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
                // 重新获取数据
                startRefresh();
            }
        });
        return view;
    }

    View headerView;

    @Override
    public void initHeaderView(BaseQuickAdapter<RefreshBean, BaseViewHolder> adapter) {
        if (headerView != null) {
            adapter.removeHeaderView(headerView);
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
        adapter.addHeaderView(headerView);
    }

    View footerView;

    @Override
    public void initFooterView(BaseQuickAdapter<RefreshBean, BaseViewHolder> adapter) {
        if (footerView != null) {
            adapter.removeFooterView(footerView);
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
        adapter.addFooterView(footerView);
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
                // 置顶
                toScrollTop();
            }
        });
        endLayout.addView(endView);
    }

    /**
     * 是否网格布局
     */
    private boolean isGrid;

    /**
     * 切换列表和网格
     *
     * @param isGrid
     */
    public void updataListGrid(boolean isGrid) {
        this.isGrid = isGrid;
        for (int i = 0; i < dataList.size(); i++) {
            dataList.get(i).setItemType(isGrid ? 1 : 0);
        }
        setLayoutManager(isGrid ? new GridLayoutManager(mActivity, 2) : new LinearLayoutManager(mActivity));
        setItemDecoration(
                isGrid ? new GridItemDecoration2(getActivity(), adapter, true) : new ListItemDecoration(getActivity()));
        // 重新绑定RecyclerView
        adapter.onAttachedToRecyclerView(recyclerView);
    }

    private View titlebar;
    private int scrollY = 0;

    @Override
    public void setRefreshView() {
        titlebar = ((SmartRefreshActivity) getActivity()).getTitlebar();
        titlebar.getBackground().mutate().setAlpha(0);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollY += dy;
                if (scrollY > 2040) {
                    titlebar.getBackground().mutate().setAlpha(255);
                } else {
                    titlebar.getBackground().mutate().setAlpha(scrollY / 8);
                }
            }
        });
    }
}
