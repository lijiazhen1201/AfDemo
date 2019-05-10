package cn.appoa.aframework.widget.pager;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;

import cn.appoa.aframework.utils.AtyUtils;

/**
 * 自动适应图片高度的ViewPager
 */
public class HeightWrapViewPager extends ViewPager {

    public HeightWrapViewPager(Context context) {
        super(context);
    }

    public HeightWrapViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 轮播图
     */
    private ArrayList<String> images;

    /**
     * 默认高度
     */
    private int defaultheight;

    /**
     * 设置轮播图
     *
     * @param images
     */
    public void setBanner(ArrayList<String> images) {
        this.images = images;
        if (images != null && images.size() > 0) {
            Glide.with(getContext()).load(images.get(0))
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation
                                <? super Bitmap> glideAnimation) {
                            float scale = (float) resource.getHeight() / resource.getWidth();
                            defaultheight = (int) (scale * AtyUtils.getScreenWidth(getContext()));
                            ViewGroup.LayoutParams params = getLayoutParams();
                            params.height = defaultheight;
                            setLayoutParams(params);
                            initViewPager();
                        }
                    });
        }
    }

    /**
     * 所有图片的高度
     */
    private int[] imgheights;

    /**
     * 获取第一张图片高度后，给viewpager设置adapter
     */
    private void initViewPager() {
        setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {
                if (imgheights == null || imgheights.length != images.size()) {
                    imgheights = null;
                    imgheights = new int[images.size()];
                }
                return images.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                final ImageView imageView = new ImageView(getContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onPageSelectedListener != null) {
                            onPageSelectedListener.onClick(position, images, v);
                        }
                    }
                });
                Glide.with(getContext()).load(images.get(position))
                        .asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new ImageViewTarget<Bitmap>(imageView) {
                            @Override
                            protected void setResource(Bitmap loadedImage) {
                                if (loadedImage != null) {
                                    float scale = (float) loadedImage.getHeight() / loadedImage.getWidth();
                                    imgheights[position] = (int) (scale * AtyUtils.getScreenWidth(getContext()));
                                    imageView.setImageBitmap(loadedImage);
                                }
                            }
                        });
                container.addView(imageView);
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
        //监听
        addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == imgheights.length - 1) {
                    return;
                }

                //计算ViewPager现在应该的高度,heights[]表示页面高度的数组。
                int height = (int) ((imgheights[position] == 0 ? defaultheight : imgheights[position])
                        * (1 - positionOffset) +
                        (imgheights[position + 1] == 0 ? defaultheight : imgheights[position + 1])
                                * positionOffset);

                //为ViewPager设置高度
                ViewGroup.LayoutParams params = getLayoutParams();
                params.height = height;
                setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                if (onPageSelectedListener != null) {
                    onPageSelectedListener.onPageSelected(position, images);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private OnPageSelectedListener onPageSelectedListener;

    public void setOnPageSelectedListener(OnPageSelectedListener onPageSelectedListener) {
        this.onPageSelectedListener = onPageSelectedListener;
    }

    public interface OnPageSelectedListener {

        void onClick(int position, ArrayList<String> images, View v);

        void onPageSelected(int position, ArrayList<String> images);

    }

}
