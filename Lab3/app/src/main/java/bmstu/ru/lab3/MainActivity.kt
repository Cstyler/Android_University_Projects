package bmstu.ru.lab3

import android.annotation.SuppressLint
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        private val LOCALES = listOf(Locale("ru", "RU"), Locale("en", "US"))
        private var curLocaleIndex = 0
        private const val MAX_NEWS_NUM = 20
        private const val COUNTRY = "us"
        private var listAdapter: ListAdapter? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val locale = LOCALES[curLocaleIndex]
        Log.i("OnCreate.Locale", locale.toString())
        changeLocale(locale)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        recView.layoutManager = LinearLayoutManager(this)
        recView.setHasFixedSize(true)

        if (listAdapter == null) {
            listAdapter = ListAdapter(MAX_NEWS_NUM)
            recView.adapter = listAdapter
            performRequest()
        }
        recView.adapter = listAdapter

        textView.setTextColor(Color.MAGENTA)

        Log.i("OnCreate.MainActivity", "loaded")
    }

    @SuppressLint("StaticFieldLeak")
    inner class AsyncRequestTask : AsyncTask<String, Void, String>() {
        private val apiKey = "1eda2241237f48f8867b94fce684d025"
        private val newsApiRoute = "https://newsapi.org/v2/top-headlines"

        private fun readDataFromUri(newsUri: Uri?): String? {
            val url = URL(newsUri.toString())
            val urlConnection = url.openConnection() as HttpURLConnection
            try {
                val inputStream = urlConnection.inputStream
                val scanner = Scanner(inputStream).useDelimiter("\\A")
                if (scanner.hasNext()) {
                    return scanner.next()
                }
            } finally {
                urlConnection.disconnect()
            }
            return null
        }

        private fun getNewsRequestUri(country: String): Uri? {
            return Uri.parse(newsApiRoute).buildUpon()
                    .appendQueryParameter("country", country)
                    .appendQueryParameter("apiKey", apiKey)
                    .build()
        }

        override fun doInBackground(vararg params: String): String? {
            val uri = getNewsRequestUri(params[0])
            return readDataFromUri(uri) ?: return null
        }

        override fun onPostExecute(result: String?) {
            Log.i("PostExecute", "list adapter creation")
            listAdapter?.articles = parseJSON(result)
            listAdapter?.notifyDataSetChanged()
        }

        private fun parseJSON(jsonStr: String?): JSONArray {
            return JSONObject(jsonStr).getJSONArray("articles")
        }
    }

    private fun performRequest() {
        AsyncRequestTask().execute(COUNTRY)
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
            R.id.refresh -> {
                Log.i("Menu", "refresh button pressed")
                performRequest()
                Toast.makeText(this, "Refresh news...", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onContextItemSelected(item)
    }


    private fun incLocaleIndex() {
        curLocaleIndex = (curLocaleIndex + 1) % LOCALES.size
    }

    private fun changeLocale(locale: Locale) {
        resources.configuration.setLocale(locale)
        resources.updateConfiguration(resources.configuration, resources.displayMetrics)
    }
}

