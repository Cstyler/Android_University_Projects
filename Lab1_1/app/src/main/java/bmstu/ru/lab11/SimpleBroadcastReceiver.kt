package bmstu.ru.lab11

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class SimpleBroadcastReceiver : BroadcastReceiver() {
    companion object {
        private const val TAG = "SimpleBroadcastReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "Action: ${intent?.action}", Toast.LENGTH_SHORT).show()
    }
}