package bmstu.ru.lab4

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ShareCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        private val LOCALES = listOf(Locale("ru", "RU"),
                Locale("en", "US"))
        private var curLocaleIndex = 0
        private const val REQUEST_DAUGHTER_ACTIVITY = 1
        private const val GOOGLE_URL = "https://www.google.com/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val locale = LOCALES[curLocaleIndex]
        Log.i("OnCreate.Locale", locale.toString())
        changeLocale(locale)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        Log.i("OnCreate.MainActivity", "loaded")

        daughter_button.setOnClickListener {
            val intent = Intent(this, DaughterActivity::class.java)
            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, REQUEST_DAUGHTER_ACTIVITY)
            }
        }

        webbrowser_button.setOnClickListener {
            openWebPage(GOOGLE_URL)
        }

        share_text_button.setOnClickListener {
            shareText("Hello!")
        }

        map_button.setOnClickListener {
            showMap(getGeoLocation("100, 100"))
        }

        sms_button.setOnClickListener {
            composeSmsMessage("Hello!")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_DAUGHTER_ACTIVITY && resultCode == RESULT_OK) {
            val login = data.getStringExtra("Login")
            if (login != null) textViewLogin.text = login
        }
    }

    private fun composeSmsMessage(message: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:")  // This ensures only SMS apps respond
            putExtra("sms_body", message)
        }
        startIntent(intent)
    }

    private fun getGeoLocation(coords: String): Uri {
        return Uri.Builder().scheme("geo").path(coords).query("").build()
    }

    private fun showMap(geoLocation: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = geoLocation
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun shareText(text: String) {
        val intent = ShareCompat.IntentBuilder
                .from(this)
                .setText(text)
                .setType("text/plain")
                .intent
        startIntent(intent)
    }

    private fun openWebPage(url: String) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        startIntent(intent)
    }

    private fun startIntent(intent: Intent) {
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }


    private fun incLocaleIndex() {
        curLocaleIndex = (curLocaleIndex + 1) % LOCALES.size
    }

    private fun changeLocale(locale: Locale) {
        resources.configuration.setLocale(locale)
        resources.updateConfiguration(resources.configuration, resources.displayMetrics)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        Log.i("Menu", "menu loaded")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.language_change -> {
                Log.i("Menu", "Language button pressed")
                incLocaleIndex()
                val curLocale = LOCALES[curLocaleIndex]
                Log.i("Menu.Locale", "$curLocale")
                changeLocale(curLocale)
                recreate()
            }
        }
        return super.onContextItemSelected(item)
    }
}
