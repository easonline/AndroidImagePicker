package com.pizidea.imagepicker;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * <b>desc your class</b><br/>
 * Created by yflai on 2015/11/1.
 */
public class PicassoImagePresenter implements ImagePresenter{
    @Override
    public void onPresentImage(ImageView imageView, String imageUri, int size) {
        Picasso.with(imageView.getContext())
                .load(new File(imageUri))
                .centerCrop()
                //.dontAnimate()
                //.thumbnail(0.5f)
                //.override(size, size)
                .resize(size/4*3, size/4*3)
                .placeholder(R.drawable.default_img)
                //.error(R.drawable.default_img)
                .into(imageView);

    }

}
