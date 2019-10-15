package cn.appoa.afdemo.activity;

import android.os.Bundle;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseActivity;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;
import cn.appoa.aframework.widget.text.NumberAnimTextView;

/**
 * 数字增加和减小动画TextView
 */
public class NumberAnimTextViewActivity extends BaseActivity {

    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(this).setTitle("NumberAnimTextView")
                .setBackImage(R.drawable.back_white).create();
    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        setContent(R.layout.activity_number_anim_text_view);
    }

    private NumberAnimTextView tv_number_anim1;
    private NumberAnimTextView tv_number_anim2;
    private NumberAnimTextView tv_number_anim3;

    @Override
    public void initView() {
        super.initView();
        tv_number_anim1 = (NumberAnimTextView) findViewById(R.id.tv_number_anim1);
        tv_number_anim2 = (NumberAnimTextView) findViewById(R.id.tv_number_anim2);
        tv_number_anim3 = (NumberAnimTextView) findViewById(R.id.tv_number_anim3);
    }

    @Override
    public void initData() {
        // 设置前缀
        tv_number_anim1.setPrefixString("¥ ");
        tv_number_anim1.setNumberString("98765432.75");
        // 设置后缀
        tv_number_anim2.setPostfixString("%");
        tv_number_anim2.setNumberString("86.39");
        // 设置动画时长
        tv_number_anim3.setDuration(2000);
        // 设置数字增加范围
        //tv_number_anim3.setNumberString("19.75", "99.75");
        tv_number_anim3.setNumberString( "99.75","19.75");
    }

}
