package bmstu.ru.lab10

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    var items = listOf<Pair<String, String>>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvContactDisplayName = itemView.findViewById<TextView>(R.id.tvContactDisplayName)
        private val tvContactNumber = itemView.findViewById<TextView>(R.id.tvContactNumber)

        fun setData(displayName: String, number: String) {
            tvContactDisplayName.text = displayName
            tvContactNumber.text = number
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_contact, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pair = items[position]
        holder.setData(pair.first, pair.second)
    }
}
