package cn.appoa.aframework.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import cn.appoa.aframework.R;
import cn.appoa.aframework.listener.OnCallbackListener;
import zm.bus.event.BusProvider;

public abstract class BaseDialog implements View.OnClickListener {

    /**
     * 上下文
     */
    public Context context;

    /**
     * 弹窗布局
     */
    public int layoutId;
    /**
     * 弹窗
     */
    public Dialog dialog;

    public BaseDialog(Context context) {
        this(context, null);
    }

    /**
     * 监听回调
     */
    protected OnCallbackListener onCallbackListener;

    public void setOnCallbackListener(OnCallbackListener onCallbackListener) {
        this.onCallbackListener = onCallbackListener;
    }

    public BaseDialog(Context context, OnCallbackListener onCallbackListener) {
        this(context, onCallbackListener, 0);
    }

    public BaseDialog(Context context, OnCallbackListener onCallbackListener, int layoutId) {
        this.context = context;
        this.onCallbackListener = onCallbackListener;
        this.layoutId = layoutId;
        BusProvider.getInstance().register(this);
        dialog = initDialog(context);
    }

    /**
     * 显示弹窗
     */
    public void showDialog() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * 隐藏弹窗
     */
    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {

    }

    @SuppressWarnings("unused")
    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        if (dialog != null) {
            this.onDismissListener = onDismissListener;
            dialog.setOnDismissListener(onDismissListener);
        }
    }

    /**
     * 显示加载框
     */
    public void showLoading(CharSequence message) {
        DefaultLoadingDialog.getInstance().showLoading(context, message);
    }

    /**
     * 隐藏加载框
     */
    public void dismissLoading() {
        DefaultLoadingDialog.getInstance().dismissLoading();
    }

    /**
     * 弹出键盘，需在showDialog()后调用
     */
    public void requestFocus(EditText et) {
        if (et != null) {
            et.requestFocus();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    /**
     * 初始化弹窗
     */
    public Dialog initWrapDialog(View view, Context context, int Gravity, int animId) {
        return initDialog(view, context, Gravity, animId, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, 0.6f);
    }

    /**
     * 初始化弹窗
     */
    public Dialog initMatchDialog(View view, Context context, int Gravity, int animId) {
        return initDialog(view, context, Gravity, animId, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT, 0.6f);
    }

    /**
     * 初始化弹窗
     */
    public Dialog initMatchWrapDialog(View view, Context context, int Gravity, int animId) {
        return initDialog(view, context, Gravity, animId, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, 0.6f);
    }

    /**
     * 初始化弹窗
     */
    public Dialog initWrapMatchDialog(View view, Context context, int Gravity, int animId) {
        return initDialog(view, context, Gravity, animId, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.MATCH_PARENT, 0.6f);
    }

    /**
     * 初始化弹窗
     */
    public Dialog initCenterToastDialog(View view, Context context) {
        return initDialog(view, context, Gravity.CENTER, android.R.style.Animation_Toast,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, 0.6f);
    }

    /**
     * 初始化弹窗
     */
    public Dialog initBottomInputMethodDialog(View view, Context context) {
        return initDialog(view, context, Gravity.BOTTOM, android.R.style.Animation_InputMethod,
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, 0.6f);
    }

    /**
     * 初始化弹窗
     */
    public Dialog initDialog(View view, Context context, int Gravity, int animId, int width, int height,
                             float dimAmount) {
        Dialog dialog = new AfDialog(context, R.style.SimpleDialog);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity;// Gravity.BOTTOM
        params.width = width;// WindowManager.LayoutParams.MATCH_PARENT
        params.height = height;// WindowManager.LayoutParams.WRAP_CONTENT
        params.dimAmount = dimAmount;// 0.0f时背景全透明
        window.setAttributes(params);
        window.setWindowAnimations(animId);// android.R.style.Animation_InputMethod
        return dialog;
    }

    /**
     * 初始化弹窗
     */
    public abstract Dialog initDialog(Context context);
}
