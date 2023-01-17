package bmstu.ru.lab10pt2

import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var listAdapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvPeople.layoutManager = LinearLayoutManager(this)
        rvPeople.setHasFixedSize(true)
        listAdapter = ListAdapter()
        rvPeople.adapter = listAdapter
        changeView()
        Log.i(TAG, "onCreate")
    }

    fun onClickAddName(view: View) {
        val value = editText.text.toString()
        if (value == "") {
            Toast.makeText(this, getString(R.string.empty_string_toast_text), Toast.LENGTH_SHORT).show()
            return
        }
        val values = ContentValues()
        values.put(
            PeopleProvider.NAME_COLUMN,
            value
        )
        val uri = contentResolver.insert(
            PeopleProvider.CONTENT_URI, values
        )
        changeView()
        Log.i(TAG, "onClickAddName. Uri: $uri")
    }

    fun onClickDelete(view: View) {
        val uri = contentResolver.delete(PeopleProvider.CONTENT_URI, null, null)
        emptyView()
        Log.i(TAG, "onClickDelete. Uri: $uri")
    }

    private fun emptyView() {
        listAdapter.items = listOf()
        listAdapter.notifyDataSetChanged()
    }

    private fun changeView() {
        val c = contentResolver.query(
            PeopleProvider.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        c?.let {
            val rows = cursor2List(c)
            listAdapter.items = rows
            listAdapter.notifyDataSetChanged()
        }
    }

    private fun cursor2List(c: Cursor): List<Pair<Int, String>> {
        return (0 until c.count).map {
            c.moveToPosition(it)
            Pair(
                c.getInt(c.getColumnIndex(PeopleProvider.ID)),
                c.getString(c.getColumnIndex(PeopleProvider.NAME_COLUMN))
            )
        }
    }
}