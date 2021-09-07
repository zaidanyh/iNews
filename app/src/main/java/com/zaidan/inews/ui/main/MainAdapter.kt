package com.zaidan.inews.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zaidan.inews.data.response.ArticlesItem
import com.zaidan.inews.databinding.ItemNewsBinding

class MainAdapter: RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var listNews = ArrayList<ArticlesItem>()
    private lateinit var onItemClickListener: OnItemClickListener

    fun setListNews(listNews: List<ArticlesItem>?) {
        if (listNews == null) return
        this.listNews.clear()
        this.listNews.addAll(listNews)
    }

    fun setOnClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.MainViewHolder {
        return MainViewHolder(ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MainAdapter.MainViewHolder, position: Int) {
        val article = listNews[position]
        holder.bind(article)
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClicked(listNews[holder.bindingAdapterPosition])
        }
    }

    override fun getItemCount(): Int = listNews.size

    inner class MainViewHolder(private val binding: ItemNewsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(articles: ArticlesItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(articles.urlToImage)
                    .into(imgNews)
                newsTitle.text = articles.title
                newsDesc.text = articles.description
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(articles: ArticlesItem)
    }
}