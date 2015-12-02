package com.pizidea.imagepicker.data;

import com.pizidea.imagepicker.bean.ImageItem;
import com.pizidea.imagepicker.bean.ImageSet;

import java.util.List;

/**
 * <b>Listener when data ready</b><br/>
 * Created by yflai on 2015/11/1.
 */
public interface OnImagesLoadedListener {
    void onImagesLoaded(List<ImageSet> imageSetList);
}
