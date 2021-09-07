package com.zaidan.inews.ui.favorite

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.zaidan.inews.R
import com.zaidan.inews.data.response.ArticlesItem
import com.zaidan.inews.databinding.ActivityFavoriteBinding
import com.zaidan.inews.ui.MainViewModel
import com.zaidan.inews.ui.detail.DetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteActivity : AppCompatActivity() {
    private var _binding: ActivityFavoriteBinding? = null
    private val binding get() = _binding!!
    private var favoriteAdapter: FavoriteAdapter? = null
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = getString(R.string.favorite_list)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(ContextCompat.getDrawable(this@FavoriteActivity, R.drawable.ic_baseline_arrow_back_ios))
        }

        favoriteAdapter = FavoriteAdapter()
        setupUI()
        setupFavorite()
    }

    private fun setupUI() {
        with(binding.rvNews) {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            setHasFixedSize(true)
            adapter = favoriteAdapter
        }

        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val articlesItem = favoriteAdapter?.getNewsAt(viewHolder.bindingAdapterPosition)
                articlesItem?.let {
                    viewModel.deleteToDb(it)
                    favoriteAdapter?.notifyItemChanged(viewHolder.bindingAdapterPosition)
                    favoriteAdapter?.notifyDataSetChanged()
                }

                val snackBar = Snackbar.make(binding.root, "1 Artikel telah dihapus", Snackbar.LENGTH_LONG)
                snackBar.setAction("Batalkan") { _ ->
                    articlesItem?.let {
                        viewModel.insertToDb(it)
                        favoriteAdapter?.notifyItemChanged(viewHolder.bindingAdapterPosition)
                        favoriteAdapter?.notifyDataSetChanged()
                    }
                }
                snackBar.setActionTextColor(Color.parseColor("#FFFFFF"))
                snackBar.show()
            }
        }).attachToRecyclerView(binding.rvNews)

        favoriteAdapter?.setOnClickListener(object: FavoriteAdapter.OnItemClickListener {
            override fun onItemClicked(articlesItem: ArticlesItem) {
                val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.NEWS_KEY, articlesItem)
                startActivity(intent)
                finish()
            }
        })
    }

    private fun setupFavorite() {
        viewModel.favNews.observe(this, { response ->
            if (response.isNullOrEmpty()) {
                Toast.makeText(this, "Daftar Favorite Kosong", Toast.LENGTH_SHORT).show()
            } else {
                favoriteAdapter?.setListFav(response)
                favoriteAdapter?.notifyDataSetChanged()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}