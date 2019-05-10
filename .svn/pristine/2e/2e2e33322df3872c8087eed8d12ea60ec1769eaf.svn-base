package cn.appoa.aframework.dialog;

import android.app.ProgressDialog;
import android.content.Context;

public class DefaultLoadingDialog {

    /**
     * the global DefaultLoadingDialog instance
     */
    private static DefaultLoadingDialog instance = null;

    private DefaultLoadingDialog() {
    }

    /**
     * get instance of DefaultLoadingDialog
     *
     * @return
     */
    public synchronized static DefaultLoadingDialog getInstance() {
        if (instance == null) {
            instance = new DefaultLoadingDialog();
        }
        return instance;
    }

    /**
     * 加载框
     */
    private ProgressDialog mLoading = null;

    /**
     * 显示加载框
     */
    public void showLoading(Context context, CharSequence message) {
        try {
            if (context == null) {
                return;
            }
            if (mLoading == null) {
                mLoading = new ProgressDialog(context);
            }
            mLoading.setCancelable(false);
            mLoading.setCanceledOnTouchOutside(false);
            mLoading.setMessage(message);
            if (!mLoading.isShowing()) {
                mLoading.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏加载框
     */
    public void dismissLoading() {
        try {
            if (mLoading != null && mLoading.isShowing()) {
                mLoading.dismiss();
                mLoading = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
