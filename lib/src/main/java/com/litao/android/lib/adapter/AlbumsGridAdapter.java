package com.litao.android.lib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.litao.android.lib.R;
import com.litao.android.lib.controller.MediaController;
import com.litao.android.lib.entity.AlbumEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李涛 on 16/4/25.
 */
public class AlbumsGridAdapter extends RecyclerView.Adapter<AlbumsGridAdapter.ViewHolder> {

    private List<AlbumEntry> list = new ArrayList<AlbumEntry>();

    private Context mContext;

    public MediaController.OnViewClickListener mClickListener;

    public AlbumsGridAdapter(Context mContext, MediaController.OnViewClickListener mClickListener) {
        this.mClickListener = mClickListener;
        this.mContext = mContext;
    }

    public void appendList(List<AlbumEntry> data) {
        if (data != null) {
            list.clear();
            list.addAll(data);
        } else {
            list.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewHolder vh = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_grid_album, null), mClickListener, i);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        AlbumEntry entry = list.get(i);

        viewHolder.mTextViewName.setText(entry.getBucketName());
        viewHolder.mTextViewCount.setText(String.valueOf(entry.getPhotos().size()));

        Glide.with(mContext)
                .load(new File(entry.getCoverPhoto().getPath()))
                .centerCrop()
                .placeholder(R.mipmap.default_image)
                .into(viewHolder.mImageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RelativeLayout mLayout;

        private ImageView mImageView;

        private TextView mTextViewName;

        private TextView mTextViewCount;

        public MediaController.OnViewClickListener mListener;

        private int position;

        public ViewHolder(View itemView, MediaController.OnViewClickListener mListener, int position) {
            super(itemView);

            this.position = position;
            this.mListener = mListener;

            mLayout = (RelativeLayout) itemView.findViewById(R.id.grid_item);
            mImageView = (ImageView) itemView.findViewById(R.id.image);
            mTextViewName = (TextView) itemView.findViewById(R.id.name);
            mTextViewCount = (TextView) itemView.findViewById(R.id.count);

            mLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onAlbumClicked(position,view);
        }
    }
}
