package com.ppy.nfcsample.adapter

import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

/**
 * Created by ZP on 2017/8/8.
 */

class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mViews: SparseArray<View> = SparseArray()


    fun <V : View> getView(id: Int): V {
        var view: View? = mViews.get(id)
        if (view == null) {
            view = itemView.findViewById(id)
            mViews.put(id, view)
        }
        return view as V
    }

    fun setText(id: Int, text: String): BaseViewHolder {
        val tv = getView<TextView>(id)
        tv.text = text
        return this
    }

    fun setImageResource(viewId: Int, resId: Int): BaseViewHolder {
        val view = getView<ImageView>(viewId)
        view.setImageResource(resId)
        return this
    }

    fun setImagePath(viewId: Int, abstractImageLoader: AbstractImageLoader): BaseViewHolder {
        val view = getView<ImageView>(viewId)
        abstractImageLoader.loadImage(view, abstractImageLoader.path)
        return this
    }

    fun setVisibility(viewId: Int, visibility: Int): BaseViewHolder {
        val view = getView<View>(viewId)
        view.visibility = visibility
        return this
    }

    fun setOnItemClickListener(listener: View.OnClickListener): BaseViewHolder {
        itemView.setOnClickListener(listener)
        return this
    }
}
