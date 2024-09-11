package com.example.data.database

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.example.domain.model.ArticlesItem
import com.example.domain.model.Source
import kotlinx.coroutines.flow.Flow


@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewsToDB(list: List<NewsEntity>)

    @Query("SELECT * FROM news WHERE category = :category")

    fun getNewsFromDB(category: String): Flow<List<NewsEntity>>
}

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey val id: Int,
    val publishedAt: String,
    val author: String,
    val urlToImage: String,
    val description: String,
    val title: String,
    val url: String,
    val content: String,
    val category: String // Add this field
) {
    fun toModel(): ArticlesItem {
        return ArticlesItem(
            publishedAt = publishedAt,
            author = author,
            urlToImage = urlToImage,
            title = title,
            description = description,
            url = url,
            content = content,



        )
    }
}
