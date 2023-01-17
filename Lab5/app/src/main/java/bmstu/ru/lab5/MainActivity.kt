package bmstu.ru.lab5

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<String> {
    companion object {
        private var curLocaleIndex = 0
        private var listAdapter: ListAdapter? = null
        private val LOCALES = listOf(Locale("ru", "RU"), Locale("en", "US"))
        private const val MAX_NEWS_NUM = 20
        private const val COUNTRY = "us"
        private const val COUNTRY_KEY = "country"
        private const val LOADER_ID = 10
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
            startAsyncTaskLoader()
        }
        recView.adapter = listAdapter

        textView.setTextColor(Color.MAGENTA)

        Log.i("OnCreate.MainActivity", "loaded")
    }

    private fun startAsyncTaskLoader() {
        val asyncTaskLoaderParams = Bundle()
        asyncTaskLoaderParams.putString(COUNTRY_KEY, COUNTRY)
        supportLoaderManager.restartLoader(LOADER_ID, asyncTaskLoaderParams, this).forceLoad()
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<String> {
        Log.i("MainActivity.LOADER", args.toString())

        class NewsLoader : AsyncTaskLoader<String>(this) {
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

            override fun loadInBackground(): String? {
                val param = args!!.getString(COUNTRY_KEY)
                val uri = getNewsRequestUri(param)
                return readDataFromUri(uri) ?: return null
            }
        }
        return NewsLoader()
    }

    override fun onLoadFinished(loader: Loader<String>, data: String?) {
        Log.i("PostExecute", "list adapter creation")
        listAdapter?.articles = parseJSON(data)
        listAdapter?.notifyDataSetChanged()
    }

    private fun parseJSON(jsonStr: String?): JSONArray {
        return JSONObject(jsonStr).getJSONArray("articles")
    }

    override fun onLoaderReset(loader: Loader<String>) {
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

