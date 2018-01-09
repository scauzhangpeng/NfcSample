package com.ppy.nfcsample.card.reader;

import com.ppy.nfclib.NfcCardReaderManager;
import com.ppy.nfcsample.card.Commands;
import com.ppy.nfcsample.card.Iso7816;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ZP on 2018/1/5.
 */

public class BaseReader {


    // "OK" status word sent in response to SELECT AID command (0x9000)
    private final byte[] SELECT_OK_SW = {(byte) 0x90, (byte) 0x00};

    protected boolean isSuccess(byte[] tranceive) {
        if (tranceive == null) {
            return false;
        }
        int length = tranceive.length;
        if (length < 2) {
            return false;
        }
        byte[] statusWord = {tranceive[length - 2], tranceive[length - 1]};
        return Arrays.equals(statusWord, SELECT_OK_SW);
    }

    protected List<Iso7816> executeCommands(NfcCardReaderManager mReader, List<Iso7816> commands) throws IOException {
        List<Iso7816> resp = new ArrayList<>();
        for (Iso7816 command : commands) {
            Iso7816 iso7816 = executeSingleCommand(mReader, command);
            if (iso7816 == null) {
                return null;
            }
            resp.add(iso7816);
        }
        return resp;
    }

    protected Iso7816 executeSingleCommand(NfcCardReaderManager mReader, Iso7816 command) throws IOException {
        byte[] cmd = command.getCmd();
        System.out.print("指令:" + Commands.ByteArrayToHexString(cmd));
        byte[] resp = mReader.tranceive(cmd);
        System.out.println("  响应:" + Commands.ByteArrayToHexString(resp));
        command.setResp(resp);
        if (!isSuccess(resp) && !command.isContinue()) {
            return null;
        }
        return command;
    }

}
