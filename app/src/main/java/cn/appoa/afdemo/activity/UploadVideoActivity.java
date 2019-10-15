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

import java.io.File;
import java.io.FileInputStream;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseActivity;
import cn.appoa.aframework.activity.AfImageActivity;
import cn.appoa.aframework.app.AfApplication;
import cn.appoa.aframework.dialog.DefaultUploadVideoDialog;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;
import cn.jzvd.JZVideoPlayerActivity;
import zm.imageloader.LoadingBitmapListener;

/**
 * 视频上传
 */
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
    private ImageView iv_video_logos;

    @Override
    public void initView() {
        super.initView();
        findViewById(R.id.tv_play_video).setOnClickListener(this);
        iv_video_img = (ImageView) findViewById(R.id.iv_picker_add);
        iv_video_img.setOnClickListener(this);
        iv_video_img_del = (ImageView) findViewById(R.id.iv_picker_del);
        iv_video_img_del.setOnClickListener(this);
        iv_video_logos = (ImageView) findViewById(R.id.iv_video_logos);
    }

    @Override
    public void initData() {
        clearVideo();
    }

    /**
     * 视频选择弹窗
     */
    private DefaultUploadVideoDialog dialogUpload;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_play_video://播放网络视频
                playVideo(0,
                        "http://200024424.vod.myqcloud.com/200024424_670222d0bdf811e6ad39991f76a4df69.f30.mp4",
                        "https://mc.qcloudimg.com/static/img/c635908bebbdf8fb747388a83902886e/mlvb_basic_function+(2).jpeg",
                        "基础功能");
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
                    playVideo(1, video_path, video_path_img, "");
                }
                break;
            case R.id.iv_picker_del:
                //删除视频
                clearVideo();
                break;
        }
    }

    /**
     * 播放视频
     *
     * @param type  类型1本地视频2网络视频
     * @param url   视频地址
     * @param image 视频封面
     * @param title 视频标题
     */
    private void playVideo(int type, String url, String image, String title) {
        Intent intent = new Intent(mActivity, JZVideoPlayerActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("url", url);
        intent.putExtra("image", image);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    /**
     * 删除视频
     */
    private void clearVideo() {
        video_path = null;
        iv_video_img.setImageResource(R.drawable.btn_addpic_yes);
        iv_video_img_del.setVisibility(View.INVISIBLE);
        iv_video_logos.setVisibility(View.INVISIBLE);
    }

    private static final int REQUEST_CODE_VIDEO_RECORDER = 1;
    private static final int REQUEST_CODE_VIDEO_LOCAL = 2;

    @Override
    public void onUploadVideo(int type) {
        switch (type) {
            case 1://拍摄
                Intent intent = new Intent(mActivity, JCameraViewActivity.class);
                //duration拍摄时长（单位毫秒）（默认10秒），从按下到拍摄可能需要准备时间，最好+1秒，这里传16表示拍摄15秒
                intent.putExtra("duration", 16 * 1000);
                //state拍摄类型（默认点击拍照，长按摄像）
                //待选值：
                //JCameraView.BUTTON_STATE_BOTH（两种都行）
                //JCameraView.BUTTON_STATE_ONLY_CAPTURE（只能拍照）
                //JCameraView.BUTTON_STATE_ONLY_RECORDER（只能摄像）
                intent.putExtra("state", JCameraView.BUTTON_STATE_ONLY_RECORDER);
                startActivityForResult(intent, REQUEST_CODE_VIDEO_RECORDER);
                break;
            case 2://选择本地视频
                startActivityForResult(new Intent(mActivity, LocalVideoActivity.class), REQUEST_CODE_VIDEO_LOCAL);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_VIDEO_RECORDER:
                int type = data.getIntExtra("type", 0);
                if (type == 1) {
                    //录像成功
                    video_path = data.getStringExtra("video_path");
                    video_path_img = data.getStringExtra("video_path_img");
                    video_duration = data.getLongExtra("video_duration", 0L);
                    setVideo();
                } else if (type == 2) {
                    //拍照成功
                    String image_path = data.getStringExtra("image_path");
                }
                break;
            case REQUEST_CODE_VIDEO_LOCAL://选择本地视频成功
                video_path = data.getStringExtra("video_path");
                video_path_img = data.getStringExtra("video_path_img");
                video_duration = data.getLongExtra("video_duration", 0L);
                setVideo();
                break;
        }
    }

    /**
     * 设置视频数据
     */
    private void setVideo() {
        iv_video_img_del.setVisibility(View.VISIBLE);
        iv_video_logos.setVisibility(View.VISIBLE);
        AfApplication.imageLoader.loadImage(video_path_img, iv_video_img, R.drawable.btn_addpic_yes,
                new LoadingBitmapListener() {

                    @Override
                    public void loadingBitmapSuccess(Bitmap bitmap) {
                        if (bitmap != null) {
                            base64Cover = AfImageActivity.bitmapToBase64(bitmap);
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
