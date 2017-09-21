package com.ppy.nfcsample.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ZP on 2017/8/8.
 */

public class RiotGameViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;

    public RiotGameViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }


    public <V extends View> V getView(int id) {
        View view = mViews.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            mViews.put(id, view);
        }
        return (V) view;
    }

    public RiotGameViewHolder setText(int id, String text) {
        TextView tv = getView(id);
        tv.setText(text);
        return this;
    }

    public RiotGameViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public RiotGameViewHolder setImagePath(int viewId, AbstractImageLoader abstractImageLoader) {
        ImageView view = getView(viewId);
        abstractImageLoader.loadImage(view, abstractImageLoader.getPath());
        return this;
    }

    public RiotGameViewHolder setVisibility(int viewId, int visibility) {
        View view = getView(viewId);
        view.setVisibility(visibility);
        return this;
    }

    public RiotGameViewHolder setOnItemClickListener(View.OnClickListener listener) {
        itemView.setOnClickListener(listener);
        return this;
    }
}
