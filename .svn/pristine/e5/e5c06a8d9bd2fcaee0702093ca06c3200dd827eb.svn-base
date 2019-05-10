package cn.appoa.wxvoiceselector;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import java.io.IOException;

/**
 * Created by zhangsong on 17-10-20.
 */

public class EaseChatRowVoicePlayer {
    private static final String TAG = "ConcurrentMediaPlayer";
    private static EaseChatRowVoicePlayer instance = null;

    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private String playingId;

    private MediaPlayer.OnCompletionListener onCompletionListener;

    public static EaseChatRowVoicePlayer getInstance(Context context) {
        if (instance == null) {
            synchronized (EaseChatRowVoicePlayer.class) {
                if (instance == null) {
                    instance = new EaseChatRowVoicePlayer(context);
                }
            }
        }
        return instance;
    }

    public MediaPlayer getPlayer() {
        return mediaPlayer;
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    /**
     * May null, please consider.
     *
     * @return
     */
    public String getCurrentPlayingId() {
        return playingId;
    }

    /**
     * 播放
     *
     * @param msgId      标记id
     * @param speakerOn  是否开始扬声器
     * @param voice_path 音频地址
     * @param listener   监听
     */
    public void play(String msgId, boolean speakerOn, final String voice_path,
                     final MediaPlayer.OnCompletionListener listener) {
        if (TextUtils.isEmpty(voice_path))
            return;

        if (mediaPlayer.isPlaying()) {
            stop();
        }

        playingId = msgId;
        onCompletionListener = listener;

        try {
            setSpeaker(speakerOn);
            mediaPlayer.setDataSource(voice_path);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stop();

                    playingId = null;
                    onCompletionListener = null;
                }
            });
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        mediaPlayer.stop();
        mediaPlayer.reset();

        /**
         * This listener is to stop the voice play animation currently, considered the
         * following 3 conditions:
         *
         * 1.A new voice item is clicked to play, to stop the previous playing voice
         * item animation. 2.The voice is play complete, to stop it's voice play
         * animation. 3.Press the voice record button will stop the voice play and must
         * stop voice play animation.
         *
         */
        if (onCompletionListener != null) {
            onCompletionListener.onCompletion(mediaPlayer);
        }
    }

    private EaseChatRowVoicePlayer(Context cxt) {
        Context baseContext = cxt.getApplicationContext();
        audioManager = (AudioManager) baseContext.getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = new MediaPlayer();
    }

    private void setSpeaker(boolean speakerOn) {
        if (speakerOn) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        } else {
            audioManager.setSpeakerphoneOn(false);// 关闭扬声器
            // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        }
    }

    /**
     * 格式化时间
     *
     * @param second
     * @return
     */
    public Spanned formatSecond(int second) {
        Spanned spanned = null;
        int h = 0;
        int m = 0;
        int s = 0;
        int temp = second % 3600;
        if (second > 3600) {
            h = second / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    m = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            m = second / 60;
            if (second % 60 != 0) {
                s = second % 60;
            }
        }
        if (h > 0) {
            spanned = Html.fromHtml((h * 60 + m) + "&apos;" + s + "&apos;&apos;");
        } else if (m > 0) {
            spanned = Html.fromHtml(m + "&apos;" + s + "&apos;&apos;");
        } else {
            spanned = Html.fromHtml(s + "&apos;&apos;");
        }
        return spanned;
    }
}
