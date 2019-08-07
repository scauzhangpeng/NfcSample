package com.ppy.nfcsample

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.nfc.tech.NfcF
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.util.*

/**
 * Created by ZP on 2017/9/20.
 */

class NfcJellyBeanActivity : AppCompatActivity() {

    private var mNfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        pendingIntent = PendingIntent.getActivity(
                this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
    }

    override fun onResume() {
        Log.d(TAG, "onResume: ")
        super.onResume()
        val techListsArray = arrayOf(arrayOf(NfcF::class.java.name), arrayOf(IsoDep::class.java.name))
        mNfcAdapter?.enableForegroundDispatch(this, pendingIntent, null, techListsArray)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d(TAG, "onNewIntent: ")
        if (intent != null) {
            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            println(Arrays.toString(tag.techList))
        } else {
            println("intent null")
        }
    }

    override fun onPause() {
        Log.d(TAG, "onPause: ")
        super.onPause()
        mNfcAdapter?.disableForegroundDispatch(this)
    }

    companion object {

        private const val TAG = "NfcJellyBeanActivity"
    }
}
