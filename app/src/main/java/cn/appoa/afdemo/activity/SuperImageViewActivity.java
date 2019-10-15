package cn.appoa.afdemo.activity;

import android.graphics.Color;
import android.os.Bundle;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseActivity;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;
import cn.appoa.aframework.utils.AtyUtils;
import cn.appoa.aframework.widget.image.SuperImageView;

/**
 * 圆形，圆角，带边框的ImageView
 */
public class SuperImageViewActivity extends BaseActivity {

    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(this).setTitle("SuperImageView")
                .setBackImage(R.drawable.back_white).create();
    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        setContent(R.layout.activity_super_image_view);
    }

    private SuperImageView iv_avatar;

    @Override
    public void initView() {
        super.initView();
        iv_avatar = (SuperImageView) findViewById(R.id.iv_avatar);
    }

    @Override
    public void initData() {
        //设置类型0默认1圆形2矩形3椭圆(不同圆角)
        iv_avatar.setShapeType(0);
        //设置圆角大小(仅在shapeType=2时候有效)
        iv_avatar.setRadius(AtyUtils.dip2px(mActivity, 10.0f));
        //设置圆角大小(仅在shapeType=3时候有效)
        iv_avatar.setRadius(
                AtyUtils.dip2px(mActivity, 0.0f),
                AtyUtils.dip2px(mActivity, 5.0f),
                AtyUtils.dip2px(mActivity, 10.0f),
                AtyUtils.dip2px(mActivity, 20.0f));
        //分别设置圆角大小(仅在shapeType=3时候有效)
        iv_avatar.setRadiusTopLeft(AtyUtils.dip2px(mActivity, 0.0f));
        iv_avatar.setRadiusTopRight(AtyUtils.dip2px(mActivity, 5.0f));
        iv_avatar.setRadiusBottomLeft(AtyUtils.dip2px(mActivity, 10.0f));
        iv_avatar.setRadiusBottomRight(AtyUtils.dip2px(mActivity, 20.0f));
        //设置边框颜色
        iv_avatar.setBorderColor(Color.BLACK);
        //设置边框宽度
        iv_avatar.setBorderWidth(AtyUtils.dip2px(mActivity, 2.0f));
        //设置宽高比
        iv_avatar.setRatio(0.5f);
        iv_avatar.setRatio(1,1);
        //设置按下颜色(暂无效果)
        iv_avatar.setPressColor(Color.GRAY);
        //设置按下透明度(暂无效果)
        iv_avatar.setPressAlpha(1);
    }
}
