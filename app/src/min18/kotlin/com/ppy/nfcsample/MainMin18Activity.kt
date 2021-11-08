package com.ppy.nfcsample

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ppy.nfclib.util.Util
import com.ppy.nfcsample.card.DefaultCardRecord
import java.util.*

class MainMin18Activity: CommonActivity() {

    private lateinit var mLlReadCard: LinearLayout
    private lateinit var mLlShowCard: LinearLayout

    private lateinit var mTvCardNumber: TextView
    private lateinit var mTvCardBalance: TextView

    private lateinit var mRvCardRecord: RecyclerView
    private lateinit var mAdapter: BaseQuickAdapter<DefaultCardRecord, BaseViewHolder>
    private lateinit var mCardRecords: MutableList<DefaultCardRecord>

    override fun setContentUiView() {
        super.setContentUiView()
        setContentView(R.layout.activity_min18_main)
        initViews()
        isPreLiveData.observe(this, { t ->
            mLlReadCard.visibility = if (t) View.VISIBLE else View.GONE
            mLlShowCard.visibility = View.GONE
        })

        mCardInfoLiveData.observe(this, { cardInfo ->
            mLlShowCard.visibility = View.VISIBLE


            when(cardInfo.type) {
                0 -> {
                    getString(R.string.card_number_yc, cardInfo.cardNumber!!)
                }
                else -> {
                    getString(R.string.card_number_sz, cardInfo.cardNumber!!)
                }
            }.also { mTvCardNumber.text = it }

            mTvCardBalance.text = getString(R.string.card_balance, Util.toAmountString(cardInfo.balance))
            mCardRecords.clear()
            mCardRecords.addAll(cardInfo.records)
            mAdapter.notifyDataSetChanged()
        })
    }

    private fun initViews() {
        mLlReadCard = findViewById(R.id.ll_read_card)
        mLlShowCard = findViewById(R.id.ll_show_card)

        mTvCardNumber = findViewById(R.id.tv_card_number)
        mTvCardBalance = findViewById(R.id.tv_card_balance)

        mRvCardRecord = findViewById(R.id.rv_card_record)
        mRvCardRecord.layoutManager = LinearLayoutManager(this)
        mRvCardRecord.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        mCardRecords = ArrayList()
        mAdapter = object : BaseQuickAdapter<DefaultCardRecord, BaseViewHolder>(R.layout.item_card_record, mCardRecords) {
            override fun convert(holder: BaseViewHolder, item: DefaultCardRecord) {
                holder.setText(R.id.tv_record_type, item.typeName.toString())
                holder.setText(R.id.tv_record_date, DateUtil.str2str(item.date!!, DateUtil.MMddHHmmss, DateUtil.MM_dd_HH_mm))
                val price = Util.toAmountString(item.price)
                if ("09" == item.typeCode) {
                    holder.setText(R.id.tv_record_price, "-$price 元")
                } else {
                    holder.setText(R.id.tv_record_price, "+$price 元")
                }
            }
        }
        mRvCardRecord.adapter = mAdapter
    }
}