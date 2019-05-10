package cn.appoa.aframework.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import cn.appoa.aframework.R;
import cn.appoa.aframework.listener.DefaultHintDialogListener;
import cn.appoa.aframework.listener.OnCallbackListener;

public class DefaultHintDialog extends BaseDialog {

    public DefaultHintDialog(Context context) {
        super(context);
    }

    public DefaultHintDialog(Context context, OnCallbackListener onCallbackListener) {
        super(context, onCallbackListener);
    }

    public TextView tv_hint_title;
    public TextView tv_hint_message;
    public TextView tv_hint_cancel;
    public TextView tv_hint_confirm;
    public View lint_hint_middle;

    @Override
    public Dialog initDialog(Context context) {
        View view = View.inflate(context, R.layout.dialog_default_hint, null);
        tv_hint_title = (TextView) view.findViewById(R.id.tv_hint_title);
        tv_hint_message = (TextView) view.findViewById(R.id.tv_hint_message);
        tv_hint_cancel = (TextView) view.findViewById(R.id.tv_hint_cancel);
        tv_hint_confirm = (TextView) view.findViewById(R.id.tv_hint_confirm);
        lint_hint_middle = view.findViewById(R.id.lint_hint_middle);
        tv_hint_cancel.setOnClickListener(this);
        tv_hint_confirm.setOnClickListener(this);
        return initCenterToastDialog(view, context);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            if (v.getId() == R.id.tv_hint_cancel) {
                listener.clickCancelButton();
            }
            if (v.getId() == R.id.tv_hint_confirm) {
                listener.clickConfirmButton();
            }
            dismissDialog();
        }
    }

    /**
     * 按钮数量
     */
    private void showButtonCount(int type) {
        tv_hint_title.setText(context.getResources().getString(R.string.hint));
        tv_hint_message.setText(null);
        tv_hint_cancel.setText(context.getResources().getString(R.string.cancel));
        tv_hint_confirm.setText(context.getResources().getString(R.string.confirm));
        if (type == 1) {
            tv_hint_cancel.setVisibility(View.GONE);
            tv_hint_confirm.setVisibility(View.VISIBLE);
            lint_hint_middle.setVisibility(View.GONE);
        } else if (type == 2) {
            tv_hint_cancel.setVisibility(View.VISIBLE);
            tv_hint_confirm.setVisibility(View.VISIBLE);
            lint_hint_middle.setVisibility(View.VISIBLE);
        }
    }

    private DefaultHintDialogListener listener;

    public void showHintDialog1(CharSequence message, DefaultHintDialogListener listener) {
        showButtonCount(1);
        tv_hint_message.setText(message);
        this.listener = listener;
        showDialog();
    }

    public void showHintDialog1(CharSequence title, CharSequence message, DefaultHintDialogListener listener) {
        showButtonCount(1);
        tv_hint_title.setText(title);
        tv_hint_message.setText(message);
        this.listener = listener;
        showDialog();
    }

    public void showHintDialog1(CharSequence confirm, CharSequence title, CharSequence message,
                                DefaultHintDialogListener listener) {
        showButtonCount(1);
        tv_hint_confirm.setText(confirm);
        tv_hint_title.setText(title);
        tv_hint_message.setText(message);
        this.listener = listener;
        showDialog();
    }

    public void showHintDialog2(CharSequence message, DefaultHintDialogListener listener) {
        showButtonCount(2);
        tv_hint_message.setText(message);
        this.listener = listener;
        showDialog();
    }

    public void showHintDialog2(CharSequence title, CharSequence message, DefaultHintDialogListener listener) {
        showButtonCount(2);
        tv_hint_title.setText(title);
        tv_hint_message.setText(message);
        this.listener = listener;
        showDialog();
    }

    public void showHintDialog2(CharSequence cancel, CharSequence confirm, CharSequence title, CharSequence message,
                                DefaultHintDialogListener listener) {
        showButtonCount(2);
        tv_hint_cancel.setText(cancel);
        tv_hint_confirm.setText(confirm);
        tv_hint_title.setText(title);
        tv_hint_message.setText(message);
        this.listener = listener;
        showDialog();
    }

}
