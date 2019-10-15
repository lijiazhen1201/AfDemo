package cn.appoa.afdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseActivity;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;
import cn.appoa.aframework.utils.AtyUtils;

/**
 * 可设置宽高比的RelativeLayout
 */
public class RatioRelativeLayoutActivity extends BaseActivity {

    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(this).setTitle("RatioRelativeLayout")
                .setBackImage(R.drawable.back_white).create();
    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        setContent(R.layout.activity_ratio_relative_layout);
    }

    private EditText et_width;
    private EditText et_height;
    private TextView tv_confirm;
    private cn.appoa.aframework.widget.layout.RatioRelativeLayout mRatioRelativeLayout;

    @Override
    public void initView() {
        super.initView();
        et_width = (EditText) findViewById(R.id.et_width);
        et_height = (EditText) findViewById(R.id.et_height);
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        mRatioRelativeLayout = (cn.appoa.aframework.widget.layout.RatioRelativeLayout) findViewById(R.id.mRatioRelativeLayout);

    }

    @Override
    public void initData() {
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AtyUtils.isTextEmpty(et_width)) {
                    AtyUtils.showShort(mActivity, et_width.getHint(), false);
                    return;
                }
                if (AtyUtils.isTextEmpty(et_height)) {
                    AtyUtils.showShort(mActivity, et_height.getHint(), false);
                    return;
                }
                int width = Integer.parseInt(AtyUtils.getText(et_width));
                int height = Integer.parseInt(AtyUtils.getText(et_height));
                if (width == 0) {
                    AtyUtils.showShort(mActivity, "宽度必须大于0", false);
                    return;
                }
                mRatioRelativeLayout.setRatio(width, height);
            }
        });
    }
}
