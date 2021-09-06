package com.it.restroexample.adapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.it.restroexample.R
import com.it.restroexample.dto.ArticleResponse
import com.it.restroexample.util.OnClickListenerApp
import java.lang.NullPointerException

/**
 * created by Akash on 24/12/2020
 */
class NewsAdapter(list: List<ArticleResponse>?, limitCount:Int) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var list = ArrayList<ArticleResponse>()
    var onClickCallback: OnClickListenerApp.OnClickCallback? = null
    private val TYPE_FOOTER = 1
    private val TYPE_ITEM = 2
    private var limitCount = 0
    /**
     * init block for initializing the list
     */
    init {
        this.limitCount=limitCount
        this.list = list as ArrayList<ArticleResponse>
    }

    /**
     *GetItem type this will retun new type of item for load more
     */

    override fun getItemViewType(position: Int): Int {
        if (position == list.size) {
            return TYPE_FOOTER
        }
        return TYPE_ITEM
    }
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        internal var tvScore: TextView = itemView.findViewById(R.id.tvScore)

    }
     inner class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var footerText: TextView = view.findViewById(R.id.footer_text) as TextView
     }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_FOOTER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_footer, parent, false)
            return FooterViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_home_list, parent, false)
            return MyViewHolder(view)
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            if (holder is MyViewHolder) {
                val dto = list[position]
                holder.tvTitle.text = dto.title
                holder.tvScore.text = dto.score.toString()
                /**
                 * call back for item into Fragment
                */

                holder.itemView.setOnClickListener { view ->
                    OnClickListenerApp(
                        position,
                        dto.url!!,
                        onClickCallback!!
                    ).onClick(view)
                }
            }
            /**
             *for load more extra item view
             */
            else if(holder is FooterViewHolder)
            {
                holder.footerText.text = "Load More News"
                holder.footerText.setOnClickListener { view ->
                    OnClickListenerApp(
                        position,
                        "load",
                        onClickCallback!!
                    ).onClick(view)
                }
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }
    override fun getItemCount(): Int {
        if (list.size <limitCount && list.size != 0)
            return list.size + 1
        else
            return list.size
    }
}
