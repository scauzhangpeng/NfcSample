package com.ppy.nfcsample

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ppy.nfcsample.ui.brand
import com.ppy.nfcsample.ui.white

/**
 * Created by ZP on 2019/3/21.
 */
class NfcCardReaderActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(modifier = Modifier.padding(all = 16.dp)) {
                OpenReadCityCard()
            }
        }
    }

    private fun openReadCityCardActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    @Preview(showBackground = true)
    @Composable
    fun OpenReadCityCard() {
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = brand, contentColor = white),
            onClick = { openReadCityCardActivity() },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.city_card_reader))
        }
    }
}
