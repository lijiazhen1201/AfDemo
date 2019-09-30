package cn.appoa.afdemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.bean.GoodsDetails;
import cn.appoa.afdemo.bean.GoodsSpecItemChildList;
import cn.appoa.afdemo.bean.GoodsSpecItemList;
import cn.appoa.afdemo.bean.GoodsSpecTypeList;
import cn.appoa.afdemo.net.API;
import cn.appoa.aframework.adapter.ZmAdapter;
import cn.appoa.aframework.adapter.ZmHolder;
import cn.appoa.aframework.app.AfApplication;
import cn.appoa.aframework.dialog.BaseDialog;
import cn.appoa.aframework.listener.OnCallbackListener;
import cn.appoa.aframework.utils.AtyUtils;
import cn.appoa.aframework.widget.flowlayout.FlowLayout;
import cn.appoa.aframework.widget.flowlayout.TagAdapter;
import cn.appoa.aframework.widget.flowlayout.TagFlowLayout;


public class GoodsSpecDialog extends BaseDialog {

    public GoodsSpecDialog(Context context, OnCallbackListener onCallbackListener) {
        super(context, onCallbackListener);
    }

    private ImageView iv_goods_cover;
    private TextView tv_goods_name;
    private TextView tv_goods_price;
    private TextView tv_goods_spec;
    private ListView lv_goods_spec;
    private ImageView iv_jian;
    private TextView tv_goods_count;
    private ImageView iv_jia;
    private TextView tv_dialog_confirm;

    @Override
    public Dialog initDialog(Context context) {
        View view = View.inflate(context, R.layout.dialog_goods_spec, null);
        iv_goods_cover = (ImageView) view.findViewById(R.id.iv_goods_cover);
        tv_goods_name = (TextView) view.findViewById(R.id.tv_goods_name);
        tv_goods_price = (TextView) view.findViewById(R.id.tv_goods_price);
        tv_goods_spec = (TextView) view.findViewById(R.id.tv_goods_spec);
        lv_goods_spec = (ListView) view.findViewById(R.id.lv_goods_spec);
        iv_jian = (ImageView) view.findViewById(R.id.iv_jian);
        tv_goods_count = (TextView) view.findViewById(R.id.tv_goods_count);
        iv_jia = (ImageView) view.findViewById(R.id.iv_jia);
        tv_dialog_confirm = (TextView) view.findViewById(R.id.tv_dialog_confirm);
        //点击事件
        iv_jian.setOnClickListener(this);
        iv_jia.setOnClickListener(this);
        tv_dialog_confirm.setOnClickListener(this);
        return initBottomInputMethodDialog(view, context);
    }

    /**
     * 0未知1加入购物车2立即购买
     */
    private int type;

    /**
     * 商品数量
     */
    private long count = 1;

