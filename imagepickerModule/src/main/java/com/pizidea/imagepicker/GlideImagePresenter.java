package com.pizidea.imagepicker;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.File;

/**
 * <b>desc your class</b><br/>
 * Created by yflai on 2015/11/1.
 */
public class GlideImagePresenter implements ImagePresenter{
    @Override
    public void onPresentImage(ImageView imageView, String imageUri, int size) {
        Glide.with(imageView.getContext())
                .load(new File(imageUri))
                .centerCrop()
                .dontAnimate()
                .thumbnail(0.5f)
                .override(size/4*3, size/4*3)
                .placeholder(R.drawable.default_img)
                .error(R.drawable.default_img)
                .into(imageView);

    }

}
