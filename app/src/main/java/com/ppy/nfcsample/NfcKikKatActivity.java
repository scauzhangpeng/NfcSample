package com.ppy.nfcsample;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ppy.nfclib.Util;

import java.util.Arrays;

/**
 * Created by ZP on 2017/9/20.
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class NfcKikKatActivity extends AppCompatActivity {

    private static final String TAG = "NfcKikKatActivity";
    private static final int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_A
            | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK | NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS;

    private NfcAdapter mNfcAdapter;
    private NfcAdapter.ReaderCallback mReaderCallback = new NfcAdapter.ReaderCallback() {
        @Override
        public void onTagDiscovered(Tag tag) {
            System.out.println(Arrays.toString(tag.getTechList()));
            System.out.println(tag.describeContents());
            System.out.println(Util.ByteArrayToHexString(tag.getId()));
            System.out.println(tag.toString());
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableReaderMode();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableReaderMode();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void enableReaderMode() {
        Log.i(TAG, "Enabling reader mode");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
            if (nfc != null) {
                nfc.enableReaderMode(this, mReaderCallback, READER_FLAGS, null);
            }
        }

    }

    private void disableReaderMode() {
        Log.i(TAG, "Disabling reader mode");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            NfcAdapter nfc = NfcAdapter.getDefaultAdapter(this);
            if (nfc != null) {
                nfc.disableReaderMode(this);
            }
        }
    }
}
