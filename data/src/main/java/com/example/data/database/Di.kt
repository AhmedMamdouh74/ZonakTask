package com.example.data.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Di {
    @Singleton
    @Provides
    fun provideNewsDatabase(
        @ApplicationContext context: Context
    ): NewsLocalDatabase {
        return Room.databaseBuilder(context, NewsLocalDatabase::class.java, "Products Local DB")
            .fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideProductsDAO(
        productsLocalDatabase: NewsLocalDatabase
    ): NewsDao {
        return productsLocalDatabase.getNewsDao()
    }
}