package com.zaidan.inews.di

import com.zaidan.inews.data.ApiInterface
import com.zaidan.inews.data.MainRepository
import com.zaidan.inews.data.room.NewsDao
import org.koin.dsl.module

val repositoryModule = module {
    single { provideNewsRepository(get(), get()) }
}

fun provideNewsRepository(apiInterface: ApiInterface, dao: NewsDao): MainRepository =
    MainRepository(apiInterface, dao)