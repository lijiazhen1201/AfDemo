package cn.appoa.wxvoiceselector;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class EaseVoicePlayerView extends RelativeLayout {

    protected Context context;
    protected EaseChatRowVoicePlayer voicePlayer;
    protected ImageView voiceImageView;
    protected TextView voiceLengthView;

    public EaseVoicePlayerView(Context context) {
        super(context);
        init(context);
    }

    public EaseVoicePlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EaseVoicePlayerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        this.context = context;
        voicePlayer = EaseChatRowVoicePlayer.getInstance(context);
        LayoutInflater.from(context).inflate(R.layout.ease_widget_voice_player, this);
        voiceImageView = ((ImageView) findViewById(R.id.iv_voice));
        voiceLengthView = (TextView) findViewById(R.id.tv_length);
    }

    private String msgId;
    private String voiceFilePath;
    private int voiceTimeLength;

    /**
     * 设置音频数据
     *
     * @param id
     * @param path
     * @param length
     */
    public void setDataSource(String id, String path, int length) {
        msgId = id;
        voiceFilePath = path;
        voiceTimeLength = length;
        if (length > 0) {
            voiceLengthView.setText(voicePlayer.formatSecond(voiceTimeLength));
        }
    }

    /**
     * 清空数据
     */
    public void clearDataSource() {
        voiceFilePath = null;
        voiceTimeLength = 0;
        voiceLengthView.setText(null);
        play();
    }

    /**
     * 开始播放
     */
    public void play() {
        if (TextUtils.isEmpty(msgId)) {
            return;
        }
        if (voicePlayer.isPlaying()) {
            // Stop the voice play first, no matter the playing voice item is this or
            // others.
            voicePlayer.stop();
            // Stop the voice play animation.
            stopVoicePlayAnimation();
            // If the playing voice item is this item, only need stop play.
            String playingId = voicePlayer.getCurrentPlayingId();
            if (TextUtils.equals(msgId, playingId)) {
                return;
            }
        }
        if (TextUtils.isEmpty(voiceFilePath)) {
            return;
        }
        if (voiceFilePath.startsWith("http")) {
            File file = getLocalFile(voiceFilePath);
            if (file != null && file.exists()) {
                // 有缓存
                voiceFilePath = file.getAbsolutePath();
            } else {
                asyncDownloadVoice();
                //return;
            }
        }
        voicePlayer.play(msgId, true, voiceFilePath, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Stop the voice play animation.
                stopVoicePlayAnimation();
            }
        });
        // Start the voice play animation.
        startVoicePlayAnimation();
    }

    /**
     * 获取文件名
     *
     * @param url
     * @return
     */
    private String getLocalFileName(String url) {
        if (!TextUtils.isEmpty(url) && url.startsWith("http") && url.contains("/")) {
            return url.substring(url.lastIndexOf("/") + 1);
        }
        return null;
    }

    /**
     * 获取本地文件
     *
     * @param url
     * @return
     */
    private File getLocalFile(String url) {
        File file = null;
        String filename = getLocalFileName(url);
        if (!TextUtils.isEmpty(filename)) {
            file = new File(getVoiceCacheDir(), filename);
        }
        return file;
    }

    /**
     * 获取缓存文件夹
     *
     * @return
     */
    public String getVoiceCacheDir() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            return context.getExternalCacheDir().getPath();
        } else {
            return context.getCacheDir().getPath();
        }
    }

    /**
     * 异步下载文件
     */
    private void asyncDownloadVoice() {
        new MyAsyncTask(this).execute();
    }

    static class MyAsyncTask extends AsyncTask<Void, Void, String> {

        private WeakReference<EaseVoicePlayerView> mOuter;

        public MyAsyncTask(EaseVoicePlayerView activity) {
            mOuter = new WeakReference<EaseVoicePlayerView>(activity);
        }

        @Override
        protected String doInBackground(Void... params) {
            EaseVoicePlayerView outer = mOuter.get();
            if (outer != null) {// Do something with outer as your wish.
                String filename = outer.getLocalFileName(outer.voiceFilePath);
                File file = new File(outer.getVoiceCacheDir(), filename);
                try {
                    URL mURL = new URL(outer.voiceFilePath);
                    // 打开连接
                    HttpURLConnection conn = (HttpURLConnection) mURL.openConnection();
                    conn.connect();
                    // 打开输入流
                    InputStream is = conn.getInputStream();
                    // 获得长度
                    int contentLength = conn.getContentLength();
                    // 创建字节流
                    byte[] bs = new byte[1024];
                    int len;
                    OutputStream os = new FileOutputStream(file);
                    // 写数据
                    while ((len = is.read(bs)) != -1) {
                        os.write(bs, 0, len);
                        os.flush();
                    }
                    // 完成后关闭流
                    os.close();
                    is.close();
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return file.exists() ? file.getAbsolutePath() : null;
            }
            return null;
        }

        protected void onPostExecute(String result) {
//            EaseVoicePlayerView outer = mOuter.get();
//            if (outer != null) {
//                outer.voiceFilePath = result;
//                outer.play();
//            }
        }

    }

    /**
     * 动画
     */
    private AnimationDrawable voiceAnimation;

    /**
     * 播放动画
     */
    private void startVoicePlayAnimation() {
        voiceImageView.setImageResource(R.drawable.voice_playing);
        voiceAnimation = (AnimationDrawable) voiceImageView.getDrawable();
        voiceAnimation.start();
    }

    /**
     * 停止动画
     */
    private void stopVoicePlayAnimation() {
        if (voiceAnimation != null) {
            voiceAnimation.stop();
        }
        voiceImageView.setImageResource(R.drawable.voice_playing_f0);
    }

    /**
     * 音频base64
     *
     * @return
     */
    public String getVoiceBase64() {
        String base64 = "";
        try {
            File file = new File(voiceFilePath);
            FileInputStream inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            base64 = Base64.encodeToString(buffer, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64;
    }
}
