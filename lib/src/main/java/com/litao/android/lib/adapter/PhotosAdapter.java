package com.litao.android.lib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.litao.android.lib.Configuration;
import com.litao.android.lib.R;
import com.litao.android.lib.controller.MediaController;
import com.litao.android.lib.entity.PhotoEntry;
import com.litao.android.lib.view.MDCheckBox;
import com.litao.android.lib.view.SquaredView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李涛 on 16/4/21.
 */
public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    private List<PhotoEntry> list = new ArrayList<PhotoEntry>();

    private Context mContext;

    private LayoutInflater mInflater;

    private Configuration mConfig;

    public MediaController.OnViewClickListener mClickListener;

    public PhotosAdapter(Context mContext, MediaController.OnViewClickListener mClickListener, Configuration mConfig) {
        this.mClickListener = mClickListener;
        this.mContext = mContext;
        this.mConfig = mConfig;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void reloadList(List<PhotoEntry> data) {
        if (data != null) {
            list.clear();
            if (mConfig.hasCamera) {
                PhotoEntry cameraEntry = new PhotoEntry();
                list.add(cameraEntry);
            }
            list.addAll(data);
        } else {
            list.clear();
        }
        notifyDataSetChanged();
    }

    public void appendList(List<PhotoEntry> data) {
        if (data != null) {
            if (mConfig.hasCamera) {
                PhotoEntry cameraEntry = new PhotoEntry();
                list.add(cameraEntry);
            }
            list.addAll(data);
        } else {
            list.clear();
        }
        notifyDataSetChanged();
    }

    public void appendPhoto(PhotoEntry entry) {
        if (entry != null) {
            list.add(entry);
        }
        notifyDataSetChanged();
    }

    public void addCamera(){
        if (mConfig.hasCamera) {
            PhotoEntry cameraEntry = new PhotoEntry();
            list.add(cameraEntry);
        }
        notifyDataSetChanged();
    }

    public PhotoEntry getEntry(int position) {
        return list.get(mConfig.hasCamera ? position + 1 : position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewHolder vh = new ViewHolder(mInflater.inflate(R.layout.item_photo, viewGroup, false), mClickListener, i, mConfig.hasCamera);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        if (mConfig.hasCamera && i == 0) {
            viewHolder.mImageView.setImageResource(R.mipmap.camera);
            viewHolder.mCheckBox.setVisibility(View.INVISIBLE);

        } else {
            PhotoEntry entry = list.get(i);
            viewHolder.mCheckBox.setChecked(entry.isChecked());
            viewHolder.mCheckBox.setVisibility(View.VISIBLE);
            if (mConfig.hasShade) {
                viewHolder.mViewShade.setVisibility(entry.isChecked() ? View.VISIBLE : View.INVISIBLE);
            }

            Glide.with(mContext)
                    .load(new File(entry.getPath()))
                    .centerCrop()
                    .placeholder(R.mipmap.default_image)
                    .into(viewHolder.mImageView);

        }
        viewHolder.mCheckBox.setCheckBoxColor(mConfig.checkBoxColor);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageView;

        private MDCheckBox mCheckBox;

        private SquaredView mViewShade;

        public MediaController.OnViewClickListener mListener;

        private int position;

        private boolean hasCamera;

        public ViewHolder(View itemView, MediaController.OnViewClickListener mListener, int position, boolean hasCamera) {

            super(itemView);

            this.position = position;
            this.hasCamera = hasCamera;
            this.mListener = mListener;

            mImageView = (ImageView) itemView.findViewById(R.id.image);
            mCheckBox = (MDCheckBox) itemView.findViewById(R.id.checkbox);
            mViewShade = (SquaredView) itemView.findViewById(R.id.shade);
            mImageView.setOnClickListener(this);
            mCheckBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener == null) return;

            if (view instanceof ImageView) {
                if (position == 0 && hasCamera) {
                    mListener.onCameraClicked();
                }else {
                    mListener.onPhotoClicked(hasCamera ? position - 1 : position, mCheckBox, mViewShade);
                }
            } else if (view instanceof MDCheckBox) {
                mListener.onCheckBoxClicked(hasCamera ? position - 1 : position, mCheckBox, mViewShade);
            }
        }
    }
}
