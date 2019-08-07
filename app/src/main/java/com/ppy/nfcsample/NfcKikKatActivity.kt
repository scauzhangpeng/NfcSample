package com.ppy.nfcsample

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.ppy.nfclib.Util
import java.util.*

/**
 * Created by ZP on 2017/9/20.
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
class NfcKikKatActivity : AppCompatActivity() {

    private val mReaderCallback = NfcAdapter.ReaderCallback { tag ->
        println(Arrays.toString(tag.techList))
        println(tag.describeContents())
        println(Util.ByteArrayToHexString(tag.id))
        println(tag.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        enableReaderMode()
    }

    override fun onPause() {
        super.onPause()
        disableReaderMode()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
    }

    private fun enableReaderMode() {
        Log.i(TAG, "Enabling reader mode")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val nfc = NfcAdapter.getDefaultAdapter(this)
            nfc?.enableReaderMode(this, mReaderCallback, READER_FLAGS, null)
        }

    }

    private fun disableReaderMode() {
        Log.i(TAG, "Disabling reader mode")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val nfc = NfcAdapter.getDefaultAdapter(this)
            nfc?.disableReaderMode(this)
        }
    }

    companion object {

        private val TAG = "NfcKikKatActivity"
        private const val READER_FLAGS = (NfcAdapter.FLAG_READER_NFC_A
                or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK or NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS)
    }
}
