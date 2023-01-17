package bmstu.ru.lab6

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView1.setTextColor(Color.MAGENTA)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        showCheckboxPreference(sharedPreferences, getString(R.string.checkbox_pref_key))
        showListItem(sharedPreferences, getString(R.string.list_pref_key))
        showEditTextPreference(sharedPreferences, getString(R.string.edit_text_pref_key))
        showSwitchFlag(sharedPreferences, getString(R.string.switch_pref_key))
        Log.i(TAG, "OnCreate")
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        when (key) {
            getString(R.string.checkbox_pref_key) -> showCheckboxPreference(sharedPreferences, key)
            getString(R.string.list_pref_key) -> showListItem(sharedPreferences, key)
            getString(R.string.edit_text_pref_key) -> showEditTextPreference(sharedPreferences, key)
            getString(R.string.switch_pref_key) -> showSwitchFlag(sharedPreferences, key)
        }
    }

    private fun showSwitchFlag(sharedPreferences: SharedPreferences, key: String?) {
        val flag = sharedPreferences.getBoolean(key, true)
        if (flag) {
            textView4.text = "Switch on"
        } else {
            textView4.text = "Switch off"
        }
    }

    private fun showCheckboxPreference(sharedPreferences: SharedPreferences, key: String?) {
        val flag = sharedPreferences.getBoolean(key, true)
        if (flag) {
            textView1.text = "Show notifications"
        } else {
            textView1.text = "Don't show notifications"
        }
    }

    private fun showListItem(sharedPreferences: SharedPreferences, key: String?) {
        val money = sharedPreferences.getString(key, "")
        textView2.text = money
    }

    private fun showEditTextPreference(sharedPreferences: SharedPreferences, key: String?) {
        val price = sharedPreferences.getString(key, "")
        textView3.text = price
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager
            .getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        Log.i(TAG, "menu loaded")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }
        }
        return super.onContextItemSelected(item)
    }
}
