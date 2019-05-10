package com.cjt2325.cameralibrary.local;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cjt2325.cameralibrary.R;

import java.util.List;

public class LocalVideoAdapter extends BaseAdapter {

    private Context mContext;
    private List<LocalVideo> itemList;

    public LocalVideoAdapter(Context mContext, List<LocalVideo> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList == null ? 0 : itemList.size();
    }

    @Override
    public LocalVideo getItem(int position) {
        return itemList == null ? null : itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LocalVideoHolder holder = null;
        if (convertView == null) {
            holder = new LocalVideoHolder();
            convertView = View.inflate(mContext, R.layout.item_local_video, null);
            holder.iv_local_video_img = convertView.findViewById(R.id.iv_local_video_img);
            holder.iv_local_video_logo = convertView.findViewById(R.id.iv_local_video_logo);
            convertView.setTag(holder);
        } else {
            holder = (LocalVideoHolder) convertView.getTag();
        }
        LocalVideo item = getItem(position);
        if (item != null) {
            holder.iv_local_video_img.setImageBitmap(item.thumbnail);
        }
        return convertView;
    }


    class LocalVideoHolder {
        ImageView iv_local_video_img;
        ImageView iv_local_video_logo;
    }
}
