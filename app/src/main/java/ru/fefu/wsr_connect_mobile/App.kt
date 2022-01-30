package ru.fefu.wsr_connect_mobile

import android.app.Application
import android.content.SharedPreferences
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class App : Application() {

    companion object {
        lateinit var INSTANCE: App
        lateinit var sharedPreferences: SharedPreferences
        lateinit var mSocket: Socket
    }

    @Synchronized
    fun setSocket() {
        try {
            mSocket = IO.socket(BASE_URL)
        } catch (e: URISyntaxException) {

        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        setSocket()
    }

    override fun onTerminate() {
        super.onTerminate()
        closeConnection()
    }
}