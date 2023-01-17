package bmstu.ru.rk1

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.currency_activity.*

class CurrencyActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "CurrencyActivity"
        private const val ITEM_TEXT_KEY = "item_text"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.currency_activity)

        val text = intent.getStringExtra(ITEM_TEXT_KEY)
        textView.text = text
        Log.i(TAG, "OnCreate")
    }
}
