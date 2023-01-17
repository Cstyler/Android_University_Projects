package bmstu.ru.lab11

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val CUSTOM_ACTION = "bmstu.ru.lab11.CUSTOM_ACTION"
    }

    private lateinit var broadcastReceiver: SimpleBroadcastReceiver
    private lateinit var filter: IntentFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        filter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        filter.addAction(CUSTOM_ACTION)

        broadcastReceiver = SimpleBroadcastReceiver()
        registerReceiver(broadcastReceiver, filter)

        setContentView(R.layout.activity_main)
    }

    fun onSendBroadcastClick(view: View) {
        val intent = Intent(CUSTOM_ACTION)
        sendBroadcast(intent)
    }

    fun onStartServiceClick(view: View) {
        val serviceIntent = Intent(this, SimpleService::class.java)
        startService(serviceIntent)
    }

    override fun onResume() {
        Log.i(TAG, "onResume")
        registerReceiver(broadcastReceiver, filter)
        super.onResume()
    }

    override fun onPause() {
        Log.i(TAG, "onPause")
        unregisterReceiver(broadcastReceiver)
        super.onPause()
    }
}
