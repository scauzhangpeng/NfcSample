package com.ppy.nfcsample

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by ZP on 2019/3/21.
 */
class NfcCardReaderMin18Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfc_cardreader)

        findViewById<Button>(R.id.btnOpenReadCityCardActivity).setOnClickListener {
            openReadCityCardActivity()
        }
    }

    private fun openReadCityCardActivity() {
        startActivity(Intent(this, MainMin18Activity::class.java))
    }

}
