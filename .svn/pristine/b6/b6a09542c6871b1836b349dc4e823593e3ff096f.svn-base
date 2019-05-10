package cn.appoa.aframework.listener;

import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.widget.CompoundButton;
import android.widget.EditText;

/**
 * 显示隐藏密码
 */
public class EditOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

    private EditText et;

    public EditOnCheckedChangeListener(EditText et) {
        super();
        this.et = et;
        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            // 显示为普通文本
            et.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            // 显示为密码
            et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        // 光标移动到最后
        Editable etable = et.getText();
        Selection.setSelection(etable, etable.length());
    }

}
