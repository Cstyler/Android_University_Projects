package bmstu.ru.lab10

import android.Manifest
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_PERMISSION = 10
        private const val TAG = "MainActivity"
    }

    private lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermission()

        rvContracts.layoutManager = LinearLayoutManager(this)
        rvContracts.setHasFixedSize(true)
        adapter = ListAdapter()

        queryData()

        rvContracts.adapter = adapter

        Log.i(TAG, "OnCreate")
    }

    private fun queryData() {
        val projectionFields = arrayOf(
            ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val c = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projectionFields,
            null,
            null,
            null
        )

        c?.let {
            val contactsList = cursor2List(c)
            adapter.items = contactsList
            c.close()
        }
    }

    private fun cursor2List(c: Cursor): List<Pair<String, String>> {
        return (0 until c.count).map {
            c.moveToPosition(it)
            Pair(
                c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            )
        }
    }

    private fun requestPermission() {
        val permissions = arrayOf(
            Manifest.permission.READ_CONTACTS
        )
        requestPermissions(permissions, REQUEST_PERMISSION)
    }
}
