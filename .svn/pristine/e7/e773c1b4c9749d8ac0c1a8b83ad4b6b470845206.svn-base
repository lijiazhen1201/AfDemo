package cn.appoa.afdemo.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.appoa.afdemo.R;
import cn.appoa.afdemo.bean.RefreshBean;
import cn.appoa.aframework.adapter.ZmAdapter;
import cn.appoa.aframework.adapter.ZmHolder;
import cn.appoa.aframework.utils.AtyUtils;

public class RefreshBeanAdapter extends ZmAdapter<RefreshBean> {

    public RefreshBeanAdapter(Context mContext, List<RefreshBean> itemList, int layoutId) {
        super(mContext, itemList, layoutId);
    }

    @Override
    public void setList(List<RefreshBean> itemList) {
        super.setList(itemList);
        notifyDataSetChanged();
    }

    @Override
    public void init(ZmHolder zmHolder, final RefreshBean t, int position) {
        TextView tv_refresh_id = zmHolder.getView(R.id.tv_refresh_id);
        TextView tv_refresh_title = zmHolder.getView(R.id.tv_refresh_title);
        if (t != null) {
            tv_refresh_id.setText("ID:" + t.ID);
            tv_refresh_title.setText(t.Title);
            zmHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AtyUtils.showShort(mContext, t.Title, true);
                }
            });
        }
    }
}
