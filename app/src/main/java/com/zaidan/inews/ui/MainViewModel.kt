package com.zaidan.inews.ui

import androidx.lifecycle.*
import com.zaidan.inews.data.MainRepository
import com.zaidan.inews.data.response.ArticlesItem
import com.zaidan.inews.data.response.MainResponse
import com.zaidan.inews.utils.ResponseState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(private val mainRepository: MainRepository): ViewModel() {
    private var _news = MutableLiveData<ResponseState<MainResponse>>()
    val news: LiveData<ResponseState<MainResponse>> = _news
    private val _favNews = MutableLiveData<List<ArticlesItem>>()
    val favNews: LiveData<List<ArticlesItem>> = _favNews

    init {
        fetchNews()
        fetchFavorite()
    }

    private fun fetchNews() = viewModelScope.launch {
        mainRepository.getNews("").collect {
            _news.value = it
        }
    }
    fun fetchNewsByCategory(category: String): LiveData<ResponseState<MainResponse>> {
        val fetchByCategory = MutableLiveData<ResponseState<MainResponse>>()
        viewModelScope.launch {
            mainRepository.getNews(category).collect { fetchByCategory.value = it }
        }
        return fetchByCategory
    }

    fun insertToDb(articlesItem: ArticlesItem) = viewModelScope.launch {
        mainRepository.insertNews(articlesItem)
    }

    private fun fetchFavorite() = viewModelScope.launch {
        mainRepository.getFavNews().collect {
            _favNews.value = it
        }
    }

    fun deleteToDb(articlesItem: ArticlesItem) = viewModelScope.launch {
        mainRepository.deleteNews(articlesItem)
    }
}