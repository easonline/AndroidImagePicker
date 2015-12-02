package com.pizidea.imagepicker.data;

/**
 * <b>DataSource of imagePicker</b><br/>
 * data can be from network source or android local database
 * Created by yflai on 2015/11/1.
 */
public interface DataSource {
    void provideMediaItems(OnImagesLoadedListener loadedListener);
}
