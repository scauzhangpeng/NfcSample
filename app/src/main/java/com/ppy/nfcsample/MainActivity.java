package com.ppy.nfcsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.ppy.nfclib.CardOperatorListener;
import com.ppy.nfclib.NfcCardReaderManager;
import com.ppy.nfclib.Util;
import com.ppy.nfcsample.adapter.RiotGameAdapter;
import com.ppy.nfcsample.adapter.RiotGameViewHolder;
import com.ppy.nfcsample.card.DefaultCardRecord;
import com.ppy.nfcsample.card.Commands;
import com.ppy.nfcsample.card.Iso7816;
import com.ppy.nfcsample.card.YangChengTong;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private TextView mTvCardNumber;
    private TextView mTvCardBalance;

    private RecyclerView mRvCardRecord;
    private RiotGameAdapter<DefaultCardRecord> mAdapter;
    private List<DefaultCardRecord> mCardRecords;
    private YangChengTong mYangChengTong;

    private NfcCardReaderManager mReaderManager;
    private CardOperatorListener mCardOperatorListener = new CardOperatorListener() {
        @Override
        public void onCardConnected(boolean isConnected) {
            System.out.println(Thread.currentThread().getName());
            if (isConnected) {
                readCard();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initNfcCardReader();
    }

    private void initViews() {
        mTvCardNumber = (TextView) findViewById(R.id.tv_card_number);
        mTvCardBalance = (TextView) findViewById(R.id.tv_card_balance);

        mYangChengTong = new YangChengTong();

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

    private void readCard() {
        try {
            Iso7816 dir_pse = new Iso7816(Commands.selectByName(Commands.DFN_PSE));
            Iso7816 dir_srv = new Iso7816(Commands.selectByName(Commands.DFN_SRV));
            Iso7816 dir_srv_s1 = new Iso7816(Commands.selectByName(Commands.DFN_SRV_S1));
            Iso7816 dir_srv_s2 = new Iso7816(Commands.selectByName(Commands.DFN_SRV_S2));
            Iso7816 dir_srv_info = new Iso7816(Commands.readBinary(21));
            Iso7816 balance = new Iso7816(Commands.getBalance(true));
            List<Iso7816> cmds = new ArrayList<>();
//                cmds.add(dir_pse);
            cmds.add(dir_srv);
            cmds.add(dir_srv_info);
//                cmds.add(dir_srv_s1);
            cmds.add(dir_srv_s2);
            cmds.add(balance);
            for (int i = 1; i <= 12; i++) {
                Iso7816 dir_srv_record = new Iso7816(Commands.readRecord(24, i));
                cmds.add(dir_srv_record);
            }
//                cmds.add(dir_srv_info);
            for (int i = 0; i < cmds.size(); i++) {
                System.out.print("指令:" + Commands.ByteArrayToHexString(cmds.get(i).getCmd()));
                byte[] resp = mReaderManager.tranceive(cmds.get(i).getCmd());
                cmds.get(i).setResp(resp);
                System.out.println("  响应:" + Commands.ByteArrayToHexString(resp));
            }

            parseInfo(cmds.get(1));
            parseBalance(cmds.get(3));
            List<Iso7816> cmds1 = new ArrayList<>();
            for (int i = 4; i < 16; i++) {
                cmds1.add(cmds.get(i));
            }
            parseRecords(cmds1);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void parseRecords(List<Iso7816> cmds1) {
        for (int i = 0; i < cmds1.size(); i++) {
            DefaultCardRecord records = new DefaultCardRecord();
            try {
                records.readRecord(cmds1.get(i).getResp());
                mCardRecords.add(records);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void parseBalance(Iso7816 iso7816) {
        mYangChengTong.parseCardBalance(iso7816.getResp());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvCardBalance.setText("余额：" + Util.toAmountString(mYangChengTong.getBalance()));
            }
        });
    }

    private void parseInfo(Iso7816 iso7816) {
        try {
            if (iso7816.getResp() != null) {
                String src = Util.ByteArrayToHexString(iso7816.getResp());
                mYangChengTong.parseCardInfo(src);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvCardNumber.setText("卡号：" + mYangChengTong.getCardNumber());
            }
        });
    }
}
