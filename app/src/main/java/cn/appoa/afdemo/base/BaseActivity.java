package cn.appoa.afdemo.base;

import cn.appoa.aframework.activity.AfActivity;
import cn.appoa.aframework.presenter.BasePresenter;

public abstract class BaseActivity<P extends BasePresenter> extends AfActivity<P> {

    @Override
    public boolean enableSliding() {
        //开启侧滑返回
        return true;
    }
}
