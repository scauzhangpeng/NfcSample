package com.ppy.nfcsample.adapter;

import android.widget.ImageView;

/**
 * Created by ZP on 2017/8/8.
 */

public abstract class AbstractImageLoader {

    private String path;

    public AbstractImageLoader(String path) {
        this.path = path;
    }

    //需要复写该方法加载图片
    public abstract void loadImage(ImageView imageView, String path);

    public String getPath() {
        return path;
    }
}
