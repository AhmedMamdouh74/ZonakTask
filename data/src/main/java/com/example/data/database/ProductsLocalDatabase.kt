package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NewsEntity::class], version = 2)
abstract class NewsLocalDatabase : RoomDatabase() {

    abstract fun getNewsDao(): NewsDao


}