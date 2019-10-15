package cn.appoa.afdemo.activity;

/**
 * Github优秀开源库
 */
public class GithubProjectActivity extends AbsListActivity {

    @Override
    protected CharSequence initTitle() {
        return "Github优秀开源库";
    }

    @Override
    protected String[] initTitles() {
        return new String[]{
                "AndroidAutoLayout\n【Android屏幕适配方案，直接填写设计图上的像素尺寸即可完成适配，最大限度解决适配问题。】",
                "AndroidUtilCode\n【AndroidUtilCode是一个强大易用的安卓工具类库，它合理地封装了安卓开发中常用的函数，具有完善的 Demo 和单元测试，利用其封装好的 APIs 可以大大提高开发效率。】",
                "AppUpdate\n【Android 版本更新 a library for android version update】",
                "MPAndroidChart\n【A powerful Android chart view / graph view library, supporting line- bar- pie- radar- bubble- and candlestick charts as well as scaling, dragging and animations.】",
                "NCalendar\n【一款安卓日历，仿miui，钉钉，华为的日历，万年历、365、周日历，月日历，月视图、周视图滑动切换，农历，节气，Andriod Calendar , MIUI Calendar,小米日历】",
                "OkGo\n【OkGo - 3.0 震撼来袭，该库是基于 Http 协议，封装了 OkHttp 的网络请求框架，比 Retrofit 更简单易用，支持 RxJava，RxJava2，支持自定义缓存，支持批量断点下载管理和批量上传管理功能】",
                "PRDownloader\n【A file downloader library for Android with pause and resume support】",
                "QMUI_Android\n【QMUI Android 的设计目的是用于辅助快速搭建一个具备基本设计还原效果的 Android 项目，同时利用自身提供的丰富控件及兼容处理，让开发者能专注于业务需求而无需耗费精力在基础代码的设计上。不管是新项目的创建，或是已有项目的维护，均可使开发效率和项目质量得到大幅度提升。】",
                "SmartTable\n【一款android自动生成表格框架---An Android automatically generated table framework】",
                "SuperButton\n【一个使用简单且能满足日常各种需求的按钮。】",
                "SuperTextView\n【SuperTextView 的与众不同在于，它只是一个简单的控件元素，但却不仅仅是一个控件。它生而灵动多变，强大的内嵌逻辑，为你持续提供丰富多彩却异常简单的开发支持。】",
        };
    }

    @Override
    protected Class[] initClass() {
        return new Class[]{
                AndroidAutoLayoutActivity.class,
                AndroidUtilCodeActivity.class,
                AppUpdateActivity.class,
                MPAndroidChartActivity.class,
                NCalendarActivity.class,
                OkGoActivity.class,
                PRDownloaderActivity.class,
                QMUI_AndroidActivity.class,
                SmartTableActivity.class,
                SuperButtonActivity.class,
                SuperTextViewActivity.class,
        };
    }

}
