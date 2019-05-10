package com.scwang.smartrefresh.layout.divider;

import android.content.Context;
import android.support.v4.content.ContextCompat;

public class ListItemDecoration extends UniversalItemDecoration {

    public static final int VERTICAL = 0;
    public static final int HORIZONTAL = 1;
    private Context mContext;
    private int mOrientation;
    private int mColor;
    private int mHeight;

    public ListItemDecoration(Context context) {
        this(context, VERTICAL);
    }

    public ListItemDecoration(Context context, int orientation) {
        mContext = context;
        setDecorationOrientation(orientation);
        setDecorationColor(0xffe6e6e6);
        setDecorationHeight(0.5f);
    }

    /**
     * 设置方向
     *
     * @param orientation
     */
    public void setDecorationOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("invalid orientation:" + orientation);
        }
        if (orientation == mOrientation) {
            return;
        }
        mOrientation = orientation;
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
        if (mOrientation == VERTICAL) {
            decoration.top = 0;
            decoration.bottom = mHeight;
            decoration.left = 0;
            decoration.right = 0;
        } else if (mOrientation == HORIZONTAL) {
            decoration.top = 0;
            decoration.bottom = 0;
            decoration.left = 0;
            decoration.right = mHeight;
        }
        return decoration;
    }
}
