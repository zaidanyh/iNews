package com.zaidan.inews.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.zaidan.inews.R
import com.zaidan.inews.data.response.ArticlesItem
import com.zaidan.inews.databinding.ActivityDetailBinding
import com.zaidan.inews.ui.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat

class DetailActivity : AppCompatActivity() {

    companion object {
        const val NEWS_KEY = "news_key"
    }

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModel()
    private var article: ArticlesItem? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(ContextCompat.getDrawable(this@DetailActivity, R.drawable.ic_baseline_arrow_back_ios))
        }

        article = intent.getParcelableExtra(NEWS_KEY)
        if (article != null) {
            setFavorite(article?.isFav!!)
            viewModel.favNews.observe(this, {
                it.forEach { item ->
                    if (item.urlToImage == article?.urlToImage) {
                        article?.isFav = item.isFav
                        article?.id = item.id
                        setFavorite(true)
                    }
                }
            })
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val formatter = SimpleDateFormat("dd MMMM yyyy - HH:mm")
            val date = dateFormat.parse(article?.publishedAt!!)
            val newDate = formatter.format(date!!)
            with(binding) {
                newsTitle.text = article?.title
                Glide.with(this@DetailActivity)
                    .load(article?.urlToImage)
                    .into(imgNews)
                txtPublished.text = newDate
                txtContent.text = article?.content
                txtDecs.text = article?.description
                if (article?.author == null) {
                    txtAuthor.text = getString(R.string.author_not_found)
                } else {
                    txtAuthor.text = getString(R.string.author, article?.author)
                }
                txtUrl.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(article?.url)
                    startActivity(intent)
                }
                fabFav.setOnClickListener {
                    fabFavClicked()
                }
            }
        } else {
            Toast.makeText(this, "Artikel tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setFavorite(state: Boolean) {
        val fab = binding.fabFav
        if (!state) {
            fab.setImageResource(R.drawable.ic_baseline_favorite_border)
        } else {
            fab.setImageResource(R.drawable.ic_baseline_favorite)
        }
    }

    private fun fabFavClicked() {
        article?.let {
            it.isFav = !it.isFav
            if (!it.isFav) {
                viewModel.deleteToDb(it)
                Toast.makeText(this, "Artikel ini telah dihapus dari daftar favorit!", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.insertToDb(it.copy(
                    publishedAt = it.publishedAt,
                    author = it.author,
                    urlToImage = it.urlToImage,
                    description = it.description,
                    title = it.title,
                    url = it.url,
                    content = it.content,
                    isFav = it.isFav
                ))
                Toast.makeText(this@DetailActivity, "Artikel ini telah ditambahkan ke daftar favorit!", Toast.LENGTH_SHORT).show()
            }
            setFavorite(it.isFav)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}