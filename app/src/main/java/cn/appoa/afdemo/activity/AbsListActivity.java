package cn.appoa.afdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.divider.ListItemDecoration;

import java.util.Arrays;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseActivity;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;

/**
 * 单个列表Activity
 */
public abstract class AbsListActivity extends BaseActivity {

    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(this).setTitle(initTitle())
                .setBackImage(R.drawable.back_white).create();
    }

    /**
     * 标题
     *
     * @return
     */
    protected abstract CharSequence initTitle();

    protected RecyclerView recyclerView;

    @Override
    public void initContent(Bundle savedInstanceState) {
        recyclerView = new RecyclerView(mActivity);
        setContent(recyclerView);
    }

    @Override
    public void initView() {
        super.initView();
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new ListItemDecoration(mActivity));
    }

    @Override
    public void initData() {
        final String[] titles = initTitles();
        final Class[] clazzs = initClass();
        recyclerView.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>
                (R.layout.item_main, Arrays.asList(titles)) {

            @Override
            protected void convert(BaseViewHolder helper, String item) {
                final int position = helper.getLayoutPosition();
                helper.setText(R.id.tv_main, item);
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            startActivity(new Intent(mActivity, clazzs[position]));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    /**
     * 栏目
     *
     * @return
     */
    protected abstract String[] initTitles();

    /**
     * 跳转的页面
     *
     * @return
     */
    protected abstract Class[] initClass();

}
