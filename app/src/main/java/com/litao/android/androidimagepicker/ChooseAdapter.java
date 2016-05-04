package com.litao.android.androidimagepicker;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.litao.android.lib.entity.PhotoEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 李涛 on 16/4/30.
 */
public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.ViewHolder> {

    private List<PhotoEntry> list = new ArrayList<PhotoEntry>();

    private Context mContext;

    private LayoutInflater mInflater;

    private OnItmeClickListener mlistener;

    public  interface OnItmeClickListener{
        void onItemClicked(int position);

    }

    public ChooseAdapter(Context mContext) {
        this.mContext = mContext;
        mlistener = (OnItmeClickListener) mContext;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        list.add(createAddEntry());
    }

    public void reloadList(List<PhotoEntry> data) {
        if (data != null) {
            list.clear();
            list.addAll(data);
            list.add(createAddEntry());
        } else {
            list.clear();
        }
        notifyDataSetChanged();

    }

    public void appendList(List<PhotoEntry> data) {
        if (data != null) {
            list.addAll(list.size()-1,data);
        } else {
            list.clear();
        }
        notifyDataSetChanged();

    }


    public void appendPhoto(PhotoEntry entry) {
        if (entry != null) {
            list.add(list.size()-1,entry);
        }
        notifyDataSetChanged();
    }

    public List<PhotoEntry> getData(){
        return list.subList(0,list.size()-1);
    }
    public PhotoEntry getEntry(int position) {
        return list.get(position);
    }

    private PhotoEntry createAddEntry(){
        return new PhotoEntry();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewHolder vh = new ViewHolder(mInflater.inflate(R.layout.item_selected_photo, viewGroup, false), i);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (i==list.size()-1){
            viewHolder.mImageView.setImageResource(R.mipmap.add);
        }else {
            PhotoEntry entry = list.get(i);
            Glide.with(mContext)
                    .load(new File(entry.getPath()))
                    .centerCrop()
                    .placeholder(com.litao.android.lib.R.mipmap.default_image)
                    .into(viewHolder.mImageView);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;

        private int position;

        public ViewHolder(View itemView, int position) {
            super(itemView);
            this.position = position;
            mImageView = (ImageView) itemView.findViewById(R.id.image);
            mImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mlistener.onItemClicked(position);
        }
    }

}
