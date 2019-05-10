package cn.appoa.aframework.widget.side;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.appoa.aframework.R;

public class SortAdapter extends SortBaseAdapter {

    public SortAdapter(Context context, List<Sort> sortList) {
        super(context, sortList);
    }

    @Override
    public View initView(final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(context, R.layout.item_sort, null);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.sort_letter);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.sort_title);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final Sort sort = sortList.get(position);
        // 根据position获取分类的首字母的Char ascii值
        int section = sort.getInitialLetter().charAt(0);
        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(sort.getInitialLetter());
        } else {
            // 否则隐藏字母
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
        viewHolder.tvTitle.setText(sort.name);
        // 点击事件
        viewHolder.tvTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSortClickListener != null) {
                    onSortClickListener.onSortClick(position, sort);
                }
            }
        });
        return view;
    }

    class ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
    }

}
