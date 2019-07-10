package cn.appoa.aframework.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import cn.appoa.aframework.R;
import cn.appoa.aframework.listener.OnCallbackListener;

public class DefaultUploadVideoDialog extends BaseDialog {

    public DefaultUploadVideoDialog(Context context) {
        super(context);
    }

    public DefaultUploadVideoDialog(Context context, OnCallbackListener onCallbackListener) {
        super(context, onCallbackListener);
    }

    public boolean isConfirm;
    public TextView tv_upload_video_camera;
    public TextView tv_upload_video_album;
    public TextView tv_upload_cancel;

    @Override
    public Dialog initDialog(Context context) {
        View view = View.inflate(context, R.layout.dialog_default_upload_video, null);
        tv_upload_video_camera = (TextView) view.findViewById(R.id.tv_upload_video_camera);
        tv_upload_video_album = (TextView) view.findViewById(R.id.tv_upload_video_album);
        tv_upload_cancel = (TextView) view.findViewById(R.id.tv_upload_cancel);
        tv_upload_video_camera.setOnClickListener(this);
        tv_upload_video_album.setOnClickListener(this);
        tv_upload_cancel.setOnClickListener(this);
        return initBottomInputMethodDialog(view, context);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            int id = v.getId();
            if (id == R.id.tv_upload_video_camera) {
                // 拍摄
                isConfirm = true;
                listener.onUploadVideo(1);
            }
            if (id == R.id.tv_upload_video_album) {
                // 本地
                isConfirm = true;
                listener.onUploadVideo(2);
            }
            if (id == R.id.tv_upload_cancel) {
                // 取消
            }
            dismissDialog();
        }
    }

    private OnUploadVideoListener listener;

    public void setOnUploadVideoListener(OnUploadVideoListener listener) {
        this.listener = listener;
    }

    public interface OnUploadVideoListener {

        void onUploadVideo(int type);
    }
}
