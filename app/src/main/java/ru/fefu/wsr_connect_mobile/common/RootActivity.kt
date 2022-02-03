package ru.fefu.wsr_connect_mobile.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.fefu.wsr_connect_mobile.R
import ru.fefu.wsr_connect_mobile.remote.SocketHandler

class RootActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketHandler.closeConnection()
    }
}