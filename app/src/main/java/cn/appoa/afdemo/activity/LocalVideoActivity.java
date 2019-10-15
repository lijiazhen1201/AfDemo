package cn.appoa.afdemo.activity;

import android.os.Bundle;

import com.cjt2325.cameralibrary.local.LocalVideoFragment;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseActivity;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;

/**
 * 本地视频
 */
public class LocalVideoActivity extends BaseActivity {

    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(this).setTitle("本地视频")
                .setBackImage(R.drawable.back_white).create();
    }

    @Override
    public void initContent(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        super.initView();
    }

    private LocalVideoFragment fragment;

    @Override
    public void initData() {
        fragment = new LocalVideoFragment();
        mFragmentManager.beginTransaction().replace(R.id.fl_fragment, fragment).commit();
    }
}
