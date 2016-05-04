package com.litao.android.lib;


import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by 李涛 on 16/4/22.
 */
public abstract class BaseGalleryActivity extends AppCompatActivity implements GalleryFragment.OnGalleryAttachedListener {

    private GalleryFragment fragment;

    protected void attachFragment(int layoutId) {
        fragment = (GalleryFragment) GalleryFragment.newInstance();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(layoutId, fragment).commit();
    }

    protected void openAlbum() {
        fragment.openAlbum();
    }

    protected void sendPhotos() {
        fragment.sendPhtots();
    }

}
