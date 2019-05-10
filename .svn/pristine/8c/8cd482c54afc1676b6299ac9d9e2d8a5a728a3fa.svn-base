package cn.appoa.aframework.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import zm.bus.event.BusProvider;

public class ZmViewAdapter extends PagerAdapter {

    private List<View> viewList;

    public ZmViewAdapter(List<View> viewList) {
        super();
        this.viewList = viewList;
        BusProvider.getInstance().register(this);
    }

    @Override
    public int getCount() {
        if (viewList == null) {
            return 0;
        } else {
            return viewList.size();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (viewList == null) {
            return super.instantiateItem(container, position);
        } else {
            View v = viewList.get(position);
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
            container.addView(viewList.get(position));
            return viewList.get(position);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (container == null) {
            super.destroyItem(container, position, object);
        } else {
            container.removeView((View) object);
        }
    }

}
