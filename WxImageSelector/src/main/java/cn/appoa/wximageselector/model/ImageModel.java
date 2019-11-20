package cn.appoa.wximageselector.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.appoa.wximageselector.R;
import cn.appoa.wximageselector.entry.Folder;
import cn.appoa.wximageselector.entry.Image;
import cn.appoa.wximageselector.utils.StringUtils;

public class ImageModel {

    /**
     * 从SDCard加载图片
     *
     * @param context
     * @param isImage
     * @param isVideo
     * @param callback
     */
    public static void loadImageForSDCard(final Context context, final boolean isImage,
                                          final boolean isVideo, final DataCallback callback) {
        // 由于扫描图片是耗时的操作，所以要在子线程处理。
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Image> images = new ArrayList<>();
                if (isImage) {
                    // 扫描图片
                    Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    ContentResolver mContentResolver = context.getContentResolver();

                    Cursor mCursor = mContentResolver.query(mImageUri,
                            new String[]{
                                    MediaStore.Images.Media._ID,
                                    MediaStore.Images.Media.DISPLAY_NAME,
                                    MediaStore.Images.Media.DATA,
                                    MediaStore.Images.Media.DATE_ADDED},
                            null, null, MediaStore.Images.Media.DATE_ADDED);

                    // 读取扫描到的图片
                    if (mCursor != null) {
                        while (mCursor.moveToNext()) {
                            long id = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Images.Media._ID));
                            // 获取图片的路径
                            String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                            // 获取图片名称
                            String name = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                            // 获取图片时间
                            long time = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                            if (!".downloading".equals(path)) { // 过滤未下载完成的文件
                                images.add(new Image(id, path, time, name));
                            }
                        }
                        mCursor.close();
                    }
                }
                if (isVideo) {
                    // 扫描视频
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
                            long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                            String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                            long time = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED));
                            String name = cursor
                                    .getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                            long duration = cursor
                                    .getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                            long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

                            if (!".downloading".equals(path)) { // 过滤未下载完成的文件
                                images.add(new Image(id, path, time, name, duration, size));
                            }
                        }
                        cursor.close();
                    }
                }
                //排序
                Collections.sort(images, new Comparator<Image>() {
                    @Override
                    public int compare(Image o1, Image o2) {
                        if (o1.getTime() > o2.getTime()) {
                            return -1;
                        } else if (o1.getTime() < o2.getTime()) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });
                //Collections.reverse(images);
                callback.onSuccess(splitFolder(context, isImage, isVideo, images));
            }
        }).start();
    }

    /**
     * 把图片按文件夹拆分，第一个文件夹保存所有的图片
     *
     * @param images
     * @return
     */
    private static ArrayList<Folder> splitFolder(Context context, boolean isImage, boolean isVideo, ArrayList<Image> images) {
        ArrayList<Folder> folders = new ArrayList<>();
        folders.add(new Folder(context.getResources().getString(
                isVideo ? isImage ? R.string.all_image_video :
                        R.string.all_video : R.string.all_image), images));
        if (isVideo && isImage) {
            ArrayList<Image> videos = new ArrayList<>();
            for (int i = 0; i < images.size(); i++) {
                Image image = images.get(i);
                if (image.isVideo()) {
                    videos.add(image);
                }
            }
            folders.add(new Folder(context.getResources().getString(R.string.all_video), videos));
        }
        if (images != null && !images.isEmpty()) {
            int size = images.size();
            for (int i = 0; i < size; i++) {
                String path = images.get(i).getPath();
                String name = getFolderName(path);
                if (StringUtils.isNotEmptyString(name)) {
                    Folder folder = getFolder(name, folders);
                    folder.addImage(images.get(i));
                }
            }
        }
        return folders;
    }

    /*
     * Java文件操作 获取文件扩展名
     */
    public static String getExtensionName(String filename) {
        if (filename != null && filename.length() > 0) {
            int dot = filename.lastIndexOf('.');
            if (dot > -1 && dot < filename.length() - 1) {
                return filename.substring(dot + 1);
            }
        }
        return "";
    }

    /**
     * 跟着图片路径，获取图片文件夹名称
     *
     * @param path
     * @return
     */
    private static String getFolderName(String path) {
        if (StringUtils.isNotEmptyString(path)) {
            String[] strings = path.split(File.separator);
            if (strings.length >= 2) {
                return strings[strings.length - 2];
            }
        }
        return "";
    }

    private static Folder getFolder(String name, List<Folder> folders) {
        if (!folders.isEmpty()) {
            int size = folders.size();
            for (int i = 0; i < size; i++) {
                Folder folder = folders.get(i);
                if (name.equals(folder.getName())) {
                    return folder;
                }
            }
        }
        Folder newFolder = new Folder(name);
        folders.add(newFolder);
        return newFolder;
    }

    public interface DataCallback {
        void onSuccess(ArrayList<Folder> folders);
    }

    /**
     * 获取视频封面
     *
     * @param context
     * @param image
     * @return
     */
    public static Bitmap getVideoThumbnailBitmap(Context context, Image image) {
        Bitmap bitmap = null;
        if (image != null && image.getId() > 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inDither = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = (MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(), image.getId(),
                    MediaStore.Images.Thumbnails.MINI_KIND, options));
        }
        return bitmap;
    }

    /**
     * 获取视频封面
     *
     * @param context
     * @param image
     * @return
     */
    public static File getVideoThumbnailFile(Context context, Image image) {
        File file = null;
        Bitmap bitmap = getVideoThumbnailBitmap(context, image);
        if (bitmap != null) {
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
        }
        return file;
    }

}
