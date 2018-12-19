package cn.appoa.aframework.widget.side;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class SortBaseAdapter extends BaseAdapter {

    @SuppressWarnings("unused")
    protected Context context;
    protected List<Sort> sortList;
    protected List<Sort> sortAllList;

    public SortBaseAdapter(Context context, List<Sort> sortList) {
        super();
        this.context = context;
        this.sortList = sortList;
        sortAllList = this.sortList;
    }

    /**
     * 设置全部数据
     *
     * @param sortList
     */
    public void setSortAllList(List<Sort> sortAllList) {
        this.sortAllList = sortAllList;
    }

    /**
     * 更新数据
     *
     * @param sortList
     */
    public void setSortList(List<Sort> sortList) {
        this.sortList = sortList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (sortList == null) {
            return 0;
        } else {
            return sortList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (sortList == null) {
            return null;
        } else {
            return sortList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        return initView(position, view, parent);
    }

    /**
     * 重写的方法
     *
     * @param position
     * @param view
     * @param parent
     * @return
     */
    public abstract View initView(int position, View view, ViewGroup parent);

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = sortList.get(i).getInitialLetter();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    protected OnSortClickListener onSortClickListener;

    public void setOnSortClickListener(OnSortClickListener onSortClickListener) {
        this.onSortClickListener = onSortClickListener;
    }

    public interface OnSortClickListener {
        void onSortClick(int position, Sort sort);
    }

    /**
     * 根据关键词搜索更新数据
     *
     * @param key
     */
    public void searchData(String key) {
        List<Sort> searchList = new ArrayList<>();
        if (TextUtils.isEmpty(key)) {
            searchList = sortAllList;
        } else {
            searchList.clear();
            for (int i = 0; i < sortAllList.size(); i++) {
                String word = sortAllList.get(i).name;
                if (word.indexOf(key.toString()) != -1
                        || CharacterParser.getInstance().getSelling(word).startsWith(key.toString())) {
                    searchList.add(sortAllList.get(i));
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(searchList, new PinyinComparator());
        setSortList(searchList);
    }

}
