package cn.appoa.aframework.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.appoa.aframework.R;
import cn.appoa.aframework.adapter.ZmPagerAdapter;
import cn.appoa.aframework.presenter.BasePresenter;
import cn.appoa.aframework.utils.AtyUtils;
import cn.appoa.aframework.widget.noscroll.NoScrollViewPager;

public abstract class TabLayoutFragment<P extends BasePresenter> extends AfFragment<P> {

    @Override
    public View initFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_layout, container,false);
    }

    protected TabLayout tabLayout;
    protected NoScrollViewPager viewPager;

    @Override
    public void initView(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (NoScrollViewPager) view.findViewById(R.id.viewPager);
        initTabLayout(tabLayout);
    }

    protected List<String> listTitle;
    protected List<Fragment> listFragment;

    @Override
    public void initData() {
        listTitle = initTitle();
        listFragment = initFragment();
        if (listTitle != null && listFragment != null && listTitle.size() == listFragment.size()) {
            viewPager.setAdapter(new ZmPagerAdapter(mFragmentManager, listFragment, listTitle));
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.post(new Runnable() {
                @Override
                public void run() {
                    AtyUtils.setTabLayoutIndicator(tabLayout, 48, 48);
                }
            });
        }
    }

    /**
     * TabLayout 相关设置
     *
     * @param tabLayout
     */
    protected abstract void initTabLayout(TabLayout tabLayout);

    /**
     * 标题
     *
     * @return
     */
    protected abstract List<String> initTitle();

    /**
     * Fragment
     *
     * @return
     */
    protected abstract List<Fragment> initFragment();
}
