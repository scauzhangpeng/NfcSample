package com.ppy.nfcsample.card.reader;

import android.util.Log;

import com.ppy.nfclib.NfcCardReaderManager;
import com.ppy.nfcsample.card.Commands;
import com.ppy.nfcsample.card.DefaultCardInfo;
import com.ppy.nfcsample.card.DefaultCardRecord;
import com.ppy.nfcsample.card.Iso7816;
import com.ppy.nfcsample.card.ShenZhenTong;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZP on 2018/1/4.
 */

public class SZTReader extends BaseReader implements IReader {
    private static final String TAG = "SZTReader";

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public DefaultCardInfo readCard(NfcCardReaderManager mReaderManager) throws IOException {
        Log.d(TAG, "readCard: ");
        ShenZhenTong cardInfo = new ShenZhenTong();
        cardInfo.setType(getType());

        Iso7816 dir_1001 = new Iso7816(Commands.select_1001());
        Iso7816 dir_srv_info = new Iso7816(Commands.readBinary(21));
        Iso7816 balance = new Iso7816(Commands.getBalance(true));
        List<Iso7816> cmds = new ArrayList<>();
        cmds.add(dir_1001);
        cmds.add(dir_srv_info);
        cmds.add(balance);

       cmds = executeCommands(mReaderManager, cmds);
        if (cmds == null || cmds.size() < 3) {
            return null;
        }

        boolean parseCardInfoSuccess = cardInfo.parseCardInfo(cmds.get(1).getResp());
        if (!parseCardInfoSuccess) {
            return null;
        }
        boolean parseBalanceSuccess = cardInfo.parseCardBalance(cmds.get(2).getResp());
        if (!parseBalanceSuccess) {
            return null;
        }
        //read record
        cmds.clear();
        for (int i = 1; i <= 10; i++) {
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
