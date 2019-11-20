package cn.appoa.wximageselector;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.appoa.wximageselector.adapter.FolderAdapter;
import cn.appoa.wximageselector.adapter.ImageAdapter;
import cn.appoa.wximageselector.constant.Constants;
import cn.appoa.wximageselector.entry.Folder;
import cn.appoa.wximageselector.entry.Image;
import cn.appoa.wximageselector.model.ImageModel;
import cn.appoa.wximageselector.utils.DateUtils;
import cn.appoa.wximageselector.utils.ImageSelectorUtils;

import static android.app.Activity.RESULT_OK;

public class ImageSelectorActivity extends AppCompatActivity {

    public static int NUM_COLUMNS_PORTRAIT = 4;//竖屏显示数量
    public static int NUM_COLUMNS_LANDSCAPE = 6;//横屏显示数量
    private TextView tvTime;
    private TextView tvFolderName;
    private TextView tvTitle;
    private TextView tvConfirm;
    private TextView tvPreview;
    private FrameLayout btnConfirm;
    private FrameLayout btnPreview;
    private FrameLayout btnCamera;
    private GridView rvImage;
    private ListView rvFolder;
    private View masking;

    private ImageAdapter mAdapter;

    private ArrayList<Folder> mFolders;
    private Folder mFolder;
    private boolean isToSettings = false;
    private static final int PERMISSION_REQUEST_CODE = 0X00000011;

    private boolean isOpenFolder;
    private boolean isShowTime;
    private boolean isInitFolder;
    private boolean isSingle;
    private boolean isCamera;
    private boolean isImage;
    private boolean isVideo;
    private int mMaxCount;
    private int videoDuration;

    private Handler mHideHandler = new Handler();
    private Runnable mHide = new Runnable() {
        @Override
        public void run() {
            hideTime();
        }
    };

    /**
     * 启动图片选择器
     *
     * @param activity
     * @param requestCode
     * @param isSingle       是否单选
     * @param maxSelectCount 图片的最大选择数量，小于等于0时，不限数量，isSingle为false时才有用。
     */
    public static void openActivity(Activity activity, int requestCode, boolean isSingle,
                                    int maxSelectCount) {
        openActivity(activity, requestCode, isSingle, maxSelectCount, false);
    }

    /**
     * 启动图片选择器
     *
     * @param activity
     * @param requestCode
     * @param isSingle       是否单选
     * @param maxSelectCount 图片的最大选择数量，小于等于0时，不限数量，isSingle为false时才有用。
     */
    public static void openActivity(Activity activity, int requestCode, boolean isSingle,
                                    int maxSelectCount, boolean isCamera) {
        openActivity(activity, requestCode, isSingle, maxSelectCount, isCamera, false);
    }

    /**
     * 启动图片视频选择器
     *
     * @param activity
     * @param requestCode
     * @param isSingle       是否单选
     * @param maxSelectCount 图片的最大选择数量，小于等于0时，不限数量，isSingle为false时才有用。
     */
    public static void openActivity(Activity activity, int requestCode, boolean isSingle,
                                    int maxSelectCount, boolean isCamera, boolean isVideo) {
        openActivity(activity, requestCode, isSingle, maxSelectCount, isCamera, isVideo, 0);
    }

    /**
     * 启动图片视频选择器
     *
     * @param activity
     * @param requestCode
     * @param isSingle       是否单选
     * @param maxSelectCount 图片的最大选择数量，小于等于0时，不限数量，isSingle为false时才有用。
     */
    public static void openActivity(Activity activity, int requestCode, boolean isSingle,
                                    int maxSelectCount, boolean isCamera, boolean isVideo, int videoDuration) {
        openActivity(activity, requestCode, isSingle, maxSelectCount, isCamera, true, isVideo, 0);
    }

