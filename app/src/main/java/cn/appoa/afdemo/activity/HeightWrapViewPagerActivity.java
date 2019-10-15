package cn.appoa.afdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseActivity;
import cn.appoa.afdemo.net.API;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;
import cn.appoa.aframework.widget.pager.HeightWrapViewPager;
import cn.appoa.wximageselector.ShowBigImageListActivity;

/**
 * 自动适应图片高度的ViewPager
 */
public class HeightWrapViewPagerActivity extends BaseActivity {

    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(this).setTitle("HeightWrapViewPager")
                .setBackImage(R.drawable.back_white).create();
    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        setContent(R.layout.activity_height_wrap_view_pager);
    }

    private HeightWrapViewPager viewPager;
    private TextView tv_page;

    @Override
    public void initView() {
        super.initView();
        viewPager = (HeightWrapViewPager) findViewById(R.id.viewPager);
        tv_page = (TextView) findViewById(R.id.tv_page);
    }

    @Override
    public void initData() {
        //TODO 模拟数据
        List<String> imgs = new ArrayList<>();
        imgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1569044854025&di=772c312cca8449bf069e9cb027c6ea76&imgtype=0&src=http%3A%2F%2Fimgtianqi.eastday.com%2Fres%2Fupload%2Fue%2Fimage%2F20171116%2F1510798702456062.jpeg");
        imgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1569044874866&di=84df5db84c5ddfa5922a3845159b3394&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201207%2F03%2F20120703150132_dANFT.jpeg");
        imgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1569044861613&di=8852b8bed76557f207957df00ef19da2&imgtype=0&src=http%3A%2F%2Fimg.mp.sohu.com%2Fupload%2F20170616%2F10a1704edd724b8b96948f25c2cbf14c_th.png");
        imgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1569044898022&di=b68c00f121e228665731ee377e19ba31&imgtype=0&src=http%3A%2F%2Fimages.hljtv.com%2Fmaterial%2Fnews%2Fimg%2F2019%2F09%2Fa9535142cb481f869679924749e60931.jpg");
        imgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1569044911777&di=a12bf60a50b9270a41f41ad78edd971b&imgtype=0&src=http%3A%2F%2Fimg1.cache.netease.com%2Fcatchpic%2F7%2F76%2F7663885F0BCFACD79CA43354245D84B0.jpg");
        imgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1569044921025&di=793142d5644bd6598103637e4cae05f7&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201505%2F26%2F20150526212800_mAjWZ.jpeg");
        imgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1569044931692&di=257f8481c33896f974b4817f851f8c10&imgtype=0&src=http%3A%2F%2Fcs.vmovier.com%2FUploads%2Fpost%2F2015-07-22%2F55af58c724555.jpg");
        imgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1569044964018&di=e1dda404b9d85f7d3466fad8918e247a&imgtype=0&src=http%3A%2F%2Fguangdong.sinaimg.cn%2F2014%2F0909%2FU11004P693DT20140909094746.jpg");
        imgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1569044973390&di=b38d3785f72c7d8e1cdd7b0e1d74060e&imgtype=0&src=http%3A%2F%2Fbbs-fd.zol-img.com.cn%2Ft_s1200x5000%2Fg1%2FM07%2F0C%2F00%2FCg-4jVMCZ9GIEi93AAvNhOybLiQAAHv-QJHsA8AC82c813.jpg");
        if (imgs != null && imgs.size() > 0) {
            ArrayList<String> images = new ArrayList<>();
            for (int i = 0; i < imgs.size(); i++) {
                images.add(API.IMAGE_URL + imgs.get(i));
            }
            tv_page.setText("1/" + images.size());
            viewPager.setBanner(images);
            viewPager.setOnPageSelectedListener(new HeightWrapViewPager.OnPageSelectedListener() {

                @Override
                public void onClick(int position, ArrayList<String> images, View v) {
                    //点击页面时
                    startActivity(new Intent(mActivity, ShowBigImageListActivity.class)
                            .putExtra("page", position)
                            .putStringArrayListExtra("images", images));
                }

                @Override
                public void onPageSelected(int position, ArrayList<String> images) {
                    //页面选中时
                    tv_page.setText(position + 1 + "/" + images.size());
                }
            });
        }
    }
}
