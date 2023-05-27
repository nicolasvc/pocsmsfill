package com.example.pocsmsfill

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast


class MySmsReceiver : BroadcastReceiver() {
    val TAG = MySmsReceiver::class.java.simpleName
    val pdu_type = "pdus"

    companion object {
        private var broadcastListener: MyBroadcastListener? = null

        fun setBroadcastListener(listener: MyBroadcastListener) {
            broadcastListener = listener
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle: Bundle? = intent?.getExtras()
        val msgs: Array<SmsMessage?>
        var strMessage = ""
        val format = bundle?.getString("format")

        val pdus = bundle!![pdu_type] as Array<*>?
        if (pdus != null) {
            // Check the Android version.
            // Fill the msgs array.
            msgs = arrayOfNulls(pdus.size)
            for (i in msgs.indices) {
                // Check Android version and use appropriate createFromPdu.
                // If Android version M or newer:
                msgs[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray, format)

                // Build the message to show.
                strMessage += msgs[i]?.messageBody
                strMessage += """
            """.trimIndent()
                // Log and display the SMS message.
                Log.d(TAG, "onReceive: $strMessage")
                Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show()
                if (strMessage.length == 5)
                    broadcastListener?.onBroadcastReceived(strMessage)
            }
        }
    }
}