package com.litao.android.lib;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.litao.android.lib.Utils.GridSpacingItemDecoration;
import com.litao.android.lib.Utils.Utils;
import com.litao.android.lib.Utils.VerticalSpaceItemDecoration;
import com.litao.android.lib.adapter.AlbumsGridAdapter;
import com.litao.android.lib.adapter.AlbumsListAdapter;
import com.litao.android.lib.adapter.PhotosAdapter;
import com.litao.android.lib.controller.MediaController;
import com.litao.android.lib.entity.AlbumEntry;
import com.litao.android.lib.entity.PhotoEntry;
import com.litao.android.lib.view.MDCheckBox;
import com.litao.android.lib.view.SquaredView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李涛 on 16/4/21.
 */
public class GalleryFragment extends Fragment implements MediaController.OnViewClickListener, MediaController.OnDataLoadListener {

    private final String  TAG = "GalleryFragment";

    /**
     * take photo request code
     */
    private static final int REQUEST_TAKE_PHOTO = 0x00001024;

    /**
     * take photo id
     */
    private static final int TAKE_PHOTO_ID = Integer.MAX_VALUE;

    private int mPhotoColumnNumber;

    private int mAlbumColumnNumber;

    private PhotosAdapter mAdapterPhoto;

    private AlbumsGridAdapter mAdapterAlbumGrid;

    private AlbumsListAdapter mAdapterAlbumList;

    private Configuration mConfig;

    private MediaController mMediaController;

    private List<AlbumEntry> albumsSorted;

    private List<PhotoEntry> selectedPhotos;

    private BottomSheetDialog dialog;

    private BottomSheetBehavior mBehavior;

    private File mTmpFile;

    private OnGalleryAttachedListener mListener;

    public interface OnGalleryAttachedListener {
        Configuration getConfiguration();

        List<PhotoEntry> getSelectPhotos();

        void onSelectedCountChanged(int count);

        void onAlbumChanged(String name);

        void onTakePhoto(PhotoEntry entry);

        void onChoosePhotos(List<PhotoEntry> entries);

        void onPhotoClick(PhotoEntry entry);
    }

    public static Fragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConfig = mListener.getConfiguration();

        mAdapterPhoto = new PhotosAdapter(getActivity(), this, mConfig);

        if (mConfig.dialogMode >= Configuration.DIALOG_GRID) {
            mAdapterAlbumGrid = new AlbumsGridAdapter(getActivity(), this);
        }else {
            mAdapterAlbumList = new AlbumsListAdapter(getActivity(),this);
        }

        selectedPhotos = new ArrayList<PhotoEntry>();
        mMediaController = new MediaController(this, getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPhotoColumnNumber = getGallerWidth(container) / dp2px(mConfig.photoMaxWidth);
        mAlbumColumnNumber = getGallerWidth(container) / dp2px(mConfig.photoMaxWidth * 1.5f);
        return inflater.inflate(R.layout.layout_photos, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView mGalleryView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mGalleryView.setLayoutManager(new GridLayoutManager(getActivity(), mPhotoColumnNumber));
        mGalleryView.setAdapter(mAdapterPhoto);
        mGalleryView.addItemDecoration(new GridSpacingItemDecoration(mPhotoColumnNumber, dp2px(mConfig.spaceSize), true));


        mMediaController.loadGalleryPhotos();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnGalleryAttachedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnGalleryAttachedListener");
        }
    }


