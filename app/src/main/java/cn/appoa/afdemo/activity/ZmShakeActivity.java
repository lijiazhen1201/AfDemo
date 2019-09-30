package cn.appoa.afdemo.activity;

import android.os.Bundle;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseActivity;
import cn.appoa.afdemo.fragment.MyShakeFragment;


public class ZmShakeActivity extends BaseActivity {

    @Override
    public void initContent(Bundle savedInstanceState) {

    }

    private MyShakeFragment fragment;

    @Override
    public void initData() {
        fragment = new MyShakeFragment();
        mFragmentManager.beginTransaction().replace(R.id.fl_fragment, fragment).commit();
    }
}
