package cn.appoa.qrcodescan.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.Vector;

import cn.appoa.qrcodescan.R;
import cn.appoa.qrcodescan.camera.CameraManager;
import cn.appoa.qrcodescan.decoding.CaptureActivityHandler;
import cn.appoa.qrcodescan.decoding.InactivityTimer;
import cn.appoa.qrcodescan.decoding.RGBLuminanceSource;
import cn.appoa.qrcodescan.view.ViewfinderView;

/**
 * 二维码扫描的Fragment
 */
public class ZmQRCodeFragment extends Fragment implements SurfaceHolder.Callback {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zm_qr_code, container, false);
        initView(view);
        return view;
    }

    /**
     * 扫描布局
     */
    protected RelativeLayout captureContainer;

    /**
     * SurfaceView
     */
    protected SurfaceView surfaceView;

    /**
     * 扫描框
     */
    protected ViewfinderView viewfinderView;

    /**
     * 获取扫描框
     *
     * @return
     */
    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    /**
     * 绘制扫描框
     */
    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    /**
     * 初始化控件
     *
     * @param view
     */
    private void initView(View view) {
        scanHandler = new MyHandler(this);
        captureContainer = (RelativeLayout) view.findViewById(R.id.capture_container);
        surfaceView = (SurfaceView) view.findViewById(R.id.capture_preview);
        viewfinderView = (ViewfinderView) view.findViewById(R.id.view_finder_view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initCamera();
    }

    /**
     *
     */
    protected boolean hasSurface;

    /**
     *
     */
    protected InactivityTimer inactivityTimer;

    /**
     *
     */
    private void initCamera() {
        // 当此窗口为用户可见时，保持设备常开，并保持亮度不变。
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // 初始化CameraManager
        CameraManager.init(getActivity());
        //
        hasSurface = false;
        //
        inactivityTimer = new InactivityTimer(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        //
        initSurface();
    }

    /**
     *
     */
    protected Vector<BarcodeFormat> decodeFormats;

    /**
     *
     */
    protected String characterSet;

    /**
     *
     */
    protected boolean playBeep;

    /**
     *
     */
    protected boolean vibrate;

    /**
     *
     */
    private void initSurface() {
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getActivity().getSystemService(getActivity().AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    /**
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    /**
     *
     */
    protected MediaPlayer mediaPlayer;

    /**
     *
     */
    private static final float BEEP_VOLUME = 0.10f;

    /**
     *
     */
    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            ((Activity) getActivity()).setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        //
        pauseHandler();
    }

    /**
     *
     */
    protected CaptureActivityHandler handler;

    /**
     * @return
     */
    public Handler getHandler() {
        return handler;
    }

    /**
     *
     */
    private void pauseHandler() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    public void onDestroy() {
        //
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        if (onQRCodeResultListener != null) {
            onQRCodeResultListener.onQRCodeResult(resultString, barcode);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(5000);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // 5秒后再开启扫描
                        if (handler != null) {
                            handler.restartPreviewAndDecode();
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 处理扫描结果的回调
     */
    protected OnQRCodeResultListener onQRCodeResultListener;

    /**
     * 处理扫描结果的回调
     *
     * @param onQRCodeResultListener
     */
    public void setOnQRCodeResultListener(OnQRCodeResultListener onQRCodeResultListener) {
        this.onQRCodeResultListener = onQRCodeResultListener;
    }

    /**
     * 处理扫描结果的回调
     */
    public interface OnQRCodeResultListener {
        void onQRCodeResult(String result, Bitmap barcode);
    }

    /**
     *
     */
    private static final long VIBRATE_DURATION = 200L;

    /**
     *
     */
    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    /**
     * 裁剪后接收到的图片bitmap
     */
    private Bitmap imageBitmap = null;

    /**
     * 解析bitmap
     *
     * @param bitmap
     */
    public void decodeBitmap(final Bitmap bitmap) {
        imageBitmap = bitmap;
        new Thread() {
            @Override
            public void run() {
                RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
                BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
                Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
                hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
                QRCodeReader reader = new QRCodeReader();
                Result result = null;
                try {
                    result = reader.decode(binaryBitmap, hints);
                } catch (NotFoundException e) {
                    e.printStackTrace();
                } catch (ChecksumException e) {
                    e.printStackTrace();
                } catch (FormatException e) {
                    e.printStackTrace();
                } finally {
                    reader.reset();
                }
                Message message = Message.obtain();
                if (result != null) {
                    message.what = 0;
                    message.obj = result.getText();
                    scanHandler.sendMessage(message);
                }
                if (handler != null) {
                    handler.restartPreviewAndDecode();
                }
            }
        }.start();

    }

    private Handler scanHandler;

    static class MyHandler extends Handler {

        private WeakReference<ZmQRCodeFragment> mOuter;

        public MyHandler(ZmQRCodeFragment activity) {
            mOuter = new WeakReference<ZmQRCodeFragment>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ZmQRCodeFragment outer = mOuter.get();
            if (outer != null) {// Do something with outer as your wish.
                if (msg.what == 0) {
                    // 扫描成功获取信息
                    if (outer.onQRCodeResultListener != null) {
                        outer.onQRCodeResultListener.onQRCodeResult((String) msg.obj, outer.imageBitmap);
                    }
                }
            }
        }
    }

}
