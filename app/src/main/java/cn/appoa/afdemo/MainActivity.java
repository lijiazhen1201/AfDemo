package cn.appoa.afdemo;

import android.view.KeyEvent;

import cn.appoa.afdemo.activity.AbsListActivity;
import cn.appoa.afdemo.activity.BannerActivity;
import cn.appoa.afdemo.activity.CommonUtilsActivity;
import cn.appoa.afdemo.activity.CustomWidgetActivity;
import cn.appoa.afdemo.activity.GithubProjectActivity;
import cn.appoa.afdemo.activity.RefreshBeanActivity;
import cn.appoa.afdemo.activity.UploadAvatarActivity;
import cn.appoa.afdemo.activity.UploadImageActivity;
import cn.appoa.afdemo.activity.UploadVideoActivity;
import cn.appoa.afdemo.activity.UploadVoiceActivity;
import cn.appoa.afdemo.activity.WebViewActivity;
import cn.appoa.afdemo.activity.ZmQrCodeActivity;
import cn.appoa.afdemo.activity.ZmShakeActivity;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;

/**
 * 主页
 */
public class MainActivity extends AbsListActivity {

    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(this).setTitle("AfDemo").create();
    }

    @Override
    protected CharSequence initTitle() {
        return "AfDemo";
    }

    @Override
    protected String[] initTitles() {
        return new String[]{
                "轮播图",
                "常用工具类",
                "自定义控件",
                "Github优秀开源库",
                "下拉刷新",
                "头像上传",
                "图片多选",
                "视频上传",
                "语音上传",
                "WebView的使用",
                "微信扫一扫",
                "微信摇一摇",
        };
    }

    @Override
    protected Class[] initClass() {
        return new Class[]{
                BannerActivity.class,
                CommonUtilsActivity.class,
                CustomWidgetActivity.class,
                GithubProjectActivity.class,
                RefreshBeanActivity.class,
                UploadAvatarActivity.class,
                UploadImageActivity.class,
                UploadVideoActivity.class,
                UploadVoiceActivity.class,
                WebViewActivity.class,
                ZmQrCodeActivity.class,
                ZmShakeActivity.class,
        };
    }

    @Override
    public void initData() {
        //requestAllPermissions();
        super.initData();
    }

    @Override
    public boolean enableSliding() {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && //
                event.getAction() == KeyEvent.ACTION_DOWN) {
            doubleClickExit(2000, "再按一次返回键退出程序");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
