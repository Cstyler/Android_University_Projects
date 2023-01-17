package bmstu.ru.lab11

import android.app.IntentService
import android.content.Intent
import android.util.Log
import android.widget.Toast

class SimpleService : IntentService("testService") {

    companion object {
        private const val TAG = "SimpleService"
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.i(TAG, "Start handling intent")

        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }

        Log.i(TAG, "Stop handling intent")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, "Service starting", Toast.LENGTH_SHORT).show()
        return super.onStartCommand(intent, flags, startId)
    }
}