package com.zaidan.inews.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.zaidan.inews.R
import com.zaidan.inews.data.response.ArticlesItem
import com.zaidan.inews.databinding.ActivityMainBinding
import com.zaidan.inews.ui.MainViewModel
import com.zaidan.inews.ui.detail.DetailActivity
import com.zaidan.inews.ui.favorite.FavoriteActivity
import com.zaidan.inews.utils.Status
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var mainAdapter: MainAdapter? = null
    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainAdapter = MainAdapter()
        setupUI()
        setupNews()
    }

    private fun setupUI() {
        with(binding.rvNews) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = mainAdapter
        }
        mainAdapter?.setOnClickListener(object: MainAdapter.OnItemClickListener {
            override fun onItemClicked(articles: ArticlesItem) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.NEWS_KEY, articles)
                startActivity(intent)
            }
        })
    }

    private fun setupNews() {
        mainViewModel.news.observe(this, { response ->
            when(response.status) {
                Status.LOADING -> {
                    with(binding) {
                        shimmer.startShimmer()
                        shimmer.visibility = View.VISIBLE
                        rvNews.visibility = View.GONE
                    }
                }
                Status.SUCCESS -> {
                    with(binding) {
                        shimmer.visibility = View.GONE
                        rvNews.visibility = View.VISIBLE
                    }
                    response.result?.articles.let {
                        mainAdapter?.setListNews(it)
                        mainAdapter?.notifyDataSetChanged()
                    }
                    mainAdapter?.notifyDataSetChanged()
                }
                Status.ERROR -> {
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setupByCategory(category: String) {
        mainViewModel.fetchNewsByCategory(category).observe(this, { response ->
            when(response.status) {
                Status.LOADING -> {
                    with(binding) {
                        shimmer.startShimmer()
                        shimmer.visibility = View.VISIBLE
                        rvNews.visibility = View.GONE
                    }
                }
                Status.SUCCESS -> {
                    with(binding) {
                        shimmer.visibility = View.GONE
                        rvNews.visibility = View.VISIBLE
                    }
                    response.result?.articles.let {
                        mainAdapter?.setListNews(it)
                        mainAdapter?.notifyDataSetChanged()
                    }
                    mainAdapter?.notifyDataSetChanged()
                }
                Status.ERROR -> {
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_all -> {
                setupNews()
                item.isChecked = true
            }
            R.id.action_business -> {
                setupByCategory(getString(R.string.business))
                item.isChecked = true
            }
            R.id.action_entertainment -> {
                setupByCategory(getString(R.string.entertainment))
                item.isChecked = true
            }
            R.id.action_health -> {
                setupByCategory(getString(R.string.health))
                item.isChecked = true
            }
            R.id.action_science -> {
                setupByCategory(getString(R.string.science))
                item.isChecked = true
            }
            R.id.action_sports -> {
                setupByCategory(getString(R.string.sports))
                item.isChecked = true
            }
            R.id.action_technology -> {
                setupByCategory(getString(R.string.technology))
                item.isChecked = true
            }
            R.id.action_favorite -> startActivity(Intent(this, FavoriteActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}