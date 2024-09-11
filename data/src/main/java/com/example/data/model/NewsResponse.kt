package com.example.data.model

import com.example.data.database.NewsEntity
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
fun <T> Any.convertTo(clazz: Class<T>): T {
	val json = Gson().toJson(this)
	return Gson().fromJson(json, clazz)
}
data class NewsResponseDto(

	@field:SerializedName("totalResults")
	val totalResults: Int? = null,

	@field:SerializedName("articles")
	val articles: List<ArticlesItemDto?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class SourceDto(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class ArticlesItemDto(

	@field:SerializedName("publishedAt")
	val publishedAt: String? = null,

	@field:SerializedName("author")
	val author: String? = null,

	@field:SerializedName("urlToImage")
	val urlToImage: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("source")
	val source: SourceDto? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("content")
	val content: String? = null
){
	fun toEntity(category:String): NewsEntity {
		return NewsEntity(
			id = url.hashCode(),
			title = title ?: "",
			description = description ?: "",
			publishedAt = publishedAt?:"",
			url = url?:"",
			content = content?:"",
			urlToImage = urlToImage?:"",
			author = author?:"",
			category = category


		)
	}
}
