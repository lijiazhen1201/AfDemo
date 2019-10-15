package cn.appoa.afdemo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.base.BaseActivity;
import cn.appoa.afdemo.bean.GoodsDetails;
import cn.appoa.afdemo.bean.GoodsSpecTypeList;
import cn.appoa.afdemo.dialog.GoodsSpecDialog;
import cn.appoa.aframework.listener.OnCallbackListener;
import cn.appoa.aframework.titlebar.BaseTitlebar;
import cn.appoa.aframework.titlebar.DefaultTitlebar;
import cn.appoa.aframework.utils.AtyUtils;
import cn.appoa.aframework.widget.flowlayout.FlowLayout;
import cn.appoa.aframework.widget.flowlayout.TagAdapter;

/**
 * 自动换行布局，流式布局
 */
public class FlowLayoutActivity extends BaseActivity {

    @Override
    public BaseTitlebar initTitlebar() {
        return new DefaultTitlebar.Builder(this).setTitle("FlowLayout")
                .setBackImage(R.drawable.back_white).create();
    }

    @Override
    public void initContent(Bundle savedInstanceState) {
        setContent(R.layout.activity_flow_layout);
    }

    private cn.appoa.aframework.widget.flowlayout.TagFlowLayout tagLayout;
    private TextView tv_goods_spec;

    @Override
    public void initView() {
        super.initView();
        tagLayout = (cn.appoa.aframework.widget.flowlayout.TagFlowLayout) findViewById(R.id.tagLayout);
        tv_goods_spec = (TextView) findViewById(R.id.tv_goods_spec);
    }

    @Override
    public void initData() {
        getHotSearch();
        getGoodsDetails();
    }

    /**
     * 热门搜索
     */
    private void getHotSearch() {
        //TODO 模拟数据
        List<String> datas = new ArrayList<>();
        datas.add("小米曲面电视");
        datas.add("火锅");
        datas.add("自助餐");
        datas.add("美食");
        datas.add("2019最新女装");
        datas.add("洗衣液");
        datas.add("快捷酒店");
        datas.add("iPhone11");
        datas.add("华为荣耀8X");
        datas.add("游戏键盘鼠标");
        tagLayout.setAdapter(new TagAdapter<String>(mActivity, datas) {

            @Override
            public View getView(FlowLayout parent, int position, final String t) {
                TextView tv = (TextView) View.inflate(context, R.layout.item_hot_search, null);
                tv.setText(t);
                tv.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO 发起搜索
                    }
                });
                return tv;
            }
        });
    }

    /**
     * 商品详情
     */
    private GoodsDetails dataBean;

    /**
     * 选择规格弹窗
     */
    private GoodsSpecDialog dialogSpec;

    /**
     * 获取商品详情
     */
    private void getGoodsDetails() {
        String goods = AtyUtils.getJsonString(mActivity, "goods.json");
        if (!TextUtils.isEmpty(goods)) {
            dataBean = JSON.parseObject(goods, GoodsDetails.class);
            if (dataBean != null) {
                //默认选中第一个
                if (dataBean.SpecTypeList != null && dataBean.SpecTypeList.size() > 0) {
                    setSpecTypeList(1, dataBean.SpecTypeList.get(0));
                }
                //选择规格弹窗
                tv_goods_spec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialogSpec == null) {
                            dialogSpec = new GoodsSpecDialog(mActivity, new OnCallbackListener() {
                                @Override
                                public void onCallback(int type, Object... obj) {
                                    setSpecTypeList((long) obj[0], (GoodsSpecTypeList) obj[1]);
                                }
                            });
                            dialogSpec.showGoodsSpecDialog(dataBean);
                        } else {
                            dialogSpec.showDialog();
                        }
                    }
                });
            }
        }
    }

    /**
     * 商品数量
     */
    private long goodsCount;

    /**
     * 商品规格
     */
    private GoodsSpecTypeList specType;

    /**
     * 设置商品规格
     *
     * @param count
     * @param data
     */
    private void setSpecTypeList(long count, GoodsSpecTypeList data) {
        goodsCount = count;
        specType = data;
        if (specType != null) {
            tv_goods_spec.setText(specType.PropertiesName);
        }
    }

}
