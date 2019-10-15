package cn.appoa.afdemo.app;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.squareup.leakcanary.LeakCanary;

import cn.appoa.aframework.app.AfApplication;
import cn.appoa.aframework.cache.ACache;
import cn.appoa.aframework.utils.AESUtils;
import cn.appoa.aframework.utils.AtyUtils;

public class MyApplication extends AfApplication {

    //当前选中城市
    public static String local_city_id = "";
    public static String local_city_name = "";
    //AES加密的key
    public static String aes_key;

    @Override
    public void initApplication() {
        // 分包
        MultiDex.install(this);
        //AES加密
        aes_key = ACache.get(this).getAsString("aes_key");
        if (aes_key == null) {
            aes_key = "bWFsbHB3ZA==WNST";
        }
        AESUtils.init(aes_key);
        //内存泄漏
        if (AtyUtils.isDebug) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install(this);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 分包
        MultiDex.install(base);
    }

}
