package cn.appoa.aframework.widget.wheel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import cn.appoa.aframework.R;
import cn.appoa.aframework.widget.wheel.adapter.WheelAdapter;
import cn.appoa.aframework.widget.wheel.listener.OnWheelChangedListener;
import cn.appoa.aframework.widget.wheel.listener.OnWheelScrollListener;

public class WheelView extends View {

    /**
     * 滚动的持续时间
     */
    public static final int SCROLLING_DURATION = 500;

    /**
     * 滚动系数
     */
    public static final int MIN_DELTA_FOR_SCROLLING = 1;

    /**
     * 当前值与标签文本颜色
     */
    public static int VALUE_TEXT_COLOR = 0xFF636363;

    /**
     * 项目文本颜色
     */
    public static int ITEMS_TEXT_COLOR = 0xFFAAAAAA;

    /**
     * 顶部和底部的阴影颜色
     */
    public static final int[] SHADOWS_COLORS = new int[]{0x00000000, 0x00000000, 0x00000000};

    /**
     * 每行的高
     */
    public static final int ADDITIONAL_ITEM_HEIGHT = 80;

    /**
     * 顶部和底部距离抵消
     */
    public int ITEM_OFFSET;

    /**
     * 额外的宽度
     */
    public static final int ADDITIONAL_ITEMS_SPACE = 1000;

    /**
     * 标签抵消
     */
    public static final int LABEL_OFFSET = 8;

    /**
     * 左和右间距
     */
    public static final int PADDING = 10;

    /**
     * 默认可见行数
     */
    private int DEF_VISIBLE_ITEMS = 5;

    /**
     * 上下文
     */
    private Context context;

    private int itemHeight = 0;
    private int itemsWidth = 0;
    private int labelWidth = 0;

    private StaticLayout itemsLayout;
    private StaticLayout labelLayout;
    private StaticLayout valueLayout;

    private Drawable centerDrawable;
    private GradientDrawable topShadow;
    private GradientDrawable bottomShadow;

    private boolean isScrollingPerformed;
    private int scrollingOffset;

