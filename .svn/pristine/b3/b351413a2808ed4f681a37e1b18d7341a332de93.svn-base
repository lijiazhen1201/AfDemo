package cn.appoa.aframework.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class AfDialog extends Dialog {

    public AfDialog(Context context) {
        super(context);
    }

    public AfDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void dismiss() {
        View view = getCurrentFocus();
        if (view != null && view instanceof TextView) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
        super.dismiss();
    }
}