    /**
     * 启动图片视频选择器
     *
     * @param activity
     * @param requestCode    请求码
     * @param isSingle       是否单选
     * @param maxSelectCount 最大数量
     * @param isCamera       是否可以拍照
     * @param isImage        是否可以选择图片
     * @param isVideo        是否可以选择视频
     * @param videoDuration  视频时长
     */
    public static void openActivity(Activity activity, int requestCode, boolean isSingle,
                                    int maxSelectCount, boolean isCamera, boolean isImage,
                                    boolean isVideo, int videoDuration) {
        Intent intent = new Intent(activity, ImageSelectorActivity.class);
        intent.putExtra(Constants.MAX_SELECT_COUNT, maxSelectCount);
        intent.putExtra(Constants.IS_SINGLE, isSingle);
        intent.putExtra(Constants.IS_CAMERA, isCamera);
        intent.putExtra(Constants.IS_IMAGE, isImage);
        intent.putExtra(Constants.IS_VIDEO, isVideo);
        intent.putExtra(Constants.VIDEO_DURATION, videoDuration);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 启动图片视频选择器
     *
     * @param fragment
     * @param requestCode    请求码
     * @param isSingle       是否单选
     * @param maxSelectCount 最大数量
     * @param isCamera       是否可以拍照
     * @param isImage        是否可以选择图片
     * @param isVideo        是否可以选择视频
     * @param videoDuration  视频时长
     */
    public static void openActivity(Fragment fragment, int requestCode, boolean isSingle,
                                    int maxSelectCount, boolean isCamera, boolean isImage,
                                    boolean isVideo, int videoDuration) {
        Intent intent = new Intent(fragment.getActivity(), ImageSelectorActivity.class);
        intent.putExtra(Constants.MAX_SELECT_COUNT, maxSelectCount);
        intent.putExtra(Constants.IS_SINGLE, isSingle);
        intent.putExtra(Constants.IS_CAMERA, isCamera);
        intent.putExtra(Constants.IS_IMAGE, isImage);
        intent.putExtra(Constants.IS_VIDEO, isVideo);
        intent.putExtra(Constants.VIDEO_DURATION, videoDuration);
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_image_select);

        Intent intent = getIntent();
        mMaxCount = intent.getIntExtra(Constants.MAX_SELECT_COUNT, 0);
        isSingle = intent.getBooleanExtra(Constants.IS_SINGLE, false);
        isCamera = intent.getBooleanExtra(Constants.IS_CAMERA, false);
        isImage = intent.getBooleanExtra(Constants.IS_IMAGE, true);
        isVideo = intent.getBooleanExtra(Constants.IS_VIDEO, false);
        videoDuration = intent.getIntExtra(Constants.VIDEO_DURATION, 0);

        if (!isImage) {//如果不能选图片，拍照也禁用
            isCamera = false;
        }

        setStatusBarColor();
        initView();
        initListener();
        initImageList();
        checkPermissionCamera();
        checkPermissionAndLoadImages();
        hideFolderList();
        setSelectImageCount(0);
    }

