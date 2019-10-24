package com.scwang.smartrefresh.layout.divider;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;

@SuppressWarnings("rawtypes")
public class GridItemDecoration extends UniversalItemDecoration {

    private Context mContext;
    private BaseQuickAdapter mAdapter;
    private int mSize;
    private boolean mMargin;
    private int mColor;
    private int mHeight;

    public GridItemDecoration(Context context, BaseQuickAdapter adapter, int size) {
        this(context, adapter, size, false);
    }

    public GridItemDecoration(Context context, BaseQuickAdapter adapter, int size, boolean margin) {
        mContext = context;
        mAdapter = adapter;
        mSize = size;
        setDecorationMargin(margin);
        setDecorationColor(0xfff5f5f5);
        setDecorationHeight(4.0f);
    }

    /**
     * 设置是否显示边距
     *
     * @param margin
     */
    public void setDecorationMargin(boolean margin) {
        mMargin = margin;
    }

    /**
     * 设置颜色
     *
     * @param colorId
     */
    public void setDecorationColorRes(int colorId) {
        setDecorationColor(ContextCompat.getColor(mContext, colorId));
    }

    /**
     * 设置颜色
     *
     * @param color
     */
    public void setDecorationColor(int color) {
        mColor = color;
    }

    /**
     * 设置高度
     *
     * @param heightId
     */
    public void setDecorationHeightRes(int heightId) {
        mHeight = (int) mContext.getResources().getDimension(heightId);
    }

    /**
     * 设置高度
     *
     * @param height
     */
    public void setDecorationHeight(float height) {
        mHeight = dip2px(mContext, height);
    }

    @Override
    public Decoration getItemOffsets(int position) {
        ColorDecoration decoration = new ColorDecoration();
        decoration.decorationColor = mColor;
        if (isFixedViewType(mAdapter.getItemViewType(position))) {
            decoration.top = 0;
            decoration.bottom = 0;
            decoration.left = 0;
            decoration.right = 0;
        } else {
            position = position - mAdapter.getHeaderLayoutCount();
            int index = position % mSize;
            if (index == 0) {//左
                decoration.top = position == index ? (mMargin ? mHeight : 0) : 0;
                decoration.bottom = mHeight;
                decoration.left = mMargin ? mHeight : 0;
                decoration.right = mHeight / 2;
            } else if (index == mSize - 1) {//右
                decoration.top = position == index ? (mMargin ? mHeight : 0) : 0;
                decoration.bottom = mHeight;
                decoration.left = mHeight / 2;
                decoration.right = mMargin ? mHeight : 0;
            } else {//中
                decoration.top = position == index ? (mMargin ? mHeight : 0) : 0;
                decoration.bottom = mHeight;
                decoration.left = mHeight / 2;
                decoration.right = mHeight / 2;
            }
        }
        return decoration;
    }

    private boolean isFixedViewType(int type) {
        return type == BaseQuickAdapter.EMPTY_VIEW || type == BaseQuickAdapter.HEADER_VIEW
                || type == BaseQuickAdapter.FOOTER_VIEW || type == BaseQuickAdapter.LOADING_VIEW;
    }

}
