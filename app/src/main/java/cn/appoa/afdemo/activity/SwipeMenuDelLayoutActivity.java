package cn.appoa.afdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseActivity;
import cn.appoa.aframework.adapter.ZmAdapter;
import cn.appoa.aframework.adapter.ZmHolder;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;
import cn.appoa.aframework.utils.AtyUtils;
import cn.appoa.aframework.widget.layout.SwipeMenuDelLayout;

/**
 * Item侧滑删除菜单Layout
 */
public class SwipeMenuDelLayoutActivity extends BaseActivity {

    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(this).setTitle("SwipeMenuDelLayout")
                .setBackImage(R.drawable.back_white).create();
    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        setContent(R.layout.activity_swipe_menu_del_layout);
    }

    private ListView mListView;

    @Override
    public void initView() {
        super.initView();
        mListView = (ListView) findViewById(R.id.mListView);
    }

    @Override
    public void initData() {
        //TODO 模拟数据
        List<String> titles = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            titles.add("标题" + (i + 1));
        }
        mListView.setAdapter(new ZmAdapter<String>(mActivity, titles, R.layout.item_swipe_menu_del) {
            @Override
            public void init(ZmHolder zmHolder, String t, int position) {
                zmHolder.setText(R.id.tv_main, t);
                final SwipeMenuDelLayout delLayout = zmHolder.getView(R.id.delLayout);
                TextView tv_edit = zmHolder.getView(R.id.tv_edit);
                TextView tv_del = zmHolder.getView(R.id.tv_del);
                tv_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO 编辑
                        AtyUtils.showShort(mContext, "编辑", false);
                        delLayout.quickClose();//关闭
                    }
                });
                tv_del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO 删除
                        AtyUtils.showShort(mContext, "删除", false);
                        delLayout.quickClose();//关闭
                    }
                });
            }
        });
    }
}
