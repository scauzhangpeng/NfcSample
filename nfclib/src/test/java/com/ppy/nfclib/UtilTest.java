package com.ppy.nfclib;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by ZP on 2017/9/20.
 */
public class UtilTest {
    @Test
    public void hexToInt() throws Exception {
        final byte[] testData = {(byte) 0x00, (byte) 0x00, (byte) 0x05, (byte)0xB9};
        final int startIndex = 0;
        final int len = testData.length;
        Assert.assertEquals(1465, Util.hexToInt(testData, startIndex, len));

        final byte[] testData1 = {(byte) 0x80, (byte) 0x00, (byte) 0x03, (byte)0xA2};
        final int startIndex1 = 1;
        final int len1 = testData.length - 1;
        Assert.assertEquals(1465, Util.hexToInt(testData1, startIndex1, len1));
    }

    @Test
    public void toAmountString() throws Exception {
        final long[] input = {0, 1, 9, 10, 11, 99, 100, 101, 1000, 1001};
        final String[] expect = {"0.00", "0.01", "0.09", "0.10", "0.11", "0.99", "1.00", "1.01", "10.00", "10.01"};
        for (int i = 0; i < input.length; i++) {
            String output = Util.toAmountString(input[i]);
            Assert.assertEquals(expect[i], output);
        }
    }

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