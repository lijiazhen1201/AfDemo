package cn.appoa.aframework.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import cn.appoa.aframework.R;
import cn.appoa.aframework.listener.OnCallbackListener;

/**
 * 上传图片弹窗
 */
public class DefaultUploadImgDialog extends BaseDialog {

    public DefaultUploadImgDialog(Context context) {
        super(context);
    }

    public DefaultUploadImgDialog(Context context, OnCallbackListener onCallbackListener) {
        super(context, onCallbackListener);
    }

    public boolean isConfirm;
    public TextView tv_upload_from_camera;
    public TextView tv_upload_from_album;
    public TextView tv_upload_cancel;

    @Override
    public Dialog initDialog(Context context) {
        View view = View.inflate(context, R.layout.dialog_default_upload_img, null);
        view.setClickable(true);
        tv_upload_from_camera = (TextView) view.findViewById(R.id.tv_upload_from_camera);
        tv_upload_from_album = (TextView) view.findViewById(R.id.tv_upload_from_album);
        tv_upload_cancel = (TextView) view.findViewById(R.id.tv_upload_cancel);
        tv_upload_from_camera.setOnClickListener(this);
        tv_upload_from_album.setOnClickListener(this);
        tv_upload_cancel.setOnClickListener(this);
        return initBottomInputMethodDialog(view, context);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            if (v.getId() == R.id.tv_upload_from_camera) {
                // 拍照
                isConfirm = true;
                listener.onUploadImg(1);
            }
            if (v.getId() == R.id.tv_upload_from_album) {
                // 相册
                isConfirm = true;
                listener.onUploadImg(2);
            }
            if (v.getId() == R.id.tv_upload_cancel) {
                // 取消
            }
            dismissDialog();
        }
    }

    private OnUploadImgListener listener;

    public void setOnUploadImgListener(OnUploadImgListener listener) {
        this.listener = listener;
    }

    public interface OnUploadImgListener {

        void onUploadImg(int type);
    }
}
