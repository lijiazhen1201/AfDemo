package cn.appoa.wximageselector.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.appoa.wximageselector.R;
import cn.appoa.wximageselector.entry.Data;
import cn.appoa.wximageselector.view.range.RotateTransformation;


public class ClipVideoAdapter extends RecyclerView.Adapter<ClipVideoAdapter.Holder> {

    private Context context;
    private List<Data> dataList;
    private int imagWidth = -1;
    private String parentPath;
    /**
     * 视频的旋转角度,手机拍摄的视频有时候会有一个旋转角度，因此解析出的图片也有一个角度
     */
    private float rotation = 0;

    public ClipVideoAdapter(Context context, List<Data> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_per_image, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if (imagWidth > 0) {
            ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
            layoutParams.width = imagWidth;
            holder.imageView.setLayoutParams(layoutParams);
        }
        Glide.with(context)
                .load(parentPath + dataList.get(position).getImageName())
                .transform(new RotateTransformation(context, rotation))
                .into(holder.imageView);
    }

    public List<Data> getDataList() {
        return dataList;
    }

    public void setDataList(List<Data> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public int getImagWidth() {
        return imagWidth;
    }

    public void setImagWidth(int imagWidth) {
        this.imagWidth = imagWidth;
    }

    class Holder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public Holder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img);
        }
    }
}
