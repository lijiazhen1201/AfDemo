package cn.appoa.afdemo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.tmall.ultraviewpager.UltraViewPager;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoaderInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.app.MyApplication;
import cn.appoa.afdemo.base.BaseActivity;
import cn.appoa.afdemo.bean.BannerBean;
import cn.appoa.afdemo.net.API;
import cn.appoa.aframework.adapter.ZmImageAdapter;
import cn.appoa.aframework.app.AfApplication;
import cn.appoa.aframework.listener.VolleyDatasListener;
import cn.appoa.aframework.listener.VolleyErrorListener;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;
import cn.appoa.aframework.utils.AtyUtils;
import cn.appoa.aframework.widget.pager.RollViewPager;
import cn.appoa.wximageselector.ShowBigImageListActivity;
import zm.http.volley.ZmVolley;
import zm.http.volley.request.ZmStringRequest;

/**
 * 轮播图
 */
public class BannerActivity extends BaseActivity {

    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(this).setTitle("轮播图")
                .setBackImage(R.drawable.back_white).create();
    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        setContent(R.layout.activity_banner);
    }

    private cn.appoa.aframework.widget.pager.RollViewPager mRollViewPager;
    private LinearLayout mLinearLayout;
    private com.youth.banner.Banner mBanner;
    private com.tmall.ultraviewpager.UltraViewPager mUltraViewPager;

    @Override
    public void initView() {
        super.initView();
        mRollViewPager = (cn.appoa.aframework.widget.pager.RollViewPager) findViewById(R.id.mRollViewPager);
        mLinearLayout = (LinearLayout) findViewById(R.id.mLinearLayout);
        mBanner = (com.youth.banner.Banner) findViewById(R.id.mBanner);
        mUltraViewPager = (com.tmall.ultraviewpager.UltraViewPager) findViewById(R.id.mUltraViewPager);
    }

    @Override
    public void initData() {
        getBanner(1);
        getBanner(2);
        getBanner(3);
    }

    private void getBanner(final int type) {
        Map<String, String> params = new HashMap<>();
        params.put("type", String.valueOf(type));//1、附近商家 2、技能街
        ZmVolley.request(new ZmStringRequest(API.posterlist, params, new VolleyDatasListener<BannerBean>
                (this, "广告列表", BannerBean.class) {
            @Override
            public void onDatasResponse(List<BannerBean> datas) {
                if (datas != null && datas.size() > 0) {
                    setBanner(type, datas);
                }
            }
        }, new VolleyErrorListener(this, "广告列表") {
            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
                //TODO 模拟数据
                List<BannerBean> datas = new ArrayList<>();
                datas.add(new BannerBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1568955725471&di=3b22b6cdc58c91464c437f206433d398&imgtype=jpg&src=http%3A%2F%2Fimg2.imgtn.bdimg.com%2Fit%2Fu%3D139570417%2C1338626679%26fm%3D214%26gp%3D0.jpg"));
                datas.add(new BannerBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1568955747593&di=4c662eda1ad71673cb2a1f76c4069eee&imgtype=0&src=http%3A%2F%2Fwww.hubei.gov.cn%2Fmlhb%2Flyms%2F201507%2FW020150703539666970590.jpg"));
                datas.add(new BannerBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1568955757883&di=3a5fd7dc5728dd029aa729011cc9f278&imgtype=0&src=http%3A%2F%2Fimg.alicdn.com%2Fimgextra%2Fi1%2F3166929863%2FTB2Xiygh88lpuFjSspaXXXJKpXa_%2521%25213166929863.jpg"));
                datas.add(new BannerBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1568955768810&di=071a9335916bf7ddfab2a476c15aeb66&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20171106%2F06112e1362464829a36e7734fb2c6d09.jpeg"));
                datas.add(new BannerBean("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1568955782087&di=4657006e40179a1afdab894c70af777e&imgtype=0&src=http%3A%2F%2Fm.tuniucdn.com%2Ffilebroker%2Fcdn%2Fsnc%2F9e%2F7d%2F9e7dafa44f55d361c1877e5c00f5d2b6_w800_h400_c1_t0.jpg"));
                setBanner(type, datas);
            }
        }), REQUEST_TAG);
    }

    private void setBanner(int type, List<BannerBean> banners) {
        if (type == 1) {
            setBanner(mActivity, mRollViewPager, mLinearLayout, banners);
        } else if (type == 2) {
            setBanner(mActivity, mBanner, banners);
        } else if (type == 3) {
            setBanner(mActivity, mUltraViewPager, banners, 10.0f);
        }
    }

    /**
     * 设置轮播图
     *
     * @param context
     * @param mRollViewPager
     * @param mLinearLayout
     * @param banners
     */
    private void setBanner(final Context context, RollViewPager mRollViewPager,
                           LinearLayout mLinearLayout, final List<BannerBean> banners) {
        if (context == null || mRollViewPager == null || mLinearLayout == null) {
            return;
        }
        List<ImageView> imageList = new ArrayList<ImageView>();
        for (int i = 0; i < banners.size(); i++) {
            BannerBean banner = banners.get(i);
            ImageView iv_banner = new ImageView(context);
            iv_banner.setScaleType(ImageView.ScaleType.FIT_XY);
            AfApplication.imageLoader.loadImage(API.IMAGE_URL + banner.Pic, iv_banner);
            final int position = i;
            iv_banner.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO 轮播图点击事件
                    ArrayList<String> images = new ArrayList<>();
                    for (int i = 0; i < banners.size(); i++) {
                        images.add(API.IMAGE_URL + banners.get(i).Pic);
                    }
                    context.startActivity(new Intent(context, ShowBigImageListActivity.class)
                            .putExtra("page", position)
                            .putStringArrayListExtra("images", images));
                }
            });
            imageList.add(iv_banner);
        }
        if (imageList.size() > 0) {
            ZmImageAdapter adapter = new ZmImageAdapter(imageList);
            mRollViewPager.setMyAdapter(adapter);
            mRollViewPager.initPointList(imageList.size(), mLinearLayout);
        }
    }

    /**
     * 设置轮播图
     *
     * @param mBanner
     * @param banners
     */
    private void setBanner(final Context context, Banner mBanner, final List<BannerBean> banners) {
        if (context == null || mBanner == null) {
            return;
        }
        mBanner.setImages(banners)
                .setImageLoader(new BannerImageLoader(R.layout.item_banner))//图片加载
                .setOnBannerListener(new OnBannerListener() {

                    @Override
                    public void OnBannerClick(int position) {
                        // TODO 轮播图点击事件
                        ArrayList<String> images = new ArrayList<>();
                        for (int i = 0; i < banners.size(); i++) {
                            images.add(API.IMAGE_URL + banners.get(i).Pic);
                        }
                        context.startActivity(new Intent(context, ShowBigImageListActivity.class)
                                .putExtra("page", position)
                                .putStringArrayListExtra("images", images));
                    }
                }).start();
    }

    /**
     * 轮播图加载图片
     */
    public class BannerImageLoader implements ImageLoaderInterface<View> {

        private int layoutId;

        public BannerImageLoader() {
            super();
            this.layoutId = R.layout.item_banner;
        }

        public BannerImageLoader(int layoutId) {
            super();
            this.layoutId = layoutId;
        }

        @Override
        public View createImageView(Context context) {
            return View.inflate(context, layoutId, null);
        }

        @Override
        public void displayImage(Context context, Object obj, View view) {
            BannerBean banner = (BannerBean) obj;
            ImageView iv_banner = (ImageView) view.findViewById(R.id.iv_banner);
            AfApplication.imageLoader.loadImage(API.IMAGE_URL + banner.Pic, iv_banner);
        }

    }


    /**
     * 设置轮播图
     *
     * @param context
     * @param ultraViewPager
     * @param banners
     * @param bottom
     */
    private void setBanner(final Context context, UltraViewPager ultraViewPager,
                           final List<BannerBean> banners,
                           float bottom) {
        if (context == null || ultraViewPager == null) {
            return;
        }
        if (banners != null && banners.size() > 0) {
            final ArrayList<String> images = new ArrayList<>();
            for (int i = 0; i < banners.size(); i++) {
                images.add(API.IMAGE_URL + banners.get(i).Pic);
            }
            ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
            // UltraPagerAdapter 绑定子view到UltraViewPager
            ultraViewPager.setAdapter(new PagerAdapter() {

                @Override
                public boolean isViewFromObject(View view, Object object) {
                    // 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
                    return view == object;
                }

                @Override
                public int getCount() {
                    // 获取要滑动的控件的数量，在这里我们以滑动的广告栏为例，那么这里就应该是展示的广告图片的ImageView数量
                    return banners.size();
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    // PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
                    LinearLayout view = (LinearLayout) object;
                    container.removeView(view);
                }

                @Override
                public Object instantiateItem(ViewGroup container, final int position) {
                    // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
                    LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(container.getContext())
                            .inflate(R.layout.item_banner, container, false);
                    ImageView imageView = (ImageView) linearLayout.findViewById(R.id.iv_banner);
                    final BannerBean banner = banners.get(position);
                    MyApplication.imageLoader.loadImage(API.IMAGE_URL + banner.Pic, imageView);
                    imageView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO 轮播图点击事件
                            context.startActivity(new Intent(context, ShowBigImageListActivity.class)
                                    .putExtra("page", position)
                                    .putStringArrayListExtra("images", images));
                        }
                    });
                    container.addView(linearLayout);
                    return linearLayout;
                }
            });
            // 内置indicator初始化
            ultraViewPager.initIndicator();
            // 设置indicator样式
            ultraViewPager.getIndicator()//
                    .setOrientation(UltraViewPager.Orientation.HORIZONTAL)//
                    .setFocusColor(0xffffffff)//
                    .setNormalColor(0x30000000)//
                    .setIndicatorPadding(AtyUtils.dip2px(context, 5.0f))//
                    .setMargin(0, 0, 0, AtyUtils.dip2px(context, bottom))//
                    .setRadius(8);
            // 设置indicator对齐方式
            ultraViewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
            // 构造indicator,绑定到UltraViewPager
            ultraViewPager.getIndicator().build();
            // 设定页面循环播放
            ultraViewPager.setInfiniteLoop(true);
            // 设定页面自动切换 间隔5秒
            ultraViewPager.setAutoScroll(5000);
        }
    }
}
