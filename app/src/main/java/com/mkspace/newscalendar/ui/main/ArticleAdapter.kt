package com.mkspace.newscalendar.ui.main

import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.funin.base.funinbase.extension.inflate
import com.mkspace.newscalendar.R
import com.mkspace.newscalendar.data.vo.Article
import kotlinx.android.synthetic.main.item_main_article.view.*

class ArticleAdapter(
    private val listener: OnClickListener?
) : PagingDataAdapter<Article, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.originalLink == newItem.originalLink

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ArticleViewHolder(parent.inflate(R.layout.item_main_article))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item !is Article) return

        (holder as? ArticleViewHolder)?.bind(item)
    }

    interface OnClickListener {
        fun onWebLinkClick(webUrl: String)
    }

    private fun ArticleViewHolder.bind(item: Article) {
        titleView.text = Html.fromHtml(item.title)
        descriptionView.text = item.description
        publishDateView.text = item.publishDate
        itemView.setOnClickListener { listener?.onWebLinkClick(item.link) }
    }

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView: TextView = itemView.mainArticleTitle
        val descriptionView: TextView = itemView.mainArticleDescription
        val publishDateView: TextView = itemView.mainArticlePublishDate
    }
}