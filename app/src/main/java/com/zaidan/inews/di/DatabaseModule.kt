package com.zaidan.inews.di

import android.app.Application
import androidx.room.Room
import com.zaidan.inews.data.room.NewsDao
import com.zaidan.inews.data.room.NewsDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single { provideDatabase(androidApplication()) }
    single { provideNewsDao(get()) }
}

fun provideDatabase(application: Application): NewsDatabase =
    Room.databaseBuilder(application, NewsDatabase::class.java, "News.db").build()

fun provideNewsDao(database: NewsDatabase): NewsDao = database.newsDao()