package com.zaidan.inews.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zaidan.inews.data.response.ArticlesItem
import com.zaidan.inews.databinding.ItemNewsBinding

class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private var listFav = ArrayList<ArticlesItem>()
    private lateinit var onItemClickListener: OnItemClickListener

    fun setListFav(listFav: List<ArticlesItem>?) {
        if (listFav == null) return
        this.listFav.clear()
        this.listFav.addAll(listFav)
    }

    fun setOnClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.FavoriteViewHolder {
        return FavoriteViewHolder(ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.FavoriteViewHolder, position: Int) {
        val favItem = listFav[position]
        holder.bind(favItem)
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClicked(listFav[holder.bindingAdapterPosition])
        }
    }

    fun getNewsAt(position: Int): ArticlesItem = listFav[position]

    override fun getItemCount(): Int = listFav.size

    inner class FavoriteViewHolder(private val binding: ItemNewsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(articlesItem: ArticlesItem) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(articlesItem.urlToImage)
                    .into(imgNews)
                newsTitle.text = articlesItem.title
                newsDesc.text = articlesItem.description
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(articlesItem: ArticlesItem)
    }
}