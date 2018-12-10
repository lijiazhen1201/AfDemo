package cn.appoa.aframework.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

import zm.bus.event.BusProvider;

/**
 * ViewPager中放Fragment的适配器
 */
public class ZmPagerAdapter extends FragmentPagerAdapter {

    protected List<Fragment> listFragment;
    protected List<String> listTitle;

    public ZmPagerAdapter(FragmentManager fm) {
        super(fm);
        BusProvider.getInstance().register(this);
    }

    public ZmPagerAdapter(FragmentManager fm, List<Fragment> listFragment) {
        super(fm);
        this.listFragment = listFragment;
        BusProvider.getInstance().register(this);
    }

    public ZmPagerAdapter(FragmentManager fm, List<Fragment> listFragment, List<String> listTitle) {
        super(fm);
        this.listFragment = listFragment;
        this.listTitle = listTitle;
        BusProvider.getInstance().register(this);
    }

    @Override
    public Fragment getItem(int position) {
        if (listFragment == null) {
            return null;
        } else {
            return listFragment.get(position);
        }
    }

    @Override
    public int getCount() {
        if (listFragment == null) {
            return 0;
        } else {
            return listFragment.size();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (listTitle == null) {
            return "";
        } else {
            return listTitle.get(position);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container, position, object);
    }

    public void setPageTitle(int position, String title) {
        if (listTitle != null && position >= 0 && position < listTitle.size()) {
            listTitle.set(position, title);
            notifyDataSetChanged();
        }
    }
}
