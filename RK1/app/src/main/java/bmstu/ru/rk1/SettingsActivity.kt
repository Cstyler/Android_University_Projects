package bmstu.ru.rk1

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class SettingsActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "SettingsActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_fragment)
        Log.i(TAG, "OnCreate")
    }
}
