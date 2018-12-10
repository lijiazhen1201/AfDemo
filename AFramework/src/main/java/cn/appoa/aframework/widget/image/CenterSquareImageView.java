package cn.appoa.aframework.widget.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * 图片截取正中间的正方形部分的ImageView
 */
public class CenterSquareImageView extends AppCompatImageView {

    public CenterSquareImageView(Context context) {
        super(context);
    }

    public CenterSquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CenterSquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("unused")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
        int childHeightSize = getMeasuredHeight();
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize * 1 / 1, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 获取当前控件的 drawable
        Drawable drawable = getDrawable();
        if (drawable == null) {
            super.onDraw(canvas);
            return;
        }
        //这里 get 回来的宽度和高度是当前控件相对应的宽度和高度（在xml设置）
        if (getWidth() == 0 || getHeight() == 0) {
            super.onDraw(canvas);
            return;
        }
        //正方形部分的边长
        int edgeLength = getWidth();
        // 获取当前控件的bitmap
        Bitmap bitmap = getBitmapFromDrawable(drawable);
        if (bitmap == null) {
            super.onDraw(canvas);
            return;
        }
        //截取中间正方形部分
        bitmap = centerSquareScaleBitmap(bitmap, edgeLength);
        if (bitmap == null) {
            super.onDraw(canvas);
            return;
        }
        //重新绘制
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    /**
     * 定义 Bitmap 的默认配置
     */
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.RGB_565;

    /**
     * Drawable转Bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap;
        int width = Math.max(drawable.getIntrinsicWidth(), 2);
        int height = Math.max(drawable.getIntrinsicHeight(), 2);
        try {
            bitmap = Bitmap.createBitmap(width, height, BITMAP_CONFIG);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            bitmap = null;
        }
        return bitmap;
    }

    /**
     * 将给定图片维持宽高比缩放后，截取正中间的正方形部分。
     *
     * @param bitmap     原图
     * @param edgeLength 希望得到的正方形部分的边长
     * @return 缩放截取正中部分后的位图。
     */
    private Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (bitmap == null || edgeLength <= 0) {
            return null;
        }
        Bitmap result = null;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();
        if (widthOrg >= edgeLength && heightOrg >= edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;
            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }
            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;
            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }
        return result;
    }
}
