package com.litao.android.lib.controller;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.litao.android.lib.entity.AlbumEntry;
import com.litao.android.lib.entity.PhotoEntry;
import com.litao.android.lib.view.MDCheckBox;
import com.litao.android.lib.view.SquaredView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 李涛 on 16/4/22.
 */
public class MediaController {
    private String TAG = "MediaController";

    private OnDataLoadListener listener;

    private Activity mContext;

    private static final String[] projectionPhotos = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_TAKEN
    };

    public interface OnDataLoadListener {
        void onLoadFinished(List<AlbumEntry> data);
    }

    public interface OnViewClickListener {
        void onPhotoClicked(int position, MDCheckBox checkBox, SquaredView viewShade);

        void onAlbumClicked(int position, View imageView);

        void onCheckBoxClicked(int position, MDCheckBox checkBox, SquaredView viewShade);

        void onCameraClicked();

    }


    public MediaController(OnDataLoadListener listener, Activity mContext) {
        this.listener = listener;
        this.mContext = mContext;
    }

    public void loadGalleryPhotos() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<AlbumEntry> albumsSorted = new ArrayList<>();
                HashMap<Integer, AlbumEntry> albums = new HashMap<>();
                AlbumEntry allPhotosAlbum = null;
                String cameraFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/" + "Camera/";

                Cursor cursor = null;

                try {
                    cursor = MediaStore.Images.Media.query(mContext.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projectionPhotos, "", null, MediaStore.Images.Media.DATE_TAKEN + " DESC");
                    if (cursor != null) {
                        int imageIdColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                        int bucketIdColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
                        int bucketNameColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                        int dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        int dateColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN);

                        while (cursor.moveToNext()) {
                            int imageId = cursor.getInt(imageIdColumn);
                            int bucketId = cursor.getInt(bucketIdColumn);
                            String bucketName = cursor.getString(bucketNameColumn);
                            String path = cursor.getString(dataColumn);
                            long dateTaken = cursor.getLong(dateColumn);

                            if (path == null || path.length() == 0) {
                                continue;
                            }

                            PhotoEntry photoEntry = new PhotoEntry(bucketId, imageId, dateTaken, path);

                            if (allPhotosAlbum == null) {
                                allPhotosAlbum = new AlbumEntry(0, "All Photos", photoEntry);
                                albumsSorted.add(0, allPhotosAlbum);
                            }

                            allPhotosAlbum.addPhoto(photoEntry);

                            AlbumEntry albumEntry = albums.get(bucketId);
                            if (albumEntry == null) {
                                albumEntry = new AlbumEntry(bucketId, bucketName, photoEntry);
                                albums.put(bucketId, albumEntry);

                                albumsSorted.add(albumEntry);
                            }

                            albumEntry.addPhoto(photoEntry);
                        }
                        mContext.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listener.onLoadFinished(albumsSorted);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        try {
                            cursor.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }).start();
    }


}
