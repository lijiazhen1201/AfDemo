package com.scwang.smartrefresh.layout.utils;


import android.content.Context;

import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.fragment.R;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

public final class SmartRefreshHelper {

    /**
     * 初始化刷新文字
     *
     * @param context
     */
    public static void initRefreshText(Context context) {
        if (context == null) {
            return;
        }
        ClassicsHeader.REFRESH_HEADER_PULLING = context.getString(R.string.refresh_header_pulldown);
        ClassicsHeader.REFRESH_HEADER_REFRESHING = context.getString(R.string.refresh_header_refreshing);
        ClassicsHeader.REFRESH_HEADER_LOADING = context.getString(R.string.refresh_header_loading);
        ClassicsHeader.REFRESH_HEADER_RELEASE = context.getString(R.string.refresh_header_release);
        ClassicsHeader.REFRESH_HEADER_FINISH = context.getString(R.string.refresh_header_finish);
        ClassicsHeader.REFRESH_HEADER_FAILED = context.getString(R.string.refresh_header_failed);
        ClassicsHeader.REFRESH_HEADER_UPDATE = context.getString(R.string.refresh_header_lasttime);
        ClassicsHeader.REFRESH_HEADER_SECONDARY = context.getString(R.string.refresh_header_second_floor);

        ClassicsFooter.REFRESH_FOOTER_PULLING = context.getString(R.string.refresh_footer_pullup);
        ClassicsFooter.REFRESH_FOOTER_RELEASE = context.getString(R.string.refresh_footer_release);
        ClassicsFooter.REFRESH_FOOTER_LOADING = context.getString(R.string.refresh_footer_loading);
        ClassicsFooter.REFRESH_FOOTER_REFRESHING = context.getString(R.string.refresh_footer_refreshing);
        ClassicsFooter.REFRESH_FOOTER_FINISH = context.getString(R.string.refresh_footer_finish);
        ClassicsFooter.REFRESH_FOOTER_FAILED = context.getString(R.string.refresh_footer_failed);
        ClassicsFooter.REFRESH_FOOTER_NOTHING = context.getString(R.string.refresh_footer_allloaded);
    }
}
