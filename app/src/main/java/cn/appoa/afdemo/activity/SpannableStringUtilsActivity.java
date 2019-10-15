package cn.appoa.afdemo.activity;

import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseActivity;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;
import cn.appoa.aframework.utils.AtyUtils;
import cn.appoa.aframework.utils.SpannableStringUtils;

/**
 * SpannableString工具类
 */
public class SpannableStringUtilsActivity extends BaseActivity {

    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(this).setTitle("SpannableStringUtils")
                .setBackImage(R.drawable.back_white).create();
    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        setContent(R.layout.activity_spannable_string_utils);
    }

    private TextView tv_result;
    private int colorAccent;

    @Override
    public void initView() {
        super.initView();
        tv_result = (TextView) findViewById(R.id.tv_result);
        colorAccent = ContextCompat.getColor(mActivity, R.color.colorAccent);
    }

    @Override
    public void initData() {
        tv_result.setText(SpannableStringUtils
                .getBuilder("SpannableStringUtils相关方法\n")//获取建造者
                .append("设置标识\n").setFlag(Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                .append("设置前景色\n").setForegroundColor(colorAccent)
                .append("设置背景色\n").setBackgroundColor(colorAccent)
                .append("设置引用线的颜色\n").setQuoteColor(colorAccent)
                .append("设置缩进\n").setLeadingMargin(100, 300)
                .append("设置列表标记\n").setBullet(100, colorAccent)
                .append("设置字体比例2倍\n").setProportion(2f)
                .append("设置字体比例0.5倍\n").setProportion(0.5f)
                .append("设置字体横向比例2倍\n").setXProportion(2f)
                .append("设置字体横向比例0.5倍\n").setXProportion(0.5f)
                .append("设置删除线\n").setStrikethrough()
                .append("设置下划线\n").setUnderline()
                .append("设置").append("上标\n").setSuperscript()
                .append("设置").append("下标\n").setSubscript()
                .append("设置粗体\n").setBold()
                .append("设置斜体\n").setItalic()
                .append("设置粗斜体\n").setBoldItalic()
                .append("设置字体monospace\n").setFontFamily("monospace")
                .append("设置字体serif\n").setFontFamily("serif")
                .append("设置字体sans-serif\n").setFontFamily("sans-serif")
                .append("设置对齐正常\n").setAlign(Layout.Alignment.ALIGN_NORMAL)
                .append("设置对齐相反\n").setAlign(Layout.Alignment.ALIGN_OPPOSITE)
                .append("设置对齐居中\n").setAlign(Layout.Alignment.ALIGN_CENTER)
                .append("设置图片位图\n").setBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .append("设置图片资源\n").setDrawable(ContextCompat.getDrawable(mActivity, R.mipmap.ic_launcher))
                //.append("设置图片uri\n").setUri()
                .append("设置图片资源id\n").setResourceId(R.mipmap.ic_launcher)
                .append("设置点击事件\n").setClickSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        AtyUtils.showShort(mActivity, "点击事件", false);
                    }
                })
                .append("设置超链接\n").setUrl("https://www.baidu.com")
                .append("设置模糊NORMAL\n").setBlur(3, BlurMaskFilter.Blur.NORMAL)
                .append("设置模糊SOLID\n").setBlur(3, BlurMaskFilter.Blur.SOLID)
                .append("设置模糊OUTER\n").setBlur(3, BlurMaskFilter.Blur.OUTER)
                .append("设置模糊INNER\n").setBlur(3, BlurMaskFilter.Blur.INNER)
                .create());//创建样式字符串
    }
}
