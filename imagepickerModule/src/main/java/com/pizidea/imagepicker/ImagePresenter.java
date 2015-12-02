package com.pizidea.imagepicker;

import android.net.Uri;
import android.widget.ImageView;

/**
 * <b>interface for image showing </b><br/>
 * you can implements it with UIL,picasso or glide
 * Created by yflai on 2015/11/1.
 */
public interface ImagePresenter {
    void onPresentImage(ImageView imageView, String imageUri, int size);
}
