package com.ppy.nfcsample

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.ppy.nfclib.exception.ExceptionConstant
import com.ppy.nfclib.util.Util
import com.ppy.nfcsample.card.DefaultCardInfo
import com.ppy.nfcsample.card.reader.CardClient
import com.ppy.nfcsample.card.reader.SZTReader
import com.ppy.nfcsample.card.reader.YCTReader
import java.io.IOException
import kotlin.concurrent.thread

open class CommonActivity: NfcActivity() {

    private lateinit var mCardClient: CardClient
    private var mDialog: Dialog? = null

    protected var isPre = mutableStateOf(true)
    protected var mCardInfo: MutableState<DefaultCardInfo?> = mutableStateOf(null)

    protected var isPreLiveData = MutableLiveData<Boolean>()
    protected var mCardInfoLiveData = MutableLiveData<DefaultCardInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentUiView()
        initCardClient()
    }

    protected open fun setContentUiView() {

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
        dismissDialog()
    }

    override fun doOnNfcOff() {
        showDialog("接收到系统广播", "Nfc已经关闭，前往打开？") {
            dismissDialog()
            Util.intentToNfcSetting(this)
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
                Util.intentToNfcSetting(this)
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
        thread {//读卡耗时放在子线程
            val cardInfo: DefaultCardInfo?
            try {
                cardInfo = mCardClient.execute()
                ContextCompat.getMainExecutor(this).execute {
                    cardInfo?.let {
                        doOnReadCardSuccess(it)
                    } ?: doOnReadCardError()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                ContextCompat.getMainExecutor(this).execute {
                    showDialog("读卡失败", "请重新贴紧卡片") {
                        dismissDialog()
                        isPre.value = true
                        isPreLiveData.postValue(true)
                    }
                }
            }
        }
    }

    private fun doOnReadCardSuccess(cardInfo: DefaultCardInfo) {
        isPre.value = false
        isPreLiveData.postValue(false)
        mCardInfo.value = cardInfo
        mCardInfoLiveData.postValue(cardInfo)
    }

    private fun doOnReadCardError() {
        showDialog("读卡失败", "暂不支持此类卡片") {
            dismissDialog()
            isPre.value = true
            isPreLiveData.postValue(true)
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
}