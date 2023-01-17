package bmstu.ru.lab52

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        private const val SAVE_STRING_KEY = "bundle_key"
        private const val TAG = "MainActivity"
        private var savedString: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val funName = "OnCreate"
        Log.i(TAG, funName)
        val appendStr = "\n$funName"
        textView.append(appendStr)
        textView2.append(appendStr)
        textView3.append(appendStr)

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVE_STRING_KEY)) {
                val text = savedInstanceState.getString(SAVE_STRING_KEY)
//                Log.i(TAG, "TextView: $text. TextView.text ${textView.text}")
                textView.text = text
            }
        }

        if (savedString != null) {
//            Log.i(TAG, "Saved string: $savedString. TextView2.text ${textView2.text}")
            textView2.text = savedString
        }
    }

    override fun onStop() {
        super.onStop()

        val funName = "onStop"
        Log.i(TAG, funName)
        val appendStr = "\n$funName"
        textView.append(appendStr)
        textView2.append(appendStr)
        textView3.append(appendStr)
    }

    override fun onDestroy() {
        super.onDestroy()
        val funName = "OnDestroy"
        Log.i(TAG, funName)
        val appendStr = "\n$funName"
        textView.append(appendStr)
        textView2.append(appendStr)
        textView3.append(appendStr)

        val text = textView2.text.toString()
        savedString = text
    }

    override fun onPause() {
        super.onPause()
        val funName = "OnPause"
        Log.i(TAG, funName)
        val appendStr = "\n$funName"
        textView.append(appendStr)
        textView2.append(appendStr)
        textView3.append(appendStr)
    }

    override fun onResume() {
        super.onResume()
        val funName = "OnResume"
        Log.i(TAG, funName)
        val appendStr = "\n$funName"
        textView.append(appendStr)
        textView2.append(appendStr)
        textView3.append(appendStr)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(SAVE_STRING_KEY, textView.text.toString())
    }
}
