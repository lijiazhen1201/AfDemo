package cn.appoa.aframework.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Base64;

import com.anthonycr.grant.PermissionsResultAction;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cn.appoa.aframework.R;
import cn.appoa.aframework.app.AfApplication;
import cn.appoa.aframework.dialog.DefaultUploadImgDialog;
import cn.appoa.aframework.presenter.BasePresenter;
import cn.appoa.aframework.utils.AtyUtils;

/**
 * 带上传图片的Activity基类
 */
public abstract class AfImageActivity<P extends BasePresenter> extends AfActivity<P>
        implements DefaultUploadImgDialog.OnUploadImgListener {

    protected DefaultUploadImgDialog dialogUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogUpload = new DefaultUploadImgDialog(this);
        dialogUpload.setOnUploadImgListener(this);
    }

    @Override
    public void onUploadImg(int type) {
        if (type == 1) {
            // 拍照
            selectPicFromCamera();
        }
        if (type == 2) {
            // 相册
            selectPicFromAlbum();
        }
    }

    // 需要的权限
    // <uses-permission android:name="android.permission.CAMERA"/>
    // <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    // <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    // 需要的provider(authorities的路径每个app需不同)
    // <provider
    // android:name="android.support.v4.content.FileProvider"
    // android:authorities="${applicationId}.fileprovider"
    // android:exported="false"
    // android:grantUriPermissions="true">
    // <meta-data
    // android:name="android.support.FILE_PROVIDER_PATHS"
    // android:resource="@xml/file_provider_paths" />
    // </provider>

    /**
     * 拍照的临时文件
     */
    private File cameraFile;

    /**
     * 裁剪的临时文件
     */
    private File cropFile;

    /**
     * 拍照的请求码
     */
    private static final int REQUEST_CODE_CAMERA = 1001;

    /**
     * 相册的请求码
     */
    private static final int REQUEST_CODE_ALBUM = 1002;

    /**
     * 裁剪的请求码
     */
    protected static final int REQUEST_CODE_CUT = 1003;

    /**
     * 拍照获取图片
     */
    protected void selectPicFromCamera() {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        requestPermissions(permissions, new PermissionsResultAction() {

            @Override
            public void onGranted() {
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    AtyUtils.showShort(AfImageActivity.this, R.string.not_found_sd_card, false);
                    return;
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    // 7.0系统
                    doTakePhotoNew();
                } else {
                    doTakePhotoOld();
                }
            }

            @Override
            public void onDenied(String permission) {
                AtyUtils.showShort(AfImageActivity.this, R.string.open_camera_permission, false);
            }
        });
    }

    /**
     * 老方法[Android7.0以及以上报错FileUriExposedException]
     */
    private void doTakePhotoOld() {
        cameraFile = getTempFile();
        if (cameraFile != null) {
            cameraFile.getParentFile().mkdirs();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
            startActivityForResult(intent, REQUEST_CODE_CAMERA);
        }
    }

    /**
     * 解决三星手机拍照后无法获取数据，android 7.0手机拍照后获取数据适配。 报错FileUriExposedException(SamSung
     * SM-N9006 Android5.0也有这问题)
     */
    private void doTakePhotoNew() {
        cameraFile = getTempFile();
        if (cameraFile != null) {
            cameraFile.getParentFile().mkdirs();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imageUri = FileProvider.getUriForFile(this, AfApplication.FILE_PROVIDER, cameraFile);
            List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, imageUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, REQUEST_CODE_CAMERA);
        }
    }

    /**
     * 从图库获取图片
     */
    protected void selectPicFromAlbum() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        requestPermissions(permissions, new PermissionsResultAction() {

            @Override
            public void onGranted() {
                Intent intent = null;
                if (Build.VERSION.SDK_INT < 19) {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                } else {
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                }
                if (intent != null) {
                    startActivityForResult(intent, REQUEST_CODE_ALBUM);
                }
            }

            @Override
            public void onDenied(String permission) {
                AtyUtils.showShort(AfImageActivity.this, R.string.open_album_permission, false);
            }
        });
    }

    /**
     * 接收到的图片的uri
     */
    private Uri imageUri = null;

    /**
     * 裁剪后的图片的uri
     */
    private Uri cropUri = null;

    /**
     * 接收到的图片的路径
     */
    private String imagePath = null;

    /**
     * 裁剪后接收到的图片bitmap
     */
    private Bitmap imageBitmap = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                // 拍照
                if (cameraFile != null && cameraFile.exists()) {
                    if (Build.VERSION.SDK_INT >= 24) {
                        imageUri = FileProvider.getUriForFile(this, AfApplication.FILE_PROVIDER, cameraFile);
                    } else {
                        imageUri = Uri.fromFile(cameraFile);
                    }
                    imagePath = cameraFile.getAbsolutePath();
                    imageBitmap = revitionImageSize(imagePath);
                    getImageBitmap(imageUri, imagePath, imageBitmap);
                }
                break;
            case REQUEST_CODE_ALBUM:
                // 图库
                if (data != null) {
                    if (data.getData() != null) {
                        imageUri = data.getData();
                    } else {
                        Bundle bundle = data.getExtras();
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        imageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                    }
                    imagePath = getPhotoPathFromContentUri(this, imageUri);
                    imageBitmap = revitionImageSize(imagePath);
                    getImageBitmap(imageUri, imagePath, imageBitmap);
                }
                break;
            case REQUEST_CODE_CUT:
                // 裁剪
                if (isBigImage) {
                    if (cropUri != null) {
                        imageBitmap = uriToBitmap(this, cropUri);
                        getImageBitmap(imageBitmap);
                    }
                } else {
                    if (data != null) {
                        imageBitmap = data.getParcelableExtra("data");
                        getImageBitmap(imageBitmap);
                    }
                }
                break;
        }
    }

    /**
     * 从资源图片获取bitmap
     *
     * @param resId
     * @return
     */
    public static Bitmap resToBitmap(Context context, int resId) {
        Bitmap bitmap = null;
        if (context != null) {
            Resources res = context.getResources();
            bitmap = BitmapFactory.decodeResource(res, resId);
        }
        return bitmap;
    }

    /**
     * bitmap缩放
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        Bitmap newbmp = null;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }

    /**
     * 旋转Bitmap
     *
     * @param bitmap
     * @param degrees
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, float degrees) {
        Bitmap mBitmap = null;
        try {
            Matrix m = new Matrix();
            m.setRotate(degrees % 360);
            mBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mBitmap;
    }

    /**
     * 获得圆角bitmap
     *
     * @param bitmap
     * @param roundPx
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    /**
     * 获得带倒影的bitmap
     *
     * @param bitmap
     * @return
     */
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w, h / 2, matrix, false);
        Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);
        canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, h, w, bitmapWithReflection.getHeight() + reflectionGap, paint);
        return bitmapWithReflection;
    }

    /**
     * uri转bitmap
     *
     * @param uri
     * @return
     */
    public static Bitmap uriToBitmap(Context context, Uri uri) {
        Bitmap bitmap = null;
        if (uri != null) {
            try {
                bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * bitmap转base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        String base64 = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                base64 = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return base64;
    }

    /**
     * base64转bitmap
     *
     * @param base64
     * @return
     */
    public static Bitmap base64ToBitmap(String base64) {
        Bitmap bitmap = null;
        byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    /**
     * bitmap转file
     *
     * @param bitmap
     * @return
     */
    public static File bitmapToFile(Bitmap bitmap) {
        File file = null;
        file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/DCIM/Camera", System.currentTimeMillis() + ".jpeg");
        file.getParentFile().mkdirs();
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * file转Bitmap
     *
     * @param file
     * @param width
     * @param height
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Bitmap fileToBitmap(File file, int width, int height) {
        Bitmap bitmap = null;
        if (file != null && file.exists()) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(file.getPath(), opts);
                // 计算图片缩放比例
                final int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength, width * height);
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            try {
                bitmap = BitmapFactory.decodeFile(file.getPath(), opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    private static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128
                : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * bitmap转byte[]
     *
     * @param bitmap
     * @return
     */
    public static byte[] bitmapToBytes(Bitmap bitmap) {
        byte[] bytes = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        bitmap.recycle();
        bytes = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * byte[]转bitmap
     *
     * @param bytes
     * @return
     */
    public static Bitmap bytesToBitmap(byte[] bytes) {
        Bitmap bitmap = null;
        if (bytes != null && bytes.length != 0) {
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return bitmap;
    }

    /**
     * bitmap转Drawable
     *
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToDrawable(Context context, Bitmap bitmap) {
        Drawable drawable = null;
        if (context != null) {
            Resources res = context.getResources();
            drawable = new BitmapDrawable(res, bitmap);
        }
        return drawable;
    }

    /**
     * drawable转Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        // 取 drawable 的颜色格式
        Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565;
        // 建立对应 bitmap
        bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 是否裁剪大图
     */
    private boolean isBigImage = false;

    /**
     * 裁剪图片
     *
     * @param isBigImage 是否裁剪大图(360以上传true)
     * @param imageUri   图片uri
     * @param aspectX    aspectX aspectY 是剪裁图片的宽高比
     * @param aspectY    aspectX aspectY 是剪裁图片的宽高比
     * @param outputX    outputX,outputY 是剪裁图片的分辨率
     * @param outputY    outputX,outputY 是剪裁图片的分辨率
     */
    protected void cuttingImage(boolean isBigImage, Uri imageUri, int aspectX, int aspectY, int outputX, int outputY) {
        cropFile = getTempFile();
        if (cropFile != null) {
            cropFile.getParentFile().mkdirs();
            Intent intent = new Intent("com.android.camera.action.CROP");
            if (Build.VERSION.SDK_INT >= 24) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                cropUri = FileProvider.getUriForFile(this, AfApplication.FILE_PROVIDER, cropFile);
            } else {
                cropUri = Uri.fromFile(cropFile);
            }
            intent.setDataAndType(imageUri, "image/*");
            // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
            intent.putExtra("crop", "true");
            // 去黑边
            intent.putExtra("scale", true);
            // 去黑边
            intent.putExtra("scaleUpIfNeeded", true);
            // 取消人脸识别功能
            intent.putExtra("noFaceDetection", true);
            // 图片输出格式
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            // aspectX aspectY 是剪裁图片的宽高比
            intent.putExtra("aspectX", aspectX);
            intent.putExtra("aspectY", aspectY);
            // outputX,outputY 是剪裁图片的分辨率
            intent.putExtra("outputX", outputX);
            intent.putExtra("outputY", outputY);
            if (isBigImage) {
                intent.putExtra("return-data", false);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
            } else {
                intent.putExtra("return-data", true);
            }
            this.isBigImage = isBigImage;
            startActivityForResult(intent, REQUEST_CODE_CUT);
        }
    }

    /**
     * bitmap压缩（质量压缩法）
     *
     * @param image
     * @return
     */
    public static Bitmap compressImageQuality(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * bitmap压缩（按比例大小压缩）
     *
     * @param image
     * @return
     */
    public static Bitmap compressImageProportion(Bitmap image, float hh, float ww) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        // float hh = 800f;// 这里设置高度为800f
        // float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImageQuality(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 通过路径压缩bitmap
     *
     * @param path
     * @return
     */
    public static Bitmap revitionImageSize(String path) {
        Bitmap bitmap = null;
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(new File(path)));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            int i = 0;
            while (true) {
                if ((options.outWidth >> i <= 1000) && (options.outHeight >> i <= 1000)) {
                    in = new BufferedInputStream(new FileInputStream(new File(path)));
                    options.inSampleSize = (int) Math.pow(2.0D, i);
                    options.inJustDecodeBounds = false;
                    bitmap = BitmapFactory.decodeStream(in, null, options);
                    break;
                }
                i += 1;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 通过uri获取图片路径
     *
     * @param context
     * @param uri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPhotoPathFromContentUri(Context context, Uri uri) {
        String photoPath = "";
        if (context == null || uri == null) {
            return photoPath;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                String[] split = docId.split(":");
                if (split.length >= 2) {
                    String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        photoPath = Environment.getExternalStorageDirectory()
                                .getAbsolutePath() + "/DCIM/Camera" + "/" + split[1];
                    }
                }
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                photoPath = getDataColumn(context, contentUri, null, null);
            } else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String[] split = docId.split(":");
                if (split.length >= 2) {
                    String type = split[0];
                    Uri contentUris = null;
                    if ("image".equals(type)) {
                        contentUris = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUris = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUris = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    String selection = MediaStore.Images.Media._ID + "=?";
                    String[] selectionArgs = new String[]{split[1]};
                    photoPath = getDataColumn(context, contentUris, selection, selectionArgs);
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            photoPath = uri.getPath();
        } else {
            photoPath = getDataColumn(context, uri, null, null);
        }
        return photoPath;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isDeleteFile()) {
            if (cameraFile != null && cameraFile.exists())
                cameraFile.delete();
            if (cropFile != null && cropFile.exists())
                cropFile.delete();
        }
    }

    /**
     * 是否删除临时文件
     */
    public boolean isDeleteFile() {
        return false;
    }

    /**
     * 获取临时文件
     */
    private File getTempFile() {
        File file = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/DCIM/Camera", System.currentTimeMillis() + ".jpeg");
        }
        return file;
    }

    /**
     * 处理拍照或图库获取的图片
     *
     * @param imageUri    图片uri
     * @param imagePath   图片路径
     * @param imageBitmap 压缩后的bitmap
     */
    public abstract void getImageBitmap(Uri imageUri, String imagePath, Bitmap imageBitmap);

    /**
     * 处理裁剪后获得的bitmap
     *
     * @param imageBitmap 裁剪后的bitmap
     */
    public abstract void getImageBitmap(Bitmap imageBitmap);
}
