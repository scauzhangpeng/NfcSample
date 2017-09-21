package com.ppy.nfcsample.card;

import java.nio.ByteBuffer;

/**
 * Created by ZP on 2016/12/18.
 */

public class Commands {

    public final static byte[] DFN_SRV = { (byte) 'P', (byte) 'A', (byte) 'Y',
            (byte) '.', (byte) 'A', (byte) 'P', (byte) 'P', (byte) 'Y', };

    public final static byte[] DFN_SRV_S1 = { (byte) 'P', (byte) 'A',
            (byte) 'Y', (byte) '.', (byte) 'P', (byte) 'A', (byte) 'S',
            (byte) 'D', };

    public final static byte[] DFN_SRV_S2 = { (byte) 'P', (byte) 'A',
            (byte) 'Y', (byte) '.', (byte) 'T', (byte) 'I', (byte) 'C',
            (byte) 'L', };

    public final static byte[] DFN_PSE = { (byte) '1', (byte) 'P',
            (byte) 'A', (byte) 'Y', (byte) '.', (byte) 'S', (byte) 'Y',
            (byte) 'S', (byte) '.', (byte) 'D', (byte) 'D', (byte) 'F',
            (byte) '0', (byte) '1', };

    public static byte[] selectByName(byte... name) {
        ByteBuffer buff = ByteBuffer.allocate(name.length + 6);
        buff.put((byte) 0x00) // CLA Class
                .put((byte) 0xA4) // INS Instruction
                .put((byte) 0x04) // P1 Parameter 1
                .put((byte) 0x00) // P2 Parameter 2
                .put((byte) name.length) // Lc
                .put(name).put((byte) 0x00); // Le

        return buff.array();
    }

    public static byte[] readBinary(int sfi) {
        final byte[] cmd = { (byte) 0x00, // CLA Class
                (byte) 0xB0, // INS Instruction
                (byte) (0x00000080 | (sfi & 0x1F)), // P1 Parameter 1
                (byte) 0x00, // P2 Parameter 2
                (byte) 0x00, // Le
        };

        return cmd;
    }

    public static byte[] readRecord(int sfi) {
        final byte[] cmd = { (byte) 0x00, // CLA Class
                (byte) 0xB2, // INS Instruction
                (byte) 0x01, // P1 Parameter 1
                (byte) ((sfi << 3) | 0x05), // P2 Parameter 2
                (byte) 0x00, // Le
        };

        return cmd;
    }

    public static byte[] readRecord(int sfi, int index) {
        final byte[] cmd = { (byte) 0x00, // CLA Class
                (byte) 0xB2, // INS Instruction
                (byte) index, // P1 Parameter 1
                (byte) ((sfi << 3) | 0x04), // P2 Parameter 2
                (byte) 0x00, // Le
        };

        return cmd;
    }

    public static byte[] getBalance(boolean isEP) {
        final byte[] cmd = { (byte) 0x80, // CLA Class
                (byte) 0x5C, // INS Instruction
                (byte) 0x00, // P1 Parameter 1
                (byte) (isEP ? 2 : 1), // P2 Parameter 2
                (byte) 0x04, // Le
        };

        return cmd;
    }

    /**
     * Utility class to convert a byte array to a hexadecimal string.
     *
     * @param bytes Bytes to convert
     * @return String, containing hexadecimal representation.
     */
    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Utility class to convert a hexadecimal string to a byte string.
     *
     * <p>Behavior with input strings containing non-hexadecimal characters is undefined.
     *
     * @param s String containing hexadecimal characters to convert
     * @return Byte array generated from input
     */
    public static byte[] HexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static int toInt(byte[] b, int s, int n) {
        int ret = 0;

        final int e = s + n;
        for (int i = s; i < e; ++i) {
            ret <<= 8;
            ret |= b[i] & 0xFF;
        }
        return ret;
    }

    public static String toAmountString(float value) {
        return String.format("%.2f", value);
    }

}
