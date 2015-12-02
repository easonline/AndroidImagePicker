package com.pizidea.imagepicker;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

/**
 * <b>desc your class</b><br/>
 * Created by yflai on 2015/11/1.
 */
public class UilImagePresenter implements ImagePresenter{
    @Override
    public void onPresentImage(ImageView imageView, String imageUri, int size) {
        ImageDownloader.Scheme scheme = ImageDownloader.Scheme.FILE;

        ImageLoader.getInstance().displayImage(scheme.wrap(imageUri), imageView, new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_img)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .showImageOnFail(R.drawable.default_img)
                .showImageForEmptyUri(R.drawable.default_img)
                .showImageOnLoading(R.drawable.default_img)
                .build());
    }

    public void onPresentImage2(ImageView imageView, String imageUri, int size) {
        ImageDownloader.Scheme scheme = ImageDownloader.Scheme.FILE;

        ImageLoader.getInstance().displayImage(scheme.wrap(imageUri), imageView, new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_img)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .showImageOnFail(R.drawable.default_img)
                .showImageForEmptyUri(R.drawable.default_img)
                .showImageOnLoading(R.drawable.default_img)
                .build());
    }
}
