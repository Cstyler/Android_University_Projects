package bmstu.ru.lab10pt2

import android.content.*
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.provider.BaseColumns
import android.text.TextUtils
import java.util.*


class PeopleProvider : ContentProvider() {
    private lateinit var db: SQLiteDatabase

    override fun onCreate(): Boolean {
        val localContext = context
        localContext?.let {
            val dbHelper = DbHelper(localContext)
            db = dbHelper.writableDatabase
            return true
        }
        return false
    }

    override fun query(
        uri: Uri, projection: Array<String>?,
        selection: String?, selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val qb = SQLiteQueryBuilder()
        qb.tables = TABLE_NAME

        when (uriMatcher.match(uri)) {
            PEOPLE -> {
                qb.setProjectionMap(STUDENTS_PROJECTION_MAP)
            }
            PEOPLE_ID -> {
                val id = uri.pathSegments[1]
                qb.appendWhere("$ID=$id")
            }
        }

        return qb.query(
            db, projection, selection,
            selectionArgs, null, null, sortOrder
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val rowId = db.insert(TABLE_NAME, null, values)
        if (rowId > 0) {
            val newUri = ContentUris.withAppendedId(CONTENT_URI, rowId)
//            context?.contentResolver?.notifyChange(newUri, null)
            return newUri
        }
        throw SQLException("Failed to add a record into $uri")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return when (uriMatcher.match(uri)) {
            PEOPLE -> db.delete(TABLE_NAME, selection, selectionArgs)
            PEOPLE_ID -> {
                val id = uri.pathSegments[1]
                val selectionString = if (!TextUtils.isEmpty(selection))
                    "AND (" + selection + ')'.toString()
                else ""
                db.delete(
                    TABLE_NAME,
                    "$ID = $id $selectionString",
                    selectionArgs
                )
            }
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?,
        selection: String?, selectionArgs: Array<String>?
    ): Int {
        return when (uriMatcher.match(uri)) {
            PEOPLE -> db.update(TABLE_NAME, values, selection, selectionArgs)
            PEOPLE_ID -> {
                val selectionString = if (!TextUtils.isEmpty(selection))
                    "AND ($selection)" else ""
                val id = uri.pathSegments[1]
                db.update(
                    TABLE_NAME, values,
                    "$ID = $id $selectionString",
                    selectionArgs
                )
            }
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            PEOPLE -> "vnd.android.cursor.dir/vnd.example.people"
            PEOPLE_ID -> "vnd.android.cursor.item/vnd.example.people"
            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
    }

    companion object {
        private const val PROVIDER_NAME = "ru.bmstu.lab10pt2.Provider"
        const val TABLE_NAME = "people"
        private const val URL = "content://$PROVIDER_NAME/$TABLE_NAME"
        val CONTENT_URI = Uri.parse(URL)!!

        const val ID = BaseColumns._ID
        const val NAME_COLUMN = "name"

        private val STUDENTS_PROJECTION_MAP: HashMap<String, String>? = null

        private const val PEOPLE = 1
        private const val PEOPLE_ID = 2

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(PROVIDER_NAME, TABLE_NAME, PEOPLE)
            uriMatcher.addURI(PROVIDER_NAME, "$TABLE_NAME/#", PEOPLE_ID)
        }
    }
}