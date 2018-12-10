package cn.appoa.aframework.app;

import android.app.Application;
import android.content.Context;

import cn.appoa.aframework.R;
import cn.appoa.aframework.manager.Foreground;
import zm.http.volley.ZmVolley;
import zm.imageloader.ZmImageLoader;
import zm.imageloader.impl.ImageLoaderGlide;

public abstract class AfApplication extends Application {

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
    public static Application app;

    /**
     * ImageLoader
     */
    public static ZmImageLoader imageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        FILE_PROVIDER = getPackageName() + ".fileprovider";
        appContext = getApplicationContext();
        app = this;
        Foreground.init(this);
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
        initApplication();
    }

    /**
     * 初始化ImageLoader
     */
    public ZmImageLoader initImageLoader() {
        return null;
    }

    /**
     * 初始化App
     */
    public abstract void initApplication();
}
