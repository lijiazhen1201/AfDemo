package cn.appoa.afdemo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.app.MyApplication;
import cn.appoa.afdemo.base.BaseActivity;
import cn.appoa.aframework.bean.BMapApi;
import cn.appoa.aframework.bean.CityList;
import cn.appoa.aframework.bean.ProvinceList;
import cn.appoa.aframework.utils.AtyUtils;
import cn.appoa.aframework.utils.SpannableStringUtils;
import cn.appoa.aframework.widget.side.PinyinComparator;
import cn.appoa.aframework.widget.side.SideBar;
import cn.appoa.aframework.widget.side.Sort;
import cn.appoa.aframework.widget.side.SortAdapter;
import cn.appoa.aframework.widget.side.SortBaseAdapter;

/**
 * 字母表导航，城市选择
 */
public class SideBarActivity extends BaseActivity
        implements SideBar.OnPressDownLetterListener, TextView.OnEditorActionListener,
        View.OnClickListener, SortBaseAdapter.OnSortClickListener {

//    @Override
//    public BaseTitlebar initTitlebar() {
//        return new DefaultTitlebar.Builder(this).setTitle("SideBar")
//                .setBackImage(R.drawable.back_white).create();
//    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        setContent(R.layout.activity_side_bar);
    }

    private EditText et_search;
    private TextView tv_search;
    private TextView tv_city;
    private ListView mListView;
    private TextView mTextView;
    private SideBar mSideBar;

    @Override
    public void initView() {
        super.initView();
        AtyUtils.setPaddingTop(mActivity, findViewById(R.id.ll_search));
        et_search = (EditText) findViewById(R.id.et_search);
        tv_search = (TextView) findViewById(R.id.tv_search);
        tv_city = (TextView) findViewById(R.id.tv_city);
        mListView = (ListView) findViewById(R.id.mListView);
        mTextView = (TextView) findViewById(R.id.mTextView);
        mSideBar = (SideBar) findViewById(R.id.mSideBar);
        //当前城市
        tv_city.setText(SpannableStringUtils.getBuilder("当前城市：")
                .append(MyApplication.local_city_name)
                .setForegroundColor(ContextCompat.getColor(mActivity, R.color.colorText))
                .create());
        //设置字母表
        mSideBar.setDialogView(mTextView);
        mSideBar.setLetterColor(ContextCompat.getColor(mActivity, R.color.colorTheme),
                ContextCompat.getColor(mActivity, R.color.colorWhite));
        mSideBar.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorTransparent),
                ContextCompat.getColor(mActivity, R.color.colorTranslucence));
        mSideBar.setOnPressDownLetterListener(this);
        //事件监听
        et_search.setOnEditorActionListener(this);
        tv_search.setOnClickListener(this);
        mListView.setOnTouchListener(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });
    }

    @Override
    public void initData() {
        //TODO 模拟数据
        List<CityList> list = new ArrayList<>();
        List<ProvinceList> provinceList = BMapApi.getProvinceList(mActivity);
        for (int i = 0; i < provinceList.size(); i++) {
            ProvinceList province = provinceList.get(i);
            String provinceName = province.area_name;
            if (provinceName.startsWith("北京") || provinceName.startsWith("重庆") ||
                    provinceName.startsWith("上海") || provinceName.startsWith("天津") ||
                    provinceName.startsWith("香港") || provinceName.startsWith("澳门")) {
                CityList city = new CityList();
                city.area_code = province.area_code;
                city.area_name = provinceName;
                city.area_type = province.area_type;
                city.geo = province.geo;
                list.add(city);
            } else {
                list.addAll(province.sub);
            }
        }
        setCityList(list);
    }

    private List<Sort> sortList;
    private SortBaseAdapter adapter;

    /**
     * 设置城市
     *
     * @param cityList
     */
    private void setCityList(List<CityList> cityList) {
        sortList = new ArrayList<>();
        if (cityList != null && cityList.size() > 0) {
            for (int i = 0; i < cityList.size(); i++) {
                CityList c = cityList.get(i);
                Sort s = new Sort(c.area_name);
                s.setCustomInfo("id", c.area_code);
                if (!TextUtils.isEmpty(s.name) && s.name.startsWith("重庆")) {
                    s.setInitialLetter("C");
                }
                sortList.add(s);
            }
        }
        Collections.sort(sortList, new PinyinComparator());
        adapter = new SortAdapter(mActivity, sortList);
        adapter.setOnSortClickListener(this);
        mListView.setAdapter(adapter);
    }

    @Override
    public void onPressDownLetter(int index, String letter) {
        // 该字母首次出现的位置
        if (TextUtils.equals(letter, "↑")) {
            // 置顶
            if (mListView != null) {
                mListView.setSelection(0);
            }
        } else {
            int position = -1;
            if (adapter != null)
                position = adapter.getPositionForSection(letter.charAt(0));
            if (position != -1 && mListView != null)
                mListView.setSelection(position + mListView.getHeaderViewsCount());
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getId() == R.id.et_search && actionId == EditorInfo.IME_ACTION_SEARCH) {
            onClick(tv_search);
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_search) {
            //发起搜索
            String key = AtyUtils.getText(et_search);
            hideSoftKeyboard();
            if (adapter != null) {
                adapter.searchData(key);
            }
        }
    }

    @Override
    public void onSortClick(int position, Sort sort) {
        //点击事件
        MyApplication.local_city_id = (String) sort.getCustomInfo("id");
        MyApplication.local_city_name = sort.name;
        setResult(RESULT_OK, new Intent()
                .putExtra("city", sort.name));
        finish();
    }

}
