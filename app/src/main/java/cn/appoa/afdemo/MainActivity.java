package cn.appoa.afdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.divider.ListItemDecoration;

import java.util.Arrays;

import cn.appoa.afdemo.activity.BannerActivity;
import cn.appoa.afdemo.activity.RefreshBeanActivity;
import cn.appoa.afdemo.activity.UploadAvatarActivity;
import cn.appoa.afdemo.activity.UploadImageActivity;
import cn.appoa.afdemo.activity.UploadVideoActivity;
import cn.appoa.afdemo.activity.UploadVoiceActivity;
import cn.appoa.afdemo.base.BaseActivity;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;

public class MainActivity extends BaseActivity {

    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(this).setTitle("首页").create();
    }

    private RecyclerView recyclerView;

    @Override
    public void initContent(Bundle savedInstanceState) {
        recyclerView = new RecyclerView(mActivity);
        setContent(recyclerView);
    }

    @Override
    public void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new ListItemDecoration(mActivity));
    }

    private String[] titles = {"头像上传", "图片多选", "视频上传", "下拉刷新", "轮播图", "语音上传"};
    private Class[] clazzs = {UploadAvatarActivity.class, UploadImageActivity.class,
            UploadVideoActivity.class, RefreshBeanActivity.class, BannerActivity.class,
            UploadVoiceActivity.class};

    @Override
    public void initData() {
        recyclerView.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_main, Arrays.asList(titles)) {

            @Override
            protected void convert(BaseViewHolder helper, String item) {
                final int position = helper.getLayoutPosition();
                helper.setText(R.id.tv_main, item);
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            startActivity(new Intent(mActivity, clazzs[position]));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean enableSliding() {
        return false;
    }
}
