package com.ppy.nfcsample.card.reader;

import com.ppy.nfclib.NfcCardReaderManager;
import com.ppy.nfcsample.card.DefaultCardInfo;

import java.io.IOException;
import java.util.List;

/**
 * Created by ZP on 2018/1/4.
 */

public class CardReaderChain implements IReader.Chain {

    private List<IReader> mReaders;

    public CardReaderChain(List<IReader> readers) {
        mReaders = readers;
    }

    @Override
    public DefaultCardInfo proceed(NfcCardReaderManager nfcCardReaderManager) throws IOException {
        DefaultCardInfo defaultCardInfo = null;
        for (IReader reader : mReaders) {
            defaultCardInfo = reader.readCard(nfcCardReaderManager);
            if (defaultCardInfo != null) {
                break;
            }
        }
        return defaultCardInfo;
    }
}
