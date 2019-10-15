package cn.appoa.wximageselector.entry;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 图片实体类
 */
public class Image implements Parcelable {

    private long id;
    private String path;
    private long time;
    private String name;
    private boolean isVideo;
    private long duration;
    private long size;
    private String durationStr;
    private String sizeStr;

    public Image() {
    }

    public Image(long id, String path, long time, String name) {
        isVideo = false;
        this.id = id;
        this.path = path;
        this.time = time;
        this.name = name;
    }

    public Image(long id, String path, long time, String name, long duration, long size) {
        isVideo = true;
        this.id = id;
        this.path = path;
        this.time = time;
        this.name = name;
        this.duration = duration;
        this.size = size;
        this.durationStr = LongToHms(this.duration);
        this.sizeStr = LongToPoint(this.size);
    }

    /**
     * 将长整型转换成时间格式
     *
     * @param duration
     * @return
     */
    private String LongToHms(long duration) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
        return dateFormat.format(duration);
    }

    /**
     * 将长整型转换成带两位小数点的string格式
     *
     * @param size
     * @return
     */
    private String LongToPoint(long size) {
        float i = Float.parseFloat(String.valueOf(size));
        DecimalFormat fnum = new DecimalFormat("##0.00");
        if (i / 1024 / 1024 > 500) {
            return fnum.format(i / 1024 / 1024 / 1024) + " G";
        } else {
            return fnum.format(i / 1024 / 1024) + " M";
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDurationStr() {
        return durationStr;
    }

    public void setDurationStr(String durationStr) {
        this.durationStr = durationStr;
    }

    public String getSizeStr() {
        return sizeStr;
    }

    public void setSizeStr(String sizeStr) {
        this.sizeStr = sizeStr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeLong(this.time);
        dest.writeString(this.name);
    }

    protected Image(Parcel in) {
        this.path = in.readString();
        this.time = in.readLong();
        this.name = in.readString();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
