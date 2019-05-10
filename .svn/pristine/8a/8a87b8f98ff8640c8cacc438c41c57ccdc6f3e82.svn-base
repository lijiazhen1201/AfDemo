package cn.appoa.aframework.listener;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * 显示隐藏刪除
 */
public class EditTextDeleteWatcher implements TextWatcher {

    private boolean hasFocus;
    private boolean isHasFocus;
    private EditText et;
    private ImageView iv;

    public EditTextDeleteWatcher(EditText et, ImageView iv) {
        this(et, iv, false);
    }

    public EditTextDeleteWatcher(EditText et, ImageView iv, boolean isHasFocus) {
        super();
        this.et = et;
        this.iv = iv;
        this.isHasFocus = isHasFocus;
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditTextDeleteWatcher.this.hasFocus = hasFocus;
            }
        });
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTextDeleteWatcher.this.et.setText(null);
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (isHasFocus) {
            if (hasFocus && !TextUtils.isEmpty(s)) {
                iv.setVisibility(View.VISIBLE);
            } else {
                iv.setVisibility(View.GONE);
            }
        } else {
            if (TextUtils.isEmpty(s)) {
                iv.setVisibility(View.GONE);
            } else {
                iv.setVisibility(View.VISIBLE);
            }
        }
    }

}
