package com.ppy.peng.nfclib;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by ZP on 2017/9/20.
 */
public class UtilTest {
    /**
     * Test converting from binary to a hex string
     */
    @Test
    public void byteArrayToHexString() throws Exception {
        final byte[] input = {(byte) 0xc0, (byte) 0xff, (byte) 0xee};
        final String output = Util.ByteArrayToHexString(input);
        assertEquals("C0FFEE", output);
    }

    /**
     * Test converting from a hex string to binary.
     */
    @Test
    public void hexStringToByteArray() throws Exception {
        final byte[] testData = {(byte) 0xc0, (byte) 0xff, (byte) 0xee};
        final byte[] output = Util.HexStringToByteArray("C0FFEE");
        for (int i = 0; i < testData.length; i++) {
            assertEquals(testData[i], output[i]);
        }
    }

}