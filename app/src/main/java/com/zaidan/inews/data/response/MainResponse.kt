package com.zaidan.inews.data.response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class MainResponse(

	@field:SerializedName("totalResults")
	val totalResults: Int,

	@field:SerializedName("articles")
	val articles: List<ArticlesItem>,

	@field:SerializedName("status")
	val status: String
)

@Parcelize
@Entity(tableName = "news_entity")
data class ArticlesItem(

	@field:SerializedName("publishedAt")
	val publishedAt: String,

	@field:SerializedName("author")
	val author: String?,

	@field:SerializedName("urlToImage")
	val urlToImage: String?,

	@field:SerializedName("description")
	val description: String?,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("url")
	val url: String,

	@field:SerializedName("content")
	val content: String?,

	@PrimaryKey(autoGenerate = true)
	var id: Int,
) : Parcelable
