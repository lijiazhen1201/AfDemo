package cn.appoa.wxvoiceselector;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;


/**
 * Voice recorder view
 */
public class EaseVoiceRecorderView extends RelativeLayout {
    protected Context context;
    protected LayoutInflater inflater;
    protected Drawable[] micImages;
    protected EaseVoiceRecorder voiceRecorder;

    protected PowerManager.WakeLock wakeLock;
    protected ImageView micImage;
    protected TextView recordingHint;

    protected Handler micImageHandler;

    static class MyHandler extends Handler {

        private WeakReference<EaseVoiceRecorderView> mOuter;

        public MyHandler(EaseVoiceRecorderView activity) {
            mOuter = new WeakReference<EaseVoiceRecorderView>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            EaseVoiceRecorderView outer = mOuter.get();
            if (outer != null) {// Do something with outer as your wish.
                // change image
                outer.micImage.setImageDrawable(outer.micImages[msg.what]);
            }
        }
    }

    public EaseVoiceRecorderView(Context context) {
        super(context);
        init(context);
    }

    public EaseVoiceRecorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EaseVoiceRecorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        LayoutInflater.from(context).inflate(R.layout.ease_widget_voice_recorder, this);

        micImage = (ImageView) findViewById(R.id.mic_image);
        recordingHint = (TextView) findViewById(R.id.recording_hint);

        voiceRecorder = new EaseVoiceRecorder(micImageHandler);

        // animation resources, used for recording
        micImages = new Drawable[]{ContextCompat.getDrawable(context, R.drawable.ease_record_animate_01),
                ContextCompat.getDrawable(context, R.drawable.ease_record_animate_02),
                ContextCompat.getDrawable(context, R.drawable.ease_record_animate_03),
                ContextCompat.getDrawable(context, R.drawable.ease_record_animate_04),
                ContextCompat.getDrawable(context, R.drawable.ease_record_animate_05),
                ContextCompat.getDrawable(context, R.drawable.ease_record_animate_06),
                ContextCompat.getDrawable(context, R.drawable.ease_record_animate_07),
                ContextCompat.getDrawable(context, R.drawable.ease_record_animate_08),
                ContextCompat.getDrawable(context, R.drawable.ease_record_animate_09),
                ContextCompat.getDrawable(context, R.drawable.ease_record_animate_10),
                ContextCompat.getDrawable(context, R.drawable.ease_record_animate_11),
                ContextCompat.getDrawable(context, R.drawable.ease_record_animate_12),
                ContextCompat.getDrawable(context, R.drawable.ease_record_animate_13),
                ContextCompat.getDrawable(context, R.drawable.ease_record_animate_14),};

        wakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");

        micImageHandler = new MyHandler(this);
    }

    /**
     * on speak button touched
     *
     * @param v
     * @param event
     */
    public boolean onPressToSpeakBtnTouch(View v, MotionEvent event, EaseVoiceRecorderCallback recorderCallback) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                try {
                    EaseChatRowVoicePlayer voicePlayer = EaseChatRowVoicePlayer.getInstance(context);
                    if (voicePlayer.isPlaying())
                        voicePlayer.stop();
                    v.setPressed(true);
                    startRecording();
                } catch (Exception e) {
                    v.setPressed(false);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (event.getY() < 0) {
                    showReleaseToCancelHint();
                } else {
                    showMoveUpToCancelHint();
                }
                return true;
            case MotionEvent.ACTION_UP:
                v.setPressed(false);
                if (event.getY() < 0) {
                    // discard the recorded audio.
                    discardRecording();
                } else {
                    // stop recording and send voice file
                    try {
                        int length = stopRecoding();
                        if (length > 0) {
                            if (recorderCallback != null) {
                                recorderCallback.onVoiceRecordComplete(getVoiceFilePath(), length);
                            }
                        } else if (length == 401) {
                            Toast mToast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
                            mToast.setText(R.string.Recording_without_permission);
                            mToast.show();
                        } else {
                            Toast mToast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
                            mToast.setText(R.string.The_recording_time_is_too_short);
                            mToast.show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast mToast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
                        mToast.setText(R.string.recoding_fail);
                        mToast.show();
                    }
                }
                return true;
            default:
                discardRecording();
                return false;
        }
    }

    public interface EaseVoiceRecorderCallback {
        /**
         * on voice record complete
         *
         * @param voiceFilePath   录音完毕后的文件路径
         * @param voiceTimeLength 录音时长
         */
        void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength);
    }

    public void startRecording() {
        if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            Toast mToast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
            mToast.setText(R.string.Send_voice_need_sdcard_support);
            mToast.show();
            return;
        }
        try {
            wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
            this.setVisibility(View.VISIBLE);
            recordingHint.setText(context.getString(R.string.move_up_to_cancel));
            recordingHint.setBackgroundColor(Color.TRANSPARENT);
            voiceRecorder.startRecording(context);
        } catch (Exception e) {
            e.printStackTrace();
            if (wakeLock.isHeld())
                wakeLock.release();
            if (voiceRecorder != null)
                voiceRecorder.discardRecording();
            this.setVisibility(View.INVISIBLE);
            Toast mToast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
            mToast.setText(R.string.recoding_fail);
            mToast.show();
            return;
        }
    }

    public void showReleaseToCancelHint() {
        recordingHint.setText(context.getString(R.string.release_to_cancel));
        recordingHint.setBackgroundResource(R.drawable.ease_recording_text_hint_bg);
    }

    public void showMoveUpToCancelHint() {
        recordingHint.setText(context.getString(R.string.move_up_to_cancel));
        recordingHint.setBackgroundColor(Color.TRANSPARENT);
    }

    public void discardRecording() {
        if (wakeLock.isHeld())
            wakeLock.release();
        try {
            // stop recording
            if (voiceRecorder.isRecording()) {
                voiceRecorder.discardRecording();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setVisibility(View.INVISIBLE);
    }

    public int stopRecoding() {
        this.setVisibility(View.INVISIBLE);
        if (wakeLock.isHeld())
            wakeLock.release();
        return voiceRecorder.stopRecoding();
    }

    public String getVoiceFilePath() {
        return voiceRecorder.getVoiceFilePath();
    }

    public String getVoiceFileName() {
        return voiceRecorder.getVoiceFileName();
    }

    public boolean isRecording() {
        return voiceRecorder.isRecording();
    }

}
