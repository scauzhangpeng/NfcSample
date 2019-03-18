package com.ppy.nfcsample;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ppy.nfclib.CardOperatorListener;
import com.ppy.nfclib.NfcCardReaderManager;
import com.ppy.nfclib.NfcStatusChangeBroadcastReceiver;
import com.ppy.nfclib.Util;
import com.ppy.nfcsample.adapter.RiotGameAdapter;
import com.ppy.nfcsample.adapter.RiotGameViewHolder;
import com.ppy.nfcsample.card.DefaultCardInfo;
import com.ppy.nfcsample.card.DefaultCardRecord;
import com.ppy.nfcsample.card.reader.CardClient;
import com.ppy.nfcsample.card.reader.SZTReader;
import com.ppy.nfcsample.card.reader.YCTReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private LinearLayout mLlReadCard;
    private LinearLayout mLlShowCard;

    private TextView mTvCardNumber;
    private TextView mTvCardBalance;

    private RecyclerView mRvCardRecord;
    private RiotGameAdapter<DefaultCardRecord> mAdapter;
    private List<DefaultCardRecord> mCardRecords;

    private NfcCardReaderManager mReaderManager;
    private CardOperatorListener mCardOperatorListener = new CardOperatorListener() {
        @Override
        public void onCardConnected(boolean isConnected) {
            System.out.println(Thread.currentThread().getName());
            if (isConnected) {
                execute();
            }
        }

        @Override
        public void onException(int code, String message) {
            if (code == 1) {
                showDialog("NFC设备", "NFC未打开，前往打开？", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissDialog();
                        Util.intentToNfcSetting(MainActivity.this);
                    }
                });
            }

            if (code == 0) {
                showDialog("NFC设备", "设备不支持NFC", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissDialog();
                    }
                });
            }
        }
    };

    private NfcStatusChangeBroadcastReceiver mNfcStatusChangeBroadcastReceiver = new NfcStatusChangeBroadcastReceiver() {
        @Override
        protected void doOnNfcOn() {
            super.doOnNfcOn();
            dismissDialog();
        }

        @Override
        protected void doOnNfcOff() {
            super.doOnNfcOff();
            showDialog("接收到系统广播", "Nfc已经关闭，前往打开？", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissDialog();
                    Util.intentToNfcSetting(MainActivity.this);
                }
            });
        }
    };

    private CardClient mCardClient;
    private Dialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initNfcCardReader();
        initCardClient();
        mReaderManager.onCreate(getIntent());
        registerReceiver(mNfcStatusChangeBroadcastReceiver, NfcStatusChangeBroadcastReceiver.getNfcBroadcastReceiverIntentFilter());
    }

    private void initViews() {
        mLlReadCard = (LinearLayout) findViewById(R.id.ll_read_card);
        mLlShowCard = (LinearLayout) findViewById(R.id.ll_show_card);

        mTvCardNumber = (TextView) findViewById(R.id.tv_card_number);
        mTvCardBalance = (TextView) findViewById(R.id.tv_card_balance);

        mRvCardRecord = (RecyclerView) findViewById(R.id.rv_card_record);
        mRvCardRecord.setLayoutManager(new LinearLayoutManager(this));
        mRvCardRecord.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mCardRecords = new ArrayList<>();
        mAdapter = new RiotGameAdapter<DefaultCardRecord>(R.layout.item_card_record, mCardRecords, this) {
            @Override
            protected void bindData(RiotGameViewHolder holder, DefaultCardRecord cardRecord, int position) {
                holder.setText(R.id.tv_record_type, String.valueOf(cardRecord.getTypeName()));
                holder.setText(R.id.tv_record_date, DateUtil.str2str(cardRecord.getDate(), DateUtil.MMddHHmmss, DateUtil.MM_dd_HH_mm));
                String price = Util.toAmountString(cardRecord.getPrice());
                if ("09".equals(cardRecord.getTypeCode())) {
                    holder.setText(R.id.tv_record_price, "-" + price);
                } else {
                    holder.setText(R.id.tv_record_price, price);
                }
            }
        };
        mRvCardRecord.setAdapter(mAdapter);
    }

    private void initNfcCardReader() {
        mReaderManager = new NfcCardReaderManager.Builder(this)
                .enableSound(false)
//                .setReaderPresenceCheckDelay(30000)
                .build();
        mReaderManager.setOnCardOperatorListener(mCardOperatorListener);
    }

    private void initCardClient() {
        mCardClient = new CardClient.Builder()
                .nfcManager(mReaderManager)
                .addReader(new SZTReader())
                .addReader(new YCTReader())
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        mReaderManager.onStart(this);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        mReaderManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        mReaderManager.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        dismissDialog();
        mReaderManager.onDestroy();
        unregisterReceiver(mNfcStatusChangeBroadcastReceiver);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: " + intent.getAction());
        mReaderManager.onNewIntent(intent);
    }

    private void execute() {
        dismissDialog();
        final DefaultCardInfo cardInfo;
        try {
            cardInfo = mCardClient.execute();
        } catch (IOException e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showDialog("读卡失败", "请重新贴紧卡片", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismissDialog();
                            mLlShowCard.setVisibility(View.GONE);
                            mLlReadCard.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });
            return;
        }
        if (cardInfo == null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showDialog("读卡失败", "暂不支持此类卡片", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismissDialog();
                            mLlShowCard.setVisibility(View.GONE);
                            mLlReadCard.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLlReadCard.setVisibility(View.GONE);
                mLlShowCard.setVisibility(View.VISIBLE);
                if (cardInfo.getType() == 0) {
                    mTvCardNumber.setText("羊城通卡号:" + cardInfo.getCardNumber());
                    mTvCardBalance.setText("余额：" + Util.toAmountString(cardInfo.getBalance()));
                    mCardRecords.clear();
                    mCardRecords.addAll(cardInfo.getRecords());
                    mAdapter.notifyDataSetChanged();
                }
                if (cardInfo.getType() == 1) {
                    mTvCardNumber.setText("深圳通卡号:" + cardInfo.getCardNumber());
                    mTvCardBalance.setText("余额：" + Util.toAmountString(cardInfo.getBalance()));
                    mCardRecords.clear();
                    mCardRecords.addAll(cardInfo.getRecords());
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void showDialog(String title, String content, View.OnClickListener listener) {
        dismissDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_tag_lost, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.dialog_title);
        TextView tvContent = (TextView) view.findViewById(R.id.dialog_content);
        tvTitle.setText(title);
        tvContent.setText(content);
        Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btnOk = (Button) view.findViewById(R.id.btn_confirm);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
        btnOk.setOnClickListener(listener);

        builder.setView(view);
        mDialog = builder.create();
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    private void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
