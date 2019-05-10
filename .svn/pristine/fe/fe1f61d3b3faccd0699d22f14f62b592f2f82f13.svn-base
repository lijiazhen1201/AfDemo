package cn.appoa.aframework.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import zm.bus.event.BusProvider;

/**
 * ListView和GridView的适配器
 *
 * @param <T>
 */
public abstract class ZmAdapter<T> extends BaseAdapter {

    /**
     * 上下文
     */
    protected Context mContext;

    /**
     * 数据源
     */
    protected List<T> itemList;

    /**
     * 布局加载器
     */
    protected LayoutInflater mInflater;

    /**
     * item的布局id
     */
    protected int layoutId;

    /**
     * 构造器
     *
     * @param mContext
     * @param list
     */
    public ZmAdapter(Context mContext, List<T> itemList) {
        super();
        this.mContext = mContext;
        this.itemList = itemList;
        this.mInflater = LayoutInflater.from(this.mContext);
        this.layoutId = setLayout();
        BusProvider.getInstance().register(this);
    }

    /**
     * 构造器
     *
     * @param mContext
     * @param list
     */
    public ZmAdapter(Context mContext, List<T> itemList, int layoutId) {
        super();
        this.mContext = mContext;
        this.itemList = itemList;
        this.mInflater = LayoutInflater.from(this.mContext);
        this.layoutId = layoutId;
        BusProvider.getInstance().register(this);
    }

    /**
     * 设置数据源
     *
     * @param list
     */
    public void setList(List<T> itemList) {
        this.itemList = itemList;
    }

    /**
     * item的数量
     *
     * @return
     */
    @Override
    public int getCount() {
        if (itemList == null) {
            return 0;
        } else {
            return itemList.size();
        }
    }

    /**
     * 获得item
     *
     * @param position
     * @return
     */
    @Override
    public T getItem(int position) {
        if (itemList == null) {
            return null;
        } else {
            return itemList.get(position);
        }
    }

    /**
     * 获得id
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 获得view
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ZmHolder zmHolder = ZmHolder.get(mContext, mInflater, convertView, parent, layoutId, position);
        init(zmHolder, itemList.get(position), position);
        return zmHolder.getConvertView();
    }

    /**
     * 重写此方法，在里面初始化item控件和设置item控件中的值
     *
     * @param zmHolder
     * @param t
     * @param position
     */
    public abstract void init(ZmHolder zmHolder, T t, int position);

    /**
     * 重写此方法，在里面返回布局的id
     *
     * @return
     */
    public int setLayout() {
        return 0;
    }
}
