package com.ppy.nfcsample.card.reader;

import com.ppy.nfclib.NfcCardReaderManager;
import com.ppy.nfcsample.card.DefaultCardInfo;

import java.io.IOException;

/**
 * Created by ZP on 2018/1/4.
 */

public interface IReader {

    int getType();

    DefaultCardInfo readCard(NfcCardReaderManager nfcCardReaderManager) throws IOException;

    interface Chain {
        DefaultCardInfo proceed(NfcCardReaderManager nfcCardReaderManager) throws IOException;
    }
}
