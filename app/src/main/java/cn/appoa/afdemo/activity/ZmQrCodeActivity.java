package cn.appoa.afdemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.anthonycr.grant.PermissionsResultAction;
import com.theartofdev.edmodo.cropper.CropImage;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseImageActivity;
import cn.appoa.aframework.utils.AtyUtils;
import cn.appoa.qrcodescan.camera.CameraManager;
import cn.appoa.qrcodescan.ui.ZmQRCodeFragment;

/**
 * 微信扫一扫
 */
public class ZmQrCodeActivity extends BaseImageActivity
        implements CompoundButton.OnCheckedChangeListener, ZmQRCodeFragment.OnQRCodeResultListener {

    @Override
    public void initContent(Bundle savedInstanceState) {
        setContent(R.layout.activity_zm_qr_code);
    }

    private CheckBox cb_qrcode_flash;
    private ZmQRCodeFragment fragment;

    @Override
    public void initView() {
        super.initView();
        AtyUtils.setPaddingTop(mActivity, findViewById(R.id.mView));
        findViewById(R.id.iv_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //扫描本地二维码（暂时有bug，页面重启后SurfaceView会黑屏）
                //selectPicFromAlbum();
            }
        });
        cb_qrcode_flash = (CheckBox) findViewById(R.id.cb_qrcode_flash);
        cb_qrcode_flash.setOnCheckedChangeListener(this);
        fragment = new ZmQRCodeFragment();
        fragment.setOnQRCodeResultListener(this);
        mFragmentManager.beginTransaction().replace(R.id.rl_fragment, fragment).commit();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
        String[] permissions = {"android.permission.FLASHLIGHT"};
        requestPermissions(permissions, new PermissionsResultAction() {

            @Override
            public void onGranted() {
                if (isChecked) {
                    // 开启闪光灯
                    CameraManager.get().openLight();
                } else {
                    // 关闭闪光灯
                    CameraManager.get().offLight();
                }
            }

            @Override
            public void onDenied(String permission) {
                AtyUtils.showShort(mActivity, "请开启闪光灯权限", false);
            }
        });
    }

    @Override
    public void initData() {
        //根据光线自动开启闪光灯（暂时有bug，检测有问题）
        //openSensorManager();
    }

    /**
     * 是否开启过
     */
    private boolean isOpenSensorManager;

    /**
     * 自动开启闪光灯
     */
    private void openSensorManager() {
        isOpenSensorManager = true;
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor ligthSensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        sm.registerListener(new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                if (isOpenSensorManager) {
                    float lux = event.values[0];// 获取光线强度
                    int retval = Float.compare(lux, 10.0f);
                    if (retval > 0) {// 光线强度>10.0
                        CameraManager.get().offLight();
                    } else {
                        CameraManager.get().openLight();
                    }
                    isOpenSensorManager = false;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, ligthSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onQRCodeResult(String result, Bitmap barcode) {
        // 扫码结果
        AtyUtils.i("扫码结果", result);
        AtyUtils.showShort(mActivity, result, false);
        setResult(RESULT_OK, new Intent()
                .putExtra("result", result));
        finish();
    }

    @Override
    public void getImageBitmap(Uri imageUri, String imagePath, Bitmap imageBitmap) {
        //1.裁剪图片
        CropImage.activity(imageUri).setAspectRatio(1, 1).start(this);
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

    @Override
    public void getImageBitmap(Bitmap imageBitmap) {
        //3.裁剪结束时回调此方法，处理显示图片的逻辑
        if (imageBitmap != null) {
            if (fragment != null) {
                fragment.decodeBitmap(imageBitmap);
            }
        }
    }

}
