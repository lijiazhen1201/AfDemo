package cn.appoa.afdemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.theartofdev.edmodo.cropper.CropImage;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseImageActivity;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;


public class UploadAvatarActivity extends BaseImageActivity {

    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(this).setTitle("头像上传")
                .setBackImage(R.drawable.back_white).create();
    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        setContent(R.layout.activity_upload_avatar);
    }

    private ImageView iv_avatar;

    @Override
    public void initView() {
        super.initView();
        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUpload.showDialog();
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void getImageBitmap(Uri imageUri, String imagePath, Bitmap imageBitmap) {
        //裁剪
        CropImage.activity(imageUri).setAspectRatio(1, 1).start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (result != null && result.getUri() != null) {
                getImageBitmap(uriToBitmap(result.getUri()));
            }
        }
    }

    /**
     * 图片
     */
    private String base64Avatar = "";

    @Override
    public void getImageBitmap(Bitmap imageBitmap) {
        if (imageBitmap != null) {
            iv_avatar.setImageBitmap(imageBitmap);
            base64Avatar = bitmapToBase64(imageBitmap);
        }
    }
}
