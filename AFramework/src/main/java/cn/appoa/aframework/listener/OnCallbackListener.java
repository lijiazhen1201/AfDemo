package cn.appoa.aframework.listener;

/**
 * 通用回调监听
 */
public interface OnCallbackListener {

    void onCallback(int type, Object... obj);
}
