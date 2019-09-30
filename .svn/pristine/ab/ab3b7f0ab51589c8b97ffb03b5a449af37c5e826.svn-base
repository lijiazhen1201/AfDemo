package cn.appoa.afdemo.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseImageActivity;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;


/**
 * 这是一份全面 & 详细的Webview使用攻略
 *
 * @author https://blog.csdn.net/yllp_1230/article/details/80655350
 *         <p>
 *         最全面总结 Android WebView与 JS 的交互方式
 * @author https://www.jianshu.com/p/345f4d8a5cfa
 */
public class WebViewActivity extends BaseImageActivity {

    public static final String baidu_url = "https://www.baidu.com";
    public static final String shop_url = "http://wx.chengziwangluo.com";
    public static final String vr_url = "http://editor.sser.wang/pano/50367";

    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(this).setTitle("WebView的使用")
                .setBackImage(R.drawable.back_white).create();
    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        initWebView();
    }

    protected RelativeLayout rootLayout;
    protected WebView webView;
    protected ProgressBar pb;

    /**
     * 初始化WebView
     */
    protected void initWebView() {
        //如何避免WebView内存泄露
        //不在xml中定义 Webview ，而是在需要的时候在Activity中创建，并且Context使用 getApplicationgContext()
        rootLayout = new RelativeLayout(this);

        webView = new WebView(getApplicationContext());
        webView.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        rootLayout.addView(webView);

        pb = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        pb.setProgressDrawable(this.getResources().getDrawable(R.drawable.pb_layer_list));
        pb.setMax(100);
        rootLayout.addView(pb, RelativeLayout.LayoutParams.MATCH_PARENT, 5);

        setContent(rootLayout);

        //事件监听
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(final String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {
                new AlertDialog.Builder(WebViewActivity.this)
                        .setTitle("Download")
                        .setMessage("Allow to download？")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //系统浏览器下载
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(
                                        WebViewActivity.this,
                                        "refuse download...",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });
    }

    @Override
    public void initView() {
        super.initView();
        initWebSettings();
        initWebViewClient();
        initWebChromeClient();
    }

    @Override
    public void initData() {
        //加载网页
        webView.loadUrl(WebViewActivity.vr_url);
    }

    protected WebSettings webSettings;
    protected CookieManager mCookieManager;

    /**
     * 对WebView进行配置和管理
     */
    protected void initWebSettings() {
        //声明WebSettings子类
        webSettings = webView.getSettings();

        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可

        //支持插件
        webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setSupportMultipleWindows(true); //
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        //缓存模式如下：
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能
        webSettings.setAppCacheMaxSize(Long.MAX_VALUE);
        webSettings.setGeolocationEnabled(true);//
        // 特别注意：5.1以上默认禁止了https和http混用，以下方式是开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        mCookieManager = CookieManager.getInstance();
        mCookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCookieManager.setAcceptThirdPartyCookies(webView, true);
        }
    }

    protected boolean loadError;

    /**
     * 处理各种通知 & 请求事件
     */
    protected void initWebViewClient() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //复写shouldOverrideUrlLoading()方法，
                //使得打开网页时不调用系统浏览器， 而是在本WebView中显示
                if (TextUtils.isEmpty(url)) {
                    return false;
                }
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    view.loadUrl(url);
                } else {
                    try {
                        // 其他自定义的scheme
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    } catch (Exception e) {
                        // 防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                        return false;
                    }
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //开始载入页面调用的，我们可以设定一个loading的页面，告诉用户程序在等待网络响应
                super.onPageStarted(view, url, favicon);
                //设定加载开始的操作
                loadError = false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //在页面加载结束时调用。我们可以关闭loading 条，切换程序动作
                super.onPageFinished(view, url);
                //设定加载结束的操作
                if (loadError) {
                } else {
                    mCookieManager.setCookie(url, mCookieManager.getCookie(url));
                    CookieSyncManager.getInstance().sync();
                }
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                //在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次
                super.onLoadResource(view, url);
                //设定加载资源的操作
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //加载页面的服务器出现错误时（如404）调用
                super.onReceivedError(view, errorCode, description, failingUrl);
                loadError = true;
                //步骤1：写一个html文件（error_handle.html），用于出错时展示给用户看的提示页面
                //步骤2：将该html文件放置到代码根目录的assets文件夹下
                //步骤3：复写WebViewClient的onRecievedError方法
                //该方法传回了错误码，根据错误类型可以进行不同的错误分类处理
                switch (errorCode) {
                    case 404:
                        //loadHtmlAsset("error_handle.html");
                        break;
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //处理https请求
                super.onReceivedSslError(view, handler, error);
                //表示等待证书响应
                handler.proceed();
                //表示挂起连接，为默认方式
                //handler.cancel();
                //可做其他处理
                //handler.handleMessage(null);
            }
        });
    }

    protected ValueCallback<Uri> uploadFile;
    protected ValueCallback<Uri[]> uploadFiles;

    /**
     * 辅助 WebView 处理 Javascript 的对话框,网站图标,网站标题等等
     */
    protected void initWebChromeClient() {
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                //获得网页的加载进度并显示
                if (newProgress < 100) {
                    pb.setVisibility(View.VISIBLE);// 开始加载网页时显示进度条
                    pb.setProgress(newProgress);// 设置进度值
                } else {
                    pb.setVisibility(View.GONE);// 加载完网页进度条消失
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //获取Web页中的标题
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                //支持javascript的警告框
                new AlertDialog.Builder(WebViewActivity.this)
                        .setTitle("JsAlert")
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setCancelable(false)
                        .show();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                //支持javascript的确认框
                new AlertDialog.Builder(WebViewActivity.this)
                        .setTitle("JsConfirm")
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        })
                        .setCancelable(false)
                        .show();
                // 返回布尔值：判断点击时确认还是取消
                // true表示点击了确认；false表示点击了取消；
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                //支持javascript输入框
                final EditText et = new EditText(WebViewActivity.this);
                et.setText(defaultValue);
                new AlertDialog.Builder(WebViewActivity.this)
                        .setTitle(message)
                        .setView(et)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm(et.getText().toString());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        })
                        .setCancelable(false)
                        .show();
                return true;
            }

            // For Android < 3.0
            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> valueCallback) {
                uploadFile = valueCallback;
                openFileChooseProcess();
            }

            // For Android >= 3.0
            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType) {
                uploadFile = valueCallback;
                openFileChooseProcess();
            }

            // For Android >= 4.1
            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
                uploadFile = valueCallback;
                openFileChooseProcess();
            }

            // For Android >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback,
                                             WebChromeClient.FileChooserParams fileChooserParams) {
                uploadFiles = filePathCallback;
                openFileChooseProcess();
                return true;
            }
            // 在android 4.4.0 4.4.1 4.4.2版本中，以上三方法失效。原生的WebView只会调用一次
        });

    }

    /**
     * 打开文件选择器
     */
    protected void openFileChooseProcess() {
        dialogUpload.isConfirm = false;
        dialogUpload.showDialog();
    }

    /**
     * 上传文件
     *
     * @param uri
     */
    protected void uploadFileChooser(Uri uri) {
        if (uri == null) {
            return;
        }
        ArrayList<Uri> uris = new ArrayList<>();
        uris.add(uri);
        uploadFileChooser(uris);
    }

    /**
     * 上传文件
     *
     * @param uris
     */
    protected void uploadFileChooser(ArrayList<Uri> uris) {
        if (uris == null || uris.size() == 0) {
            return;
        }
        if (uploadFiles != null) {
            uploadFiles.onReceiveValue(uris.toArray(new Uri[uris.size()]));
            uploadFiles = null;
        } else if (uploadFile != null) {
            uploadFile.onReceiveValue(uris.get(0));
            uploadFile = null;
        }
    }

    /**
     * 释放资源
     */
    protected void clearFileChooser() {
        if (uploadFiles != null) {
            uploadFiles.onReceiveValue(null);
            uploadFiles = null;
        } else if (uploadFile != null) {
            uploadFile.onReceiveValue(null);
            uploadFile = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogUpload.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!dialogUpload.isConfirm) {
                    // 没有确定
                    clearFileChooser();
                }
            }
        });
    }

    @Override
    public void getImageBitmap(Uri imageUri, String imagePath, Bitmap imageBitmap) {
        uploadFileChooser(imageUri);
    }

    @Override
    public void getImageBitmap(Bitmap imageBitmap) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            clearFileChooser();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        if (webSettings != null) {
            webSettings.setJavaScriptEnabled(true);
        }
        if (webView != null) {
            //激活WebView为活跃状态，能正常执行网页的响应
            webView.onResume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (webView != null) {
            //当页面被失去焦点被切换到后台不可见状态，需要执行onPause
            //通过onPause动作通知内核暂停所有的动作，比如DOM的解析、plugin的执行、JavaScript执行。
            webView.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (webSettings != null) {
            webSettings.setJavaScriptEnabled(false);
        }
        if (webView != null) {
            //当应用程序(存在webview)被切换到后台时，这个方法不仅仅针对当前的webview而是全局的全应用程序的webview
            //它会暂停所有webview的layout，parsing，javascripttimer。降低CPU功耗。
            webView.pauseTimers();
            //恢复pauseTimers状态
            webView.resumeTimers();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //销毁Webview
        //在关闭了Activity时，如果Webview的音乐或视频，还在播放。就必须销毁Webview
        //但是注意：webview调用destory时,webview仍绑定在Activity上
        //这是由于自定义webview构建时传入了该Activity的context对象
        //因此需要先从父容器中移除webview,然后再销毁webview:
        //在 Activity 销毁（ WebView ）的时候，先让 WebView 加载null内容，然后移除 WebView，再销毁 WebView，最后置空
        if (webView != null) {
            loadHtmlData(null, "");
            clearData();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    /**
     * 加载富文本
     *
     * @param baseUrl
     * @param data
     */
    protected void loadHtmlData(String baseUrl, String data) {
        if (webView == null) {
            return;
        }
        if (data == null) {
            data = "";
        }
        data.replace("\\", "");
        webView.loadDataWithBaseURL(baseUrl, data, "text/html", "UTF-8", null);
    }

    /**
     * 清除数据
     */
    protected void clearData() {
        if (webView == null) {
            return;
        }
        //清除网页访问留下的缓存
        //由于内核缓存是全局的因此这个方法不仅仅针对webview而是针对整个应用程序.
        webView.clearCache(true);
        //清除当前webview访问的历史记录
        //只会webview访问历史记录里的所有记录除了当前访问记录
        webView.clearHistory();
        //这个api仅仅清除自动完成填充的表单数据，并不会清除WebView存储到本地的数据
        webView.clearFormData();
    }

    /**
     * 加载assets里的html
     */
    @SuppressWarnings("unused")
    protected void loadHtmlAsset(String fileName) {
        if (webView == null || TextUtils.isEmpty(fileName)) {
            return;
        }
        webView.loadUrl("file:///android_asset/" + fileName);
    }

    /**
     * 前进
     */
    @SuppressWarnings("unused")
    protected boolean goForward() {
        if (webView == null) {
            return false;
        }
        //是否可以前进
        if (webView.canGoForward()) {
            //前进网页
            webView.goForward();
            return true;
        }
        return false;
    }

    /**
     * 后退
     */
    protected boolean goBack() {
        if (webView == null) {
            return false;
        }
        //是否可以后退
        if (webView.canGoBack()) {
            //后退网页
            webView.goBack();
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //在不做任何处理前提下 ，浏览网页时点击系统的“Back”键,整个 Browser 会调用 finish()而结束自身
        if (keyCode == KeyEvent.KEYCODE_BACK && goBack()) {
            //点击返回后，是网页回退而不是推出浏览器
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
