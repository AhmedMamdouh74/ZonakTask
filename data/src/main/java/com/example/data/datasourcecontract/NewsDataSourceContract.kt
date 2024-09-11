package com.example.data.datasourcecontract

import com.example.domain.common.ResultWrapper
import com.example.domain.model.ArticlesItem
import kotlinx.coroutines.flow.Flow

interface NewsDataSourceContract {
     fun getNewsFromAPI(category: String): Flow<ResultWrapper<List<ArticlesItem?>?>>
     fun getNewsFromLocalDB(category: String): Flow<ResultWrapper<List<ArticlesItem?>?>>
}