    public void openAlbum() {
        int screenHeight = Utils.getScreenHeight(getActivity());
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            return;
        }
        dialog = new BottomSheetDialog(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_albums, null);

        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screenHeight);
        view.setLayoutParams(lp);

        TextView mTextViewTitle = (TextView) view.findViewById(R.id.title);
        mTextViewTitle.setText(mConfig.ablumsTitle == null ? getString(R.string.album) : mConfig.ablumsTitle);

        RecyclerView mGalleryView = (RecyclerView) view.findViewById(R.id.recycler_view);

        if (mConfig.dialogMode >= Configuration.DIALOG_GRID) {
            mGalleryView.setLayoutManager(new GridLayoutManager(getActivity(), mAlbumColumnNumber));
            mGalleryView.addItemDecoration(new GridSpacingItemDecoration(mAlbumColumnNumber, dp2px(mConfig.spaceSize), true));
            mGalleryView.setItemAnimator(new DefaultItemAnimator());
            mGalleryView.setAdapter(mAdapterAlbumGrid);
            mAdapterAlbumGrid.appendList(albumsSorted);
        }else {
            mGalleryView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mGalleryView.addItemDecoration(new VerticalSpaceItemDecoration(dp2px(mConfig.spaceSize)));
            mGalleryView.setItemAnimator(new DefaultItemAnimator());
            mGalleryView.setAdapter(mAdapterAlbumList);
            mAdapterAlbumList.appendList(albumsSorted);
        }


        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());

        if (mConfig.dialogHeight < 0) {
            mBehavior.setPeekHeight(mConfig.dialogHeight <= Configuration.DIALOG_HALF ? screenHeight / 2 : screenHeight);
        } else {
            mBehavior.setPeekHeight(mConfig.dialogHeight >= screenHeight ? screenHeight : mConfig.dialogHeight);
        }
        dialog.show();
    }

    public void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            mTmpFile = Utils.createTmpFile(getActivity());
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
            this.startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
        } else {
            Log.e(TAG,"No camera is found");
        }
    }

    public void sendPhtots() {
        mListener.onChoosePhotos(selectedPhotos);
    }

    public void togglePhoto(int position, MDCheckBox mCheckBox, SquaredView viewShade){
        if (mConfig.maximum <= selectedPhotos.size() && !mCheckBox.isChecked()) {
            Toast.makeText(getActivity(), mConfig.tip == null ? String.format(getString(R.string.select_maximum),mConfig.maximum) : mConfig.tip, Toast.LENGTH_SHORT).show();
        } else {
            PhotoEntry photoEntry = mAdapterPhoto.getEntry(position);
            mCheckBox.toggle();
            viewShade.toggle();
            photoEntry.toggle();
            if (mCheckBox.isChecked()) {
                selectedPhotos.add(photoEntry);
            } else {
                selectedPhotos.remove(photoEntry);
            }
            mListener.onSelectedCountChanged(selectedPhotos.size());
        }
    }

    private void updateCheckstatus() {
        List<PhotoEntry> allEntries = albumsSorted.get(0).getPhotos();
        if (mListener.getSelectPhotos() != null) {
            for (PhotoEntry entry1 : mListener.getSelectPhotos()) {
                for (PhotoEntry entry2 : allEntries) {
                    if (TextUtils.equals(entry2.getPath(),entry1.getPath())) {
                        entry2.setChecked(true);
                        selectedPhotos.add(entry2);
                        break;
                    }
                }
            }
        }
        mListener.onSelectedCountChanged(selectedPhotos.size());
        mAdapterPhoto.notifyDataSetChanged();
    }


    private int dp2px(float dp) {
        return (int) (dp * getActivity().getResources().getDisplayMetrics().density + 0.5f);
    }

    private int getGallerWidth(ViewGroup container) {
        return Utils.getScreenWidth(getActivity()) - container.getPaddingLeft() - container.getPaddingRight();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                PhotoEntry entry = new PhotoEntry();
                entry.setImageId(TAKE_PHOTO_ID);
                entry.setPath(mTmpFile.getAbsolutePath());
                Utils.addMediaToGallery(getActivity().getApplicationContext(), Uri.fromFile(new File(mTmpFile.getAbsolutePath())));
                mListener.onTakePhoto(entry);
            }
        }
    }

    @Override
    public void onPhotoClicked(int position, MDCheckBox mCheckBox, SquaredView viewShade) {
        if (mConfig.hasPreview) {
            mListener.onPhotoClick(mAdapterPhoto.getEntry(position));
        }else {
            togglePhoto(position,mCheckBox,viewShade);
        }
    }

    @Override
    public void onAlbumClicked(int position, View imageView) {
        mListener.onAlbumChanged(albumsSorted.get(position).getBucketName());
        mAdapterPhoto.reloadList(albumsSorted.get(position).getPhotos());
        dialog.dismiss();
    }

    @Override
    public void onCheckBoxClicked(int position, MDCheckBox mCheckBox,SquaredView viewShade) {
        togglePhoto(position,mCheckBox,viewShade);
    }

    @Override
    public void onCameraClicked() {
        if (mConfig.maximum > selectedPhotos.size()) {
            openCamera();
        } else {
            Toast.makeText(getActivity(), mConfig.tip == null ? String.format(getString(R.string.select_maximum),mConfig.maximum) : mConfig.tip, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoadFinished(List<AlbumEntry> data) {
        if (data != null && data.size()>0) {
            albumsSorted = data;
            mListener.onAlbumChanged(data.get(0).getBucketName());
            mAdapterPhoto.reloadList(data.get(0).getPhotos());
            updateCheckstatus();
        }else {
            mAdapterPhoto.addCamera();
        }
    }

}
