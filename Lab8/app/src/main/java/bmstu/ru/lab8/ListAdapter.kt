package bmstu.ru.lab8

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class ListAdapter : RecyclerView.Adapter<TextViewHolder>() {
    var items = listOf<Pair<Int, String>>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item, parent, false)
        return TextViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        val (id, text) = items[position]
        holder.setData(id, text)
    }
}

