package com.alawiyaa.preloaddata

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.alawiyaa.preloaddata.services.DataManagerService
import com.alawiyaa.preloaddata.services.DataManagerService.Companion.CANCEL_MESSAGE
import com.alawiyaa.preloaddata.services.DataManagerService.Companion.FAILED_MESSAGE
import com.alawiyaa.preloaddata.services.DataManagerService.Companion.PREPARATION_MESSAGE
import com.alawiyaa.preloaddata.services.DataManagerService.Companion.SUCCESS_MESSAGE
import com.alawiyaa.preloaddata.services.DataManagerService.Companion.UPDATE_MESSAGE
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(),HandlerCallBack {

    private lateinit var mBoundService: Messenger
    private var mServiceBound: Boolean = false

    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName) {
            mServiceBound = false
        }
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            mBoundService = Messenger(service)
            mServiceBound = true
        }
    }

    //Addd Main Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mBoundServiceIntent = Intent(this@MainActivity, DataManagerService::class.java)
        val mActivityMessenger = Messenger(IncomingHandler(this))
        mBoundServiceIntent.putExtra(DataManagerService.ACTIVITY_HANDLER, mActivityMessenger)
        bindService(mBoundServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE)
    }
    override fun onDestroy() {
        super.onDestroy()
        unbindService(mServiceConnection)
    }

    override fun onPreparation() {
        Toast.makeText(this, "MEMULAI MEMUAT DATA", Toast.LENGTH_LONG).show()
    }

    override fun updateProgress(progress: Long) {
        Log.d("PROGRESS", "updateProgress: $progress")
        progress_bar.progress = progress.toInt()
    }

    override fun loadSuccess() {
        Toast.makeText(this, "BERHASIL", Toast.LENGTH_LONG).show()
        startActivity(Intent(this@MainActivity, MahasiswaActivity::class.java))
        finish()
    }

    override fun loadFailed() {
        Toast.makeText(this, "GAGAL", Toast.LENGTH_LONG).show()
    }

    override fun loadCancel() {
        finish()
    }


    private class IncomingHandler (callback: HandlerCallBack) : Handler() {
        private var weakCallback: WeakReference<HandlerCallBack> = WeakReference(callback)
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                PREPARATION_MESSAGE -> weakCallback.get()?.onPreparation()
                UPDATE_MESSAGE -> {
                    val bundle = msg.data
                    val progress = bundle.getLong("KEY_PROGRESS")
                    weakCallback.get()?.updateProgress(progress)
                }
                SUCCESS_MESSAGE -> weakCallback.get()?.loadSuccess()
                FAILED_MESSAGE -> weakCallback.get()?.loadFailed()
                CANCEL_MESSAGE -> weakCallback.get()?.loadCancel()
            }
        }
    }
}

interface HandlerCallBack {

    fun onPreparation()
    fun updateProgress(progress: Long)
    fun loadSuccess()
    fun loadFailed()
    //add
    fun loadCancel()


}

