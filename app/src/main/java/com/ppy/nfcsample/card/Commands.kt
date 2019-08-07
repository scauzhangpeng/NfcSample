package com.ppy.nfcsample.card

import java.nio.ByteBuffer

/**
 * Created by ZP on 2016/12/18.
 */

object Commands {

    val DFN_SRV = byteArrayOf('P'.toByte(), 'A'.toByte(), 'Y'.toByte(), '.'.toByte(), 'A'.toByte(), 'P'.toByte(), 'P'.toByte(), 'Y'.toByte())

    val DFN_SRV_S1 = byteArrayOf('P'.toByte(), 'A'.toByte(), 'Y'.toByte(), '.'.toByte(), 'P'.toByte(), 'A'.toByte(), 'S'.toByte(), 'D'.toByte())

    val DFN_SRV_S2 = byteArrayOf('P'.toByte(), 'A'.toByte(), 'Y'.toByte(), '.'.toByte(), 'T'.toByte(), 'I'.toByte(), 'C'.toByte(), 'L'.toByte())

    val DFN_PSE = byteArrayOf('1'.toByte(), 'P'.toByte(), 'A'.toByte(), 'Y'.toByte(), '.'.toByte(), 'S'.toByte(), 'Y'.toByte(), 'S'.toByte(), '.'.toByte(), 'D'.toByte(), 'D'.toByte(), 'F'.toByte(), '0'.toByte(), '1'.toByte())

    fun select_1001(): ByteArray {
        val buff = ByteBuffer.allocate(8)
        buff.put(0x00.toByte()) // CLA Class
                .put(0xA4.toByte()) // INS Instruction
                .put(0x00.toByte()) // P1 Parameter 1
                .put(0x00.toByte()) // P2 Parameter 2
                .put(0x02.toByte()) // Lc
                .put(0x10.toByte()) //
                .put(0x01.toByte()) //
                .put(0x00.toByte()) // Le

        return buff.array()
    }

    fun selectByName(vararg name: Byte): ByteArray {
        val buff = ByteBuffer.allocate(name.size + 6)
        buff.put(0x00.toByte()) // CLA Class
                .put(0xA4.toByte()) // INS Instruction
                .put(0x04.toByte()) // P1 Parameter 1
                .put(0x00.toByte()) // P2 Parameter 2
                .put(name.size.toByte()) // Lc
                .put(name).put(0x00.toByte()) // Le

        return buff.array()
    }

    fun readBinary(sfi: Int): ByteArray {

        return byteArrayOf(0x00.toByte(), // CLA Class
                0xB0.toByte(), // INS Instruction
                (0x00000080 or (sfi and 0x1F)).toByte(), // P1 Parameter 1
                0x00.toByte(), // P2 Parameter 2
                0x00.toByte())
    }

    fun readRecord(sfi: Int): ByteArray {

        return byteArrayOf(0x00.toByte(), // CLA Class
                0xB2.toByte(), // INS Instruction
                0x01.toByte(), // P1 Parameter 1
                (sfi shl 3 or 0x05).toByte(), // P2 Parameter 2
                0x00.toByte())
    }

    fun readRecord(sfi: Int, index: Int): ByteArray {

        return byteArrayOf(0x00.toByte(), // CLA Class
                0xB2.toByte(), // INS Instruction
                index.toByte(), // P1 Parameter 1
                (sfi shl 3 or 0x04).toByte(), // P2 Parameter 2
                0x00.toByte())
    }

    fun getBalance(isEP: Boolean): ByteArray {

        return byteArrayOf(0x80.toByte(), // CLA Class
                0x5C.toByte(), // INS Instruction
                0x00.toByte(), // P1 Parameter 1
                (if (isEP) 2 else 1).toByte(), // P2 Parameter 2
                0x04.toByte())
    }

    /**
     * Utility class to convert a byte array to a hexadecimal string.
     *
     * @param bytes Bytes to convert
     * @return String, containing hexadecimal representation.
     */
    fun ByteArrayToHexString(bytes: ByteArray): String {
        val hexArray = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
        val hexChars = CharArray(bytes.size * 2)
        var v: Int
        for (j in bytes.indices) {
            v = bytes[j].toInt() and 0xFF
            hexChars[j * 2] = hexArray[v.ushr(4)]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }

    /**
     * Utility class to convert a hexadecimal string to a byte string.
     *
     *
     *
     * Behavior with input strings containing non-hexadecimal characters is undefined.
     *
     * @param s String containing hexadecimal characters to convert
     * @return Byte array generated from input
     */
    fun HexStringToByteArray(s: String): ByteArray {
        val len = s.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(s[i], 16) shl 4) + Character.digit(s[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }


}
