package com.example.domain.repository

import com.example.domain.common.ResultWrapper
import com.example.domain.model.ArticlesItem
import kotlinx.coroutines.flow.Flow

interface NewsRepo {
    fun getNews(category: String): Flow<ResultWrapper<List<ArticlesItem?>?>>
}