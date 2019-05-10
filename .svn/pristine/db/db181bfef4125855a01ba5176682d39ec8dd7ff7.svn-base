package cn.appoa.aframework.listener;

import android.text.TextUtils;

import com.android.volley.Response;

import cn.appoa.aframework.app.AfApplication;
import cn.appoa.aframework.utils.AtyUtils;
import cn.appoa.aframework.utils.JsonUtils;
import cn.appoa.aframework.view.IBaseView;

/**
 * 访问成功的回调
 */
public abstract class VolleySuccessListener implements Response.Listener<String> {

    protected IBaseView mView;
    protected String tag;
    /**
     * 消息类型 0不显示消息 1只显示成功消息 2只显示失败消息 3显示成功和失败消息
     */
    protected int messageType;

    public VolleySuccessListener(IBaseView mView) {
        this(mView, null);
    }

    public VolleySuccessListener(IBaseView mView, String tag) {
        this(mView, tag, 0);
    }

    public VolleySuccessListener(IBaseView mView, String tag, int messageType) {
        this.mView = mView;
        this.tag = tag;
        this.messageType = messageType;
    }

    @Override
    public void onResponse(String response) {
        if (mView != null) {
            mView.dismissLoading();
            if (!TextUtils.isEmpty(tag)) {
                AtyUtils.i(tag, response);
            }
            if (JsonUtils.isErrorCode(response)) {
                mView.onErrorCodeResponse(JsonUtils.getMsg(response));
                return;
            }
            if (messageType == 3) {
                JsonUtils.showMsg(AfApplication.appContext, response);
            }
            if (JsonUtils.filterJson(response)) {
                if (messageType == 1) {
                    JsonUtils.showSuccessMsg(AfApplication.appContext, response);
                }
                onSuccessResponse(response);
            } else {
                if (messageType == 2) {
                    JsonUtils.showErrorMsg(AfApplication.appContext, response);
                }
            }
        }
    }

    /**
     * 成功的回调
     *
     * @param response
     */
    public abstract void onSuccessResponse(String response);

}
