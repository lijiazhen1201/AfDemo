package cn.appoa.aframework.popwin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;

import cn.appoa.aframework.dialog.DefaultLoadingDialog;
import cn.appoa.aframework.listener.OnCallbackListener;
import zm.bus.event.BusProvider;

public abstract class BasePopWin implements View.OnClickListener {

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
    public PopupWindow pop;

    public BasePopWin(Context context) {
        this(context, null);
    }

    /**
     * 监听回调
     */
    protected OnCallbackListener onCallbackListener;

    public void setOnCallbackListener(OnCallbackListener onCallbackListener) {
        this.onCallbackListener = onCallbackListener;
    }

    public BasePopWin(Context context, OnCallbackListener onCallbackListener) {
        this(context, onCallbackListener, 0);
    }

    public BasePopWin(Context context, OnCallbackListener onCallbackListener, int layoutId) {
        this.context = context;
        this.onCallbackListener = onCallbackListener;
        this.layoutId = layoutId;
        BusProvider.getInstance().register(this);
        pop = initPop(context);
    }

    @Override
    public void onClick(View v) {

    }

    @SuppressWarnings("unused")
    private PopupWindow.OnDismissListener onDismissListener;

    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        if (pop != null) {
            this.onDismissListener = onDismissListener;
            pop.setOnDismissListener(onDismissListener);
        }
    }

    /**
     * 显示弹窗
     */
    public void showAsUp(View view) {
        if (pop != null && !pop.isShowing()) {
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            pop.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] - pop.getHeight());
        }
    }

    /**
     * 显示弹窗
     */
    public void showAsDown(View view) {
        showAsDown(view, 0, 0);
    }

    /**
     * 显示弹窗
     */
    public void showAsDown(View view, int x, int y) {
        if (pop != null && !pop.isShowing()) {
            if (Build.VERSION.SDK_INT >= 24) {
//				int[] location = new int[2];
//				view.getLocationOnScreen(location);
//				pop.showAtLocation(view, Gravity.NO_GRAVITY, x, location[1] + view.getHeight() + y);
                Rect visibleFrame = new Rect();
                view.getGlobalVisibleRect(visibleFrame);
                int height = view.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
                pop.setHeight(height);
                pop.showAsDropDown(view, 0, y);
            } else {
                pop.showAsDropDown(view, 0, y);
            }
        }
    }

    /**
     * 显示弹窗
     */
    public void showAsLeft(View view) {
        if (pop != null && !pop.isShowing()) {
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            pop.showAtLocation(view, Gravity.NO_GRAVITY, location[0] - pop.getWidth(), location[1]);
        }
    }

    /**
     * 显示弹窗
     */
    public void showAsRight(View view) {
        if (pop != null && !pop.isShowing()) {
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            pop.showAtLocation(view, Gravity.NO_GRAVITY, location[0] + view.getWidth(), location[1]);
        }
    }

    /**
     * 隐藏弹窗
     */
    public void dismissPop() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
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
     * 初始化弹窗
     */
    @SuppressWarnings("deprecation")
    public PopupWindow initPop(View view, int width, int height) {
        PopupWindow pop = new PopupWindow(view, width, height, true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pop.setTouchable(true);
        return pop;
    }

    /**
     * 设置背景透明度
     */
    public void setBackAlpha(Context context, float alpha) {
        if (context instanceof Activity) {
            WindowManager.LayoutParams params = ((Activity) context).getWindow().getAttributes();
            params.alpha = alpha;
            ((Activity) context).getWindow().setAttributes(params);
        }
    }

    /**
     * 初始化弹窗
     */
    public PopupWindow initWrapPop(View view) {
        return initPop(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    /**
     * 初始化弹窗
     */
    public PopupWindow initMatchPop(View view) {
        return initPop(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    /**
     * 初始化弹窗
     */
    public PopupWindow initMatchWrapPop(View view) {
        return initPop(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    /**
     * 初始化弹窗
     */
    public PopupWindow initWrapMatchPop(View view) {
        return initPop(view, LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
    }

    /**
     * 初始化弹窗
     */
    public abstract PopupWindow initPop(Context context);

}
