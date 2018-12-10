package cn.appoa.aframework.titlebar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;

import cn.appoa.aframework.R;

public class DefaultTitlebar extends BaseTitlebar implements OnClickListener {

    /**
     * 默认高度
     */
    public static final int default_titlebar_height = R.dimen.height_default_titlebar;

    /**
     * 默认颜色
     */
    public static final int default_titlebar_bg = R.color.colorDefaultTitlebarBg;

    /**
     * 默认标题文字颜色
     */
    public static final int default_titlebar_title = R.color.colorDefaultTitlebarTextTitle;

    /**
     * 默认菜单文字颜色
     */
    public static final int default_titlebar_text = R.color.colorDefaultTitlebarTextMenu;

    private DefaultTitlebar(Context context) {
        super(context);
    }

    /**
     * 根布局
     */
    public RelativeLayout layout;

    @Override
    public void initTitle() {
        layout = new RelativeLayout(context);
        layout.setBackgroundColor(getColor(default_titlebar_bg));
        layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                getDimension(default_titlebar_height)));
        setTitle(layout);
    }

    /**
     * 左边图片
     */
    public ImageView iv_back;
    /**
     * 右边图片
     */
    public ImageView iv_menu;
    /**
     * 左边文字
     */
    public TextView tv_back;
    /**
     * 右边文字
     */
    public TextView tv_menu;
    /**
     * 标题
     */
    public TextView tv_title;
    /**
     * 底部分割线
     */
    public TextView tv_line;

    @Override
    public void initView(View view) {
        iv_back = new ImageView(context);
        iv_back.setVisibility(View.INVISIBLE);
        iv_back.setId(R.id.default_title_bar_back_iv);
        iv_back.setScaleType(ScaleType.CENTER_INSIDE);
        iv_back.setMinimumWidth(getDimension(default_titlebar_height));
        iv_back.setMinimumHeight(getDimension(default_titlebar_height));
        iv_back.setMaxWidth(getDimension(default_titlebar_height));
        iv_back.setMaxHeight(getDimension(default_titlebar_height));
        iv_back.setOnClickListener(this);
        layout.addView(iv_back);
        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) iv_back.getLayoutParams();
        params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        iv_back.setLayoutParams(params1);

        iv_menu = new ImageView(context);
        iv_menu.setVisibility(View.INVISIBLE);
        iv_menu.setId(R.id.default_title_bar_menu_iv);
        iv_menu.setScaleType(ScaleType.CENTER_INSIDE);
        iv_menu.setMinimumWidth(getDimension(default_titlebar_height));
        iv_menu.setMinimumHeight(getDimension(default_titlebar_height));
        iv_menu.setMaxWidth(getDimension(default_titlebar_height));
        iv_menu.setMaxHeight(getDimension(default_titlebar_height));
        iv_menu.setOnClickListener(this);
        layout.addView(iv_menu);
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) iv_menu.getLayoutParams();
        params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        iv_menu.setLayoutParams(params2);

        tv_back = new TextView(context);
        tv_back.setVisibility(View.INVISIBLE);
        tv_back.setId(R.id.default_title_bar_back_tv);
        tv_back.setTextSize(14);
        tv_back.setSingleLine(true);
        tv_back.setHeight(getDimension(default_titlebar_height));
        tv_back.setPadding(getDimension(R.dimen.padding_big), 0, getDimension(R.dimen.padding_big), 0);
        tv_back.setTextColor(getColor(default_titlebar_text));
        tv_back.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        tv_back.setOnClickListener(this);
        layout.addView(tv_back);
        RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) tv_back.getLayoutParams();
        params3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        tv_back.setLayoutParams(params3);

        tv_menu = new TextView(context);
        tv_menu.setVisibility(View.INVISIBLE);
        tv_menu.setId(R.id.default_title_bar_menu_tv);
        tv_menu.setTextSize(14);
        tv_menu.setSingleLine(true);
        tv_menu.setHeight(getDimension(default_titlebar_height));
        tv_menu.setPadding(getDimension(R.dimen.padding_big), 0, getDimension(R.dimen.padding_big), 0);
        tv_menu.setTextColor(getColor(default_titlebar_text));
        tv_menu.setGravity(Gravity.CENTER);
        tv_menu.setOnClickListener(this);
        layout.addView(tv_menu);
        RelativeLayout.LayoutParams params4 = (RelativeLayout.LayoutParams) tv_menu.getLayoutParams();
        params4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tv_menu.setLayoutParams(params4);

        tv_title = new TextView(context);
        tv_title.setVisibility(View.INVISIBLE);
        tv_title.setTextSize(18);
        tv_title.setSingleLine(true);
        tv_title.setHeight(getDimension(default_titlebar_height));
        tv_title.setTextColor(getColor(default_titlebar_title));
        tv_title.setGravity(Gravity.CENTER);
        layout.addView(tv_title);
        RelativeLayout.LayoutParams params5 = (RelativeLayout.LayoutParams) tv_title.getLayoutParams();
        params5.addRule(RelativeLayout.CENTER_IN_PARENT);
        params5.setMargins(getDimension(default_titlebar_height), 0, getDimension(default_titlebar_height), 0);
        tv_title.setLayoutParams(params5);

        tv_line = new TextView(context);
        tv_line.setVisibility(View.VISIBLE);
        tv_line.setHeight(getDimension(R.dimen.height_divider));
        tv_line.setBackgroundColor(getColor(R.color.colorDivider));
        RelativeLayout.LayoutParams params6 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params6.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layout.addView(tv_line, params6);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.default_title_bar_back_iv || v.getId() == R.id.default_title_bar_back_tv) {
            if (onClickBackListener != null) {
                onClickBackListener.onClickBack(v);
            } else {
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
            }
        }

        if (v.getId() == R.id.default_title_bar_menu_iv || v.getId() == R.id.default_title_bar_menu_tv) {
            if (onClickMenuListener != null) {
                onClickMenuListener.onClickMenu(v);
            }
        }

    }

    /**
     * 标题文字加粗
     */
    private Typeface textTitleTf = null;

    /**
     * 标题文字
     */
    private CharSequence textTitle = null;

    /**
     * 标题文字颜色
     */
    private int textTitleColor = -1;

    /**
     * 返回文字
     */
    private CharSequence textBack = null;

    /**
     * 返回文字颜色
     */
    private int textBackColor = -1;

    /**
     * 菜单文字
     */
    private CharSequence textMenu = null;

    /**
     * 菜单文字颜色
     */
    private int textMenuColor = -1;

    /**
     * 返回图片
     */
    private Drawable imageBack = null;

    /**
     * 菜单图片
     */
    private Drawable imageMenu = null;

    /**
     * 分割线颜色
     */
    private int lineColor = -1;

    /**
     * 分割线高度
     */
    private float lineHeight = 0.5f;

    /**
     * 标题栏颜色
     */
    private int layoutColor = -1;

    /**
     * 标题栏高度
     */
    private float layoutHeight = -1;

    /**
     * 创建
     */
    private void create() {
        if (textTitle != null) {
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setText(textTitle);
            if (textTitleTf != null) {
                tv_title.setTypeface(textTitleTf);
            }
        }
        if (textTitleColor != -1) {
            tv_title.setVisibility(View.VISIBLE);
            tv_title.setTextColor(textTitleColor);
        }
        if (textBack != null) {
            tv_back.setVisibility(View.VISIBLE);
            tv_back.setText(textBack);
        }
        if (textBackColor != -1) {
            tv_back.setVisibility(View.VISIBLE);
            tv_back.setTextColor(textBackColor);
        }
        if (textMenu != null) {
            tv_menu.setVisibility(View.VISIBLE);
            tv_menu.setText(textMenu);
        }
        if (textMenuColor != -1) {
            tv_menu.setVisibility(View.VISIBLE);
            tv_menu.setTextColor(textMenuColor);
        }
        if (imageBack != null) {
            iv_back.setVisibility(View.VISIBLE);
            iv_back.setImageDrawable(imageBack);
        }
        if (imageMenu != null) {
            iv_menu.setVisibility(View.VISIBLE);
            iv_menu.setImageDrawable(imageMenu);
        }
        if (lineColor != -1) {
            tv_line.setVisibility(View.VISIBLE);
            tv_line.setBackgroundColor(lineColor);
        }
        if (lineHeight != 0) {
            tv_line.setVisibility(View.VISIBLE);
            tv_line.setHeight(dip2px(context, lineHeight));
        } else {
            tv_line.setVisibility(View.GONE);
        }
        if (layoutColor != -1) {
            layout.setVisibility(View.VISIBLE);
            layout.setBackgroundColor(layoutColor);
        }
        if (layoutHeight != -1) {
            layout.setVisibility(View.VISIBLE);
            layout.setMinimumHeight(dip2px(context, layoutHeight));
        }
    }

    /**
     * 获取尺寸
     */
    private int getDimension(int id) {
        return (int) context.getResources().getDimension(id);
    }

    /**
     * 获取颜色
     */
    private int getColor(int id) {
        return ContextCompat.getColor(context, id);
    }

    @SuppressWarnings("serial")
    public static class Builder implements Serializable {

        private final DefaultTitlebar D;

        public Builder(Context context) {
            D = new DefaultTitlebar(context);
        }

        // =========================获取上下文=========================

        public Context getContext() {
            return D.context;
        }

        // =========================设置标题文字=========================

        public Builder setTitleBold() {
            return setTitleTypeface(Typeface.DEFAULT_BOLD);
        }

        public Builder setTitleTypeface(Typeface tf) {
            D.textTitleTf = tf;
            return this;
        }

        public Builder setTitle(int textTitleId) {
            D.textTitle = D.context.getText(textTitleId);
            return this;
        }

        public Builder setTitle(CharSequence textTitle) {
            D.textTitle = textTitle;
            return this;
        }

        public Builder setTitleColor(int titleColor) {
            D.textTitleColor = titleColor;
            return this;
        }

        public Builder setTitleColor(String titleColor) {
            D.textTitleColor = Color.parseColor(titleColor);
            return this;
        }

        // =========================设置返回文字=========================

        public Builder setBackText(int textBackId) {
            D.textBack = D.context.getText(textBackId);
            return this;
        }

        public Builder setBackText(CharSequence textBack) {
            D.textBack = textBack;
            return this;
        }

        public Builder setBackTextColor(int textBackColor) {
            D.textBackColor = textBackColor;
            return this;
        }

        public Builder setBackTextColor(String textBackColor) {
            D.textBackColor = Color.parseColor(textBackColor);
            return this;
        }

        // =========================设置菜单文字=========================

        public Builder setMenuText(int textMenuId) {
            D.textMenu = D.context.getText(textMenuId);
            return this;
        }

        public Builder setMenuText(CharSequence textMenu) {
            D.textMenu = textMenu;
            return this;
        }

        public Builder setMenuTextColor(int textMenuColor) {
            D.textMenuColor = textMenuColor;
            return this;
        }

        public Builder setMenuTextColor(String textMenuColor) {
            D.textMenuColor = Color.parseColor(textMenuColor);
            return this;
        }

        // =========================设置返回图片=========================

        @SuppressWarnings("deprecation")
        public Builder setBackImage(int imageId) {
            D.imageBack = D.context.getResources().getDrawable(imageId);
            return this;
        }

        public Builder setBackImage(Drawable imageBack) {
            D.imageBack = imageBack;
            return this;
        }

        @SuppressWarnings("deprecation")
        public Builder setBackImage(Bitmap imageBack) {
            D.imageBack = new BitmapDrawable(imageBack);
            return this;
        }

        // =========================设置菜单图片=========================

        @SuppressWarnings("deprecation")
        public Builder setMenuImage(int imageId) {
            D.imageMenu = D.context.getResources().getDrawable(imageId);
            return this;
        }

        public Builder setMenuImage(Drawable imageMenu) {
            D.imageMenu = imageMenu;
            return this;
        }

        @SuppressWarnings("deprecation")
        public Builder setMenuImage(Bitmap imageMenu) {
            D.imageMenu = new BitmapDrawable(imageMenu);
            return this;
        }

        // =========================设置分割线=========================

        public Builder setLineColor(int lineColor) {
            D.lineColor = lineColor;
            return this;
        }

        public Builder setLineColor(String lineColor) {
            D.lineColor = Color.parseColor(lineColor);
            return this;
        }

        public Builder setLineHeight(float lineHeight) {
            D.lineHeight = lineHeight;
            return this;
        }

        public Builder setLineHeight(int lineHeightId) {
            D.lineHeight = D.context.getResources().getDimension(lineHeightId);
            return this;
        }

        // =========================设置标题栏背景色=========================

        public Builder setTitlebarColor(int layoutColor) {
            D.layoutColor = layoutColor;
            return this;
        }

        public Builder setTitlebarColor(String layoutColor) {
            D.layoutColor = Color.parseColor(layoutColor);
            return this;
        }

        public Builder setTitlebarHeight(float layoutHeight) {
            D.layoutHeight = layoutHeight;
            return this;
        }

        public Builder setTitlebarHeight(int layoutHeightId) {
            D.layoutHeight = D.context.getResources().getDimension(layoutHeightId);
            return this;
        }

        // =========================添加事件监听=========================

        public Builder setBackListener(OnClickBackListener onClickBackListener) {
            D.onClickBackListener = onClickBackListener;
            return this;
        }

        public Builder setMenuListener(OnClickMenuListener onClickMenuListener) {
            D.onClickMenuListener = onClickMenuListener;
            return this;
        }

        // =========================创建标题栏=========================
        public TextView getMenuTextView() {
            return D.tv_menu;
        }

        public DefaultTitlebar create() {
            D.create();
            return D;
        }

    }

    /**
     * dp转px
     */
    protected int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
