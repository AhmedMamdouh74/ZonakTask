package com.example.domain.usecase

import com.example.domain.common.ResultWrapper
import com.example.domain.model.ArticlesItem
import com.example.domain.repository.NewsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(private val newsRepo: NewsRepo) {
     operator fun invoke(category: String):Flow<ResultWrapper<List<ArticlesItem?>?>>{
        return newsRepo.getNews(category)
    }
}