package cn.appoa.aframework.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.appoa.aframework.R;
import cn.appoa.aframework.listener.OnCallbackListener;
import cn.appoa.aframework.widget.wheel.WheelView;
import cn.appoa.aframework.widget.wheel.adapter.ArrayWheelAdapter;
import cn.appoa.aframework.widget.wheel.listener.OnWheelChangedListener;

public class StringWheelDialog extends BaseDialog implements OnWheelChangedListener {

    private int type;

    public StringWheelDialog(Context context, OnCallbackListener onCallbackListener, int type) {
        super(context, onCallbackListener);
        this.type = type;
    }

    private TextView tv_dialog_title;
    private TextView tv_dialog_cancel;
    private TextView tv_dialog_confirm;
    private WheelView mWheelView;

    @Override
    public Dialog initDialog(Context context) {
        View view = View.inflate(context, R.layout.dialog_string_wheel, null);
        tv_dialog_title = (TextView) view.findViewById(R.id.tv_dialog_title);
        tv_dialog_cancel = (TextView) view.findViewById(R.id.tv_dialog_cancel);
        tv_dialog_cancel.setOnClickListener(this);
        tv_dialog_confirm = (TextView) view.findViewById(R.id.tv_dialog_confirm);
        tv_dialog_confirm.setOnClickListener(this);
        mWheelView = (WheelView) view.findViewById(R.id.mWheelView);
        mWheelView.addChangingListener(this);
        return initMatchWrapDialog(view, context, Gravity.BOTTOM, android.R.style.Animation_InputMethod);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_dialog_confirm) {
            if (list != null && list.size() > 0 && onCallbackListener != null) {
                onCallbackListener.onCallback(type, position, list.get(position));
            }
        } else if (id == R.id.tv_dialog_cancel) {
            // 取消
        }
        dismissDialog();
    }

    private int position;
    private List<String> list;

    public void showStringWheelDialog(String title, List<String> list) {
        tv_dialog_title.setText(title);
        this.position = 0;
        this.list = list;
        initAdapter(list);
        showDialog();
    }

    private ArrayWheelAdapter<String> adapter;

    /**
     * 初始化滚轮
     */
    private void initAdapter(List<String> list) {
        mWheelView.setAdapter(new ArrayWheelAdapter<String>(context, new String[]{}));
        if (list != null && list.size() > 0) {
            String[] items = new String[list.size()];
            list.toArray(items);
            adapter = new ArrayWheelAdapter<String>(context, items);
            mWheelView.setAdapter(adapter);
            this.position = 0;
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        this.position = newValue;
    }

}
