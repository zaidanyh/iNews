package com.zaidan.inews.data.room

import androidx.room.*
import com.zaidan.inews.data.response.ArticlesItem
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * FROM news_entity WHERE isFav = 1")
    fun getFavNews(): Flow<List<ArticlesItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavNews(articlesItem: ArticlesItem)

    @Delete
    suspend fun deleteFavNews(articlesItem: ArticlesItem)
}