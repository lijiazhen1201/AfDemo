package cn.appoa.afdemo.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import cn.appoa.afdemo.R;
import cn.appoa.aframework.dialog.BaseDialog;
import cn.appoa.aframework.listener.OnCallbackListener;
import cn.appoa.aframework.utils.AtyUtils;

public class UpgradeDialog extends BaseDialog {

    public UpgradeDialog(Context context, OnCallbackListener onCallbackListener, int layoutId) {
        super(context, onCallbackListener, layoutId);
    }

    private TextView tv_version_name;
    private TextView tv_title;
    private TextView tv_content;

    @Override
    public Dialog initDialog(Context context) {
        View view = View.inflate(context, layoutId, null);
        try {
            tv_version_name = (TextView) view.findViewById(R.id.tv_version_name);
            tv_version_name.setText("V" + AtyUtils.getVersionName(context));
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            view.findViewById(R.id.cancel_upgrade).setOnClickListener(this);
            view.findViewById(R.id.confirm_upgrade).setOnClickListener(this);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return initCenterToastDialog(view, context);
    }

    /**
     * 下载地址
     */
    private String AndroidFilePath;

    /**
     * 显示升级弹窗
     *
     * @param AndroidTitle
     * @param AndroidContents
     * @param AndroidFilePath
     */
    public void showUpgradeDialog(String AndroidTitle, String AndroidContents, String AndroidFilePath) {
        if (tv_title != null) {
            tv_title.setText(AndroidTitle);
        }
        if (tv_content != null) {
            tv_content.setText(AndroidContents);
        }
        this.AndroidFilePath = AndroidFilePath;
        showDialog();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cancel_upgrade) {
            if (onCallbackListener != null) {
                onCallbackListener.onCallback(0, AndroidFilePath);
            }
        } else if (id == R.id.confirm_upgrade) {
            if (onCallbackListener != null) {
                onCallbackListener.onCallback(1, AndroidFilePath);
            }
        }
        dismissDialog();
    }

}
