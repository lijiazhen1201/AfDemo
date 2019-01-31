package com.cjt2325.cameralibrary;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.cjt2325.cameralibrary.lisenter.ErrorLisenter;
import com.cjt2325.cameralibrary.lisenter.JCameraLisenter;
import com.cjt2325.cameralibrary.util.FileUtil;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 拍摄页面
 */
public class JCameraViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSetting();
        initView();
        initPermissions();
    }

    /**
     * 相关设置
     */
    private void initSetting() {
        // http://stackoverflow.com/questions/4341600/how-to-prevent-multiple-instances-of-an-activity-when-it-is-launched-with-differ/
        // should be in launcher activity, but all app use this can avoid the
        // problem
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, //
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 拍摄控件
     */
    private JCameraView jCameraView;

    /**
     * 状态值
     */
    private int state;

    /**
     * 时长
     */
    private int duration;


    /**
     * 初始化
     */
    private void initView() {
        //设置控件
        jCameraView = new JCameraView(this);
        setContentView(jCameraView);
        //传递数据
        Intent intent = getIntent();
        state = intent.getIntExtra("state", JCameraView.BUTTON_STATE_BOTH);
        duration = intent.getIntExtra("duration", 0);
        // 设置只能录像或只能拍照或两种都可以（默认两种都可以）
        jCameraView.setFeatures(state);
        //设置时长
        if (duration > 0) {
            jCameraView.setDuration(duration);
        }
        // 设置视频保存路径
        jCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/DCIM/Camera");
        // 设置视频质量
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_HIGH);
        // JCameraView监听
        jCameraView.setErrorLisenter(new ErrorLisenter() {

            @Override
            public void onError() {
                Toast mToast = Toast.makeText(JCameraViewActivity.this, null, Toast.LENGTH_SHORT);
                mToast.setText(R.string.open_camera_failed);
                mToast.show();
                finish();
            }

            @Override
            public void AudioPermissionError() {
                Toast mToast = Toast.makeText(JCameraViewActivity.this, null, Toast.LENGTH_SHORT);
                mToast.setText(R.string.no_recording_permission);
                mToast.show();
                finish();
            }
        });
        jCameraView.setJCameraLisenter(new JCameraLisenter() {

            @Override
            public void recordSuccess(String url, Bitmap firstFrame, long time) {
                // 录像成功
                scanFile(JCameraViewActivity.this, url);
                String name = new File(url).getName();
                Intent intent = new Intent();
                intent.putExtra("type", 1);
                intent.putExtra("video_path", url);
                intent.putExtra("video_path_img",
                        bitmapToFile(firstFrame, name.substring(0, name.indexOf(".")) + ".jpeg").getAbsolutePath());
                intent.putExtra("video_duration", time);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void captureSuccess(Bitmap bitmap) {
                // 拍照成功
                Intent intent = new Intent();
                intent.putExtra("type", 2);
                intent.putExtra("image_path",
                        bitmapToFile(bitmap, "image_" + FileUtil.formatTime(System.currentTimeMillis()) + ".jpeg")
                                .getAbsolutePath());
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void quit() {
                // 退出按钮
                JCameraViewActivity.this.finish();
            }
        });
    }

    private final int GET_PERMISSION_REQUEST = 100; // 权限申请自定义码
    private boolean granted = false;

    /**
     * 6.0动态权限获取
     */
    private void initPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                // 具有权限
                granted = true;
            } else {
                // 不具有获取权限，需要进行权限申请
                ActivityCompat
                        .requestPermissions(JCameraViewActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA},
                                GET_PERMISSION_REQUEST);
                granted = false;
            }
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GET_PERMISSION_REQUEST) {
            int size = 0;
            if (grantResults.length >= 1) {
                int writeResult = grantResults[0];
                // 读写内存权限
                boolean writeGranted = writeResult == PackageManager.PERMISSION_GRANTED;// 读写内存权限
                if (!writeGranted) {
                    size++;
                }
                // 录音权限
                int recordPermissionResult = grantResults[1];
                boolean recordPermissionGranted = recordPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!recordPermissionGranted) {
                    size++;
                }
                // 相机权限
                int cameraPermissionResult = grantResults[2];
                boolean cameraPermissionGranted = cameraPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!cameraPermissionGranted) {
                    size++;
                }
                if (size == 0) {
                    granted = true;
                    jCameraView.onResume();
                } else {
                    Toast mToast = Toast.makeText(this, null, Toast.LENGTH_SHORT);
                    mToast.setText(R.string.setting_open_permission);
                    mToast.show();
                    finish();
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(//
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE//
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION//
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN//
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION//
                            | View.SYSTEM_UI_FLAG_FULLSCREEN//
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        } else {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (granted) {
            jCameraView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        jCameraView.onPause();
    }

    /**
     * bitmap转file
     *
     * @param bitmap
     * @return
     */
    protected File bitmapToFile(Bitmap bitmap, String file_name) {
        File file = null;
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera", file_name);
        file.getParentFile().mkdirs();
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (file != null) {
                scanFile(this, file.getAbsolutePath());
            }
        }
        return file;
    }

    /**
     * bitmap转base64
     *
     * @param bitmap
     * @return
     */
    protected String bitmapToBase64(Bitmap bitmap) {
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

    /**
     * file转base64
     *
     * @param file
     * @return
     */
    protected String fileToBase64(File file) {
        String base64 = null;
        try {
            if (file != null) {
                FileInputStream inputFile = new FileInputStream(file);
                byte[] buffer = new byte[(int) file.length()];
                inputFile.read(buffer);
                inputFile.close();
                base64 = Base64.encodeToString(buffer, Base64.DEFAULT);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return base64;
    }

    /**
     * 通知媒体库更新文件
     *
     * @param context
     * @param filePath 文件全路径
     */
    protected void scanFile(Context context, String filePath) {
        if (context == null || TextUtils.isEmpty(filePath) || //
                new File(filePath) == null || !new File(filePath).exists()) {
            return;
        }
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        context.sendBroadcast(scanIntent);
    }

}
