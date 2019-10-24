package cn.appoa.aframework.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.lzy.okgo.OkGo;

import cn.appoa.aframework.R;
import cn.appoa.aframework.crash.ZmCrashHandler;
import cn.appoa.aframework.manager.Foreground;
import zm.http.volley.ZmVolley;
import zm.imageloader.ZmImageLoader;
import zm.imageloader.impl.ImageLoaderGlide;

public abstract class AfApplication extends Application
        implements Foreground.ConfigurationsListener {

    /**
     * add
     */
    public static String add = "<style> img { max-width:100% ; height:auto;}  </style>";

    /**
     * file provider
     */
    public static String FILE_PROVIDER = "cn.appoa.aframework.fileprovider";

    /**
     * ApplicationContext
     */
    public static Context appContext;

    /**
     * Application
     */
    @SuppressLint("StaticFieldLeak")
    public static Application app;

    /**
     * ImageLoader
     */
    @SuppressLint("StaticFieldLeak")
    public static ZmImageLoader imageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        FILE_PROVIDER = getPackageName() + ".fileprovider";
        appContext = getApplicationContext();
        app = this;
        Foreground.init(this);
        Foreground.get().setConfigurationsListener(this);
        ZmCrashHandler.getInstance().init(this);
        try {
            imageLoader = initImageLoader();
            if (imageLoader == null) {
                imageLoader = ImageLoaderGlide.getInstance(this);
            }
            imageLoader.init(R.drawable.default_img, R.drawable.default_img, R.drawable.default_img);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ZmVolley.initVolley(this);
        initOkGo(OkGo.getInstance().init(this));
        initFileDownloader();
        initApplication();
    }

    /**
     * 初始化下载器
     */
    private void initFileDownloader() {
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);
    }

    /**
     * 初始化ImageLoader
     */
    public ZmImageLoader initImageLoader() {
        return null;
    }

    /**
     * 初始化OkGo
     *
     * @param mOkGo
     * @since https://github.com/jeasonlzy/okhttp-OkGo/wiki/Init
     */
    public void initOkGo(OkGo mOkGo) {
    }

    @Override
    public void onStarted(Activity activity) {

    }

    @Override
    public void onStopped(Activity activity) {

    }

    /**
     * 初始化App
     */
    public abstract void initApplication();
}
