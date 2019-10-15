package cn.appoa.wximageselector.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;

import cn.appoa.wximageselector.R;
import cn.appoa.wximageselector.entry.Image;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Image> mImages;
    private LayoutInflater mInflater;

    // 保存选中的图片
    private ArrayList<Image> mSelectImages = new ArrayList<>();
    private OnImageSelectListener mSelectListener;
    private OnItemClickListener mItemClickListener;
    private int mMaxCount;
    private boolean isSingle;

    /**
     * @param maxCount 图片的最大选择数量，小于等于0时，不限数量，isSingle为false时才有用。
     * @param isSingle 是否单选
     */
    public ImageAdapter(Context context, int maxCount, boolean isSingle) {
        mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        mMaxCount = maxCount;
        this.isSingle = isSingle;
    }

    @Override
    public int getCount() {
        return mImages == null ? 0 : mImages.size();
    }

    @Override
    public Image getItem(int position) {
        return mImages == null ? null : mImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.adapter_images_item, parent, false);
            holder.itemView = convertView;
            holder.ivImage = (ImageView) convertView.findViewById(R.id.iv_image);
            holder.ivSelectIcon = (ImageView) convertView.findViewById(R.id.iv_select);
            holder.ivMasking = (ImageView) convertView.findViewById(R.id.iv_masking);
            holder.tvDuration = (TextView) convertView.findViewById(R.id.tv_duration);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Image image = mImages.get(position);
        holder.ivSelectIcon.setVisibility(image.isVideo() ? View.INVISIBLE : View.VISIBLE);
        holder.tvDuration.setVisibility(image.isVideo() ? View.VISIBLE : View.INVISIBLE);
        holder.tvDuration.setText(image.getDurationStr());
//        if (image.isVideo()) {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inDither = false;
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//            holder.ivImage.setImageBitmap((MediaStore.Video.Thumbnails.getThumbnail(mContext.getContentResolver(), image.getId(),
//                    MediaStore.Images.Thumbnails.MINI_KIND, options)));
//        } else {
//            Glide.with(mContext).load(new File(image.getPath())).diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .into(holder.ivImage);
//        }
        try {
            int resId = Integer.parseInt(image.getPath());
            holder.ivImage.setImageResource(resId);
            holder.ivSelectIcon.setVisibility(View.INVISIBLE);
        } catch (NumberFormatException e) {
            Glide.with(mContext).load(new File(image.getPath())).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.ivImage);
        }
        setItemSelect(holder, mSelectImages.contains(image));
        // 点击选中/取消选中图片
        final ViewHolder mHolder = holder;
        holder.ivSelectIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectImages.contains(image)) {
                    // 如果图片已经选中，就取消选中
                    unSelectImage(image);
                    setItemSelect(mHolder, false);
                } else if (isSingle) {
                    // 如果是单选，就先清空已经选中的图片，再选中当前图片
                    clearImageSelect();
                    selectImage(image);
                    setItemSelect(mHolder, true);
                } else if (mMaxCount <= 0 || mSelectImages.size() < mMaxCount) {
                    // 如果不限制图片的选中数量，或者图片的选中数量
                    // 还没有达到最大限制，就直接选中当前图片。
                    selectImage(image);
                    setItemSelect(mHolder, true);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.OnItemClick(image, position);
                }
            }
        });
        return convertView;
    }

    /**
     * 选中图片
     *
     * @param image
     */
    private void selectImage(Image image) {
        mSelectImages.add(image);
        if (mSelectListener != null) {
            mSelectListener.OnImageSelect(image, true, mSelectImages.size());
        }
    }

    /**
     * 取消选中图片
     *
     * @param image
     */
    private void unSelectImage(Image image) {
        mSelectImages.remove(image);
        if (mSelectListener != null) {
            mSelectListener.OnImageSelect(image, false, mSelectImages.size());
        }
    }

    public ArrayList<Image> getData() {
        return mImages;
    }

    public ArrayList<Image> getImageData() {
        ArrayList<Image> images = new ArrayList<>();
        for (int i = 0; i < mImages.size(); i++) {
            Image image = mImages.get(i);
            if (!image.isVideo() && image.getId() != -1) {
                images.add(image);
            }
        }
        return images;
    }

    public int getImagePosition(int index) {
        int position = 0;
        Image mImage = mImages.get(index);
        ArrayList<Image> images = getImageData();
        for (int i = 0; i < images.size(); i++) {
            Image image = images.get(i);
            if (mImage.getId() == image.getId()) {
                position = i;
                break;
            }
        }
        return position;
    }

    public void refresh(ArrayList<Image> data) {
        mImages = data;
        notifyDataSetChanged();
    }

    /**
     * 设置图片选中和未选中的效果
     */
    private void setItemSelect(ViewHolder holder, boolean isSelect) {
        if (isSelect) {
            holder.ivSelectIcon.setImageResource(R.drawable.icon_image_select);
            holder.ivMasking.setAlpha(0.5f);
        } else {
            holder.ivSelectIcon.setImageResource(R.drawable.icon_image_un_select);
            holder.ivMasking.setAlpha(0.2f);
        }
    }

    private void clearImageSelect() {
        if (mImages != null && mSelectImages.size() == 1) {
            int index = mImages.indexOf(mSelectImages.get(0));
            if (index != -1) {
                mSelectImages.clear();
                notifyDataSetChanged();
            }
        }
    }

    public ArrayList<Image> getSelectImages() {
        return mSelectImages;
    }

    public void setOnImageSelectListener(OnImageSelectListener listener) {
        this.mSelectListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnImageSelectListener {
        void OnImageSelect(Image image, boolean isSelect, int selectCount);
    }

    public interface OnItemClickListener {
        void OnItemClick(Image image, int position);
    }

    static class ViewHolder {

        View itemView;
        ImageView ivImage;
        ImageView ivSelectIcon;
        ImageView ivMasking;
        TextView tvDuration;
    }

}
