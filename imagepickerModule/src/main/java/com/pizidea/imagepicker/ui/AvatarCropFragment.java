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

package com.pizidea.imagepicker.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.ImgLoader;
import com.pizidea.imagepicker.R;
import com.pizidea.imagepicker.UilImgLoader;
import com.pizidea.imagepicker.Util;
import com.pizidea.imagepicker.widget.AvatarRectView;
import com.pizidea.imagepicker.widget.SuperImageView;

/**
 * <b>Image crop Fragment for avatar</b><br/>
 * Created by Eason.Lai on 2015/11/1 10:42 <br/>
 * contact：easonline7@gmail.com <br/>
 */
public class AvatarCropFragment extends Fragment{

    Activity mContext;

    SuperImageView superImageView;
    AvatarRectView mRectView;

    private int screenWidth;
    private final int margin = 30;//the left and right margins of the center circular shape

    private FrameLayout rootView;

    private String picPath;//the local image path in sdcard

    ImgLoader mImagePresenter;//interface to load image,you can implement it with your own code

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_avatar_crop,null);

        DisplayMetrics dm = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;

        initView(contentView);

        //get the image path from Arguments
        picPath = getArguments().getString(AndroidImagePicker.KEY_PIC_PATH);

        mImagePresenter = new UilImgLoader();

        if(TextUtils.isEmpty(picPath)){
            throw new RuntimeException("AndroidImagePicker:you have to give me an image path from sdcard");
        }else{
            mImagePresenter.onPresentImage(superImageView,picPath,screenWidth);
        }

        return contentView;

    }

    /**
     * init all views
     * @param contentView
     */
    void initView(View contentView){
        superImageView = (SuperImageView) contentView.findViewById(R.id.iv_pic);
        rootView = (FrameLayout) contentView.findViewById(R.id.container);

        RelativeLayout.LayoutParams rlLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //rlLayoutParams.addRule(RelativeLayout.ABOVE, R.id.photo_preview_dock);
        mRectView = new AvatarRectView(mContext, screenWidth - margin*2);
        rootView.addView(mRectView, 1, rlLayoutParams);
    }


    /**
     * public method to get the crop bitmap
     * @return
     */
    public Bitmap getCropBitmap(int expectSize){
        if(expectSize <= 0){
            return null;
        }
        Bitmap srcBitmap = ((BitmapDrawable)superImageView.getDrawable()).getBitmap();
        double rotation = superImageView.getImageRotation();
        int level = (int) Math.floor((rotation + Math.PI / 4) / (Math.PI / 2));
        if (level != 0){
            srcBitmap = Util.rotate(srcBitmap,90 * level);
        }
        Rect centerRect = mRectView.getCropRect();
        RectF matrixRect = superImageView.getMatrixRect();

        Bitmap bmp = AndroidImagePicker.makeCropBitmap(srcBitmap, centerRect, matrixRect, expectSize);
        return bmp;
    }


}
