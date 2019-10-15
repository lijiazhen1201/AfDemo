package cn.appoa.afdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseActivity;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;


/**
 * 下拉刷新
 */
public class RefreshBeanActivity extends BaseActivity implements View.OnClickListener{

    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(this).setTitle("下拉刷新")
                .setBackImage(R.drawable.back_white).create();
    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        setContent(R.layout.activity_refresg_bean);
    }

    private Button btn_refresh_scrollview;
    private Button btn_refresh_listview;
    private Button btn_refresh_gridview;
    private Button btn_refresh_recyclerview;

    @Override
    public void initView() {
        super.initView();
        btn_refresh_scrollview = (Button) findViewById(R.id.btn_refresh_scrollview);
        btn_refresh_listview = (Button) findViewById(R.id.btn_refresh_listview);
        btn_refresh_gridview = (Button) findViewById(R.id.btn_refresh_gridview);
        btn_refresh_recyclerview = (Button) findViewById(R.id.btn_refresh_recyclerview);

        btn_refresh_scrollview.setOnClickListener(this);
        btn_refresh_listview.setOnClickListener(this);
        btn_refresh_gridview.setOnClickListener(this);
        btn_refresh_recyclerview.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        int type = 0;
        String title = null;
        switch (v.getId()) {
            case R.id.btn_refresh_scrollview:
                type = 1;
                title = "PullToRefreshScrollView";
                break;
            case R.id.btn_refresh_listview:
                type = 2;
                title = "PullToRefreshListView";
                break;
            case R.id.btn_refresh_gridview:
                type = 3;
                title = "PullToRefreshGridView";
                break;
            case R.id.btn_refresh_recyclerview:
                type = 4;
                title = "PullToRefreshRecyclerView";
                break;
        }
        if (type > 0)
            startActivity(new Intent(mActivity, SmartRefreshActivity.class)//
                    .putExtra("type", type).putExtra("title", title));
    }
}
