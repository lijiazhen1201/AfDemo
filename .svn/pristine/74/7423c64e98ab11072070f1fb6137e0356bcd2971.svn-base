package cn.appoa.aframework.presenter;

import java.util.List;

import cn.appoa.aframework.bean.AppVersion;
import cn.appoa.aframework.listener.VolleyDatasListener;
import cn.appoa.aframework.listener.VolleyErrorListener;
import cn.appoa.aframework.view.IBaseView;
import cn.appoa.aframework.view.IVersionView;
import zm.http.volley.ZmVolley;
import zm.http.volley.request.ZmStringRequest;

public class VersionPresenter extends BasePresenter {

    protected IVersionView mVersionView;

    @Override
    public void onAttach(IBaseView view) {
        if (view instanceof IVersionView) {
            mVersionView = (IVersionView) view;
        }
    }

    @Override
    public void onDetach() {
        if (mVersionView != null) {
            mVersionView = null;
        }
    }

    /**
     * 获取版本信息
     *
     * @param GetAppVersion 接口地址
     */
    public void getVersion(String GetAppVersion) {
        if (mVersionView == null)
            return;
        ZmVolley.request(new ZmStringRequest(GetAppVersion,
                new VolleyDatasListener<AppVersion>(mVersionView, "版本更新", AppVersion.class) {

                    @Override
                    public void onDatasResponse(List<AppVersion> datas) {
                        if (datas != null && datas.size() > 0) {
                            mVersionView.setVersion(datas.get(0));
                        }
                    }
                }, new VolleyErrorListener(mVersionView, "版本更新")), mVersionView.getRequestTag());
    }
}
