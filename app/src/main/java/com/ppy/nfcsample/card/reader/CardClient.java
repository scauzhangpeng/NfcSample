package com.ppy.nfcsample.card.reader;

import com.ppy.nfclib.NfcCardReaderManager;
import com.ppy.nfcsample.card.DefaultCardInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZP on 2018/1/5.
 */

public class CardClient {

    private List<IReader> readers;
    private IReader.Chain chain;
    private NfcCardReaderManager mNfcCardReaderManager;


    private CardClient(Builder builder) {
        this.readers = builder.readers;
        this.mNfcCardReaderManager = builder.mNfcCardReaderManager;
        chain = new CardReaderChain(this.readers);
    }

    public DefaultCardInfo execute() throws IOException {
        return chain.proceed(mNfcCardReaderManager);
    }


    public static final class Builder {
        private final List<IReader> readers = new ArrayList<>();
        private NfcCardReaderManager mNfcCardReaderManager;

        public Builder() {
        }

        public Builder(CardClient copy) {
            this.readers.addAll(copy.readers);
            this.mNfcCardReaderManager = copy.mNfcCardReaderManager;
        }

        public Builder addReader(IReader reader) {
            readers.add(reader);
            return this;
        }

        public Builder nfcManager(NfcCardReaderManager manager) {
            mNfcCardReaderManager = manager;
            return this;
        }

        public CardClient build() {
            return new CardClient(this);
        }
    }
}
