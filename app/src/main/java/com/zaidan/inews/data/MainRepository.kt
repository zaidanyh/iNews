package com.zaidan.inews.data

import com.zaidan.inews.data.response.ArticlesItem
import com.zaidan.inews.data.response.MainResponse
import com.zaidan.inews.data.room.NewsDao
import com.zaidan.inews.utils.ResponseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.SocketTimeoutException

class MainRepository(
    private val apiInterface: ApiInterface,
    private val dao: NewsDao
) {

    fun getNews(category: String): Flow<ResponseState<MainResponse>> = flow {
        emit(ResponseState.loading(null))
        try {
            val response = apiInterface.getNews(category)
            if (response.isSuccessful) {
                emit(ResponseState.success(response.body()))
            } else {
                emit(ResponseState.error(null, "Terjadi Kesalahan"))
            }
        } catch (timeOut: SocketTimeoutException) {
            emit(ResponseState.error(null, timeOut.message.toString()))
        }
    }

    fun getFavNews() = dao.getFavNews()
    suspend fun insertNews(news: ArticlesItem) = dao.insertFavNews(news)
    suspend fun deleteNews(news: ArticlesItem) = dao.deleteFavNews(news)
}