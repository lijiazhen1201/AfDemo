package cn.appoa.wximageselector.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

import cn.appoa.wximageselector.R;
import cn.appoa.wximageselector.ShowBigImageListActivity;
import cn.appoa.wximageselector.luban.Luban;
import cn.appoa.wximageselector.luban.OnCompressListener;
import cn.appoa.wximageselector.utils.ImageSelectorUtils;

/**
 * 图片选择的GridView
 */
public class PhotoPickerGridView extends GridView {

    public PhotoPickerGridView(Context context) {
        super(context);
        init(context);
    }

    public PhotoPickerGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PhotoPickerGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
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
     * Context
     */
    private Context context;

    /**
     * Activity
     */
    private Activity activity;

    /**
     * 图片路径集合
     */
    private ArrayList<String> photoPaths;

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
     * 图片base64数组
     */
    public String[] photoBase64s;

    /**
     * Handler
     */
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 9999) {
                // 发生异常
                dismissLoading();
            } else {
                if (TextUtils.equals(defaultPhotoPath, photoPaths.get(photoPaths.size() - 1))) {
                    // 最后一张是默认图
                    if (photoPaths.size() - 2 == msg.what) {
                        dismissLoading();
                    }
                } else {
                    if (photoPaths.size() - 1 == msg.what) {
                        dismissLoading();
                    }
                }
                if (photoBase64s != null) {
                    photoBase64s[msg.what] = (String) msg.obj;
                }
            }
        }

        ;
    };

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
     * 添加图片
     *
     * @param photos
     */
    public void addPhotos(ArrayList<String> photos) {
        if (getVisibility() != View.VISIBLE) {
            setVisibility(View.VISIBLE);
        }
        if (photos != null && photos.size() > 0) {
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
            if (adapter != null)
                adapter.notifyDataSetChanged();
        }
    }

    /**
     * 清空图片
     */
    public void clearPhotos() {
        isPhotoFulled = false;
        if (photoBase64s != null)
            photoBase64s = null;
        photoBase64s = new String[max];
        if (photoPaths == null) {
            photoPaths = new ArrayList<>();
        }
        photoPaths.clear();
        photoPaths.add(defaultPhotoPath);
        if (adapter != null)
            adapter.notifyDataSetChanged();
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
     * 图片是否已达最大
     */
    private boolean isPhotoFulled;

    /**
     * 适配器
     */
    private PhotoPickerAdapter adapter;

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        this.context = context;
        if (context instanceof Activity)
            activity = (Activity) context;
        if (photoPaths == null)
            photoPaths = new ArrayList<>();
        photoPaths.add(defaultPhotoPath);
        if (photoBase64s == null)
            photoBase64s = new String[max];
        adapter = new PhotoPickerAdapter();
        setAdapter(adapter);
    }

    /**
     * 加载图片
     */
    private PhotoPickerImageLoader imageLoader;

    /**
     * 加载图片
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
     * 选择照片
     */
    public void uploadPics() {
        if (isPhotoFulled) {
            Toast mToast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
            mToast.setText(getResources().getString(R.string.most_choose) + max
                    + context.getResources().getString(R.string.one_piece));
            mToast.show();
        } else {
            if (activity != null && imageLoader != null)
                ImageSelectorUtils.openPhoto(activity, imageLoader.getRequestCode(), false,
                        max - photoPaths.size() + 1, isCamera);
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
            return photoPaths == null ? 0 : photoPaths.size();
        }

        @Override
        public Object getItem(int position) {
            return photoPaths == null ? null : photoPaths.get(position);
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
                    convertView = LayoutInflater.from(context).inflate(imageLoader.getLayoutId(), null);
                } else {
                    convertView = LayoutInflater.from(context).inflate(R.layout.item_photo_picker_grid_view, null);
                }
                holder = new ViewHolder();
                holder.iv_picker_add = (ImageView) convertView.findViewById(R.id.iv_picker_add);
                holder.iv_picker_del = (ImageView) convertView.findViewById(R.id.iv_picker_del);
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
            holder.iv_picker_add.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
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
                                .putExtra("page", position).putStringArrayListExtra("images", images));
                    }
                }
            });
            holder.iv_picker_del.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    photoPaths.remove(position);
                    addBase64Photos();
                    if (isPhotoFulled) {
                        photoPaths.add(defaultPhotoPath);
                        isPhotoFulled = false;
                    }
                    if (imageLoader != null) {
                        imageLoader.deletePic();
                    }
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        public class ViewHolder {
            ImageView iv_picker_add;
            ImageView iv_picker_del;
        }

    }

    /**
     * bitmap转file
     *
     * @param bitmap
     * @return
     */
    private File bitmapToFile(Bitmap bitmap) {
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
    private Bitmap fileToBitmap(File file) {
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
     * 获取base64
     */
    public interface GetBase64PhotosListener {

        void getBase64Photos(String base64);
    }
}
