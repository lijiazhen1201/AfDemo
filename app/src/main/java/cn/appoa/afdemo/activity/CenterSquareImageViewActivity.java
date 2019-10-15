package cn.appoa.afdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseActivity;
import cn.appoa.afdemo.net.API;
import cn.appoa.aframework.app.AfApplication;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;

/**
 * 图片截取正中间的正方形部分的ImageView
 */
public class CenterSquareImageViewActivity extends BaseActivity
        implements CompoundButton.OnCheckedChangeListener {

    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(this).setTitle("CenterSquareImageView")
                .setBackImage(R.drawable.back_white).create();
    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        setContent(R.layout.activity_center_square_image_view);
    }

    private RadioButton btn_horizontal;
    private RadioButton btn_vertical;
    private ImageView iv_image_normal;
    private ImageView iv_image_horizontal;
    private ImageView iv_image_vertical;
    private cn.appoa.aframework.widget.image.CenterSquareImageView iv_image_center;

    @Override
    public void initView() {
        super.initView();
        btn_horizontal = (RadioButton) findViewById(R.id.btn_horizontal);
        btn_vertical = (RadioButton) findViewById(R.id.btn_vertical);
        iv_image_normal = (ImageView) findViewById(R.id.iv_image_normal);
        iv_image_horizontal = (ImageView) findViewById(R.id.iv_image_horizontal);
        iv_image_vertical = (ImageView) findViewById(R.id.iv_image_vertical);
        iv_image_center = (cn.appoa.aframework.widget.image.CenterSquareImageView) findViewById(R.id.iv_image_center);

        //事件监听
        btn_horizontal.setOnCheckedChangeListener(this);
        btn_vertical.setOnCheckedChangeListener(this);
    }

    @Override
    public void initData() {
        btn_horizontal.setChecked(true);
    }

    private String url1 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1569038400883&di=50efc288ac56cd3a6a3e58231a4f471e&imgtype=0&src=http%3A%2F%2Fattachments.gfan.com%2Fforum%2F201504%2F04%2F185242kumuqezkckyykc4z.jpg";
    private String url2 = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1569038435876&di=1a4debee39980e615f20105f4e207d36&imgtype=0&src=http%3A%2F%2Fim6.leaderhero.com%2Fwallpaper%2F20150210%2Faa10f8d39a.jpg";

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            int type = 0;
            switch (buttonView.getId()) {
                case R.id.btn_horizontal:
                    type = 1;
                    break;
                case R.id.btn_vertical:
                    type = 2;
                    break;
            }
            if (type == 1) {
                AfApplication.imageLoader.loadImage(API.IMAGE_URL + url1, iv_image_normal);
                AfApplication.imageLoader.loadImage(API.IMAGE_URL + url1, iv_image_center);
                AfApplication.imageLoader.loadImage(API.IMAGE_URL + url1, iv_image_horizontal);
                AfApplication.imageLoader.loadImage(API.IMAGE_URL + url1, iv_image_vertical);
                iv_image_horizontal.setVisibility(View.VISIBLE);
                iv_image_vertical.setVisibility(View.GONE);
            } else if (type == 2) {
                AfApplication.imageLoader.loadImage(API.IMAGE_URL + url2, iv_image_normal);
                AfApplication.imageLoader.loadImage(API.IMAGE_URL + url2, iv_image_center);
                AfApplication.imageLoader.loadImage(API.IMAGE_URL + url2, iv_image_horizontal);
                AfApplication.imageLoader.loadImage(API.IMAGE_URL + url2, iv_image_vertical);
                iv_image_horizontal.setVisibility(View.GONE);
                iv_image_vertical.setVisibility(View.VISIBLE);
            }
        }
    }

}
