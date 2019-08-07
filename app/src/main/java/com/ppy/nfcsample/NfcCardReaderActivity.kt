package com.ppy.nfcsample

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by ZP on 2019/3/21.
 */
class NfcCardReaderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_cardreader)
    }

    fun openReadCityCardActivity(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
    }
}
