package com.pizidea.imagepicker.bean;

import java.io.Serializable;

/**
 * <b>desc your class</b><br/>
 * Created by yflai on 2015/11/1.
 */
public class ImageItem implements Serializable{
    public String path;
    public String name;
    public long time;

    public ImageItem(String path, String name, long time){
        this.path = path;
        this.name = name;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        try {
            ImageItem other = (ImageItem) o;
            return this.path.equalsIgnoreCase(other.path) && this.time == other.time;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }

}
