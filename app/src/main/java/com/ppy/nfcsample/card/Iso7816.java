package com.ppy.nfcsample.card;

/**
 * Created by ZP on 2016/12/18.
 */

public class Iso7816 {
    public Iso7816(byte[] cmd) {
        this.cmd = cmd;
    }

    private byte[] cmd;
    private byte[] resp;

    public byte[] getCmd() {
        return cmd;
    }

    public void setCmd(byte[] cmd) {
        this.cmd = cmd;
    }

    public byte[] getResp() {
        return resp;
    }

    public void setResp(byte[] resp) {
        this.resp = resp;
    }
}
