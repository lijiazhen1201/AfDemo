package cn.appoa.afdemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.utils.SnackbarUtils;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.Calendar;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseImageActivity;
import cn.appoa.aframework.dialog.AreaWheelDialog;
import cn.appoa.aframework.dialog.DatePickerDialog;
import cn.appoa.aframework.listener.OnCallbackListener;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;

/**
 * 头像上传
 */
public class UploadAvatarActivity extends BaseImageActivity
        implements View.OnClickListener, OnCallbackListener {

    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(this).setTitle("头像上传")
                .setBackImage(R.drawable.back_white).setMenuText("保存")
                .setMenuListener(new BaseTitlebar.OnClickMenuListener() {
                    @Override
                    public void onClickMenu(View view) {
                        //保存用户资料
                    }
                }).create();
    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        setContent(R.layout.activity_upload_avatar);
    }

    private ImageView iv_avatar;
    private TextView tv_birthday;
    private TextView tv_constellation;
    private TextView tv_area;

    @Override
    public void initView() {
        super.initView();
        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        tv_birthday = (TextView) findViewById(R.id.tv_birthday);
        tv_constellation = (TextView) findViewById(R.id.tv_constellation);
        tv_area = (TextView) findViewById(R.id.tv_area);
        //点击事件
        iv_avatar.setOnClickListener(this);
        tv_birthday.setOnClickListener(this);
        tv_constellation.setOnClickListener(this);
        tv_area.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    /**
     * 时间选择器
     */
    private DatePickerDialog dialogDate;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 省市区三级区域
     */
    private AreaWheelDialog dialogArea;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_avatar://头像
                dialogUpload.showDialog();
                break;
            case R.id.tv_birthday://生日
                if (dialogDate == null) {
                    dialogDate = new DatePickerDialog(mActivity, this, 1);
                    //生日从1900年1月1日到今天
                    dialogDate.initData(DatePickerDialog.str2Long("1900-01-01 00:00", false)
                            , System.currentTimeMillis());
                    //相关设置
                    //dialogDate.setCanShowPreciseTime(false);
                    //dialogDate.setCanShowMinuteTime(true);
                }
                if (TextUtils.isEmpty(birthday)) {
                    dialogDate.showDatePickerDialog("请选择日期", System.currentTimeMillis());
                } else {
                    dialogDate.showDatePickerDialog("请选择日期", birthday);
                }
                break;
            case R.id.tv_constellation://星座
                SnackbarUtils.Long(v, "选择生日后将自动设置星座")
                        .setAction("选择生日", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UploadAvatarActivity.this.onClick(tv_birthday);
                            }
                        }).show();
                break;
            case R.id.tv_area://区域
                if (dialogArea == null) {
                    dialogArea = new AreaWheelDialog(mActivity, this);
                    dialogArea.showAreaDialog(tv_area.getHint());
                } else {
                    dialogArea.showDialog();
                }
                break;
        }
    }

    @Override
    public void onCallback(int type, Object... obj) {
        if (type == 1) {
            birthday = (String) obj[0];
            tv_birthday.setText(birthday);
            Calendar calendar = (Calendar) obj[1];
            tv_constellation.setText(DatePickerDialog.getConstellation(calendar));
        }
        if (type < 0) {
            String provinceId = (String) obj[0];
            String cityId = (String) obj[1];
            String districtId = (String) obj[2];
            String provinceName = (String) obj[3];
            String cityName = (String) obj[4];
            String districtName = (String) obj[5];
            if (type == -1) {
                tv_area.setText(provinceName + cityName + districtName);
            }
        }
    }

    /**
     * 是否裁剪图片
     */
    private boolean isCropImage = true;

    @Override
    public void getImageBitmap(Uri imageUri, String imagePath, Bitmap imageBitmap) {
        //1.拍照结束或选择相册里的图片结束会首先回调此方法
        if (isCropImage) {
            //如果需要裁剪，请添加以下代码
            CropImage.activity(imageUri).setAspectRatio(1, 1).start(this);
        } else {
            //不需要裁剪则直接处理显示图片的逻辑
            getImageBitmap(imageBitmap);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //2.如果需要裁剪，则添加以下代码回调裁剪结果
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (result != null && result.getUri() != null) {
                getImageBitmap(uriToBitmap(this, result.getUri()));
            }
        }
    }

    /**
     * 图片base64
     */
    private String avatarBase64 = "";

    /**
     * 图片文件
     */
    private File avatarFile = null;

    @Override
    public void getImageBitmap(Bitmap imageBitmap) {
        //3.裁剪结束时回调此方法，处理显示图片的逻辑
        if (imageBitmap != null) {
            iv_avatar.setImageBitmap(imageBitmap);
            //4.根据后台上传需要转base64或文件
            avatarBase64 = bitmapToBase64(imageBitmap);
            avatarFile = bitmapToFile(imageBitmap);
        }
    }

}
