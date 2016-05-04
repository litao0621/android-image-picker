package com.litao.android.androidimagepicker;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.litao.android.lib.BaseGalleryActivity;
import com.litao.android.lib.Configuration;
import com.litao.android.lib.entity.PhotoEntry;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by 李涛 on 16/4/29.
 */
public class PhotosActivity extends BaseGalleryActivity implements View.OnClickListener  {

    private TextView mTextViewOpenAlbum;
    private TextView mTextViewSelectedCount;
    private TextView mTextViewSend;

    private List<PhotoEntry> mSelectedPhotos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        EventBus.getDefault().register(this);
        setTitle("Photos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initView();
        attachFragment(R.id.gallery_root);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initView(){

        mTextViewOpenAlbum = (TextView) findViewById(R.id.album);
        mTextViewSelectedCount = (TextView) findViewById(R.id.selected_count);
        mTextViewSend = (TextView) findViewById(R.id.send_photos);

        mTextViewOpenAlbum.setOnClickListener(this);
        mTextViewSelectedCount.setOnClickListener(this);
        mTextViewSend.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.album:
                openAlbum();
                break;
            case R.id.send_photos:
                sendPhotos();
                break;
        }
    }

    @Override
    public Configuration getConfiguration() {
        Configuration cfg=new Configuration.Builder()
                .hasCamera(true)
                .hasShade(true)
                .hasPreview(true)
                .setSpaceSize(3)
                .setPhotoMaxWidth(120)
                .setCheckBoxColor(0xFF3F51B5)
                .setDialogHeight(Configuration.DIALOG_HALF)
                .setDialogMode(Configuration.DIALOG_GRID)
                .setMaximum(9)
                .setTip(null)
                .setAblumsTitle(null)
                .build();
        return cfg;
    }

    @Override
    public List<PhotoEntry> getSelectPhotos() {
        return mSelectedPhotos;
    }

    @Override
    public void onSelectedCountChanged(int count) {
        mTextViewSelectedCount.setVisibility(count>0?View.VISIBLE:View.INVISIBLE);
        mTextViewSelectedCount.setText(String.valueOf(count));
    }

    @Override
    public void onAlbumChanged(String name) {
        getSupportActionBar().setSubtitle(name);
    }

    @Override
    public void onTakePhoto(PhotoEntry entry) {
        EventBus.getDefault().post(entry);
        finish();
    }

    @Override
    public void onChoosePhotos(List<PhotoEntry> entries) {
        EventBus.getDefault().post(new EventEntry(entries,EventEntry.RECEIVED_PHOTOS_ID));
        finish();
    }

    @Override
    public void onPhotoClick(PhotoEntry entry) {

    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void photosMessageEvent(EventEntry entry){
        if (entry.id == EventEntry.SELECTED_PHOTOS_ID) {
            mSelectedPhotos = entry.photos;
        }
    }


}