    /**
     * 商品规格
     */
    private GoodsSpecTypeList standardType;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_jian://减少
                if (count > 1) {
                    count--;
                    tv_goods_count.setText(String.valueOf(count));
                }
                break;
            case R.id.iv_jia://增加
                count++;
                tv_goods_count.setText(String.valueOf(count));
                break;
            case R.id.tv_dialog_confirm://确定
                if (standardType == null) {
                    AtyUtils.showShort(context, "请选择规格", false);
                    return;
                }
                if (standardType.Stock < count) {
                    AtyUtils.showShort(context, "库存不足", true);
                    return;
                }
                if (onCallbackListener != null) {
                    onCallbackListener.onCallback(type, count, standardType);
                }
                dismissDialog();
                break;
        }
    }

    /**
     * 商品详情
     */
    private GoodsDetails dataBean;

    /**
     * 显示弹窗（初次）
     *
     * @param data
     */
    public void showGoodsSpecDialog(GoodsDetails data) {
        dataBean = data;
        if (dataBean != null) {
            tv_goods_name.setText(dataBean.Name);
            AfApplication.imageLoader.loadImage(API.IMAGE_URL + dataBean.Pic, iv_goods_cover);
            tv_goods_price.setText("¥ " + AtyUtils.get2Point(dataBean.Price));
            if (dataBean.SpecTypeList != null && dataBean.SpecTypeList.size() > 0) {
                //默认选中第一个
                setSpecTypeList(dataBean.SpecTypeList.get(0));
                for (int i = 0; i < dataBean.SpecItemList.size(); i++) {
                    GoodsSpecItemList itemList = dataBean.SpecItemList.get(i);
                    for (int j = 0; j < itemList.ChildList.size(); j++) {
                        GoodsSpecItemChildList child = itemList.ChildList.get(j);
                        if (child.ID == standardType.SpecOne || child.ID == standardType.SpecTwo) {
                            child.isSelected = true;
                        }
                    }
                }
                //规格
                lv_goods_spec.setAdapter(new GoodsSpecItemListAdapter(context, dataBean.SpecItemList));
            }
        }
        showDialog();
    }

    public class GoodsSpecItemListAdapter extends ZmAdapter<GoodsSpecItemList> {

        public GoodsSpecItemListAdapter(Context mContext, List<GoodsSpecItemList> itemList) {
            super(mContext, itemList);
        }

        @Override
        public void setList(List<GoodsSpecItemList> itemList) {
            super.setList(itemList);
            notifyDataSetChanged();
        }

        @Override
        public int setLayout() {
            return R.layout.item_goods_spec_item_list;
        }

        @Override
        public void init(ZmHolder zmHolder, final GoodsSpecItemList t, int position) {
            TextView mTextView = zmHolder.getView(R.id.mTextView);
            TagFlowLayout mTagFlowLayout = zmHolder.getView(R.id.mTagFlowLayout);
            mTagFlowLayout.setMaxSelectCount(1);
            if (t != null) {
                mTextView.setText(t.Name);
                mTagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {

                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        for (int i = 0; i < t.ChildList.size(); i++) {
                            t.ChildList.get(i).isSelected = false;
                        }
                        t.ChildList.get(position).isSelected = true;
                        notifyGoodsSpecTypeList();
                        return true;
                    }
                });
                if (t.ChildList != null && t.ChildList.size() > 0) {
                    List<String> list = new ArrayList<>();
                    int poses = -1;
                    for (int i = 0; i < t.ChildList.size(); i++) {
                        GoodsSpecItemChildList item = t.ChildList.get(i);
                        if (item.isSelected) {
                            poses = i;
                        }
                        list.add(item.Name);
                    }
                    GoodsSpecItemChildListAdapter adapter = new GoodsSpecItemChildListAdapter(context, list);
                    if (poses != -1) {
                        adapter.setOnDataChangedListener(new TagAdapter.OnDataChangedListener() {

                            @Override
                            public void onChanged() {

                            }
                        });
                        adapter.setSelectedList(poses);
                    }
                    mTagFlowLayout.setAdapter(adapter);
                }
            }
        }
    }

    public class GoodsSpecItemChildListAdapter extends TagAdapter<String> {

        public GoodsSpecItemChildListAdapter(Context context, List<String> datas) {
            super(context, datas);
        }

        public GoodsSpecItemChildListAdapter(Context context, String[] datas) {
            super(context, datas);
        }

        @Override
        public View getView(FlowLayout parent, int position, String t) {
            TextView tv = (TextView) mInflater.inflate(
                    R.layout.item_goods_spec_item_child_list, parent, false);
            tv.setText(t);
            return tv;
        }
    }

    /**
     * 更新规格
     */
    private void notifyGoodsSpecTypeList() {
        setSpecTypeList(null);
        GoodsSpecTypeList specType = null;
        if (dataBean != null) {
            // 最多两级规格
            int[] spec_ids = new int[]{-1, -1};
            if (dataBean.SpecItemList != null && dataBean.SpecItemList.size() > 0) {
                for (int i = 0; i < dataBean.SpecItemList.size(); i++) {
                    GoodsSpecItemList spec = dataBean.SpecItemList.get(i);
                    if (spec.ChildList != null && spec.ChildList.size() > 0) {
                        for (int j = 0; j < spec.ChildList.size(); j++) {
                            GoodsSpecItemChildList item = spec.ChildList.get(j);
                            if (item.isSelected) {
                                spec_ids[i] = item.ID;
                                continue;
                            }
                        }
                    }
                }
                if (dataBean.SpecItemList.size() == 1) {
                    // 只有一种规格时，默认第二种为0
                    spec_ids[1] = 0;
                }
            }
            // 选中的规格
            if (dataBean.SpecTypeList != null && dataBean.SpecTypeList.size() > 0) {
                for (int i = 0; i < dataBean.SpecTypeList.size(); i++) {
                    GoodsSpecTypeList info = dataBean.SpecTypeList.get(i);
                    if (spec_ids[0] == info.SpecOne && spec_ids[1] == info.SpecTwo) {
                        specType = info;
                        break;
                    } else {
                        if (spec_ids[0] == info.SpecTwo && spec_ids[1] == info.SpecOne) {
                            specType = info;
                            break;
                        }
                    }
                }
            }
        }
        if (specType != null) {
            setSpecTypeList(specType);
        }
    }

    /**
     * 设置规格
     *
     * @param specType
     */
    private void setSpecTypeList(GoodsSpecTypeList specType) {
        standardType = specType;
        if (standardType == null) {
            AfApplication.imageLoader.loadImage(API.IMAGE_URL + dataBean.Pic, iv_goods_cover);
            tv_goods_price.setText("¥ " + AtyUtils.get2Point(dataBean.Price));
            tv_goods_spec.setText(null);
        } else {
            AfApplication.imageLoader.loadImage(API.IMAGE_URL + standardType.Pic, iv_goods_cover);
            tv_goods_price.setText("¥ " + AtyUtils.get2Point(standardType.Price));
            tv_goods_spec.setText("已选：" + standardType.PropertiesName);
        }
    }

}
