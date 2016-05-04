package com.litao.android.lib.entity;

/**
 * Created by 李涛 on 16/4/21.
 */
public class PhotoEntry {

    private int bucketId;
    private int imageId;
    private long dateTaken;
    private String path;
    private boolean isChecked;

    public PhotoEntry() {

    }

    public PhotoEntry(int bucketId, int imageId, long dateTaken, String path) {
        this.bucketId = bucketId;
        this.imageId = imageId;
        this.dateTaken = dateTaken;
        this.path = path;
    }

    public void toggle() {
        isChecked = !isChecked;
    }

    public int getBucketId() {
        return bucketId;
    }

    public void setBucketId(int bucketId) {
        this.bucketId = bucketId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public long getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(long dateTaken) {
        this.dateTaken = dateTaken;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
