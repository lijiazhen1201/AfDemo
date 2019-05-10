package cn.jzvd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;

import java.io.File;


/**
 * 视频播放
 */
public class JZVideoPlayerActivity extends AppCompatActivity {

    private int type;// 0网络1本地
    private String url;//视频地址
    private String image;//视频封面
    private String title;//视频标题
    private JZVideoPlayerStandard videoplayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        url = intent.getStringExtra("url");
        image = intent.getStringExtra("image");
        title = intent.getStringExtra("title");
        if (title == null) {
            title = "";
        }

        JZVideoPlayer.setMediaInterface(new JZMediaSystem());
        videoplayer = new JZVideoPlayerStandard(this);
        setContentView(videoplayer);

        Glide.with(this).load(image).into(videoplayer.thumbImageView);
        switch (type) {
            case 0:
                videoplayer.setUp(initPath(url), JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, title);
                break;
            case 1:
                videoplayer.setUp(url, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, title);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    /**
     * 设置播放地址
     */
    private String initPath(String video_url) {
        String path = video_url;
        if (!TextUtils.isEmpty(video_url)) {
            File file = getLocalFile(video_url);
            if (file != null && file.exists()) {
                // 有缓存视频
                path = file.getAbsolutePath();
            } else {
                path = video_url;
                cacheVideo(video_url);
            }
        }
        return path;
    }

    /**
     * 获取本地视频文件
     *
     * @param url
     * @return
     */
    private File getLocalFile(String url) {
        if (!TextUtils.isEmpty(url) && url.startsWith("http") && url.contains("/")) {
            return new File(getVideoCacheDir(), url.substring(url.lastIndexOf("/") + 1));
        }
        return null;
    }

    /**
     * 获取视频缓存文件夹
     *
     * @return
     */
    public String getVideoCacheDir() {
        Context context = this;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            return context.getExternalCacheDir().getPath();
        } else {
            return context.getCacheDir().getPath();
        }
    }

    /**
     * 缓存视频
     *
     * @param url
     */
    private void cacheVideo(String url) {
        String filename = url;
        if (!TextUtils.isEmpty(filename) && filename.contains("/")) {
            filename = filename.substring(filename.lastIndexOf("/") + 1);
            if (!TextUtils.isEmpty(filename)) {
                final File file = new File(getVideoCacheDir(), filename);
                PRDownloader.download(url, getVideoCacheDir(), filename).build().start(new OnDownloadListener() {

                    @Override
                    public void onDownloadComplete() {
                        Log.e("缓存视频", "缓存视频成功");
                    }

                    @Override
                    public void onError(com.downloader.Error error) {
                        Log.e("缓存视频", "缓存视频失败");
                    }
                });
            }
        }
    }
}
