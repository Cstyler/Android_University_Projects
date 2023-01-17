package bmstu.ru.rk1

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*
//, private val context: AppCompatActivity
class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    companion object {
        private const val TAG = "ListAdapter"
        private const val ITEM_TEXT_KEY = "item_text"
    }

    var items = JSONArray()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView = itemView.findViewById<TextView>(R.id.list_item_text)

        fun setData(data: String) {
            textView.text = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.length()
    }

    private fun parseItems(i: Int): Pair<String, String>? {
        Log.i(TAG, items.toString())
        return if (i < items.length()) {
            val article = items.getJSONObject(i)
            val timeStr = article.getString("time")
            val date = Date(timeStr.toLong()*1000)
            val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val time = sdf.format(date).toString()
            val close = article.getDouble("close")
            val high = article.getDouble("high")
            val low = article.getDouble("low")
            val text = "Time: $time\nClose price: $close\n---\n"
            val verboseText = "Time: $time\nClose price: $close\nHigh price: $high\nLow price: $low\n---\n"
            Pair(text, verboseText)
        }
        else {
            null
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val texts = parseItems(position) ?: return
        val (text, verboseText) = texts
        holder.setData(text)

        holder.itemView.setOnClickListener {
            val context = it.context
            Log.i(TAG, "Item view clicked. Text $text")
            val intent = Intent(context, CurrencyActivity::class.java)
            intent.putExtra(ITEM_TEXT_KEY, verboseText)
            context.startActivity(intent)
        }
    }
}

