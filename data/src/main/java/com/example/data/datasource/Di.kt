package com.example.data.datasource

import com.example.data.datasourcecontract.NewsDataSourceContract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class Di {
    @Binds
    @Singleton
    abstract fun bindsNewsDataSourceContract(newsDataSourceImpl: NewsDataSourceImpl): NewsDataSourceContract
}