package cn.appoa.afdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseActivity;
import cn.appoa.afdemo.bean.GoodsCategoryList;
import cn.appoa.afdemo.bean.GoodsDetails;
import cn.appoa.afdemo.bean.GoodsSpecItemList;
import cn.appoa.afdemo.bean.GoodsTypeList;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;
import cn.appoa.aframework.utils.AtyUtils;

/**
 * Json解析工具类
 */
public class JsonUtilsActivity extends BaseActivity
        implements View.OnClickListener {

    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(this).setTitle("JsonUtils")
                .setBackImage(R.drawable.back_white).create();
    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        setContent(R.layout.activity_json_utils);
    }

    private Button btn_json_to_bean;
    private Button btn_bean_to_json;
    private Button btn_json_to_list;
    private Button btn_list_to_json;
    private TextView tv_result;

    @Override
    public void initView() {
        super.initView();
        btn_json_to_bean = (Button) findViewById(R.id.btn_json_to_bean);
        btn_bean_to_json = (Button) findViewById(R.id.btn_bean_to_json);
        btn_json_to_list = (Button) findViewById(R.id.btn_json_to_list);
        btn_list_to_json = (Button) findViewById(R.id.btn_list_to_json);
        tv_result = (TextView) findViewById(R.id.tv_result);
    }

    private String json1;
    private String json2;

    @Override
    public void initData() {
        json1 = AtyUtils.getJsonString(mActivity, "goods.json");
        json2 = AtyUtils.getJsonString(mActivity, "spec.json");
        btn_json_to_bean.setOnClickListener(this);
        btn_bean_to_json.setOnClickListener(this);
        btn_json_to_list.setOnClickListener(this);
        btn_list_to_json.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String result = "";
        switch (v.getId()) {
            case R.id.btn_json_to_bean:
                GoodsDetails data = JSON.parseObject(json1, GoodsDetails.class);
                if (data != null) {
                    result = data.Name;
                }
                break;
            case R.id.btn_bean_to_json:
                GoodsCategoryList category = new GoodsCategoryList();
                category.ID = 1;
                category.Name = "商品分类";
                result = JSON.toJSONString(category);
                break;
            case R.id.btn_json_to_list:
                List<GoodsSpecItemList> datas = JSON.parseArray(json2, GoodsSpecItemList.class);
                if (datas != null && datas.size() > 0) {
                    for (int i = 0; i < datas.size(); i++) {
                        result = result + datas.get(i).Name + "\n";
                    }
                }
                break;
            case R.id.btn_list_to_json:
                List<GoodsTypeList> typeList = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    GoodsTypeList type = new GoodsTypeList();
                    type.ID = i + 1;
                    type.Name = "商品类型" + (i + 1);
                    typeList.add(type);
                }
                result = JSON.toJSONString(typeList);
                break;
        }
        tv_result.setText(result);
    }

}
