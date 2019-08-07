package com.ppy.nfcsample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView

/**
 * Created by ZP on 2017/8/8.
 */

abstract class BaseAdapter<T>(protected var mLayoutId: Int//布局id
                              , protected var mDatas: List<T>//数据源
                              , protected var mContext: Context//上下文
) : RecyclerView.Adapter<BaseViewHolder>() {
    private val mInflater: LayoutInflater
    private var mMultipleTypeSupport: MultipleTypeSupport<T>? = null
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null

    constructor(datas: List<T>, context: Context, multipleTypeSupport: MultipleTypeSupport<T>) : this(-1, datas, context) {
        mMultipleTypeSupport = multipleTypeSupport
    }

    init {
        this.mInflater = LayoutInflater.from(mContext)
    }

    override fun getItemViewType(position: Int): Int {
        return mMultipleTypeSupport?.getLayoutId(mDatas[position], position)
                ?: super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (mMultipleTypeSupport != null) {
            mLayoutId = viewType
        }
        val itemView = mInflater.inflate(mLayoutId, parent, false)
        return BaseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        bindData(holder, mDatas[position], position)
        mOnItemClickListener?.let {
            holder.itemView.setOnClickListener { mOnItemClickListener?.onItemClick(holder.itemView, position) }
        }
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener { mOnItemClickListener?.onItemClick(holder.itemView, position) }
        }

        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener {
                mOnItemLongClickListener?.onItemLongClick(holder.itemView, position)
                false
            }
        }
    }

    protected abstract fun bindData(holder: BaseViewHolder, t: T, position: Int)

    override fun getItemCount(): Int {
        return mDatas.size
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(view: View, position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        mOnItemLongClickListener = listener
    }
}
