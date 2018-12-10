package cn.appoa.aframework.listener;

import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import cn.appoa.aframework.app.AfApplication;
import cn.appoa.aframework.utils.AtyUtils;
import cn.appoa.aframework.view.IBaseView;

/**
 * 访问失败的回调
 */
public class VolleyErrorListener implements Response.ErrorListener {

    protected IBaseView mView;
    protected String tag;
    protected String errorMessage;

    public VolleyErrorListener(IBaseView mView) {
        this(mView, null);
    }

    public VolleyErrorListener(IBaseView mView, String tag) {
        this(mView, tag, null);
    }

    public VolleyErrorListener(IBaseView mView, String tag, String errorMessage) {
        this.mView = mView;
        this.tag = tag;
        this.errorMessage = errorMessage;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (mView != null) {
            mView.dismissLoading();
            if (!TextUtils.isEmpty(tag)) {
                AtyUtils.e(tag, error);
            }
            if (!TextUtils.isEmpty(errorMessage)) {
                AtyUtils.showShort(AfApplication.appContext, errorMessage, false);
            }
        }
    }

}