    private GestureDetector gestureDetector;
    private Scroller scroller;
    private int lastScrollY;

    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData(context);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    public WheelView(Context context) {
        super(context);
        initData(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void initData(Context context) {
        this.context = context;
        gestureDetector = new GestureDetector(context, gestureListener);
        gestureDetector.setIsLongpressEnabled(false);
        ITEM_OFFSET = -textSize(16) / 4;
        scroller = new Scroller(context);
        this.animationHandler = new MyHandler(this);
    }

    /**
     * 设置字体大小
     *
     * @param size
     * @return
     */
    private int textSize(int size) {
        return sp2px(context, size);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 滚轮适配器
     */
    private WheelAdapter adapter = null;

    public WheelAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(WheelAdapter adapter) {
        if (adapter == null)
            return;
        this.adapter = adapter;
        invalidateLayouts();
        invalidate();
    }

    /**
     * 设置指定的滚动插入器
     */
    public void setInterpolator(Interpolator interpolator) {
        scroller.forceFinished(true);
        scroller = new Scroller(getContext(), interpolator);
    }

    /**
     * 可见行数
     */
    private int visibleItems = DEF_VISIBLE_ITEMS;

    /**
     * 获得可见的行数
     */
    public int getVisibleItems() {
        return visibleItems;
    }

    /**
     * 设置可见行数
     */
    public void setVisibleItems(int count) {
        visibleItems = count;
        invalidate();
    }

    /**
     * 标签
     */
    private String label;

    /**
     * 获得标签
     *
     * @return
     */
    public String getLabel() {
        return label;
    }

    /**
     * 设置标签
     */
    public void setLabel(String newLabel) {
        if (label == null || !label.equals(newLabel)) {
            label = newLabel;
            labelLayout = null;
            invalidate();
        }
    }

    /**
     * 滚轮改变的监听
     */
    private List<OnWheelChangedListener> changingListeners = new LinkedList<OnWheelChangedListener>();

    /**
     * 添加滚轮改变的监听
     *
     * @param listener
     */
    public void addChangingListener(OnWheelChangedListener listener) {
        changingListeners.add(listener);
    }

    /**
     * 移除滚轮改变的监听
     *
     * @param listener
     */
    public void removeChangingListener(OnWheelChangedListener listener) {
        changingListeners.remove(listener);
    }

    protected void notifyChangingListeners(int oldValue, int newValue) {
        for (OnWheelChangedListener listener : changingListeners) {
            listener.onChanged(this, oldValue, newValue);
        }
    }

    /**
     * 滚轮滚动的监听
     */
    private List<OnWheelScrollListener> scrollingListeners = new LinkedList<OnWheelScrollListener>();

    /**
     * 添加滚轮滚动的监听
     *
     * @param listener
     */
    public void addScrollingListener(OnWheelScrollListener listener) {
        scrollingListeners.add(listener);
    }

    /**
     * 移除滚轮滚动的监听
     *
     * @param listener
     */
    public void removeScrollingListener(OnWheelScrollListener listener) {
        scrollingListeners.remove(listener);
    }

    protected void notifyScrollingListenersAboutStart() {
        for (OnWheelScrollListener listener : scrollingListeners) {
            listener.onScrollingStarted(this);
        }
    }

    protected void notifyScrollingListenersAboutEnd() {
        for (OnWheelScrollListener listener : scrollingListeners) {
            listener.onScrollingFinished(this);
        }
    }

    /**
     * 当前选中项下标
     */
    private int currentItem = 0;

    /**
     * 获得当前选中项
     *
     * @return
     */
    public int getCurrentItem() {
        return currentItem;
    }

    /**
     * 设置当前选中项
     *
     * @param index
     * @param animated
     */
    public void setCurrentItem(int index, boolean animated) {
        if (adapter == null || adapter.getItemsCount() == 0) {
            return;
        }
        if (index < 0 || index >= adapter.getItemsCount()) {
            if (isCyclic) {
                while (index < 0) {
                    index += adapter.getItemsCount();
                }
                index %= adapter.getItemsCount();
            } else {
                return; // throw?
            }
        }
        if (index != currentItem) {
            if (animated) {
                scroll(index - currentItem, SCROLLING_DURATION);
            } else {
                invalidateLayouts();

                int old = currentItem;
                currentItem = index;

                notifyChangingListeners(old, currentItem);
                invalidate();
            }
        }
    }

    /**
     * 设置当前选中项
     *
     * @param index
     */
    public void setCurrentItem(int index) {
        setCurrentItem(index, false);
    }

    /**
     * 是否循环滚动
     */
    boolean isCyclic = false;

    /**
     * 获得是否循环滚动
     *
     * @return
     */
    public boolean getCyclic() {
        return isCyclic;
    }

    /**
     * 设置是否循环滚动
     *
     * @param isCyclic
     */
    public void setCyclic(boolean isCyclic) {
        this.isCyclic = isCyclic;
        invalidate();
        invalidateLayouts();
    }

    /**
     * 清空layouts
     */
    private void invalidateLayouts() {
        itemsLayout = null;
        valueLayout = null;
        scrollingOffset = 0;
    }

    /**
     * 待选项
     */
    private TextPaint itemsPaint;

    /**
     * 待选项字体大小
     */
    private int itemsTextSize = 14;

    /**
     * 选中项
     */
    private TextPaint valuePaint;

    /**
     * 选中项字体大小
     */
    private int valueTextSize = 16;

    /**
     * 标签
     */
    private TextPaint labelPaint;

    /**
     * 标签字体大小
     */
    private int labelTextSize = 16;

    /**
     * 初始化资源
     */
    @SuppressWarnings("deprecation")
    private void initResourcesIfNecessary() {
        if (itemsPaint == null) {
            itemsPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            itemsPaint.density = getResources().getDisplayMetrics().density;
            itemsPaint.setTextSize(textSize(itemsTextSize));// 待选项字体大小
        }

        if (valuePaint == null) {
            valuePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
            valuePaint.density = getResources().getDisplayMetrics().density;
            valuePaint.setTextSize(textSize(valueTextSize));// 选中字大小
            valuePaint.setShadowLayer(0.1f, 0, 0.1f, 0xFFC0C0C0);
        }
        if (labelPaint == null) {
            labelPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
            labelPaint.density = getResources().getDisplayMetrics().density;
            labelPaint.setColor(VALUE_TEXT_COLOR);
            labelPaint.setTextSize(textSize(labelTextSize));// 标签大小
        }

        if (centerDrawable == null) {
            centerDrawable = getContext().getResources().getDrawable(R.drawable.wheel_val);
        }

        if (topShadow == null) {
            topShadow = new GradientDrawable(Orientation.TOP_BOTTOM, SHADOWS_COLORS);
        }

        if (bottomShadow == null) {
            bottomShadow = new GradientDrawable(Orientation.BOTTOM_TOP, SHADOWS_COLORS);
        }

        setBackgroundResource(R.drawable.wheel_bg);
    }

    /**
     * 计算所需的高度进行布局
     *
     * @param layout
     * @return
     */
    private int getDesiredHeight(Layout layout) {
        if (layout == null) {
            return 0;
        }
        int desired = getItemHeight() * visibleItems - ITEM_OFFSET * 2 - ADDITIONAL_ITEM_HEIGHT;
        // 检查最低高度
        desired = Math.max(desired, getSuggestedMinimumHeight());
        return desired;
    }

    /**
     * 返回选中的内容
     *
     * @param index
     * @return
     */
    private String getTextItem(int index) {
        if (adapter == null || adapter.getItemsCount() == 0) {
            return null;
        }
        int count = adapter.getItemsCount();
        if ((index < 0 || index >= count) && !isCyclic) {
            return null;
        } else {
            while (index < 0) {
                index = count + index;
            }
        }

        index %= count;
        return adapter.getItem(index);
    }

    /**
     * 构建文本当前值
     *
     * @param useCurrentValue
     * @return
     */
    private String buildText(boolean useCurrentValue) {
        StringBuilder itemsText = new StringBuilder();
        int addItems = visibleItems / 2 + 1;

        for (int i = currentItem - addItems; i <= currentItem + addItems; i++) {
            if (useCurrentValue || i != currentItem) {
                String text = getTextItem(i);
                if (text != null) {
                    itemsText.append(text);
                }
            }
            if (i < currentItem + addItems) {
                itemsText.append("\n");
            }
        }

        return itemsText.toString();
    }

    /**
     * 返回item Text的长度
     *
     * @return
     */
    private int getMaxTextLength() {
        WheelAdapter adapter = getAdapter();
        if (adapter == null) {
            return 0;
        }

        int adapterLength = adapter.getMaximumLength();
        if (adapterLength > 0) {
            return adapterLength;
        }

        String maxText = null;
        int addItems = visibleItems / 2;
        for (int i = Math.max(currentItem - addItems, 0); i < Math.min(currentItem + visibleItems,
                adapter.getItemsCount()); i++) {
            String text = adapter.getItem(i);
            if (text != null && (maxText == null || maxText.length() < text.length())) {
                maxText = text;
            }
        }

        return maxText != null ? maxText.length() : 0;
    }

    /**
     * 返回item的高
     *
     * @return
     */
    private int getItemHeight() {
        if (itemHeight != 0) {
            return itemHeight;
        } else if (itemsLayout != null && itemsLayout.getLineCount() > 2) {
            itemHeight = itemsLayout.getLineTop(2) - itemsLayout.getLineTop(1);
            return itemHeight;
        }

        return getHeight() / visibleItems;
    }

    /**
     * 计算控制宽度和创建文本布局
     *
     * @param widthSize
     * @param mode
     * @return
     */
    @SuppressWarnings("unused")
    private int calculateLayoutWidth(int widthSize, int mode) {
        initResourcesIfNecessary();

        int width = widthSize;
        int maxLength = getMaxTextLength();

        if (maxLength > 0) {
            float textWidth = (float) Math.ceil(Layout.getDesiredWidth("0", valuePaint));
            itemsWidth = (int) (maxLength * textWidth);
        } else {
            itemsWidth = 0;
        }
        itemsWidth += ADDITIONAL_ITEMS_SPACE; // 加上额外的宽度

        labelWidth = 0;
        if (label != null && label.length() > 0) {
            labelWidth = (int) Math.ceil(Layout.getDesiredWidth(label, labelPaint));
        }

        boolean recalculate = false;
        if (mode == MeasureSpec.EXACTLY) {
            width = widthSize;
            recalculate = true;
        } else {
            width = itemsWidth + labelWidth + 2 * PADDING;
            if (labelWidth > 0) {
                width += LABEL_OFFSET;
            }

            // 检查最小宽度
            width = Math.max(width, getSuggestedMinimumWidth());

            if (mode == MeasureSpec.AT_MOST && widthSize < width) {
                width = widthSize;
                recalculate = true;
            }
        }

        if (recalculate) {
            // 重新计算宽度
            int pureWidth = width - LABEL_OFFSET - 2 * PADDING;
            if (pureWidth <= 0) {
                itemsWidth = labelWidth = 0;
            }
            if (labelWidth > 0) {
                double newWidthItems = (double) itemsWidth * pureWidth / (itemsWidth + labelWidth);
                // itemsWidth = (int) newWidthItems;
                // labelWidth = pureWidth - itemsWidth;
                itemsWidth = (int) width / 2;
                labelWidth = (int) width / 2;
            } else {
                itemsWidth = pureWidth + LABEL_OFFSET; // 加上标签抵消的距离
            }
        }

        if (itemsWidth > 0) {
            createLayouts(itemsWidth, labelWidth);
        }

        return width;
    }

    /**
     * 创建 layouts
     *
     * @param widthItems
     * @param widthLabel
     */
    private void createLayouts(int widthItems, int widthLabel) {
        if (itemsLayout == null || itemsLayout.getWidth() > widthItems) {
            itemsLayout = new StaticLayout(buildText(isScrollingPerformed), itemsPaint, widthItems,
                    widthLabel > 0 ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_CENTER, 1,
                    ADDITIONAL_ITEM_HEIGHT, false);
        } else {
            itemsLayout.increaseWidthTo(widthItems);
        }

        if (!isScrollingPerformed && (valueLayout == null || valueLayout.getWidth() > widthItems)) {
            String text = getAdapter() != null ? getAdapter().getItem(currentItem) : null;
            valueLayout = new StaticLayout(text != null ? text : "", valuePaint, widthItems,
                    widthLabel > 0 ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_CENTER, 1,
                    ADDITIONAL_ITEM_HEIGHT, false);
        } else if (isScrollingPerformed) {
            valueLayout = null;
        } else {
            valueLayout.increaseWidthTo(widthItems);
        }

        if (widthLabel > 0) {
            if (labelLayout == null || labelLayout.getWidth() > widthLabel) {
                labelLayout = new StaticLayout(label, labelPaint, widthLabel, Layout.Alignment.ALIGN_NORMAL, 1,
                        ADDITIONAL_ITEM_HEIGHT, false);
            } else {
                labelLayout.increaseWidthTo(widthLabel);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = calculateLayoutWidth(widthSize, widthMode);

        int height;
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = getDesiredHeight(itemsLayout);

            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制中间的背景
        drawCenterRect(canvas);
        drawShadows(canvas);

        if (itemsLayout == null) {
            if (itemsWidth == 0) {
                calculateLayoutWidth(getWidth(), MeasureSpec.EXACTLY);
            } else {
                createLayouts(itemsWidth, labelWidth);
            }
        }

        if (itemsWidth > 0) {
            canvas.save();
            // 跳过填充空间和隐藏顶部和底部的一部分物品
            canvas.translate(PADDING, -ITEM_OFFSET);
            drawItems(canvas);
            drawValue(canvas);
            canvas.restore();
        }

    }

    /**
     * 在顶部和底部画阴影
     *
     * @param canvas
     */
    private void drawShadows(Canvas canvas) {
        topShadow.setBounds(0, 0, getWidth(), getHeight() / visibleItems);
        topShadow.draw(canvas);

        bottomShadow.setBounds(0, getHeight() - getHeight() / visibleItems, getWidth(), getHeight());
        bottomShadow.draw(canvas);
    }

    /**
     * 绘制标签
     *
     * @param canvas
     */
    private void drawValue(Canvas canvas) {
        valuePaint.setColor(VALUE_TEXT_COLOR);
        valuePaint.drawableState = getDrawableState();

        Rect bounds = new Rect();
        itemsLayout.getLineBounds(visibleItems / 2, bounds);

        if (labelLayout != null) {
            canvas.save();
            canvas.translate(itemsLayout.getWidth() + LABEL_OFFSET, bounds.top);
            labelLayout.draw(canvas);
            canvas.restore();
        }

        if (valueLayout != null) {
            canvas.save();
            canvas.translate(0, bounds.top + scrollingOffset);
            valueLayout.draw(canvas);
            canvas.restore();
        }
    }

    private void drawItems(Canvas canvas) {
        canvas.save();

        int top = itemsLayout.getLineTop(1);
        canvas.translate(0, -top + scrollingOffset);

        itemsPaint.setColor(ITEMS_TEXT_COLOR);
        itemsPaint.drawableState = getDrawableState();
        itemsLayout.draw(canvas);

        canvas.restore();
    }

    /**
     * 为当前值选中值绘制一个矩形背景
     *
     * @param canvas
     */
    private void drawCenterRect(Canvas canvas) {
        int center = getHeight() / 2;
        int offset = getItemHeight() / 2;
        centerDrawable.setBounds(0, center - offset, getWidth(), center + offset);
        centerDrawable.draw(canvas);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        WheelAdapter adapter = getAdapter();
        if (adapter == null) {
            return true;
        }

        if (!gestureDetector.onTouchEvent(event) && event.getAction() == MotionEvent.ACTION_UP) {
            justify();
        }
        return true;
    }

    /**
     * 滚动wheel
     *
     * @param delta
     */
    private void doScroll(int delta) {
        scrollingOffset += delta;
        int count = scrollingOffset / (getItemHeight() / 2);// 替换选中项的滚动距离
        int pos = currentItem - count;
        if (isCyclic && adapter.getItemsCount() > 0) {
            while (pos < 0) {
                pos += adapter.getItemsCount();
            }
            pos %= adapter.getItemsCount();
        } else if (isScrollingPerformed) {
            if (pos < 0) {
                count = currentItem;
                pos = 0;
            } else if (pos >= adapter.getItemsCount()) {
                count = currentItem - adapter.getItemsCount() + 1;
                pos = adapter.getItemsCount() - 1;
            }
        } else {
            pos = Math.max(pos, 0);
            pos = Math.min(pos, adapter.getItemsCount() - 1);
        }

        int offset = scrollingOffset;
        if (pos != currentItem) {
            setCurrentItem(pos, false);
        } else {
            invalidate();
        }
        // 更新offset
        scrollingOffset = offset - count * getItemHeight();
        if (scrollingOffset > getHeight()) {
            scrollingOffset = scrollingOffset % getHeight() + getHeight();
        }
    }

    private SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {
        public boolean onDown(MotionEvent e) {
            if (isScrollingPerformed) {
                scroller.forceFinished(true);
                clearMessages();
                return true;
            }
            return false;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            startScrolling();
            doScroll((int) -distanceY);
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            lastScrollY = currentItem * getItemHeight() + scrollingOffset;
            int maxY = isCyclic ? 0x7FFFFFFF : adapter.getItemsCount() * getItemHeight();
            int minY = isCyclic ? -maxY : 0;
            scroller.fling(0, lastScrollY, 0, (int) -velocityY / 2, 0, 0, minY, maxY);
            setNextMessage(MESSAGE_SCROLL);
            return true;
        }
    };

    private final int MESSAGE_SCROLL = 0;
    private final int MESSAGE_JUSTIFY = 1;

    private void setNextMessage(int message) {
        clearMessages();
        animationHandler.sendEmptyMessage(message);
    }

    private void clearMessages() {
        animationHandler.removeMessages(MESSAGE_SCROLL);
        animationHandler.removeMessages(MESSAGE_JUSTIFY);
    }

    // animation handler
    private Handler animationHandler;

    static class MyHandler extends Handler {

        private WeakReference<WheelView> mOuter;

        public MyHandler(WheelView activity) {
            mOuter = new WeakReference<WheelView>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            WheelView outer = mOuter.get();
            if (outer != null) {// Do something with outer as your wish.
                outer.scroller.computeScrollOffset();
                int currY = outer.scroller.getCurrY();
                int delta = outer.lastScrollY - currY;
                outer.lastScrollY = currY;
                if (delta != 0) {
                    outer.doScroll(delta);
                }

                // 滚动时，没有最后的Y值，通过计算获得
                if (Math.abs(currY - outer.scroller.getFinalY()) < MIN_DELTA_FOR_SCROLLING) {
                    currY = outer.scroller.getFinalY();
                    outer.scroller.forceFinished(true);
                }
                if (!outer.scroller.isFinished()) {
                    outer.animationHandler.sendEmptyMessage(msg.what);
                } else if (msg.what == outer.MESSAGE_SCROLL) {
                    outer.justify();
                } else {
                    outer.finishScrolling();
                }
            }
        }
    }

    private void justify() {
        if (adapter == null) {
            return;
        }

        lastScrollY = 0;
        int offset = scrollingOffset;
        int itemHeight = getItemHeight();
        boolean needToIncrease = offset > 0 ? currentItem < adapter.getItemsCount() : currentItem > 0;
        if ((isCyclic || needToIncrease) && Math.abs((float) offset) > (float) itemHeight / 2) {
            if (offset < 0)
                offset += itemHeight + MIN_DELTA_FOR_SCROLLING;
            else
                offset -= itemHeight + MIN_DELTA_FOR_SCROLLING;
        }
        if (Math.abs(offset) > MIN_DELTA_FOR_SCROLLING) {
            scroller.startScroll(0, 0, 0, offset, SCROLLING_DURATION);
            setNextMessage(MESSAGE_JUSTIFY);
        } else {
            finishScrolling();
        }
    }

    private void startScrolling() {
        if (!isScrollingPerformed) {
            isScrollingPerformed = true;
            notifyScrollingListenersAboutStart();
        }
    }

    private void finishScrolling() {
        if (isScrollingPerformed) {
            notifyScrollingListenersAboutEnd();
            isScrollingPerformed = false;
        }
        invalidateLayouts();
        invalidate();
    }

    /**
     * 滚动wheel
     *
     * @param itemsToScroll
     * @param time
     */
    public void scroll(int itemsToScroll, int time) {
        scroller.forceFinished(true);

        lastScrollY = scrollingOffset;
        int offset = itemsToScroll * getItemHeight();

        scroller.startScroll(0, lastScrollY, 0, offset - lastScrollY, time);
        setNextMessage(MESSAGE_SCROLL);

        startScrolling();
    }

}
