package cn.appoa.aframework.listener;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 只能输入两位小数
 */
public class Edit2PointTextWatcher implements TextWatcher {

    private EditText et;

    public Edit2PointTextWatcher(EditText et) {
        super();
        this.et = et;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
                et.setText(s);
                et.setSelection(s.length());
            }
        }
        if (s.toString().trim().substring(0).equals(".")) {
            s = "0" + s;
            et.setText(s);
            et.setSelection(2);
        }
        if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                et.setText(s.subSequence(0, 1));
                et.setSelection(1);
                return;
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
//		String temp = s.toString();
//		int posDot = temp.indexOf(".");
//		if (posDot <= 0)
//			return;
//		if (temp.length() - posDot - 1 > 2) {
//			s.delete(posDot + 3, posDot + 4);
//		}
    }

}
