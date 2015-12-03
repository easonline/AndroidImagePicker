/*
 *
 *  * Copyright (C) 2015 Eason.Lai (easonline7@gmail.com)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.pizidea.imagepickerDemo2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;
import com.pizidea.imagepicker.ui.ImagePreviewFragment;
import com.pizidea.imagepickerDemo.R;

import java.io.Serializable;
import java.util.List;

public class PreviewActivity extends AppCompatActivity implements View.OnClickListener,ImagePreviewFragment.OnImageSingleTapClickListener,ImagePreviewFragment.OnImagePageSelectedListener,AndroidImagePicker.OnImageSelectedListener {
    private static final String TAG = PreviewActivity.class.getSimpleName();

    ImagePreviewFragment mFragment;
    CheckBox mCbSelected;

    List<ImageItem> mImageList;
    int mShowItemPosition = 0;
    AndroidImagePicker androidImagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        androidImagePicker = AndroidImagePicker.getInstance();
        androidImagePicker.addOnImageSelectedListener(this);

        mImageList = AndroidImagePicker.getInstance().getImageItemsOfCurrentImageSet();
        mShowItemPosition = getIntent().getIntExtra(AndroidImagePicker.KEY_PIC_SELECTED_POSITION, 0);

        mCbSelected = (CheckBox) findViewById(R.id.btn_check);

        setTitle("1/" + mImageList.size());

        int selectedCount = AndroidImagePicker.getInstance().getSelectImageCount();

        onImageSelected(0, null, selectedCount, androidImagePicker.getSelectLimit());

        mCbSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (androidImagePicker.getSelectImageCount() > androidImagePicker.getSelectLimit()) {
                    if (mCbSelected.isChecked()) {
                        //holder.cbSelected.setCanChecked(false);
                        mCbSelected.toggle();
                        String toast = getResources().getString(R.string.you_have_a_select_limit,androidImagePicker.getSelectLimit());
                        Toast.makeText(PreviewActivity.this, toast, Toast.LENGTH_SHORT).show();
                    } else {
                        //
                    }
                } else {
                    //
                }
            }

        });

        mCbSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFragment.selectCurrent(isChecked);
            }
        });


        mFragment = new ImagePreviewFragment();
        Bundle data = new Bundle();
        data.putSerializable(AndroidImagePicker.KEY_PIC_PATH, (Serializable) mImageList);
        data.putInt(AndroidImagePicker.KEY_PIC_SELECTED_POSITION, mShowItemPosition);
        mFragment.setArguments(data);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mFragment)
                .commit();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pic_rechoose:
                finish();
                break;
            case R.id.btn_ok:
                setResult(RESULT_OK);// select complete
                finish();
                break;
            default:
                break;
        }
    }


    @Override
    public void onImageSingleTap(MotionEvent e) {
        View topBar = findViewById(R.id.detail_toolbar);
        View bottomBar = findViewById(R.id.bottom_bar);
        if (topBar.getVisibility() == View.VISIBLE) {
            topBar.setAnimation(AnimationUtils.loadAnimation(PreviewActivity.this, R.anim.top_out));
            bottomBar.setAnimation(AnimationUtils.loadAnimation(PreviewActivity.this, R.anim.fade_out));
            topBar.setVisibility(View.GONE);
            bottomBar.setVisibility(View.GONE);
        } else {
            topBar.setAnimation(AnimationUtils.loadAnimation(PreviewActivity.this, R.anim.top_in));
            bottomBar.setAnimation(AnimationUtils.loadAnimation(PreviewActivity.this, R.anim.fade_in));
            topBar.setVisibility(View.VISIBLE);
            bottomBar.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onImagePageSelected(int position, ImageItem item,boolean isSelected) {
        setTitle(position + 1 + "/" + mImageList.size());
        mCbSelected.setChecked(isSelected);
    }

    @Override
    public void onImageSelected(int position, ImageItem item, int selectedItemsCount, int maxSelectLimit) {
        if(selectedItemsCount > 0){
            setTitle(getResources().getString(R.string.select_complete, selectedItemsCount, maxSelectLimit));
        }else{
            setTitle(getResources().getString(R.string.complete));
        }
        Log.i(TAG, "=====EVENT:onImageSelected");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //NavUtils.navigateUpTo(this, new Intent(this, ItemListActivity.class));
            finish();
            return true;
        }else if(id == R.id.action_settings){
            //
            if(androidImagePicker.getSelectImageCount() > 0){
                setResult(RESULT_OK);// select complete
                finish();
                return true;
            }else{
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        androidImagePicker.removeOnImageItemSelectedListener(this);
        Log.i(TAG, "=====removeOnImageItemSelectedListener");
        super.onDestroy();
    }

}
