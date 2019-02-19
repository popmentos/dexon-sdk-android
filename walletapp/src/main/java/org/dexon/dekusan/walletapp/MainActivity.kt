package org.dexon.dekusan.walletapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.gson.Gson
import dekusan.SignRequestHelper
import org.dexon.dekusan.core.model.Message
import org.dexon.dekusan.core.model.Transaction
import pm.gnosis.eip712.adapters.moshi.MoshiAdapter

class MainActivity : AppCompatActivity(), SignRequestHelper.Callback {

    private lateinit var signHelper: SignRequestHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signHelper = SignRequestHelper(intent, this)
    }

    override fun sendTransaction(transaction: Transaction?) {
        transaction?.let {
            Log.d("WALLET_APP", "Message: " + it.value.toString() + ":" + it.to.toString())
            AlertDialog.Builder(this)
                .setMessage(transaction.toString())
                .setNegativeButton("cancel") { dialog, wich ->
                    signHelper.onSignCancel(this@MainActivity)
                }
                .setPositiveButton("ok") { dialog, which ->
                    signHelper.onTransactionSent(this, "".toByteArray())
                }
                .show()

        }
    }

    override fun signMessage(message: Message<String>?) {
        message?.let {
            Log.d("WALLET_APP", "Message: " + message.value)
            AlertDialog.Builder(this)
                    .setMessage(message.value)
                    .setNegativeButton("cancel") { dialog, wich ->
                        signHelper.onSignCancel(this@MainActivity)
                    }
                    .setPositiveButton("ok") {dialog, which ->
                        signHelper.onMessageSigned(this, "Hello!".toByteArray())
                    }
                    .show()

        }
    }

    override fun signTypedMessage(message: Message<Array<MoshiAdapter.TypedData>>?) {
        message?.let {
            Log.d("WALLET_APP", "Message: " + message.value)
            AlertDialog.Builder(this)
                    .setMessage(Gson().toJson(message.value))
                    .setNegativeButton("cancel") { dialog, wich ->
                        signHelper.onSignCancel(this@MainActivity)
                    }
                    .setPositiveButton("ok") {dialog, which ->
                        signHelper.onMessageSigned(this, "Hello!".toByteArray())
                    }
                    .show()

        }
    }

    override fun signPersonalMessage(message: Message<String>?) {
        message?.let {
            Log.d("WALLET_APP", "Personal message: " + message.value)
            AlertDialog.Builder(this)
                    .setMessage(message.value)
                    .setNegativeButton("cancel") { dialog, wich ->
                        signHelper.onSignCancel(this@MainActivity)
                    }
                    .setPositiveButton("ok") {dialog, which ->
                        signHelper.onMessageSigned(this, "Hello personal!".toByteArray())
                    }
                    .show()

        }
    }

    override fun signTransaction(transaction: Transaction?) {
        transaction?.let {
            Log.d("WALLET_APP", "Message: " + it.value.toString() + ":" + it.to.toString())
        }
    }
}
