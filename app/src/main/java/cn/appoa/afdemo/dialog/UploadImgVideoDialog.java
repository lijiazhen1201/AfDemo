package cn.appoa.afdemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import cn.appoa.afdemo.R;
import cn.appoa.aframework.dialog.BaseDialog;
import cn.appoa.aframework.listener.OnCallbackListener;


public class UploadImgVideoDialog extends BaseDialog {

    public UploadImgVideoDialog(Context context) {
        super(context);
    }

    public UploadImgVideoDialog(Context context, OnCallbackListener onCallbackListener) {
        super(context, onCallbackListener);
    }

    @Override
    public Dialog initDialog(Context context) {
        View view = View.inflate(context, R.layout.dialog_upload_img_video, null);
        view.findViewById(R.id.upload_by_camera).setOnClickListener(this);
        view.findViewById(R.id.upload_by_album).setOnClickListener(this);
        return initCenterToastDialog(view, context);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            int id = v.getId();
            if (id == R.id.upload_by_camera) {
                //拍摄
                listener.onUploadImgVideo(1);
            } else if (id == R.id.upload_by_album) {
                //从相册选择
                listener.onUploadImgVideo(2);
            }
            dismissDialog();
        }
    }

    private OnUploadImgVideoListener listener;

    public void setOnUploadImgVideoListener(OnUploadImgVideoListener listener) {
        this.listener = listener;
    }

    public interface OnUploadImgVideoListener {

        void onUploadImgVideo(int type);
    }
}
