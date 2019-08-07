package com.ppy.nfcsample.card.reader

import android.util.Log
import com.ppy.nfclib.NfcCardReaderManager
import com.ppy.nfcsample.card.*
import java.io.IOException
import java.util.*

/**
 * Created by ZP on 2018/1/4.
 */

class SZTReader : BaseReader(), IReader {

    override val type: Int
        get() = 1

    @Throws(IOException::class)
    override fun readCard(nfcCardReaderManager: NfcCardReaderManager): DefaultCardInfo? {
        Log.d(TAG, "readCard: ")
        val cardInfo = ShenZhenTong()
        cardInfo.type = type

        val dir_1001 = Iso7816(Commands.select_1001())
        val dir_srv_info = Iso7816(Commands.readBinary(21))
        val balance = Iso7816(Commands.getBalance(true))
        val cmds: MutableList<Iso7816> = ArrayList()
        cmds.add(dir_1001)
        cmds.add(dir_srv_info)
        cmds.add(balance)

        val cmdResp = executeCommands(nfcCardReaderManager, cmds) ?: return null
        if (cmdResp.size < 3) {
            return null
        }

        val parseCardInfoSuccess = cardInfo.parseCardInfo(cmdResp[1].resp)
        if (!parseCardInfoSuccess) {
            return null
        }
        val parseBalanceSuccess = cardInfo.parseCardBalance(cmdResp[2].resp)
        if (!parseBalanceSuccess) {
            return null
        }
        //read record
        cmds.clear()
        for (i in 1..10) {
            val dir_srv_record = Iso7816(Commands.readRecord(24, i), true)
            cmds.add(dir_srv_record)
        }
        for (i in cmds.indices) {
            val iso7816 = cmds[i]
            print("指令:" + Commands.ByteArrayToHexString(iso7816.cmd))
            val resp = nfcCardReaderManager.tranceive(iso7816.cmd)
            iso7816.resp = resp
            println("  响应:" + Commands.ByteArrayToHexString(resp))
            if (!isSuccess(resp) && !iso7816.isContinue) {
                return null
            }
            val record = DefaultCardRecord()
            try {
                record.readRecord(resp)
                cardInfo.records.add(record)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return cardInfo
    }

    companion object {
        private const val TAG = "SZTReader"
    }
}
