package cn.appoa.afdemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.JCameraViewActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseActivity;
import cn.appoa.aframework.app.AfApplication;
import cn.appoa.aframework.dialog.DefaultUploadVideoDialog;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;
import cn.jzvd.JZVideoPlayerActivity;
import zm.imageloader.LoadingBitmapListener;

public class UploadVideoActivity extends BaseActivity implements View.OnClickListener, DefaultUploadVideoDialog.OnUploadVideoListener {

    private String video_path;// 视频地址
    private String video_path_img;// 视频图片
    private long video_duration;// 视频长度
    private String base64Video = "";// 视频base64
    private String base64Cover = "";// 视频封面base64

    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(this).setTitle("视频上传")
                .setBackImage(R.drawable.back_white).create();
    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        setContent(R.layout.activity_upload_video);
    }

    private ImageView iv_video_img;
    private ImageView iv_video_img_del;
    private ImageView iv_video_logo;

    @Override
    public void initView() {
        super.initView();
        findViewById(R.id.tv_play_video).setOnClickListener(this);
        iv_video_img = (ImageView) findViewById(R.id.iv_picker_add);
        iv_video_img.setOnClickListener(this);
        iv_video_img_del = (ImageView) findViewById(R.id.iv_picker_del);
        iv_video_img_del.setOnClickListener(this);
        iv_video_logo = (ImageView) findViewById(R.id.iv_video_logo);
    }

    @Override
    public void initData() {
        clearVideo();
    }

    private DefaultUploadVideoDialog dialogUpload;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_play_video:
                //播放网络视频
                startActivity(new Intent(mActivity, JZVideoPlayerActivity.class)
                        .putExtra("type", 0)
                        .putExtra("url", "http://200024424.vod.myqcloud.com/200024424_670222d0bdf811e6ad39991f76a4df69.f30.mp4")
                        .putExtra("image", "https://mc.qcloudimg.com/static/img/c635908bebbdf8fb747388a83902886e/mlvb_basic_function+(2).jpeg")
                        .putExtra("title", "基础功能"));
                break;
            case R.id.iv_picker_add:
                if (TextUtils.isEmpty(video_path)) {
                    //选择视频
                    if (dialogUpload == null) {
                        dialogUpload = new DefaultUploadVideoDialog(this);
                        dialogUpload.setOnUploadVideoListener(this);
                    }
                    dialogUpload.showDialog();
                } else {
                    //播放本地视频
                    startActivity(new Intent(mActivity, JZVideoPlayerActivity.class)
                            .putExtra("type", 1)
                            .putExtra("url", video_path)
                            .putExtra("image", video_path_img)
                            .putExtra("title", ""));
                }
                break;
            case R.id.iv_picker_del:
                //删除视频
                clearVideo();
                break;
        }
    }

    /**
     * 删除视频
     */
    private void clearVideo() {
        video_path = null;
        iv_video_img.setImageResource(R.drawable.btn_addpic_yes);
        iv_video_img_del.setVisibility(View.INVISIBLE);
        iv_video_logo.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onUploadVideo(int type) {
        switch (type) {
            case 1://拍摄
                startActivityForResult(new Intent(mActivity, JCameraViewActivity.class)
                        .putExtra("state", JCameraView.BUTTON_STATE_ONLY_RECORDER), 1);
                break;
            case 2://选择
                startActivityForResult(new Intent(mActivity, LocalVideoActivity.class), 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            video_path = data.getStringExtra("video_path");
            video_path_img = data.getStringExtra("video_path_img");
            video_duration = data.getLongExtra("video_duration", 0L);
            //显示
            iv_video_img_del.setVisibility(View.VISIBLE);
            iv_video_logo.setVisibility(View.VISIBLE);
            AfApplication.imageLoader.loadImage(video_path_img, iv_video_img, R.drawable.btn_addpic_yes,
                    new LoadingBitmapListener() {

                        @Override
                        public void loadingBitmapSuccess(Bitmap bitmap) {
                            if (bitmap != null) {
                                base64Cover = bitmapToBase64(bitmap);
                            }
                        }

                        @Override
                        public void loadingBitmapFailed() {

                        }
                    });
            //视频base64
            try {
                File file = new File(video_path);
                FileInputStream inputFile = new FileInputStream(file);
                byte[] buffer = new byte[(int) file.length()];
                inputFile.read(buffer);
                inputFile.close();
                base64Video = Base64.encodeToString(buffer, Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * bitmap转base64
     *
     * @param bitmap
     * @return
     */
    private String bitmapToBase64(Bitmap bitmap) {
        String base64 = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                base64 = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return base64;
    }

}
