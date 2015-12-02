package com.pizidea.imagepicker.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <b>desc your class</b><br/>
 * Created by yflai on 2015/11/1.
 */
public class ImageSet implements Serializable {
    public String name;
    public String path;
    public ImageItem cover;
    public List<ImageItem> imageItems;

    @Override
    public boolean equals(Object o) {
        try {
            ImageSet other = (ImageSet) o;
            return this.path.equalsIgnoreCase(other.path) && this.name.equalsIgnoreCase(other.name);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }

}
