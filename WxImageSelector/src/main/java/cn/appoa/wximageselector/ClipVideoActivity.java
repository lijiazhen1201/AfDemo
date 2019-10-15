package cn.appoa.wximageselector;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.esay.ffmtool.FfmpegTool;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.appoa.wximageselector.adapter.ClipVideoAdapter;
import cn.appoa.wximageselector.entry.Data;
import cn.appoa.wximageselector.entry.Image;
import cn.appoa.wximageselector.view.range.UIUtil;
import cn.appoa.wximageselector.view.range.VideoRangeBar;


public class ClipVideoActivity extends AppCompatActivity
        implements VideoRangeBar.OnRangeBarChangeListener {

    /**
     * 工具类
     */
    private FfmpegTool ffmpegTool;

    /**
     * 监听播放进度
     */
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initIntent();
        handler = new MyHandler(this);
        setContentView(R.layout.activity_clip_video);
        ffmpegTool = FfmpegTool.getInstance(this);
        ffmpegTool.setImageDecodeing(new FfmpegTool.ImageDecodeing() {
            @Override
            public void sucessOne(String s, int i) {
                if (adapter != null) {
                    adapter.notifyItemRangeChanged(i, 1);
                }
            }
        });
        initView();
        //第一次解码，先解码三屏的图片
        runImagDecodTask(0, videoDuration * 3);
        //播放视频
        playVideo();
    }

    /**
     * 视频最大时长
     */
    private int videoDuration;

    /**
     * 本地视频
     */
    private Image video;

    /**
     * 视频地址
     */
    private String videoPath;

    /**
     * 视频时长
     */
    private long videoTime;

    /**
     * 根目录
     */
    private String parentPath;

    /**
     * intent传递数据
     */
    private void initIntent() {
        Intent intent = getIntent();
        videoDuration = intent.getIntExtra("videoDuration", 0);
        rightThumbIndex = videoDuration;
        endTime = videoDuration;
        VideoRangeBar.DEFAULT_TICK_COUNT = videoDuration + 1;
        video = (Image) intent.getParcelableExtra("video");
        if (videoDuration == 0 || video == null) {
            finish();
        }
        videoPath = video.getPath();
        videoTime = UIUtil.getVideoDuration(videoPath);
        //取视频文件名作为图片文件夹
        parentPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator + new File(videoPath).getName() + File.separator;
        File parent = new File(parentPath);
        if (!parent.exists()) {
            parent.mkdirs();
        }
    }

    private FrameLayout btnConfirm;
    private FrameLayout btnBack;
    private VideoView uVideoView;
    private RecyclerView recyclerview;
    private VideoRangeBar rangeBar;

    private LinearLayoutManager linearLayoutManager;
    private ClipVideoAdapter adapter;

    private void initView() {
        TextView tv_edit_msg = (TextView) findViewById(R.id.tv_edit_msg);
        tv_edit_msg.setText(String.format(getString(R.string.clip_video_msg), videoDuration + ""));
        btnConfirm = (FrameLayout) findViewById(R.id.btn_confirm);
        btnBack = (FrameLayout) findViewById(R.id.btn_back);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //视频裁剪
                //参数说明 视频源  输出结果地址 开始时间单位s  视频时长单位s  标志位  回调
                File file = new File(videoPath);
                final String resultPath = file.getParent() + File.separator + "clip_" + System.currentTimeMillis() + "_" + file.getName();
                ffmpegTool.clipVideo(videoPath, resultPath, startTime, endTime - startTime, 1, new FfmpegTool.VideoResult() {
                    @Override
                    public void clipResult(int code, String src, String dst, boolean sucess, int tag) {
                        if (sucess) {
                            scanFile(new File(resultPath));
                        }
                    }
                });
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        uVideoView = (VideoView) findViewById(R.id.uVideoView);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        rangeBar = (VideoRangeBar) findViewById(R.id.rangeBar);
        rangeBar.setmTickCount(videoDuration + 1);
        rangeBar.setOnRangeBarChangeListener(this);//设置滑动条的监听
        //
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerview.setLayoutManager(linearLayoutManager);
        adapter = new ClipVideoAdapter(this, getDataList(videoTime));
        adapter.setParentPath(parentPath);
        adapter.setRotation(UIUtil.strToFloat(UIUtil.getVideoInf(videoPath)));
        recyclerview.setAdapter(adapter);
        recyclerview.addOnScrollListener(onScrollListener);
    }

    /**
     * 播放视频
     */
    private void playVideo() {
        uVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mp.stop();
                finish();
                return false;
            }
        });
        uVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //mp.setLooping(true);
                mp.start();
                handler.sendEmptyMessage(0);
            }
        });
        uVideoView.setVideoPath(videoPath);
    }

    /**
     * 整个视频要解码图片的总数量
     */
    private int imagCount = 0;

    /**
     * 根据视频的时长，按秒分割成多个data先占一个位置
     *
     * @return
     */
    public List<Data> getDataList(long videoTime) {
        List<Data> dataList = new ArrayList<>();
        int seconds = (int) (videoTime / 1000);
        for (imagCount = 0; imagCount <= seconds; imagCount++) {
            dataList.add(new Data(imagCount, "temp" + imagCount + ".jpg"));
        }
        return dataList;
    }

    /**
     * recycleView当前显示的第一项
     */
    private int firstItem = 0;

    /**
     * recycleView当前显示的最后一项
     */
    private int lastItem = 0;

    /**
     * 滚动监听
     */
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                //停止滚动
                firstItem = linearLayoutManager.findFirstVisibleItemPosition();
                lastItem = linearLayoutManager.findLastVisibleItemPosition();
                List<Data> dataList = adapter.getDataList();
                for (int i = firstItem; i <= lastItem; i++) {
                    if (!UIUtil.isFileExist(parentPath + dataList.get(i).getImageName())) {
                        runImagDecodTask(i, lastItem - i + 1);
                        break;
                    }
                }
                //滑动改变时监听，重新计算时间
                calStartEndTime();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    /**
     * 线程池
     */
    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    /**
     * 运行一个图片的解码任务
     *
     * @param start 解码开始的视频时间 秒
     * @param count 一共解析多少张
     */
    private void runImagDecodTask(final int start, final int count) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                ffmpegTool.decodToImageWithCall(videoPath, parentPath, start, count);
            }
        });
    }

    /**
     * rangeBar 滑动改变时监听，重新计算时间
     *
     * @param rangeBar
     * @param leftThumbIndex
     * @param rightThumbIndex
     */
    @Override
    public void onIndexChangeListener(VideoRangeBar rangeBar, int leftThumbIndex, int rightThumbIndex) {
        this.leftThumbIndex = leftThumbIndex;
        this.rightThumbIndex = rightThumbIndex;
        calStartEndTime();
    }

    static class MyHandler extends Handler {

        private WeakReference<ClipVideoActivity> mOuter;

        public MyHandler(ClipVideoActivity activity) {
            mOuter = new WeakReference<ClipVideoActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ClipVideoActivity outer = mOuter.get();
            if (outer != null) {// Do something with outer as your wish.
                if (msg.what == 0) {
                    int current = outer.uVideoView.getCurrentPosition() / 1000;
                    if (current == outer.endTime) {
                        //已经播放到选择的结尾位置
                        outer.restartVideo();
                    }
                    outer.handler.sendEmptyMessageDelayed(0, 1000);
                }
            }
        }
    }


    /**
     * 滑动条的左端
     */
    private int leftThumbIndex = 0;
    /**
     * 滑动条的右端
     */
    private int rightThumbIndex = 0;

    /**
     * 裁剪的开始、结束时间
     */
    private int startTime, endTime;

    /**
     * 计算开始结束时间
     */
    private void calStartEndTime() {
        if (handler.hasMessages(0)) {
            handler.removeMessages(0);
        }
        int duration = rightThumbIndex - leftThumbIndex;
        startTime = firstItem + leftThumbIndex;
        endTime = startTime + duration;
        restartVideo();
        //发送消息
        handler.sendEmptyMessage(0);
    }

    /**
     * 重新播放
     */
    private void restartVideo() {
        //此时可能视频已经结束，若已结束重新start
        if (!uVideoView.isPlaying()) {
            uVideoView.start();
        }
        //把视频跳转到新选择的开始时间
        uVideoView.seekTo(startTime * 1000);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //获取到图片总的显示范围的大小后，设置每一个图片所占有的宽度
        if (hasFocus && adapter != null) {
            adapter.setImagWidth(rangeBar.getMeasuredWidth() / videoDuration);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (uVideoView.isPlaying()) {
            uVideoView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (uVideoView.isPlaying()) {
            uVideoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        if (uVideoView.isPlaying()) {
            uVideoView.stopPlayback();
        }
        //最后不要忘了删除这个临时文件夹 parentPath
        //不然时间长了会在手机上生成大量不用的图片，该activity销毁后这个文件夹就用不到了
        //如果内存大，任性不删也可以
        deleteDirWithFile(new File(parentPath));
        super.onDestroy();
    }

    /**
     * 删除文件夹和文件夹里面的文件
     *
     * @param dir
     */
    private void deleteDirWithFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWithFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    /**
     * 通知媒体库更新文件
     *
     * @param file
     */
    private void scanFile(final File file) {
        if (file != null && file.exists()) {
            Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            scanIntent.setData(Uri.fromFile(file));
            sendBroadcast(scanIntent);
            btnConfirm.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getLocalVideo(file.getAbsolutePath());
                }
            }, 1000);
        }
    }

    /**
     * 获取媒体库当前视频文件
     */
    private void getLocalVideo(final String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] mediaColumns = new String[]{ //
                        MediaStore.Video.Media._ID, //
                        MediaStore.Video.Media.DISPLAY_NAME, //
                        MediaStore.Video.Media.DATA, //
                        MediaStore.Video.Media.DATE_ADDED, //
                        MediaStore.Video.Media.DURATION, //
                        MediaStore.Video.Media.SIZE};

                Cursor cursor = getContentResolver().query(//
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, //
                        null, null, MediaStore.Video.Media.DATE_ADDED);

                // 读取扫描到的视频
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        final long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                        final String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                        final long time = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED));
                        final String name = cursor
                                .getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                        final long duration = cursor
                                .getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                        final long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

                        if (!".downloading".equals(path) && TextUtils.equals(filePath, path)) {
                            // 过滤未下载完成的文件
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent();
                                    intent.putExtra("video", new Image(id, path, time, name, duration, size));
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            });
                            break;
                        }
                    }
                    cursor.close();
                }
            }
        }).start();
    }
}
