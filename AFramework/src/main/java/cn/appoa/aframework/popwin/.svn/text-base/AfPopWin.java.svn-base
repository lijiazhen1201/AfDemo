package cn.appoa.aframework.popwin;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;

public class AfPopWin extends PopupWindow {

    public AfPopWin(Context context) {
        super(context);
    }

    public AfPopWin(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AfPopWin(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AfPopWin(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AfPopWin() {
        super();
    }

    public AfPopWin(View contentView) {
        super(contentView);
    }

    public AfPopWin(int width, int height) {
        super(width, height);
    }

    public AfPopWin(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public AfPopWin(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

}
