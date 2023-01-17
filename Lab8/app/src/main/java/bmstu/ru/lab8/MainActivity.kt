package bmstu.ru.lab8

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.provider.BaseColumns
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var db: SQLiteDatabase
    private lateinit var listAdapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dbHelper = DbHelper(this)
        db = dbHelper.writableDatabase

        recView.layoutManager = LinearLayoutManager(this)
        recView.setHasFixedSize(true)

        val cursor = getCursor()
        val column1List = cursor2List(cursor)
        listAdapter = ListAdapter()
        listAdapter.items = column1List
        recView.adapter = listAdapter

        class SwipeController : ItemTouchHelper.Callback() {

            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                return ItemTouchHelper.Callback.makeMovementFlags(
                    0,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                )
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteFromDatabaseById((viewHolder as TextViewHolder).id!!)
                changeView()
            }
        }

        val itemTouchHelper = ItemTouchHelper(SwipeController())
        itemTouchHelper.attachToRecyclerView(recView)
    }

    private fun cursor2List(c: Cursor): List<Pair<Int, String>> {
        return (1 until c.count).map {
            c.moveToPosition(it)
            Pair(
                c.getInt(c.getColumnIndex(BaseColumns._ID)),
                c.getString(c.getColumnIndex(DbContract.DbEntry.COLUMN1))
            )
        }
    }

    private fun insert2Database(value: String) {
        val values = ContentValues()
        values.put(DbContract.DbEntry.COLUMN1, value)
        db.insert(DbContract.DbEntry.TABLE_NAME, null, values)
    }

    private fun getCursor(): Cursor {
        return db.query(
            DbContract.DbEntry.TABLE_NAME,
            null, null, null, null, null, null
        )
    }

    private fun deleteFromDatabaseById(id: Int): Int {
        return db.delete(DbContract.DbEntry.TABLE_NAME, "${BaseColumns._ID} = $id", null)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.mainActivitySaveButton -> {
                val editTextText = editText.text
                val text = editTextText.toString()
                editTextText.clear()
                insert2Database(text)
                changeView()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeView() {
        val cursor = getCursor()
        val column1List = cursor2List(cursor)
        listAdapter.items = column1List
        listAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
