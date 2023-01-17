package bmstu.ru.rk1

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.LoaderManager
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader
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

class MainActivity : AppCompatActivity(),
    LoaderManager.LoaderCallbacks<String>,
    SharedPreferences.OnSharedPreferenceChangeListener {
    companion object {
        private var listAdapter: ListAdapter? = null
        private const val TAG = "MainActivity"
        private const val FSYM_KEY = "fsym"
        private const val TSYM_KEY = "tsym"
        private const val LIMIT_KEY = "limit"

        private const val LOADER_ID = 10
        private const val URL_TEMPLATE = "https://www.cryptocompare.com/coins/%s/timeline/%s"
        private var REFRESH_DATA = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        recView.layoutManager = LinearLayoutManager(this)
        recView.setHasFixedSize(true)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        val currency = sharedPreferences.getString(getString(R.string.list_pref_key), "")
        val days = sharedPreferences.getString(getString(R.string.edit_text_pref_key), "")
        Log.i(TAG, "currency: $currency, days: $days")
        if (getInCurrencyName() != null) {
            if (listAdapter == null) {
                listAdapter = ListAdapter()
                recView.adapter = listAdapter
                startAsyncTaskLoader()
            }
        }

        if (REFRESH_DATA) {
            REFRESH_DATA = false
            listAdapter = ListAdapter()
            recView.adapter = listAdapter
            if (getInCurrencyName() != null) {
                startAsyncTaskLoader()
            }
        }
        recView.adapter = listAdapter
        Log.i(TAG, "OnCreate")
    }

    private fun getInCurrencyName(): String? {
        val s = edit_text.text.toString().toLowerCase()
        Log.i(TAG, "edit text: $s")
        if (!setOf("btc", "bth", "eth").contains(s)) {
            Toast.makeText(
                this,
                "Currency must some of: \"btc\", \"bth\", \"eth\"",
                Toast.LENGTH_SHORT
            ).show()
            return null
        }
        return s
    }

    private fun getTranslateCurrency(): String {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        return sharedPreferences.getString(getString(R.string.list_pref_key), "")!!
    }

    private fun getDaysFromPrefs(): String {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        return sharedPreferences.getString(getString(R.string.edit_text_pref_key), "")!!
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        REFRESH_DATA = true
    }

    private fun startAsyncTaskLoader() {
        val asyncTaskLoaderParams = Bundle()
        asyncTaskLoaderParams.putString(TSYM_KEY, getTranslateCurrency())
        asyncTaskLoaderParams.putString(FSYM_KEY, getInCurrencyName()?.toUpperCase())
        asyncTaskLoaderParams.putString(LIMIT_KEY, getDaysFromPrefs())
        supportLoaderManager.restartLoader(LOADER_ID, asyncTaskLoaderParams, this).forceLoad()
        Log.i(TAG, "startAsyncTaskLoader")
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<String> {
        Log.i(TAG, "onCreateLoader. args: ${args.toString()}")

        class TaskLoader : AsyncTaskLoader<String>(this) {
            private val apiUri = "https://min-api.cryptocompare.com/data/histoday"

            private fun readDataFromUri(uri: Uri?): String? {
                Log.i(TAG, "Uri: $uri")
                val url = URL(uri.toString())
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

            private fun getRequestUri(fsym: String, tsym: String, days: String): Uri? {
                return Uri.parse(apiUri).buildUpon()
                    .appendQueryParameter("fsym", fsym)
                    .appendQueryParameter("tsym", tsym)
                    .appendQueryParameter("limit", days)
                    .build()
            }

            override fun loadInBackground(): String? {
                Log.i(TAG, "loadInBackground")
                Log.i(TAG, "Bundle: $args")
                val fsym = args!!.getString(FSYM_KEY)!!
                val tsym = args.getString(TSYM_KEY) ?: return null
                val days = args.getString(LIMIT_KEY)!!
                val uri = getRequestUri(fsym, tsym, days)
                return readDataFromUri(uri) ?: return null
            }
        }
        return TaskLoader()
    }

    override fun onLoadFinished(loader: Loader<String>, data: String?) {
        Log.i(TAG, "onLoadFinished. list adapter creation")
        listAdapter?.items = parseJSON(data)
        listAdapter?.notifyDataSetChanged()
    }

    private fun parseJSON(jsonStr: String?): JSONArray {
        return JSONObject(jsonStr).getJSONArray("Data")
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

    override fun onLoaderReset(loader: Loader<String>) {
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager
            .getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startIntent(intent)
            }
            R.id.go -> {
                if (listAdapter == null) {
                    listAdapter = ListAdapter()
                    recView.adapter = listAdapter
                }
                if (getInCurrencyName() != null) {
                    startAsyncTaskLoader()
                }
            }
            R.id.browser_button -> {
                val inCurrencyName = getInCurrencyName()
                val translateCurrency = getTranslateCurrency().toLowerCase()
                if (inCurrencyName != null) {
                    openWebPage(URL_TEMPLATE.format(inCurrencyName, translateCurrency))
                }
            }
        }
        return super.onContextItemSelected(item)
    }
}

