package com.ppy.nfcsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by ZP on 2019/3/21.
 */
public class NfcCardReaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_cardreader);
    }

    public void openReadCityCardActivity(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