    /**
     * 修改状态栏颜色
     */
    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#373c3d"));
        }
    }

    private void initView() {
        rvImage = (GridView) findViewById(R.id.rv_image);
        rvFolder = (ListView) findViewById(R.id.rv_folder);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvTitle.setText(isVideo ? isImage ? R.string.image_and_video : R.string.video : R.string.image);
        tvConfirm = (TextView) findViewById(R.id.tv_confirm);
        tvPreview = (TextView) findViewById(R.id.tv_preview);
        btnConfirm = (FrameLayout) findViewById(R.id.btn_confirm);
        btnPreview = (FrameLayout) findViewById(R.id.btn_preview);
        btnCamera = (FrameLayout) findViewById(R.id.btn_camera);
        tvFolderName = (TextView) findViewById(R.id.tv_folder_name);
        tvTime = (TextView) findViewById(R.id.tv_time);
        masking = findViewById(R.id.masking);
    }

    private void initListener() {
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Image> images = new ArrayList<>();
                images.addAll(mAdapter.getSelectImages());
                toPreviewActivity(images, 0);
            }
        });

        btnCamera.setVisibility(isCamera ? View.VISIBLE : View.INVISIBLE);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拍照
                takePhoto();
            }
        });
        //暂时取消按钮拍照，拍照放在相册中
        btnCamera.setVisibility(View.INVISIBLE);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });

        findViewById(R.id.btn_folder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInitFolder) {
                    if (isOpenFolder) {
                        closeFolder();
                    } else {
                        openFolder();
                    }
                }
            }
        });

        masking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFolder();
            }
        });

        rvImage.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                changeTime();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                changeTime();
            }
        });

    }

    /**
     * 初始化图片列表
     */
    private void initImageList() {
        // 判断屏幕方向
        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            rvImage.setNumColumns(NUM_COLUMNS_PORTRAIT);
        } else {
            rvImage.setNumColumns(NUM_COLUMNS_LANDSCAPE);
        }
        mAdapter = new ImageAdapter(this, mMaxCount, isSingle);
        rvImage.setAdapter(mAdapter);
        if (mFolders != null && !mFolders.isEmpty()) {
            setFolder(mFolders.get(0));
        }
        mAdapter.setOnImageSelectListener(new ImageAdapter.OnImageSelectListener() {
            @Override
            public void OnImageSelect(Image image, boolean isSelect, int selectCount) {
                setSelectImageCount(selectCount);
            }
        });
        mAdapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Image image, int position) {
                if (image.isVideo()) {
                    if (videoDuration > 0 && image.getDuration() > (videoDuration + 1) * 1000L) {
                        //超过最大时长，需要裁剪，这里+1秒是防止四舍五入，留出1秒时间
                        startActivityForResult(new Intent(ImageSelectorActivity.this,
                                ClipVideoActivity.class).putExtra("video", image)
                                .putExtra("videoDuration", videoDuration), Constants.VIDEO_CODE);
                    } else {
                        // 点击视频，把选中的视频通过Intent传给上一个Activity。
                        Intent intent = new Intent();
                        intent.putExtra(ImageSelectorUtils.SELECT_VIDEO, image);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                } else {
                    if (image.getId() == -1) {
                        //拍照
                        takePhoto();
                    } else {
                        //点击图片预览
                        toPreviewActivity(mAdapter.getImageData(), mAdapter.getImagePosition(position));
                    }
                }
            }
        });
    }

    /**
     * 初始化图片文件夹列表
     */
    private void initFolderList() {
        if (mFolders != null && !mFolders.isEmpty()) {
            isInitFolder = true;
            FolderAdapter adapter = new FolderAdapter(ImageSelectorActivity.this, mFolders);
            adapter.setOnFolderSelectListener(new FolderAdapter.OnFolderSelectListener() {
                @Override
                public void OnFolderSelect(Folder folder) {
                    setFolder(folder);
                    closeFolder();
                }
            });
            rvFolder.setAdapter(adapter);
        }
    }

    /**
     * 刚开始的时候文件夹列表默认是隐藏的
     */
    private void hideFolderList() {
        rvFolder.post(new Runnable() {
            @Override
            public void run() {
                rvFolder.setTranslationY(rvFolder.getHeight());
                rvFolder.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 设置选中的文件夹，同时刷新图片列表
     *
     * @param folder
     */
    private void setFolder(Folder folder) {
        if (folder != null && mAdapter != null && !folder.equals(mFolder)) {
            mFolder = folder;
            tvFolderName.setText(folder.getName());
            rvImage.smoothScrollToPositionFromTop(0, 0);
            mAdapter.refresh(folder.getImages());
        }
    }

    private void setSelectImageCount(int count) {
        if (count == 0) {
            btnConfirm.setEnabled(false);
            btnPreview.setEnabled(false);
            tvConfirm.setText(R.string.confirm);
            tvPreview.setText(R.string.preview);
        } else {
            btnConfirm.setEnabled(true);
            btnPreview.setEnabled(true);
            tvPreview.setText(getResources().getString(R.string.preview) + "(" + count + ")");
            if (isSingle) {
                tvConfirm.setText(R.string.confirm);
            } else if (mMaxCount > 0) {
                tvConfirm.setText(getResources().getString(R.string.confirm) + "(" + count + "/" + mMaxCount + ")");
            } else {
                tvConfirm.setText(getResources().getString(R.string.confirm) + "(" + count + ")");
            }
        }
    }

    /**
     * 弹出文件夹列表
     */
    private void openFolder() {
        if (!isOpenFolder) {
            masking.setVisibility(View.VISIBLE);
            ObjectAnimator animator = ObjectAnimator.ofFloat(rvFolder, "translationY", rvFolder.getHeight(), 0)
                    .setDuration(300);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    rvFolder.setVisibility(View.VISIBLE);
                }
            });
            animator.start();
            isOpenFolder = true;
        }
    }

    /**
     * 收起文件夹列表
     */
    private void closeFolder() {
        if (isOpenFolder) {
            masking.setVisibility(View.GONE);
            ObjectAnimator animator = ObjectAnimator.ofFloat(rvFolder, "translationY", 0, rvFolder.getHeight())
                    .setDuration(300);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    rvFolder.setVisibility(View.GONE);
                }
            });
            animator.start();
            isOpenFolder = false;
        }
    }

    /**
     * 隐藏时间条
     */
    private void hideTime() {
        if (isShowTime) {
            ObjectAnimator.ofFloat(tvTime, "alpha", 1, 0).setDuration(300).start();
            isShowTime = false;
        }
    }

    /**
     * 显示时间条
     */
    private void showTime() {
        if (!isShowTime) {
            ObjectAnimator.ofFloat(tvTime, "alpha", 0, 1).setDuration(300).start();
            isShowTime = true;
        }
    }

    /**
     * 改变时间条显示的时间（显示图片列表中的第一个可见图片的时间）
     */
    private void changeTime() {
        if (mAdapter == null) {
            return;
        }
        int firstVisibleItem = getFirstVisibleItem();
        if (firstVisibleItem >= 0 && firstVisibleItem < mAdapter.getCount()) {
            Image image = mAdapter.getData().get(firstVisibleItem);
            if (image.getId() == -1) {
                image = mAdapter.getData().get(firstVisibleItem + 1);
            }
            String time = DateUtils.getImageTime(image.getTime() * 1000);
            tvTime.setText(time);
            showTime();
            mHideHandler.removeCallbacks(mHide);
            mHideHandler.postDelayed(mHide, 1500);
        }
    }

    private int getFirstVisibleItem() {
        return rvImage.getFirstVisiblePosition();
    }

    private void confirm() {
        if (mAdapter == null) {
            return;
        }
        // 因为图片的实体类是Image，而我们返回的是String数组，所以要进行转换。
        ArrayList<Image> selectImages = mAdapter.getSelectImages();
        ArrayList<String> images = new ArrayList<>();
        for (Image image : selectImages) {
            images.add(image.getPath());
        }

        // 点击确定，把选中的图片通过Intent传给上一个Activity。
        Intent intent = new Intent();
        intent.putStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT, images);
        setResult(RESULT_OK, intent);

        finish();
    }

    private void toPreviewActivity(ArrayList<Image> images, int position) {
        if (images != null && !images.isEmpty()) {
            PreviewActivity.openActivity(this, images, mAdapter.getSelectImages(), isSingle, mMaxCount, position);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isToSettings) {
            isToSettings = false;
            checkPermissionCamera();
            checkPermissionAndLoadImages();
        }
    }

    /**
     * 处理图片预览页返回的结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RESULT_CODE) {
            if (data != null && data.getBooleanExtra(Constants.IS_CONFIRM, false)) {
                // 如果用户在预览页点击了确定，就直接把用户选中的图片返回给用户。
                confirm();
            } else {
                // 否则，就刷新当前页面。
                mAdapter.notifyDataSetChanged();
                setSelectImageCount(mAdapter.getSelectImages().size());
            }
        }
        if (requestCode == Constants.CAMERA_CODE && resultCode == RESULT_OK) {
            //拍照结果
            if (cameraFile != null && cameraFile.exists()) {
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", cameraFile);
                } else {
                    imageUri = Uri.fromFile(cameraFile);
                }
                String imagePath = cameraFile.getAbsolutePath();
                // 点击确定，把选中的图片通过Intent传给上一个Activity。
                Intent intent = new Intent();
                ArrayList<String> images = new ArrayList<>();
                images.add(imagePath);
                intent.putStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT, images);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        if (requestCode == Constants.VIDEO_CODE && resultCode == RESULT_OK) {
            //裁剪视频成功
            if (data != null) {
                Intent intent = new Intent();
                intent.putExtra(ImageSelectorUtils.SELECT_VIDEO, data.getParcelableExtra("video"));
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }


    /**
     * 横竖屏切换处理
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (rvImage != null && mAdapter != null) {
            // 切换为竖屏
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                rvImage.setNumColumns(NUM_COLUMNS_PORTRAIT);
            }
            // 切换为横屏
            else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rvImage.setNumColumns(NUM_COLUMNS_LANDSCAPE);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 检查权限并加载SD卡里的图片。
     */
    private void checkPermissionAndLoadImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // Toast.makeText(this, "没有图片", Toast.LENGTH_LONG).show();
            return;
        }
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getApplication(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
            // 有权限，加载图片。
            loadImageForSDCard();
        } else {
            // 没有权限，申请权限。
            ActivityCompat.requestPermissions(ImageSelectorActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * 检查拍照权限。
     */
    private void checkPermissionCamera() {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(getApplication(),
                Manifest.permission.CAMERA);
        if (hasWriteContactsPermission == PackageManager.PERMISSION_GRANTED) {
            // 有权限。

        } else {
            // 没有权限，申请权限。
            ActivityCompat.requestPermissions(ImageSelectorActivity.this,
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * 处理权限申请的回调。
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 允许权限，加载图片。
                loadImageForSDCard();
            } else {
                // 拒绝权限，弹出提示框。
                showExceptionDialog();
            }
        }
    }

    /**
     * 发生没有权限等异常时，显示一个提示dialog.
     */
    private void showExceptionDialog() {
        new AlertDialog.Builder(this).setCancelable(false).setTitle(R.string.choose_hint)
                .setMessage(R.string.no_read_camera_permission)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                }).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                startAppSettings();
                isToSettings = true;
            }
        }).show();
    }

    /**
     * 从SDCard加载图片。
     */
    private void loadImageForSDCard() {
        ImageModel.loadImageForSDCard(this, isImage, isVideo, new ImageModel.DataCallback() {
            @Override
            public void onSuccess(ArrayList<Folder> folders) {
                mFolders = folders;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mFolders != null && !mFolders.isEmpty()) {
                            if (isCamera) {
                                //可拍照
                                Folder folder = mFolders.get(0);
                                if (folder.getImages() != null) {
                                    ArrayList<Image> images = folder.getImages();
                                    Image image = new Image(-1,
                                            String.valueOf(R.drawable.icon_camera),
                                            System.currentTimeMillis(),
                                            getString(R.string.camera));
                                    images.add(0, image);
                                }
                            }
                            initFolderList();
                            setFolder(mFolders.get(0));
                        }
                    }
                });
            }
        });
    }

    /**
     * 启动应用的设置
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && isOpenFolder) {
            closeFolder();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast mToast = Toast.makeText(ImageSelectorActivity.this, null, Toast.LENGTH_SHORT);
            mToast.setText(R.string.not_found_camera_sd_card);
            mToast.show();
            return;
        }
        if (Build.VERSION.SDK_INT >= 24) {
            // 7.0系统
            doTakePhotoNew();
        } else {
            doTakePhotoOld();
        }
    }

    /**
     * 拍照的临时文件
     */
    private File cameraFile;

    /**
     * 接收到的图片的uri
     */
    private Uri imageUri = null;

    /**
     * 老方法[Android7.0以及以上报错FileUriExposedException]
     */
    private void doTakePhotoOld() {
        cameraFile = getTempFile();
        if (cameraFile != null) {
            cameraFile.getParentFile().mkdirs();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
            startActivityForResult(intent, Constants.CAMERA_CODE);
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
            imageUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", cameraFile);
            List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, imageUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, Constants.CAMERA_CODE);
        }
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
}
