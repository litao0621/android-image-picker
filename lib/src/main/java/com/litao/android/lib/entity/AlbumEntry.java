package com.litao.android.lib.entity;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李涛 on 16/4/22.
 */
public class AlbumEntry {
    private int bucketId;
    private String bucketName;
    private PhotoEntry coverPhoto;
    private SparseArray<PhotoEntry> photoById = new SparseArray<PhotoEntry>();
    private List<PhotoEntry> photos = new ArrayList<PhotoEntry>();

    public AlbumEntry(int bucketId, String bucketName, PhotoEntry coverPhoto) {
        this.bucketId = bucketId;
        this.bucketName = bucketName;
        this.coverPhoto = coverPhoto;
    }

    public void addPhoto(PhotoEntry photoEntry) {
        photoById.put(photoEntry.getImageId(), photoEntry);
        photos.add(photoEntry);
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public int getBucketId() {
        return bucketId;
    }

    public void setBucketId(int bucketId) {
        this.bucketId = bucketId;
    }

    public PhotoEntry getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(PhotoEntry coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public SparseArray<PhotoEntry> getPhotoById() {
        return photoById;
    }

    public void setPhotoById(SparseArray<PhotoEntry> photoById) {
        this.photoById = photoById;
    }

    public List<PhotoEntry> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoEntry> photos) {
        this.photos = photos;
    }
}
