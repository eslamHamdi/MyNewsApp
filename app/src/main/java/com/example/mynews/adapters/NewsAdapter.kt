package com.example.mynews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mynews.R
import com.example.mynews.databinding.NewsItemBinding
import com.example.mynews.domain.Article

class NewsAdapter:ListAdapter<Article,NewsAdapter.NewsViewHolder>(DiffCallBack)
{
    lateinit var binding:NewsItemBinding

    inner class NewsViewHolder(binding:NewsItemBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bind(article:Article)
        {
            binding.article = article
            binding.itemContainer.setOnClickListener{
                articleClickListener?.clickArticle(article)
            }
            binding.executePendingBindings()

        }
    }


    companion object DiffCallBack: DiffUtil.ItemCallback<Article>()
    {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url

        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {

            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder
    {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.news_item,parent,false)

        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int)
    {
        val item = this.getItem(position)
        holder.bind(item)

    }

    var articleClickListener:OnArticleClick? =null

    interface OnArticleClick
    {
        fun clickArticle(article:Article)
    }
}