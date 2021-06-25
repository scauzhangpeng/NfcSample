package com.ppy.nfcsample

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ppy.nfclib.exception.ExceptionConstant
import com.ppy.nfclib.util.Util
import com.ppy.nfcsample.adapter.BaseAdapter
import com.ppy.nfcsample.adapter.BaseViewHolder
import com.ppy.nfcsample.card.DefaultCardInfo
import com.ppy.nfcsample.card.DefaultCardRecord
import com.ppy.nfcsample.card.reader.CardClient
import com.ppy.nfcsample.card.reader.SZTReader
import com.ppy.nfcsample.card.reader.YCTReader
import java.io.IOException
import java.lang.reflect.Field
import java.util.*

class MainActivity : NfcActivity() {

    private lateinit var mLlReadCard: LinearLayout
    private lateinit var mLlShowCard: LinearLayout

    private lateinit var mTvCardNumber: TextView
    private lateinit var mTvCardBalance: TextView

    private lateinit var mRvCardRecord: RecyclerView
    private lateinit var mAdapter: BaseAdapter<DefaultCardRecord>
    private lateinit var mCardRecords: MutableList<DefaultCardRecord>

    private lateinit var mCardClient: CardClient
    private var mDialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initCardClient()
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
        mAdapter = object : BaseAdapter<DefaultCardRecord>(R.layout.item_card_record, mCardRecords, this) {
            override fun bindData(holder: BaseViewHolder, t: DefaultCardRecord, position: Int) {
                holder.setText(R.id.tv_record_type, t.typeName.toString())
                holder.setText(R.id.tv_record_date, DateUtil.str2str(t.date!!, DateUtil.MMddHHmmss, DateUtil.MM_dd_HH_mm))
                val price = Util.toAmountString(t.price)
                if ("09" == t.typeCode) {
                    holder.setText(R.id.tv_record_price, "-$price 元")
                } else {
                    holder.setText(R.id.tv_record_price, "+$price 元")
                }
            }
        }
        mRvCardRecord.adapter = mAdapter
    }

    private fun initCardClient() {
        mCardClient = CardClient.Builder()
                .nfcManager(mReaderManager)
                .addReader(SZTReader())
                .addReader(YCTReader())
                .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        fixInputMethodManagerLeak(this)
        dismissDialog()
    }

    override fun doOnNfcOff() {
        showDialog("接收到系统广播", "Nfc已经关闭，前往打开？") {
            dismissDialog()
            Util.intentToNfcSetting(this@MainActivity)
        }
    }

    override fun doOnNfcOn() {
        dismissDialog()
    }

    override fun doOnCardConnected(isConnected: Boolean) {
        if (isConnected) {
            execute()
        }
    }

    override fun doOnException(code: Int, message: String) {
        if (code == ExceptionConstant.NFC_NOT_ENABLE) {
            showDialog("NFC设备", "NFC未打开，前往打开？") {
                dismissDialog()
                Util.intentToNfcSetting(this@MainActivity)
            }
        }

        if (code == ExceptionConstant.NFC_NOT_EXIT) {
            showDialog("NFC设备", "设备不支持NFC") { dismissDialog() }
        }

        if (code == ExceptionConstant.CONNECT_TAG_FAIL) {
            showDialog("读卡失败", "请重新贴紧卡片") {
                dismissDialog()
            }
        }
    }

    private fun execute() {
        dismissDialog()
        val cardInfo: DefaultCardInfo?
        try {
            cardInfo = mCardClient.execute()
        } catch (e: IOException) {
            e.printStackTrace()
            runOnUiThread {
                showDialog("读卡失败", "请重新贴紧卡片") {
                    dismissDialog()
                    mLlShowCard.visibility = View.GONE
                    mLlReadCard.visibility = View.VISIBLE
                }
            }
            return
        }

        runOnUiThread {
            cardInfo?.let {
                doOnReadCardSuccess(it)
            } ?: doOnReadCardError()
        }
    }

    private fun doOnReadCardSuccess(cardInfo: DefaultCardInfo) {
        mLlReadCard.visibility = View.GONE
        mLlShowCard.visibility = View.VISIBLE
        if (cardInfo.type == 0) {
            mTvCardNumber.text = getString(R.string.card_number_yc, cardInfo.cardNumber!!)
            mTvCardBalance.text = getString(R.string.card_balance, Util.toAmountString(cardInfo.balance))
            mCardRecords.clear()
            mCardRecords.addAll(cardInfo.records)
            mAdapter.notifyDataSetChanged()
        }
        if (cardInfo.type == 1) {
            mTvCardNumber.text = getString(R.string.card_number_sz, cardInfo.cardNumber!!)
            mTvCardBalance.text = getString(R.string.card_balance, Util.toAmountString(cardInfo.balance))
            mCardRecords.clear()
            mCardRecords.addAll(cardInfo.records)
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun doOnReadCardError() {
        showDialog("读卡失败", "暂不支持此类卡片") {
            dismissDialog()
            mLlShowCard.visibility = View.GONE
            mLlReadCard.visibility = View.VISIBLE
        }
    }

    private fun showDialog(title: String, content: String, listener: View.OnClickListener) {
        dismissDialog()
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.dialog_tag_lost, null)
        val tvTitle = view.findViewById<TextView>(R.id.dialog_title)
        val tvContent = view.findViewById<TextView>(R.id.dialog_content)
        tvTitle.text = title
        tvContent.text = content
        val btnCancel = view.findViewById<Button>(R.id.btn_cancel)
        val btnOk = view.findViewById<Button>(R.id.btn_confirm)
        btnCancel.setOnClickListener { dismissDialog() }
        btnOk.setOnClickListener(listener)

        builder.setView(view)
        mDialog = builder.create()
        mDialog?.let {
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
            it.show()
        }

    }

    private fun dismissDialog() {
        mDialog?.let {
            if (it.isShowing) {
                it.dismiss()
                mDialog = null
            }
        }
    }


    private fun fixInputMethodManagerLeak(destContext: Context?) {
        if (destContext == null) {
            return
        }

        val imm = destContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                ?: return

        val arr = arrayOf("mCurRootView", "mServedView", "mNextServedView", "mLastSrvView")
        var f: Field?
        var obj_get: Any?
        for (i in arr.indices) {
            val param = arr[i]
            try {
                f = imm.javaClass.getDeclaredField(param)
                if (!f!!.isAccessible) {
                    f.isAccessible = true
                }
                obj_get = f.get(imm)
                if (obj_get is View) {
                    val v_get = obj_get as View?
                    if (v_get!!.context === destContext || param == "mLastSrvView") { // 被InputMethodManager持有引用的context是想要目标销毁的
                        f.set(imm, null) // 置空，破坏掉path to gc节点
                    }
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }

        }
    }

    companion object {

        private const val TAG = "MainActivity"
    }
}
