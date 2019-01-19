package cn.appoa.afdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseActivity;
import cn.appoa.aframework.app.AfApplication;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;
import cn.appoa.aframework.utils.AtyUtils;
import cn.appoa.wximageselector.utils.ImageSelectorUtils;
import cn.appoa.wximageselector.view.PhotoPickerGridView;


public class UploadImageActivity extends BaseActivity {

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


    @Override
    public void initData() {
        //设置可拍照
        mPhotoPickerGridView.setCamera(true);
        //最大数量（默认9张）
        mPhotoPickerGridView.setMax(3);
        //自定义上传图片
        mPhotoPickerGridView.setDefaultAddRes(R.drawable.upload_gray);
        //监听（必须写）
        mPhotoPickerGridView.setImageLoader(new PhotoPickerGridView.DefaultPhotoPickerImageLoader() {

            @Override
            public void loadImage(String path, ImageView iv) {
                //加载图片
                AfApplication.imageLoader.loadImage(path, iv);
            }

            @Override
            public int getRequestCode() {
                //请求码
                return 2;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            ArrayList<String> photos = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT);
            mPhotoPickerGridView.addPhotos(photos);
        }
    }

    /**
     * 上传图片
     */
    private void uploadImage() {
        if (mPhotoPickerGridView.getPhotoSize() == 0) {
            AtyUtils.showShort(mActivity, "请选择图片", false);
            return;
        }
        showLoading("正在上传图片...");
        mPhotoPickerGridView.getBase64Photos(mActivity, ",", new PhotoPickerGridView.GetBase64PhotosListener() {
            @Override
            public void getBase64Photos(String base64) {
                //掉接口上传
                dismissLoading();
            }
        });
    }
}
