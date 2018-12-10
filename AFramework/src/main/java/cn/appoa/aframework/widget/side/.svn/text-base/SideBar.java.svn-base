package cn.appoa.aframework.widget.side;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class SideBar extends View {

    /**
     * 画笔
     */
    private Paint paint;

    public SideBar(Context context) {
        super(context);
        this.paint = new Paint();
        this.paint.setAntiAlias(true);// 抗锯齿
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.paint = new Paint();
        this.paint.setAntiAlias(true);// 抗锯齿
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.paint = new Paint();
        this.paint.setAntiAlias(true);// 抗锯齿
    }

    /**
     * 26个字母
     */
    public static String[] letters = {"↑", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};

    /**
     * 画布
     */
    @SuppressWarnings("unused")
    private Canvas canvas;

    /**
     * 字母高度
     */
    private int letterHeight;

    /**
     * 字母颜色(默认黑色)
     */
    private int letterNormalColor = Color.parseColor("#000000");

    /**
     * 选中时字母颜色(默认黑色)
     */
    private int letterSelectedColor = Color.parseColor("#000000");
    ;

    /**
     * 设置字母颜色
     *
     * @param letterNormalColor   字母颜色
     * @param letterSelectedColor 选中时字母颜色
     */
    public void setLetterColor(int letterNormalColor, int letterSelectedColor) {
        this.letterNormalColor = letterNormalColor;
        this.letterSelectedColor = letterSelectedColor;
    }

    /**
     * 设置字母颜色
     *
     * @param letterNormalColor   字母颜色
     * @param letterSelectedColor 选中时字母颜色
     */
    public void setLetterColor(String letterNormalColor, String letterSelectedColor) {
        this.letterNormalColor = Color.parseColor(letterNormalColor);
        this.letterSelectedColor = Color.parseColor(letterSelectedColor);
    }

    /**
     * 字母字体样式(默认常规字体)
     */
    private Typeface letterTypeface = Typeface.DEFAULT;

    /**
     * 设置字母字体样式
     *
     * @param typeface 字体样式
     */
    public void setLetterTypeface(Typeface typeface) {
        // Typeface.DEFAULT：常规字体类型。
        // Typeface.DEFAULT_BOLD：黑体字体类型。
        // Typeface.SANS_SERIF：sans serif字体类型。
        // Typeface.SERIF：serif字体。
        // Typeface.MONOSPACE：等宽字体类型。
        // Typeface.BOLD：粗体。
        // Typeface.BOLD_ITALIC：粗斜体。
        // Typeface.ITALIC：斜体。
        // Typeface.NORMAL：常规。
        this.letterTypeface = typeface;
    }

    /**
     * 字母字体大小(默认12dp)
     */
    private float letterTextSize = 12;

    /**
     * 设置字母字体大小
     *
     * @param textSize 字体大小(单位dp)
     */
    public void setLetterTextSize(float textSize) {
        this.letterTextSize = textSize;
    }

    /**
     * 字母选中状态
     */
    private int choose = -1;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画布
        this.canvas = canvas;
        // 获取高度
        int height = getHeight();
        // 获取宽度
        int width = getWidth();
        //
        if (letters != null && letters.length > 0) {
            // 每一个字母的高度
            letterHeight = height / letters.length;
            // 循环添加字母
            for (int i = 0; i < letters.length; i++) {
                // 设置画笔颜色
                paint.setColor(letterNormalColor);
                // 设置字体样式
                paint.setTypeface(letterTypeface);
                // 设置字体大小
                paint.setTextSize(dp2px(getContext(), letterTextSize));
                // 选中状态
                if (choose == i) {
                    // 选中时的画笔颜色
                    paint.setColor(letterSelectedColor);
                    // 设置文本仿粗体。注意设置在小字体上效果会非常差。
                    paint.setFakeBoldText(true);
                }
                // x坐标等于中间-字符串宽度的一半.
                float xPos = width / 2 - paint.measureText(letters[i]) / 2;
                // y坐标
                float yPos = letterHeight * i + letterHeight;
                // 画字母
                canvas.drawText(letters[i], xPos, yPos, paint);
                // 重置画笔
                paint.reset();
            }
        }
    }

    /**
     * 背景色(默认透明色)
     */
    private int bgNormalColor = Color.parseColor("#00000000");

    /**
     * 选中时背景色(默认透明色)
     */
    private int bgSelectedColor = Color.parseColor("#00000000");

    /**
     * 设置背景色
     *
     * @param bgNormalColor   背景色
     * @param bgSelectedColor 选中时背景色
     */
    public void setBackgroundColor(int bgNormalColor, int bgSelectedColor) {
        this.bgNormalColor = bgNormalColor;
        this.bgSelectedColor = bgSelectedColor;
    }

    /**
     * 设置背景色
     *
     * @param bgNormalColor   背景色
     * @param bgSelectedColor 选中时背景色
     */
    public void setBackgroundColor(String bgNormalColor, String bgSelectedColor) {
        this.bgNormalColor = Color.parseColor(bgNormalColor);
        this.bgSelectedColor = Color.parseColor(bgSelectedColor);
    }

    /**
     * 弹出字母的TextView
     */
    private TextView dialogView;

    /**
     * 设置弹出字母的TextView
     *
     * @param dialogView 弹出字母的TextView
     */
    public void setDialogView(TextView dialogView) {
        this.dialogView = dialogView;
    }

    @SuppressWarnings("unused")
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // 点击x坐标
        float x = event.getX();
        // 点击y坐标
        float y = event.getY();
        // 之前选中字母下标
        int oldChoose = choose;
        // 点击y坐标所占总高度的比例*字母数组的长度就等于点击字母的下标
        int index = (int) (y / getHeight() * letters.length);
        // 事件分发
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:// 弹起
                // 弹起时的背景色
                if (bgNormalColor != -1) {
                    setBackgroundColor(bgNormalColor);
                }
                // 隐藏弹出字母的TextView
                if (dialogView != null) {
                    dialogView.setVisibility(View.GONE);
                }
                // 不选中任何一个
                choose = -1;
                // 重置
                invalidate();
                break;
            default:// 非弹起时候
                // 按下时的背景色
                if (bgSelectedColor != -1) {
                    setBackgroundColor(bgSelectedColor);
                }
                // 此次点击字母和之前选中字母不一样
                if (oldChoose != index) {
                    // 点击下标在数组范围以内
                    if (index >= 0 && index < letters.length) {
                        // 事件监听
                        if (onPressDownLetterListener != null) {
                            onPressDownLetterListener.onPressDownLetter(index, letters[index]);
                        }
                        // 显示弹出字母的TextView
                        if (dialogView != null) {
                            dialogView.setText(letters[index]);
                            dialogView.setVisibility(View.VISIBLE);
                        }
                        // 设置选中
                        choose = index;
                        // 重置
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 字母按下时的监听
     */
    private OnPressDownLetterListener onPressDownLetterListener;

    /**
     * 设置字母按下时的监听
     *
     * @param onPressDownLetterListener
     */
    public void setOnPressDownLetterListener(OnPressDownLetterListener onPressDownLetterListener) {
        this.onPressDownLetterListener = onPressDownLetterListener;
    }

    /**
     * 按下字母回调接口
     */
    public interface OnPressDownLetterListener {
        void onPressDownLetter(int index, String letter);
    }

    /**
     * 单位转换
     *
     * @param context
     * @param dpValue
     * @return
     */
    private int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
