package com.ppy.nfcsample

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.ppy.nfclib.exception.ExceptionConstant
import com.ppy.nfclib.util.Util
import com.ppy.nfcsample.card.DefaultCardInfo
import com.ppy.nfcsample.card.reader.CardClient
import com.ppy.nfcsample.card.reader.SZTReader
import com.ppy.nfcsample.card.reader.YCTReader
import com.ppy.nfcsample.ui.ReadCard
import java.io.IOException
import java.lang.reflect.Field
import java.util.*

class MainActivity : NfcActivity() {

    private lateinit var mCardClient: CardClient
    private var mDialog: Dialog? = null

    private var isPre = mutableStateOf(true)
    private var mCardInfo: MutableState<DefaultCardInfo?> = mutableStateOf(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReadCard(isPre.value, mCardInfo.value)
        }
        initCardClient()
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
                    isPre.value = true
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
        isPre.value = false
        mCardInfo.value = cardInfo
    }

    private fun doOnReadCardError() {
        showDialog("读卡失败", "暂不支持此类卡片") {
            dismissDialog()
            isPre.value = true
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
}
