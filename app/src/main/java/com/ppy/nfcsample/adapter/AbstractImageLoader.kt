package com.ppy.nfcsample.adapter

import android.widget.ImageView

/**
 * Created by ZP on 2017/8/8.
 */

abstract class AbstractImageLoader(val path: String) {

    //需要复写该方法加载图片
    abstract fun loadImage(imageView: ImageView, path: String)
}
