package com.cjt2325.cameralibrary.local;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 本地视频
 */
public class LocalVideoFragment extends Fragment {

    private GridView mGridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mGridView = new GridView(getActivity());
        initView();
        return mGridView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initView() {
        mGridView.setNumColumns(3);
        mGridView.setBackgroundColor(0xfff6f6f6);
        int Spacing = dip2px(getActivity(), 2.0f);
        mGridView.setVerticalSpacing(Spacing);
        mGridView.setHorizontalSpacing(Spacing);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (videoList != null && videoList.size() > 0) {
                    LocalVideo video = videoList.get(position);
                    Intent intent = new Intent();
                    intent.putExtra("video_path", video.file_path);
                    if (video.thumbnail != null)
                        intent.putExtra("video_path_img", bitmapToFile(video.thumbnail).getAbsolutePath());
                    intent.putExtra("video_duration", video.duration);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
            }
        });
    }

    private List<LocalVideo> videoList;
    private LocalVideoAdapter adapter;

    private void initData() {
        videoList = new ArrayList<>();
        new Thread(new Runnable() {
            public void run() {
                Cursor cursor = null;
                try {
                    String[] mediaColumns = new String[]{ //
                            MediaStore.Video.Media._ID, //
                            MediaStore.Video.Media.TITLE, //
                            MediaStore.Video.Media.DISPLAY_NAME, //
                            MediaStore.Video.Media.DATA, //
                            MediaStore.Video.Media.DURATION, //
                            MediaStore.Video.Media.SIZE};
                    cursor = getActivity().getContentResolver().query(//
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediaColumns, //
                            null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            LocalVideo info = new LocalVideo();
                            info.id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                            info.title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                            info.display_name = cursor
                                    .getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                            info.file_path = cursor
                                    .getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                            info.duration = cursor
                                    .getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                            info.size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                            info.time = LongToHms(info.duration);
                            info.fnum = LongToPoint(info.size);
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inDither = false;
                            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                            info.thumbnail = (MediaStore.Video.Thumbnails.getThumbnail(getActivity().getContentResolver(), info.id,
                                    MediaStore.Images.Thumbnails.MINI_KIND, options));
                            if (info.duration > 0 && info.duration <= 10 * 1000) {
                                // 10秒以内
                                if (info.size > 0 && info.size <= 2 * 1000 * 1000) {
                                    // 2Mb以内
                                    // videoList.add(0, info);
                                }
                                // videoList.add(0, info);
                            }
                            videoList.add(0, info);
                        } while (cursor.moveToNext());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        cursor.close();
                        cursor = null;
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            adapter = new LocalVideoAdapter(getActivity(), videoList);
                            mGridView.setAdapter(adapter);
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * dp转px
     */
    private int dip2px(Context context, float dipValue) {
        if (context == null)
            return 0;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    // 将长整型转换成时间格式
    private String LongToHms(long duration) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
        return dateFormat.format(duration);
    }

    // 将长整型转换成带两位小数点的string格式
    private String LongToPoint(long size) {
        float i = Float.parseFloat(String.valueOf(size));
        DecimalFormat fnum = new DecimalFormat("##0.00");
        if (i / 1024 / 1024 > 500) {
            return fnum.format(i / 1024 / 1024 / 1024) + " G";
        } else {
            return fnum.format(i / 1024 / 1024) + " M";
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
        file = new File(getMyCacheDir() + File.separator + "LocalVideo", System.currentTimeMillis() + ".jpeg");
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
     * 获取视频缓存文件夹
     *
     * @return
     */
    public String getMyCacheDir() {
        Context context = getActivity();
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            return context.getExternalCacheDir().getPath();
        } else {
            return context.getCacheDir().getPath();
        }
    }
}
