package cn.appoa.afdemo.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.divider.ListItemDecoration;

import java.util.ArrayList;
import java.util.List;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseFragment;
import cn.appoa.aframework.adapter.ZmAdapter;
import cn.appoa.aframework.adapter.ZmHolder;
import cn.appoa.aframework.utils.AtyUtils;
import cn.appoa.aframework.widget.noscroll.NoScrollGridView;
import cn.appoa.aframework.widget.noscroll.NoScrollImageGridView;
import cn.appoa.aframework.widget.noscroll.NoScrollListView;
import cn.appoa.aframework.widget.noscroll.NoScrollRecyclerView;


public class NoScrollViewFragment extends BaseFragment {

    @Override
    public View initFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_no_scroll_view, container,false);
    }

    private ListView mListView;

    @Override
    public void initView(View view) {
        mListView = (ListView) view.findViewById(R.id.mListView);
    }

    @Override
    public void initData() {
        //TODO 模拟数据
        List<List<String>> datas = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            List<String> titles = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                titles.add("标题" + (j + 1));
            }
            datas.add(titles);
        }
        mListView.setAdapter(new ZmAdapter<List<String>>(mActivity, datas, R.layout.item_no_scroll_view) {
            @Override
            public void init(ZmHolder zmHolder, List<String> strings, int position) {
                NoScrollListView mListView = zmHolder.getView(R.id.mListView);
                NoScrollGridView mGridView = zmHolder.getView(R.id.mGridView);
                NoScrollImageGridView mImageGridView = zmHolder.getView(R.id.mImageGridView);
                NoScrollRecyclerView mRecyclerView = zmHolder.getView(R.id.mRecyclerView);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
                if (mRecyclerView.getItemDecorationAt(0) == null) {
                    //防止重复添加分割线
                    mRecyclerView.addItemDecoration(new ListItemDecoration(mActivity));
                }
                //设置数据
                mListView.setAdapter(new ZmAdapter<String>(mActivity, strings, R.layout.item_main) {
                    @Override
                    public void init(ZmHolder zmHolder, String t, int position) {
                        zmHolder.setText(R.id.tv_main, t);
                        zmHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AtyUtils.showShort(mActivity, "跳转详情", false);
                            }
                        });
                    }
                });
                mGridView.setAdapter(new ZmAdapter<String>(mActivity, strings, R.layout.item_main) {
                    @Override
                    public void init(ZmHolder zmHolder, String t, int position) {
                        zmHolder.setText(R.id.tv_main, t);
                        zmHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AtyUtils.showShort(mActivity, "跳转详情", false);
                            }
                        });
                    }
                });
                mImageGridView.setAdapter(new ZmAdapter<String>(mActivity, strings, R.layout.item_main) {
                    @Override
                    public void init(ZmHolder zmHolder, String t, int position) {
                        zmHolder.setText(R.id.tv_main, t);
                        zmHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AtyUtils.showShort(mActivity, "跳转详情", false);
                            }
                        });
                    }
                });
                mRecyclerView.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_main, strings) {

                    @Override
                    protected void convert(BaseViewHolder zmHolder, String t) {
                        zmHolder.setText(R.id.tv_main, t);
                        zmHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AtyUtils.showShort(mActivity, "跳转详情", false);
                            }
                        });
                    }
                });
                // NoScrollGridView和NoScrollImageGridView的区别：
                // NoScrollGridView点击空白区域没有响应，
                // NoScrollImageGridView点击空白区域可以响应Item点击事件
                zmHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AtyUtils.showShort(mActivity, "跳转详情", false);
                    }
                });
            }
        });
    }
}
