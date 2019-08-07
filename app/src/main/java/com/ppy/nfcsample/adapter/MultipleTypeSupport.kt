package com.ppy.nfcsample.adapter

/**
 * Created by ZP on 2017/8/8.
 */

interface MultipleTypeSupport<T> {

    fun getLayoutId(t: T, position: Int): Int
}
