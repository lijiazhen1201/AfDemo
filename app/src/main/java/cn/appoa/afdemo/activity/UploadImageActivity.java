package cn.appoa.afdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.JCameraViewActivity;

import java.util.ArrayList;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseActivity;
import cn.appoa.afdemo.dialog.UploadImgVideoDialog;
import cn.appoa.aframework.app.AfApplication;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;
import cn.appoa.aframework.utils.AtyUtils;
import cn.appoa.wximageselector.view.PhotoPickerGridView;

/**
 * 上传图片
 */
public class UploadImageActivity extends BaseActivity
        implements UploadImgVideoDialog.OnUploadImgVideoListener {

    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(this).setTitle("图片多选")
                .setBackImage(R.drawable.back_white).create();
    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        setContent(R.layout.activity_upload_image);
    }

    private PhotoPickerGridView mPhotoPickerGridView;

    @Override
    public void initView() {
        super.initView();
        mPhotoPickerGridView = (PhotoPickerGridView) findViewById(R.id.mPhotoPickerGridView);
        findViewById(R.id.tv_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    /**
     * 拍摄请求码
     */
    private static final int REQUEST_CODE_VIDEO_PICKER = 1;

    /**
     * 选择请求码
     */
    private static final int REQUEST_CODE_PHOTO_PICKER = 2;

    /**
     * 上传弹窗
     */
    private UploadImgVideoDialog dialogUpload;

    @Override
    public void initData() {
        //设置是否可拍照
        //mPhotoPickerGridView.setCamera(true);
        //设置是否可选视频
        //mPhotoPickerGridView.setVideo(true);
        //设置视频最大时长
        mPhotoPickerGridView.setVideoDuration(10);
        //设置最大数量（默认9张）
        mPhotoPickerGridView.setMax(3);
        //设置自定义上传图片
        //mPhotoPickerGridView.setDefaultAddRes(R.drawable.icon_camera);
        //设置是否转换base64（上传文件时不用转换base64）
        //mPhotoPickerGridView.setToBase64(false);
        //设置监听（必须写）
        mPhotoPickerGridView.setImageLoader(new PhotoPickerGridView.DefaultPhotoPickerImageLoader() {

            @Override
            public void loadImage(String path, ImageView iv) {
                //加载图片
                AfApplication.imageLoader.loadImage(path, iv);
            }

            @Override
            public int getRequestCode() {
                //请求码
                return REQUEST_CODE_PHOTO_PICKER;
            }

            @Override
            public boolean isUploadSelf() {
                //自定义点击事件
                return true;
            }

            @Override
            public void onClickAddPic() {
                //点击添加按钮
                if (dialogUpload == null) {
                    dialogUpload = new UploadImgVideoDialog(mActivity);
                    dialogUpload.setOnUploadImgVideoListener(UploadImageActivity.this);
                }
                dialogUpload.showDialog();
            }
        });
        //添加网络图片（编辑图片用到）
        //ArrayList<String> photos = new ArrayList<>();
        //photos.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556273439607&di=6a21f28036c66f7c6a76b2b32c04f7e9&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Flarge%2F71b2cbb3gw1f5i55vwmjxj21kw1b8the.jpg");
        //photos.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556273447435&di=755672b5fb5454c7ec487232fc7136bc&imgtype=0&src=http%3A%2F%2Fgss0.bdstatic.com%2F6ONWsjip0QIZ8tyhnq%2Fit%2Fu%3D2952176766%2C2579102747%26fm%3D173%26s%3DD0B11DD74C0256D4001CD9FC0300C03B%26w%3D640%26h%3D601%26img.JPEG");
        //mPhotoPickerGridView.addPhotos(photos);
        //添加网络视频（编辑视频用到）
        //mPhotoPickerGridView.addVideo("http://200024424.vod.myqcloud.com/200024424_670222d0bdf811e6ad39991f76a4df69.f30.mp4");
    }

    @Override
    public void onUploadImgVideo(int type) {
        if (type == 1) {
            //拍摄
            startActivityForResult(new Intent(mActivity, JCameraViewActivity.class)
                    .putExtra("state", JCameraView.BUTTON_STATE_BOTH), REQUEST_CODE_VIDEO_PICKER);
        } else if (type == 2) {
            //从相册选择
            mPhotoPickerGridView.uploadPics();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_VIDEO_PICKER:
                int type = data.getIntExtra("type", 0);
                if (type == 1) {
                    //录像成功
                    String video_path = data.getStringExtra("video_path");
                    String video_path_img = data.getStringExtra("video_path_img");
                    long video_duration = data.getLongExtra("video_duration", 0);
                    //添加本地视频
                    mPhotoPickerGridView.addVideo(video_path);
                } else if (type == 2) {
                    //拍照成功
                    String image_path = data.getStringExtra("image_path");
                    ArrayList<String> photos = new ArrayList<>();
                    photos.add(image_path);
                    //添加本地图片
                    mPhotoPickerGridView.addPhotos(photos);
                }
                break;
            case REQUEST_CODE_PHOTO_PICKER:
                //添加Intent传递的视频或图片
                mPhotoPickerGridView.addData(data);
                break;
        }
    }

    /**
     * 上传图片
     */
    private void uploadImage() {
        if (mPhotoPickerGridView.getVideo() == null && mPhotoPickerGridView.getPhotoSize() == 0) {
            AtyUtils.showShort(mActivity, "请添加图片或视频", false);
            return;
        }
        if (mPhotoPickerGridView.getVideo() == null) {
            showLoading("正在上传图片...");
            mPhotoPickerGridView.getBase64Photos(mActivity, ",", new PhotoPickerGridView.GetBase64PhotosListener() {
                @Override
                public void getBase64Photos(String base64) {
                    //掉接口上传
                    try {
                        Thread.sleep(3000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    dismissLoading();
                    AtyUtils.showShort(mActivity, "上传图片成功", false);
                    setResult(RESULT_OK);
                    finish();
                }
            });
        } else {
            showLoading("正在上传视频...");
            mPhotoPickerGridView.getBase64Video(mActivity, new PhotoPickerGridView.GetBase64VideoListener() {
                @Override
                public void getBase64Video(String base64) {
                    //掉接口上传
                    try {
                        Thread.sleep(3000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    dismissLoading();
                    AtyUtils.showShort(mActivity, "上传视频成功", false);
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }
    }

}
