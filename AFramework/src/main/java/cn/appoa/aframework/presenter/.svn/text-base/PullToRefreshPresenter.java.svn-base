package cn.appoa.aframework.presenter;

import java.util.Map;

import cn.appoa.aframework.view.IBaseView;
import cn.appoa.aframework.view.IPullToRefreshView;

public abstract class PullToRefreshPresenter extends BasePresenter {

    protected IPullToRefreshView mIPullToRefreshView;

    @Override
    public void onAttach(IBaseView view) {
        if (view instanceof IPullToRefreshView) {
            mIPullToRefreshView = (IPullToRefreshView) view;
        }
    }

    @Override
    public void onDetach() {
        if (mIPullToRefreshView != null) {
            mIPullToRefreshView = null;
        }
    }

    /**
     * 获取数据
     *
     * @param url
     * @param params
     */
    public abstract void getData(String url, Map<String, String> params);

}
