package bmstu.ru.lab10pt2

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        private const val DB_NAME = "database.db"
        private const val DB_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.let {
            val createQuery =
                "CREATE TABLE ${PeopleProvider.TABLE_NAME}(${PeopleProvider.ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${PeopleProvider.NAME_COLUMN} TEXT NOT NULL);"
            it.execSQL(createQuery)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.let {
            val dropQuery = "DROP TABLE ${PeopleProvider.TABLE_NAME};"
            it.execSQL(dropQuery)
        }
    }

}