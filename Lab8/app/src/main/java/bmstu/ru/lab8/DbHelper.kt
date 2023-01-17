package bmstu.ru.lab8

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class DbHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        private const val DB_NAME = "database.db"
        private const val DB_VERSION = 1
        private const val TAG = "DbHelper"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.let {
            val tableName = DbContract.DbEntry.TABLE_NAME
            val idColumn = BaseColumns._ID
            val column1Name = DbContract.DbEntry.COLUMN1
            val createQuery =
                "CREATE TABLE $tableName($idColumn INTEGER PRIMARY KEY AUTOINCREMENT, $column1Name TEXT NOT NULL);"
            it.execSQL(createQuery)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.let {
            val tableName = DbContract.DbEntry.TABLE_NAME
            val dropQuery = "DROP TABLE $tableName;"
            it.execSQL(dropQuery)
        }
    }

}