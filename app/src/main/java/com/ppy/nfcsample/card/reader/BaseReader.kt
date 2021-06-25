package com.ppy.nfcsample.card.reader

import com.ppy.nfclib.NfcManagerCompat
import com.ppy.nfcsample.card.Commands
import com.ppy.nfcsample.card.Iso7816
import java.io.IOException
import java.util.*

/**
 * Created by ZP on 2018/1/5.
 */

open class BaseReader {


    // "OK" status word sent in response to SELECT AID command (0x9000)
    private val SELECT_OK_SW = byteArrayOf(0x90.toByte(), 0x00.toByte())

    protected fun isSuccess(tranceive: ByteArray?): Boolean {
        if (tranceive == null) {
            return false
        }
        val length = tranceive.size
        if (length < 2) {
            return false
        }
        val statusWord = byteArrayOf(tranceive[length - 2], tranceive[length - 1])
        return Arrays.equals(statusWord, SELECT_OK_SW)
    }

    @Throws(IOException::class)
    protected fun executeCommands(mReader: NfcManagerCompat, commands: MutableList<Iso7816>): MutableList<Iso7816>? {
        val resp = ArrayList<Iso7816>()
        for (command in commands) {
            val iso7816 = executeSingleCommand(mReader, command) ?: return null
            resp.add(iso7816)
        }
        return resp
    }

    @Throws(IOException::class)
    protected fun executeSingleCommand(mReader: NfcManagerCompat, command: Iso7816): Iso7816? {
        val cmd = command.cmd
        print("指令:" + Commands.ByteArrayToHexString(cmd))
        val resp = mReader.tranceive(cmd)
        println("  响应:" + Commands.ByteArrayToHexString(resp))
        command.resp = resp
        return if (!isSuccess(resp) && !command.isContinue) {
            null
        } else command
    }

}
