package bmstu.ru.lab3

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.json.JSONArray

class ListAdapter(private val totalItemCount: Int) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    var articles = JSONArray()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView = itemView.findViewById<TextView>(R.id.list_item_text)!!

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
        Log.i("ListAdapter", "Total item count: $totalItemCount")
        return totalItemCount
    }


    private fun parseArticle(articles: JSONArray, i: Int): Triple<String, String, String> {
        return if (i < articles.length()) {
            val article = articles.getJSONObject(i)
            val title = article.getString("title")
            val description = article.getString("description")
            val sourceName = article.getJSONObject("source").getString("name")
            Triple(title, description, sourceName)
        }
        else {
            Triple("", "", "")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (title, description, sourceName) = parseArticle(articles, position)
        val text = "Author: $sourceName\nTitle: $title\nDescription: $description\n---\n"
        holder.setData(text)
    }
}

