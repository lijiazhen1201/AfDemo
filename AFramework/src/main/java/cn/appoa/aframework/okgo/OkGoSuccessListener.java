package cn.appoa.aframework.okgo;

import android.text.TextUtils;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import cn.appoa.aframework.app.AfApplication;
import cn.appoa.aframework.utils.AtyUtils;
import cn.appoa.aframework.utils.JsonUtils;
import cn.appoa.aframework.view.IBaseView;

/**
 * 网络访问成功的回调
 */
public abstract class OkGoSuccessListener extends StringCallback {

    /**
     * 回调接口（不可为空）
     */
    protected IBaseView mView;
    /**
     * 打印的tag（传空不打印数据）
     */
    protected String tag;
    /**
     * 加载框显示的信息（传空不显示加载框）
     */
    protected String loadingMessage;
    /**
     * 访问成功时弹出的消息类型（0不显示消息 1只显示成功消息 2只显示失败消息 3显示成功和失败消息）
     */
    protected int messageType;
    /**
     * 访问失败时弹出的错误信息（传空不弹出toast）
     */
    protected String errorMessage;

    /**
     * 不打印，无加载框，不弹成功信息，不弹失败信息
     *
     * @param mView
     */
    public OkGoSuccessListener(IBaseView mView) {
        this(mView, null, null, 0, null);
    }

    /**
     * 打印，无加载框，不弹成功信息，不弹失败信息
     *
     * @param mView
     * @param tag
     */
    public OkGoSuccessListener(IBaseView mView, String tag) {
        this(mView, tag, null, 0, null);
    }

    /**
     * 打印，加载框，不弹成功信息，不弹失败信息
     *
     * @param mView
     * @param tag
     * @param loadingMessage
     */
    public OkGoSuccessListener(IBaseView mView, String tag, String loadingMessage) {
        this(mView, tag, loadingMessage, 0, null);
    }

    /**
     * 打印，加载框，弹成功信息，不弹失败信息
     *
     * @param mView
     * @param tag
     * @param loadingMessage
     * @param messageType
     */
    public OkGoSuccessListener(IBaseView mView, String tag, String loadingMessage, int messageType) {
        this(mView, tag, loadingMessage, messageType, null);
    }

    /**
     * 打印，加载框，不弹成功信息，弹失败信息
     *
     * @param mView
     * @param tag
     * @param loadingMessage
     * @param errorMessage
     */
    public OkGoSuccessListener(IBaseView mView, String tag, String loadingMessage, String errorMessage) {
        this(mView, tag, loadingMessage, 0, errorMessage);
    }

    /**
     * 打印，无加载框，弹成功信息，弹失败信息
     *
     * @param mView
     * @param tag
     * @param messageType
     * @param errorMessage
     */
    public OkGoSuccessListener(IBaseView mView, String tag, int messageType, String errorMessage) {
        this(mView, tag, null, messageType, errorMessage);
    }

    /**
     * 打印，加载框，弹成功信息，弹失败信息
     *
     * @param mView
     * @param tag
     * @param loadingMessage
     * @param messageType
     * @param errorMessage
     */
    public OkGoSuccessListener(IBaseView mView, String tag, String loadingMessage, int messageType, String errorMessage) {
        this.mView = mView;
        this.tag = tag;
        this.loadingMessage = loadingMessage;
        this.messageType = messageType;
        this.errorMessage = errorMessage;
    }

    @Override
    public void onSuccess(Response<String> responses) {
        //对返回数据进行操作的回调， UI线程
        String response = responses.body();
        if (TextUtils.isEmpty(response)) {
            return;
        }
        if (mView != null) {
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

    @Override
    public void onCacheSuccess(Response<String> responses) {
        //缓存成功的回调,UI线程
        onSuccess(responses);
    }

    @Override
    public void onError(Response<String> error) {
        //请求失败，响应错误，数据解析错误等，都会回调该方法， UI线程
        if (!TextUtils.isEmpty(tag)) {
            Throwable throwable = error.getException();
            if (throwable != null) {
                AtyUtils.i(tag, throwable.toString());
                String detailMessage = throwable.getMessage();
                if (!TextUtils.isEmpty(detailMessage)) {
                    AtyUtils.i(tag, detailMessage);
                }
            }
        }
        if (!TextUtils.isEmpty(errorMessage)) {
            AtyUtils.showShort(AfApplication.appContext, errorMessage, false);
        }
		onFinish();
    }

    @Override
    public void onStart(Request<String, ? extends Request> request) {
        //请求网络开始前，UI线程
        if (mView != null && !TextUtils.isEmpty(loadingMessage)) {
            mView.showLoading(loadingMessage);
        }
    }

    @Override
    public void onFinish() {
        //请求网络结束后，UI线程
        if (mView != null && !TextUtils.isEmpty(loadingMessage)) {
            mView.dismissLoading();
        }
    }

    @Override
    public void uploadProgress(Progress progress) {
        //上传过程中的进度回调，get请求不回调，UI线程
    }

    @Override
    public void downloadProgress(Progress progress) {
        //下载过程中的进度回调，UI线程
    }

    @Override
    public String convertResponse(okhttp3.Response response) throws Throwable {
        //拿到响应后，将数据转换成需要的格式，子线程中执行，可以是耗时操作
        return super.convertResponse(response);
    }

    /**
     * 成功的回调
     *
     * @param response
     */
    public abstract void onSuccessResponse(String response);

}
