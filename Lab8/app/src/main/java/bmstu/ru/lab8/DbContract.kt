package bmstu.ru.lab8

import android.provider.BaseColumns

object DbContract {
    object DbEntry : BaseColumns {
        const val TABLE_NAME = "AmazingTable"
        const val COLUMN1 = "Column1"
    }
}