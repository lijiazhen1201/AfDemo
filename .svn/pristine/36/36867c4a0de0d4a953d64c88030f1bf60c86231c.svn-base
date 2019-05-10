package cn.appoa.wximageselector;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import cn.appoa.wximageselector.photoview.OnViewTapListener;
import cn.appoa.wximageselector.photoview.PhotoView;

/**
 * 大图预览
 */
public class ShowBigImageListActivity extends AppCompatActivity {

    private ViewPager vp_image;
    private TextView tv_image_page;
    private int pageMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_show_big_image_list);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        vp_image = (ViewPager) findViewById(R.id.vp_image);
        tv_image_page = (TextView) findViewById(R.id.tv_image_page);

        Intent intent = getIntent();
        int page = intent.getIntExtra("page", 0);
        ArrayList<String> images = intent.getStringArrayListExtra("images");
        if (images != null && images.size() > 0) {
            pageMax = images.size();
            vp_image.setOffscreenPageLimit(pageMax);
            tv_image_page.setText("1/" + pageMax);
            PhotoAdapter adapter = new PhotoAdapter(images);
            vp_image.setAdapter(adapter);
            vp_image.addOnPageChangeListener(new OnPageChangeListener() {

                @Override
                public void onPageSelected(int arg0) {
                    tv_image_page.setText(arg0 + 1 + "/" + pageMax);
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                }
            });
            vp_image.setCurrentItem(page);
        }
    }

    public class PhotoAdapter extends PagerAdapter {

        private List<String> list;

        public PhotoAdapter(List<String> list) {
            super();
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            String path = list.get(position);
            if (!TextUtils.isEmpty(path) && path.toLowerCase().contains(".gif")) {
                Glide.with(ShowBigImageListActivity.this).load(path).asGif()//
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(photoView);
            } else {
                Glide.with(ShowBigImageListActivity.this).load(path).asBitmap()//
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(photoView);
            }
            photoView.setOnViewTapListener(new OnViewTapListener() {

                @Override
                public void onViewTap(View view, float x, float y) {
                    finish();
                }
            });
            // Now just add PhotoView to ViewPager and return it
            ((ViewPager) container).addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // ((ViewPager) container).removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
