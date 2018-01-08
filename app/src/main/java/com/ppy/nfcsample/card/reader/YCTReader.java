package com.ppy.nfcsample.card.reader;

import android.util.Log;

import com.ppy.nfclib.NfcCardReaderManager;
import com.ppy.nfcsample.card.Commands;
import com.ppy.nfcsample.card.DefaultCardInfo;
import com.ppy.nfcsample.card.DefaultCardRecord;
import com.ppy.nfcsample.card.Iso7816;
import com.ppy.nfcsample.card.YangChengTong;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZP on 2018/1/4.
 */

public class YCTReader extends BaseReader implements IReader {
    private static final String TAG = "YCTReader";

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public DefaultCardInfo readCard(NfcCardReaderManager mReaderManager) throws IOException {
        Log.d(TAG, "readCard: ");
        YangChengTong cardInfo = new YangChengTong();
        cardInfo.setType(getType());

        Iso7816 dir_pse = new Iso7816(Commands.selectByName(Commands.DFN_PSE));
        Iso7816 dir_srv = new Iso7816(Commands.selectByName(Commands.DFN_SRV));
        Iso7816 dir_srv_info = new Iso7816(Commands.readBinary(21));
        Iso7816 dir_srv_s2 = new Iso7816(Commands.selectByName(Commands.DFN_SRV_S2));
        Iso7816 dir_srv_s1 = new Iso7816(Commands.selectByName(Commands.DFN_SRV_S1));
        Iso7816 balance = new Iso7816(Commands.getBalance(true));
        List<Iso7816> cmds = new ArrayList<>();
        cmds.add(dir_srv);
        cmds.add(dir_srv_info);
        cmds.add(dir_srv_s2);
        cmds.add(balance);
        //read info
        cmds = executeCommands(mReaderManager, cmds);
        if (cmds == null) {
            return null;
        }
        //parse info
        boolean parseInfo = cardInfo.parseCardInfo(cmds.get(1).getResp());
        if (!parseInfo) {
            return null;
        }
        //parse balance
        boolean parseBalance = cardInfo.parseCardBalance(cmds.get(3).getResp());
        if (!parseBalance) {
            return null;
        }
        //read record
        cmds.clear();
        for (int i = 1; i <= 12; i++) {
            Iso7816 dir_srv_record = new Iso7816(Commands.readRecord(24, i), true);
            cmds.add(dir_srv_record);
        }
        for (int i = 0; i < cmds.size(); i++) {
            Iso7816 iso7816 = cmds.get(i);
            System.out.print("指令:" + Commands.ByteArrayToHexString(iso7816.getCmd()));
            byte[] resp = mReaderManager.tranceive(iso7816.getCmd());
            iso7816.setResp(resp);
            System.out.println("  响应:" + Commands.ByteArrayToHexString(resp));
            if (!isSuccess(resp) && !iso7816.isContinue()) {
                return null;
            }
            DefaultCardRecord record = new DefaultCardRecord();
            try {
                record.readRecord(resp);
                cardInfo.getRecords().add(record);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cardInfo;
    }
}
