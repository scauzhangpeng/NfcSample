package com.ppy.nfcsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ppy.nfclib.CardOperatorListener;
import com.ppy.nfclib.NfcCardReaderManager;
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
                try {
                    execute();
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLlReadCard.setVisibility(View.VISIBLE);
                            mLlShowCard.setVisibility(View.GONE);
                        }
                    });
                }
            }
        }

        @Override
        public void onException(int code, String message) {
            System.out.println(message);
            if (code == 1) {
                Util.intentToNfcSetting(MainActivity.this);
            }
        }
    };

    private CardClient mCardClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initNfcCardReader();
        initCardClient();
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
        mReaderManager = new NfcCardReaderManager.Builder()
                .mActivity(this)
                .enableSound(false)
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
        super.onDestroy();
        mReaderManager.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mReaderManager.onNewIntent(intent);
    }

    private void execute() throws IOException {
        final DefaultCardInfo cardInfo = mCardClient.execute();
        if (cardInfo == null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "not support", Toast.LENGTH_SHORT).show();
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
                    mCardRecords.addAll(cardInfo.getRecords());
                    mAdapter.notifyDataSetChanged();
                }
                if (cardInfo.getType() == 1) {
                    mTvCardNumber.setText("深圳通卡号:" + cardInfo.getCardNumber());
                    mTvCardBalance.setText("余额：" + Util.toAmountString(cardInfo.getBalance()));
                    mCardRecords.addAll(cardInfo.getRecords());
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
