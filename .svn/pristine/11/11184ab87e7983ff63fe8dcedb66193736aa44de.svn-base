package cn.appoa.afdemo.base;

import android.support.v4.content.ContextCompat;

import cn.appoa.afdemo.R;
import cn.appoa.aframework.activity.AfActivity;
import cn.appoa.aframework.presenter.BasePresenter;
import cn.appoa.aframework.utils.AtyUtils;

public abstract class BaseActivity<P extends BasePresenter> extends AfActivity<P> {

    @Override
    public boolean enableSliding() {
        //开启侧滑返回
        return true;
    }

    @Override
    public void initView() {
        //沉浸式
        if (titlebar != null) {
            titlebar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorDefaultTitlebarBg));
            AtyUtils.setPaddingTop(mActivity, titlebar);
        }
    }
}
