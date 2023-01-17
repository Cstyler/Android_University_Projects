package bmstu.ru.lab8

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var id: Int? = null
    private val textView = itemView.findViewById<TextView>(R.id.list_item_text)!!

    fun setData(id: Int, data: String) {
        textView.text = data
        this.id = id
    }
}