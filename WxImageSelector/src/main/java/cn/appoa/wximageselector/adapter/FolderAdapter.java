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
import cn.appoa.wximageselector.entry.Folder;
import cn.appoa.wximageselector.entry.Image;

public class FolderAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Folder> mFolders;
    private LayoutInflater mInflater;
    private int mSelectItem;
    private OnFolderSelectListener mListener;

    public FolderAdapter(Context context, ArrayList<Folder> folders) {
        mContext = context;
        mFolders = folders;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mFolders == null ? 0 : mFolders.size();
    }

    @Override
    public Folder getItem(int position) {
        return mFolders == null ? null : mFolders.get(position);
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
            convertView = mInflater.inflate(R.layout.adapter_folder, parent, false);
            holder.itemView = convertView;
            holder.ivImage = (ImageView) convertView.findViewById(R.id.iv_image);
            holder.ivSelect = (ImageView) convertView.findViewById(R.id.iv_select);
            holder.tvFolderName = (TextView) convertView.findViewById(R.id.tv_folder_name);
            holder.tvFolderSize = (TextView) convertView.findViewById(R.id.tv_folder_size);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Folder folder = mFolders.get(position);
        ArrayList<Image> images = folder.getImages();
        holder.tvFolderName.setText(folder.getName());
        holder.ivSelect.setVisibility(mSelectItem == position ? View.VISIBLE : View.GONE);
        if (images != null && !images.isEmpty()) {
            holder.tvFolderSize.setText("(" + images.size() + ")");
            Glide.with(mContext).load(new File(images.get(0).getPath())).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.ivImage);
        } else {
            holder.tvFolderSize.setText("(0)");
            holder.ivImage.setImageBitmap(null);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectItem = position;
                notifyDataSetChanged();
                if (mListener != null) {
                    mListener.OnFolderSelect(folder);
                }
            }
        });
        return convertView;
    }

    public void setOnFolderSelectListener(OnFolderSelectListener listener) {
        this.mListener = listener;
    }

    public interface OnFolderSelectListener {
        void OnFolderSelect(Folder folder);
    }

    static class ViewHolder {

        View itemView;
        ImageView ivImage;
        ImageView ivSelect;
        TextView tvFolderName;
        TextView tvFolderSize;

    }

}
