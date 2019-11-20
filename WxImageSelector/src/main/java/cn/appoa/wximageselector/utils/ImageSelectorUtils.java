package cn.appoa.wximageselector.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;
import cn.appoa.wximageselector.ClipImageActivity;
import cn.appoa.wximageselector.ImageSelectorActivity;

/**
 * 提供给外界相册的调用的工具类
 */
public class ImageSelectorUtils {

    //图片选择的结果
    public static final String SELECT_RESULT = "select_result";
    //视频选择的结果
    public static final String SELECT_VIDEO = "select_video";

    /**
     * 打开相册，选择图片,可多选,不限数量。
     *
     * @param activity
     * @param requestCode
     */
    public static void openPhoto(Activity activity, int requestCode) {
        openPhoto(activity, requestCode, false);
    }

    /**
     * 打开相册，选择图片,可多选,不限数量。
     *
     * @param activity
     * @param requestCode
     * @param isCamera
     */
    public static void openPhoto(Activity activity, int requestCode, boolean isCamera) {
        openPhoto(activity, requestCode, false, 0, isCamera);
    }

    /**
     * 打开相册，选择图片,可多选,限制最大的选择数量。
     *
     * @param activity
     * @param requestCode
     * @param isSingle       是否单选
     * @param maxSelectCount 图片的最大选择数量，小于等于0时，不限数量，isSingle为false时才有用。
     */
    public static void openPhoto(Activity activity, int requestCode,
                                 boolean isSingle, int maxSelectCount) {
        ImageSelectorActivity.openActivity(activity, requestCode, isSingle, maxSelectCount);
    }

    /**
     * 打开相册，选择图片,可多选,限制最大的选择数量。
     *
     * @param activity
     * @param requestCode
     * @param isSingle       是否单选
     * @param maxSelectCount 图片的最大选择数量，小于等于0时，不限数量，isSingle为false时才有用。
     * @param isCamera       是否有拍照按钮。
     */
    public static void openPhoto(Activity activity, int requestCode,
                                 boolean isSingle, int maxSelectCount, boolean isCamera) {
        ImageSelectorActivity.openActivity(activity, requestCode, isSingle, maxSelectCount, isCamera);
    }

    /**
     * 打开相册，选择图片,可多选,限制最大的选择数量。
     *
     * @param activity
     * @param requestCode
     * @param isSingle       是否单选
     * @param maxSelectCount 图片的最大选择数量，小于等于0时，不限数量，isSingle为false时才有用。
     * @param isCamera       是否有拍照按钮。
     */
    public static void openPhoto(Activity activity, int requestCode,
                                 boolean isSingle, int maxSelectCount, boolean isCamera, boolean isVideo) {
        ImageSelectorActivity.openActivity(activity, requestCode, isSingle, maxSelectCount, isCamera, isVideo);
    }

    /**
     * 打开相册，选择图片,可多选,限制最大的选择数量。
     *
     * @param activity
     * @param requestCode
     * @param isSingle       是否单选
     * @param maxSelectCount 图片的最大选择数量，小于等于0时，不限数量，isSingle为false时才有用。
     * @param isCamera       是否有拍照按钮。
     */
    public static void openPhoto(Activity activity, int requestCode,
                                 boolean isSingle, int maxSelectCount, boolean isCamera, boolean isVideo, int videoDuration) {
        ImageSelectorActivity.openActivity(activity, requestCode, isSingle, maxSelectCount, isCamera, isVideo, videoDuration);
    }

    /**
     * 打开相册，选择图片,可多选,限制最大的选择数量。
     *
     * @param activity
     * @param requestCode
     * @param isSingle       是否单选
     * @param maxSelectCount 图片的最大选择数量，小于等于0时，不限数量，isSingle为false时才有用。
     * @param isCamera       是否有拍照按钮。
     */
    public static void openPhoto(Activity activity, int requestCode,
                                 boolean isSingle, int maxSelectCount,
                                 boolean isCamera, boolean isImage, boolean isVideo, int videoDuration) {
        ImageSelectorActivity.openActivity(activity, requestCode, isSingle, maxSelectCount,
                isCamera, isImage, isVideo, videoDuration);
    }

    /**
     * 打开相册，选择图片,可多选,限制最大的选择数量。
     *
     * @param activity
     * @param requestCode
     * @param isSingle       是否单选
     * @param maxSelectCount 图片的最大选择数量，小于等于0时，不限数量，isSingle为false时才有用。
     * @param isCamera       是否有拍照按钮。
     */
    public static void openPhoto(Fragment fragment, int requestCode,
                                 boolean isSingle, int maxSelectCount,
                                 boolean isCamera, boolean isImage, boolean isVideo, int videoDuration) {
        ImageSelectorActivity.openActivity(fragment, requestCode, isSingle, maxSelectCount,
                isCamera, isImage, isVideo, videoDuration);
    }

    /**
     * 打开相册，单选图片并剪裁。
     *
     * @param activity
     * @param requestCode
     */
    public static void openPhotoAndClip(Activity activity, int requestCode) {
        ClipImageActivity.openActivity(activity, requestCode);
    }
}
