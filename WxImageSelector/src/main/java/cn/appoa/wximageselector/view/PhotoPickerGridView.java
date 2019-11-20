package cn.appoa.wximageselector.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;

import cn.appoa.wximageselector.R;
import cn.appoa.wximageselector.ShowBigImageListActivity;
import cn.appoa.wximageselector.entry.Image;
import cn.appoa.wximageselector.luban.Luban;
import cn.appoa.wximageselector.luban.OnCompressListener;
import cn.appoa.wximageselector.utils.ImageSelectorUtils;

/**
 * 图片选择的GridView
 */
public class PhotoPickerGridView extends GridView {

    public PhotoPickerGridView(Context context) {
        this(context, null);
    }

    public PhotoPickerGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoPickerGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * Context
     */
    private Context context;

    /**
     * Activity
     */
    private Activity activity;

    /**
     * Handler
     */
    private Handler handler;

    /**
     * 图片路径集合
     */
    private ArrayList<String> photoPaths;

    /**
     * 图片base64数组
     */
    public String[] photoBase64s;

    /**
     * 适配器
     */
    private PhotoPickerAdapter adapter;

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        if (this.context instanceof Activity) {
            activity = (Activity) this.context;
        }
        this.handler = new MyHandler(this);
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)
                .build();
        PRDownloader.initialize(context.getApplicationContext(), config);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PhotoPickerGridView);
            setMax(array.getInteger(R.styleable.PhotoPickerGridView_maxCount, 9));
            setDefaultAddRes(array.getInteger(R.styleable.PhotoPickerGridView_defaultPhotoRes, R.drawable.btn_addpic_yes));
            setUploadByGridView(array.getBoolean(R.styleable.PhotoPickerGridView_isUploadByGridView, true));
            setToBase64(array.getBoolean(R.styleable.PhotoPickerGridView_isToBase64, false));
            setCamera(array.getBoolean(R.styleable.PhotoPickerGridView_isCamera, false));
            setImage(array.getBoolean(R.styleable.PhotoPickerGridView_isImage, true));
            setVideo(array.getBoolean(R.styleable.PhotoPickerGridView_isVideo, false));
            setVideoDuration(array.getInteger(R.styleable.PhotoPickerGridView_videoDuration, 0));
            array.recycle();
        }
        if (photoPaths == null) {
            photoPaths = new ArrayList<>();
        }
        photoPaths.add(defaultPhotoPath);
        if (photoBase64s == null) {
            photoBase64s = new String[max];
        }
        adapter = new PhotoPickerAdapter();
        setAdapter(adapter);
    }

    /**
     * 是否正在计算高度
     */
    private boolean isOnMeasure = false;

    /**
     * 设置不滚动
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        isOnMeasure = true;
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        isOnMeasure = false;
        super.onLayout(changed, l, t, r, b);
    }

    /**
     * 图片是否已达最大
     */
    private boolean isPhotoFulled;

    /**
     * 图片最大数量
     */
    private int max = 9;

    /**
     * 设置图片最大数量
     *
     * @param max
     */
    public void setMax(int max) {
        this.max = max;
        if (photoBase64s != null) {
            photoBase64s = null;
        }
        photoBase64s = new String[max];
    }

    /**
     * 默认添加图路径
     */
    private String defaultPhotoPath = "default_photo_path";

    /**
     * 默认添加图资源id
     */
    private int defaultPhotoRes = R.drawable.btn_addpic_yes;

    /**
     * 设置默认添加图
     *
     * @param defaultPhotoRes
     */
    public void setDefaultAddRes(int defaultPhotoRes) {
        this.defaultPhotoRes = defaultPhotoRes;
    }

    /**
     * 是否显示添加按钮
     */
    private boolean isUploadByGridView = true;

    /**
     * 设置是否显示添加按钮
     *
     * @param isUploadByGridView
     */
    public void setUploadByGridView(boolean isUploadByGridView) {
        this.isUploadByGridView = isUploadByGridView;
        if (isUploadByGridView) {
            defaultPhotoRes = R.drawable.btn_addpic_yes;
        } else {
            defaultPhotoRes = R.drawable.btn_addpic_no;
        }
    }

    /**
     * 是否转base64
     */
    private boolean isToBase64 = true;

    /**
     * 是否转base64
     *
     * @param isToBase64
     */
    public void setToBase64(boolean isToBase64) {
        this.isToBase64 = isToBase64;
    }

    /**
     * 是否带拍照
     */
    private boolean isCamera;

    /**
     * 设置是否有拍照
     *
     * @param camera
     */
    public void setCamera(boolean camera) {
        isCamera = camera;
    }

    /**
     * 是否带图片
     */
    private boolean isImage;

    /**
     * 设置是否有图片
     *
     * @param image
     */
    public void setImage(boolean image) {
        isImage = image;
    }

    /**
     * 是否带视频
     */
    private boolean isVideo;

    /**
     * 视频最大时长（单位秒）
     */
    private int videoDuration;

    /**
     * 设置是否有视频(暂时只能选择一个视频)
     *
     * @param video
     */
    public void setVideo(boolean video) {
        isVideo = video;
    }

    /**
     * 设置视频最大时长（单位秒）
     *
     * @param videoDuration 视频最大时长大于0默认有视频
     */
    public void setVideoDuration(int videoDuration) {
        this.videoDuration = videoDuration;
        setVideo(this.videoDuration > 0);
    }

    /**
     * @return 视频最大时长
     */
    public int getVideoDuration() {
        return videoDuration;
    }

    /**
     * 获取图片路径集合
     *
     * @return
     */
    public ArrayList<String> getPhotoPaths() {
        ArrayList<String> paths = new ArrayList<>();
        if (photoPaths != null && photoPaths.size() > 0) {
            for (int i = 0; i < getPhotoSize(); i++) {
                paths.add(photoPaths.get(i));
            }
        }
        return paths;
    }

    /**
     * 获取图片uri
     *
     * @return
     */
    public ArrayList<Uri> getImageUris() {
        ArrayList<Uri> imageUris = new ArrayList<>();
        if (photoPaths != null && photoPaths.size() > 0) {
            for (int i = 0; i < getPhotoSize(); i++) {
                imageUris.add(Uri.fromFile(new File(photoPaths.get(i))));
            }
        }
        return imageUris;
    }

    /**
     * 获取图片数量
     *
     * @return
     */
    public int getPhotoSize() {
        int size = 0;
        if (photoPaths != null && photoPaths.size() > 0) {
            if (isPhotoFulled) {
                size = photoPaths.size();
            } else {
                size = photoPaths.size() - 1;
            }
        }
        return size;
    }

    /**
     * 添加数据（图片或视频）
     *
     * @param data
     */
    public void addData(Intent data) {
        if (data != null) {
            if (getVisibility() != View.VISIBLE) {
                setVisibility(View.VISIBLE);
            }
            Image image = data.getParcelableExtra(ImageSelectorUtils.SELECT_VIDEO);
            if (image != null) {
                addVideo(image);
            } else {
                ArrayList<String> photos = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT);
                addPhotos(photos);
            }
        }
    }

    /**
     * 添加图片
     *
     * @param photos
     */
    public void addPhotos(ArrayList<String> photos) {
        if (photos != null && photos.size() > 0) {
            if (getVisibility() != View.VISIBLE) {
                setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < photos.size(); i++) {
                String photoPath = photos.get(i);
                photoPaths.add(photoPaths.size() - 1, photoPath);
                if (photoPaths.size() == max + 1) {
                    photoPaths.remove(max);
                    isPhotoFulled = true;
                    break;
                }
            }
            addBase64Photos();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 添加视频
     *
     * @param url 视频地址
     */
    public void addVideo(final String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.startsWith("http")) {
                String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()//
                        + File.separator + "DCIM/Camera";
                String filename = url.substring(url.lastIndexOf("/") + 1);
                final File file = new File(dirPath, filename);
                if (file.exists()) {
                    scanFile(file);
                } else {
                    PRDownloader.download(url, dirPath, filename).build()
                            .start(new com.downloader.OnDownloadListener() {

                                @Override
                                public void onError(com.downloader.Error error) {

                                }

                                @Override
                                public void onDownloadComplete() {
                                    scanFile(file);
                                }
                            });
                }
            } else {
                scanFile(new File(url));
            }
        }
    }

    /**
     * 通知媒体库更新文件
     *
     * @param file
     */
    private void scanFile(final File file) {
        if (file != null && file.exists()) {
            Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            scanIntent.setData(Uri.fromFile(file));
            context.sendBroadcast(scanIntent);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    getLocalVideo(file.getAbsolutePath());
                }
            }, 1000);
        }
    }

    /**
     * 获取媒体库当前视频文件
     */
    private void getLocalVideo(final String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] mediaColumns = new String[]{ //
                        MediaStore.Video.Media._ID, //
                        MediaStore.Video.Media.DISPLAY_NAME, //
                        MediaStore.Video.Media.DATA, //
                        MediaStore.Video.Media.DATE_ADDED, //
                        MediaStore.Video.Media.DURATION, //
                        MediaStore.Video.Media.SIZE};

                Cursor cursor = context.getContentResolver().query(//
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, //
                        null, null, MediaStore.Video.Media.DATE_ADDED);

                // 读取扫描到的视频
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        final long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                        final String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                        final long time = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED));
                        final String name = cursor
                                .getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                        final long duration = cursor
                                .getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                        final long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

                        if (!".downloading".equals(path) && TextUtils.equals(filePath, path)) {
                            // 过滤未下载完成的文件
                            if (activity != null) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        addVideo(new Image(id, path, time, name, duration, size));
                                    }
                                });
                            }
                            break;
                        }
                    }
                    cursor.close();
                }
            }
        }).start();
    }

    /**
     * 视频文件
     */
    private Image video;

    /**
     * 视频base64
     */
    private String base64Video = "";

    /**
     * 添加本地视频
     *
     * @param image
     */
    public void addVideo(Image image) {
        if (image == null) {
            return;
        }
        if (image.getId() == 0) {
            getLocalVideo(image.getPath());
            return;
        }
        if (getVisibility() != View.VISIBLE) {
            setVisibility(View.VISIBLE);
        }
        clearPhotos();
        video = image;
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取视频文件
     *
     * @return
     */
    public Image getVideo() {
        return video;
    }

    /**
     * 清空视频
     */
    public void clearVideo() {
        video = null;
        base64Video = "";
        clearPhotos();
    }

    /**
     * 清空图片
     */
    public void clearPhotos() {
        isPhotoFulled = false;
        if (photoBase64s != null) {
            photoBase64s = null;
        }
        photoBase64s = new String[max];
        if (photoPaths == null) {
            photoPaths = new ArrayList<>();
        }
        photoPaths.clear();
        photoPaths.add(defaultPhotoPath);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 添加base64图
     */
    private void addBase64Photos() {
        if (isToBase64) {
            // 清空
            if (photoBase64s == null) {
                photoBase64s = new String[max];
            } else {
                for (int i = 0; i < photoBase64s.length; i++) {
                    photoBase64s[i] = "";
                }
            }
            if (photoPaths != null && photoPaths.size() > 0) {
                for (int i = 0; i < photoPaths.size(); i++) {
                    String photoPath = photoPaths.get(i);
                    if (!TextUtils.equals(photoPath, defaultPhotoPath)) {
                        if (photoPath.startsWith("http")) {
                            // 网络图片转base64
                            urlToBitmap(i, photoPath);
                        } else {
                            // 本地图片转base64
                            pathToBitmap(i, photoPath);
                        }
                    }
                }
            }
        }
    }

    /**
     * 本地图片转bitmap
     *
     * @param position
     * @param path
     */
    private void pathToBitmap(final int position, final String path) {
        showLoading(context.getResources().getString(R.string.loading_picture));
        new Thread(new Runnable() {

            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                Bitmap bitmap = null;
                BufferedInputStream in = null;
                try {
                    in = new BufferedInputStream(new FileInputStream(new File(path)));
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inPreferredConfig = Bitmap.Config.RGB_565;
                    opt.inPurgeable = true;
                    opt.inInputShareable = true;
                    bitmap = BitmapFactory.decodeStream(in, null, opt);
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(9999);
                } finally {
                    if (bitmap != null) {
                        compressImageLuban(position, bitmap);
                    }
                }
            }
        }).start();
    }

    /**
     * 网络的url转bitmap
     *
     * @param position
     * @param url
     */
    private void urlToBitmap(final int position, final String url) {
        showLoading(context.getResources().getString(R.string.loading_picture));
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (url != null) {
                    Bitmap bitmap = null;
                    InputStream in = null;
                    BufferedOutputStream out = null;
                    try {
                        // 读取图片输入流
                        in = new BufferedInputStream(new URL(url).openStream(), 2 * 1024);
                        // 准备输出流
                        final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                        out = new BufferedOutputStream(dataStream, 2 * 1024);
                        byte[] b = new byte[1024];
                        int read;
                        // 将输入流变为输出流
                        while ((read = in.read(b)) != -1) {
                            out.write(b, 0, read);
                        }
                        out.flush();
                        // 将输出流转换为bitmap
                        byte[] data = dataStream.toByteArray();
                        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        data = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(9999);
                    } finally {
                        if (bitmap != null) {
                            compressImageLuban(position, bitmap);
                        }
                    }
                }
            }
        }).start();
    }

    /**
     * bitmap压缩（鲁班压缩法）
     *
     * @param position
     * @param image
     */
    private void compressImageLuban(final int position, final Bitmap image) {
        File file = bitmapToFile(image);
        if (file != null) {
            Luban.with(context)//
                    .load(file)//
                    .setTargetDir(file.getParent())//
                    .setCompressListener(new OnCompressListener() {

                        @Override
                        public void onSuccess(File file) {
                            Bitmap bitmap = fileToBitmap(file);
                            if (bitmap != null) {
                                bitmapToBase64(position, bitmap);
                            } else {
                                handler.sendEmptyMessage(9999);
                            }
                        }

                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            handler.sendEmptyMessage(9999);
                        }
                    }).launch();
        } else {
            handler.sendEmptyMessage(9999);
        }
    }

    /**
     * base64是否转换完成
     *
     * @return
     */
    public boolean isBase64Complete() {
        boolean isComplete = false;
        if (photoBase64s != null) {
            int size = 0;
            for (int i = 0; i < photoBase64s.length; i++) {
                if (!TextUtils.isEmpty(photoBase64s[i])) {
                    size++;
                }
            }
            if (size > 0) {
                if (TextUtils.equals(defaultPhotoPath, photoPaths.get(photoPaths.size() - 1))) {
                    // 最后一张是默认图
                    if (photoPaths.size() - 1 == size) {
                        isComplete = true;
                    }
                } else {
                    if (photoPaths.size() == size) {
                        isComplete = true;
                    }
                }
            }
        }
        return isComplete;
    }

    /**
     * 获取base64视频
     *
     * @return
     */
    public void getBase64Video(final Activity mActivity, final GetBase64VideoListener listener) {
        if (isToBase64) {
            if (mActivity != null && listener != null) {
                if (video == null) {
                    listener.getBase64Video("");
                } else {
                    if (TextUtils.isEmpty(base64Video)) {
                        base64Video = "";
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    File file = new File(video.getPath());
                                    FileInputStream inputFile = new FileInputStream(file);
                                    byte[] buffer = new byte[(int) file.length()];
                                    inputFile.read(buffer);
                                    inputFile.close();
                                    base64Video = Base64.encodeToString(buffer, Base64.DEFAULT);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    mActivity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            listener.getBase64Video(base64Video);
                                        }
                                    });
                                }
                            }
                        }).start();
                    } else {
                        listener.getBase64Video(base64Video);
                    }
                }
            }
        }
    }

    /**
     * 获取base64图片，多个用sign隔开
     *
     * @param mActivity
     * @param sign
     * @param listener
     */
    public void getBase64Photos(final Activity mActivity, final String sign, final GetBase64PhotosListener listener) {
        if (isToBase64) {
            if (mActivity != null && !TextUtils.isEmpty(sign) && listener != null) {
                new Thread(new Runnable() {
                    public void run() {
                        String result = "";
                        if (isBase64Complete()) {
                            for (int i = 0; i < photoBase64s.length; i++) {
                                if (!TextUtils.isEmpty(photoBase64s[i])) {
                                    result = result + photoBase64s[i] + sign;
                                    // result = result + i + sign;
                                }
                            }
                        }
                        if (!TextUtils.isEmpty(result) && result.endsWith(sign)) {
                            result = result.substring(0, result.length() - 1);
                        }
                        final String base64 = result;
                        mActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                listener.getBase64Photos(base64);
                            }
                        });
                    }
                }).start();
            }
        }
    }

    /**
     * bitmap转base64
     *
     * @param bitmap
     * @return
     */
    private void bitmapToBase64(int position, Bitmap bitmap) {
        String base64 = "";
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                base64 = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(9999);
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                handler.sendEmptyMessage(9999);
            }
            if (bitmap != null) {
                bitmap.recycle();
            }
            Message msg = handler.obtainMessage(position, base64);
            handler.sendMessage(msg);
        }
    }

    /**
     * bitmap转file
     *
     * @param bitmap
     * @return
     */
    public File bitmapToFile(Bitmap bitmap) {
        File file = null;
        file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/DCIM/Camera", System.currentTimeMillis() + ".jpeg");
        file.getParentFile().mkdirs();
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        } catch (Exception e) {
            e.printStackTrace();
            handler.sendEmptyMessage(9999);
        } finally {
            try {
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                handler.sendEmptyMessage(9999);
            }
        }
        return file;
    }

    /**
     * file转bitmap
     *
     * @param file
     * @return
     */
    @SuppressWarnings("deprecation")
    public Bitmap fileToBitmap(File file) {
        Bitmap bitmap = null;
        if (file != null && file.exists()) {
            try {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                opt.inPurgeable = true;
                opt.inInputShareable = true;
                bitmap = BitmapFactory.decodeFile(file.getPath(), opt);
            } catch (Exception e) {
                e.printStackTrace();
                handler.sendEmptyMessage(9999);
            }
        }
        return bitmap;
    }

    /**
     * 加载图片
     */
    private PhotoPickerImageLoader imageLoader;

    /**
     * 加载图片(此方法必须调用)
     *
     * @param imageLoader
     */
    public void setImageLoader(PhotoPickerImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    /**
     * 加载图片
     */
    public interface PhotoPickerImageLoader extends Serializable {

        /**
         * 加载图片
         *
         * @param path
         * @param iv
         */
        void loadImage(String path, ImageView iv);

        /**
         * 选择图片的请求码
         *
         * @return
         */
        int getRequestCode();

        /**
         * 展示图片的布局
         *
         * @return
         */
        int getLayoutId();

        /**
         * 删除图片
         */
        void deletePic();

        /**
         * 是否自定义点击事件
         *
         * @return
         */
        boolean isUploadSelf();

        /**
         * 自定义点击事件
         */
        void onClickAddPic();

    }

    /**
     * 默认加载图片
     */
    public abstract static class DefaultPhotoPickerImageLoader implements PhotoPickerImageLoader {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        @Override
        public int getLayoutId() {
            return 0;
        }

        @Override
        public void deletePic() {

        }

        @Override
        public boolean isUploadSelf() {
            return false;
        }

        @Override
        public void onClickAddPic() {

        }
    }

    /**
     * 是否是Fragment
     */
    private Fragment fragment;

    /**
     * 设置是否是Fragment
     *
     * @param fragment
     */
    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    /**
     * 选择照片
     */
    public void uploadPics() {
        if (isPhotoFulled) {
            Toast mToast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
            mToast.setText(getResources().getString(R.string.most_choose) + max + context.getResources().getString(R.string.one_piece));
            mToast.show();
        } else {
            if (imageLoader != null) {
                if (fragment != null) {
                    ImageSelectorUtils.openPhoto(fragment, imageLoader.getRequestCode(), false,
                            max - photoPaths.size() + 1, isCamera, isImage, isVideo, videoDuration);
                } else if (activity != null) {
                    ImageSelectorUtils.openPhoto(activity, imageLoader.getRequestCode(), false,
                            max - photoPaths.size() + 1, isCamera, isImage, isVideo, videoDuration);
                }
            }
        }
    }

    /**
     * 加载框
     */
    private ProgressDialog mLoading = null;

    /**
     * 显示加载框
     *
     * @param message
     */
    private void showLoading(CharSequence message) {
        if (mLoading == null) {
            mLoading = new ProgressDialog(getContext());
        }
        mLoading.setCancelable(false);
        mLoading.setCanceledOnTouchOutside(false);
        mLoading.setMessage(message);
        if (!mLoading.isShowing()) {
            mLoading.show();
        }
    }

    /**
     * 隐藏加载框
     */
    private void dismissLoading() {
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
            mLoading = null;
        }
    }

    /**
     * 适配器
     */
    public class PhotoPickerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return video == null ? photoPaths == null ? 0 : photoPaths.size() : 1;
        }

        @Override
        public Object getItem(int position) {
            return video == null ? photoPaths == null ? null : photoPaths.get(position) : video;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                if (imageLoader != null && imageLoader.getLayoutId() != 0) {
                    convertView = LayoutInflater.from(context).inflate(imageLoader.getLayoutId(), parent, false);
                } else {
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_photo_picker_grid_view, parent, false);
                }
                holder = new ViewHolder();
                holder.iv_picker_add = (ImageView) convertView.findViewById(R.id.iv_picker_add);
                holder.iv_picker_del = (ImageView) convertView.findViewById(R.id.iv_picker_del);
                holder.iv_video_logo = (ImageView) convertView.findViewById(R.id.iv_video_logo);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (isOnMeasure) {
                // 如果是onMeasure调用的就立即返回
                return convertView;
            }
            if (holder.iv_picker_del == null || holder.iv_picker_add == null)
                return convertView;
            if (video == null) {
                holder.iv_video_logo.setVisibility(View.GONE);
                String photoPath = photoPaths.get(position);
                if (position == getCount() - 1 && !isPhotoFulled) {
                    // 是最后一个条目且图片未满
                    holder.iv_picker_del.setVisibility(View.INVISIBLE);
                    holder.iv_picker_add.setImageResource(defaultPhotoRes);
                } else {
                    holder.iv_picker_del.setVisibility(View.VISIBLE);
                    if (imageLoader != null) {
                        imageLoader.loadImage(photoPath, holder.iv_picker_add);
                    }
                }
            } else {
                holder.iv_video_logo.setVisibility(View.VISIBLE);
                holder.iv_picker_del.setVisibility(View.VISIBLE);
                Glide.with(context).load(new File(video.getPath()))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(holder.iv_picker_add);
            }
            holder.iv_video_logo.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //播放视频
                    Intent intent = new Intent();
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    File file = new File(video.getPath());
                    Uri uri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri contentUri = FileProvider.getUriForFile(context,
                                context.getPackageName() + ".fileprovider", file);
                        intent.setDataAndType(contentUri, "video/*");
                    } else {
                        uri = Uri.fromFile(file);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setDataAndType(uri, "video/*");
                    }
                    context.startActivity(intent);
                }
            });
            holder.iv_picker_add.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (video == null) {
                        if (position == getCount() - 1 && !isPhotoFulled) {
                            // 上传
                            if (isUploadByGridView) {
                                if (imageLoader != null && imageLoader.isUploadSelf()) {
                                    imageLoader.onClickAddPic();
                                } else {
                                    uploadPics();
                                }
                            }
                        } else {
                            // 预览
                            ArrayList<String> images = new ArrayList<>();
                            for (int i = 0; i < photoPaths.size(); i++) {
                                if (i == photoPaths.size() - 1) {
                                    if (isPhotoFulled) {
                                        images.add(photoPaths.get(i));
                                    }
                                } else {
                                    images.add(photoPaths.get(i));
                                }
                            }
                            context.startActivity(new Intent(context, ShowBigImageListActivity.class)
                                    .putExtra("page", position)
                                    .putStringArrayListExtra("images", images));
                        }
                    } else {

                    }
                }
            });
            holder.iv_picker_del.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (video == null) {
                        photoPaths.remove(position);
                        addBase64Photos();
                        if (isPhotoFulled) {
                            photoPaths.add(defaultPhotoPath);
                            isPhotoFulled = false;
                        }
                        notifyDataSetChanged();
                    } else {
                        //删除视频
                        clearVideo();
                    }
                    if (imageLoader != null) {
                        imageLoader.deletePic();
                    }
                }
            });
            return convertView;
        }

        public class ViewHolder {
            ImageView iv_picker_add;
            ImageView iv_picker_del;
            ImageView iv_video_logo;
        }

    }

    static class MyHandler extends Handler {

        private WeakReference<PhotoPickerGridView> mOuter;

        public MyHandler(PhotoPickerGridView activity) {
            mOuter = new WeakReference<PhotoPickerGridView>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PhotoPickerGridView outer = mOuter.get();
            if (outer != null) {// Do something with outer as your wish.
                if (msg.what == 9999) {
                    // 发生异常
                    outer.dismissLoading();
                } else {
                    if (TextUtils.equals(outer.defaultPhotoPath,
                            outer.photoPaths.get(outer.photoPaths.size() - 1))) {
                        // 最后一张是默认图
                        if (outer.photoPaths.size() - 2 == msg.what) {
                            outer.dismissLoading();
                        }
                    } else {
                        if (outer.photoPaths.size() - 1 == msg.what) {
                            outer.dismissLoading();
                        }
                    }
                    if (outer.photoBase64s != null) {
                        outer.photoBase64s[msg.what] = (String) msg.obj;
                    }
                }
            }
        }
    }

    /**
     * 获取图片base64
     */
    public interface GetBase64PhotosListener {

        void getBase64Photos(String base64);
    }

    /**
     * 获取视频base64
     */
    public interface GetBase64VideoListener {

        void getBase64Video(String base64);
    }

}
