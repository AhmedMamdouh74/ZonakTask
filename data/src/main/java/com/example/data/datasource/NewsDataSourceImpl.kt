package com.example.data.datasource

import android.util.Log
import com.example.data.api.WepServices
import com.example.data.database.NewsDao
import com.example.data.datasourcecontract.NewsDataSourceContract
import com.example.data.model.convertTo
import com.example.domain.common.ResultWrapper
import com.example.domain.exceptions.ParsingException
import com.example.domain.exceptions.ServerError
import com.example.domain.exceptions.ServerTimeOutException
import com.example.domain.model.ArticlesItem
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class NewsDataSourceImpl @Inject constructor(
    private val wepServices: WepServices,
    private val newsDao: NewsDao
) :
    NewsDataSourceContract {
    override fun getNewsFromAPI(category: String): Flow<ResultWrapper<List<ArticlesItem?>?>> =
        flow {
            emit(ResultWrapper.Loading)
            val response = wepServices.getNews(category = category)
            if (response.isSuccessful) {
                Log.d(TAG, "getNewsFromAPI: success ")
                response.body()?.let { body ->
                    val news = body.articles?.map {
                        it?.convertTo(ArticlesItem::class.java)

                    }
                    emit(ResultWrapper.Success(news))
                    Log.d(TAG, "getNewsFromAPI: ${news?.size}")

                    val newsEntity = body.articles?.mapNotNull { it?.toEntity(category) }
                    Log.d(TAG, "getNewsEntity: ${newsEntity?.size}")
                    newsDao.insertNewsToDB(newsEntity!!)
                    Log.d(TAG, "getNewsEntityInsertion: ${newsEntity?.size}")
                }
            }
        }.catch { e ->
            when (e) {
                is TimeoutException -> {
                    emit(ResultWrapper.Error(ServerTimeOutException(e)))
                    Log.d(TAG, "getNewsFromAPI: TimeoutException$e")
                }

                is IOException -> {
                    emit(ResultWrapper.Error(ServerTimeOutException(e)))
                    Log.d(TAG, "getNewsFromAPI:IOException$e ")
                }

                is HttpException -> {
                    emit(
                        ResultWrapper.ServerError(
                            ServerError(e.message.toString(), e.localizedMessage, e.code())
                        )
                    )
                    Log.d(TAG, "getNewsFromAPI:HttpException$e ")
                }

                is JsonSyntaxException -> {
                    emit(ResultWrapper.Error(ParsingException(e)))
                    Log.d(TAG, "getNewsFromAPI:JsonSyntaxException$e ")
                }

                else -> {
                    emit(ResultWrapper.Error(e as Exception))

                    Log.d(TAG, "getNewsFromAPI:JsonSyntaxException${e} ")
                }

            }

        }


        override fun getNewsFromLocalDB(category: String): Flow<ResultWrapper<List<ArticlesItem?>?>> {
        return flow {
            emit(ResultWrapper.Loading)
            val news = newsDao.getNewsFromDB(category).first().map { it.toModel() }
            emit(ResultWrapper.Success(news))
        }.catch {
            emit(ResultWrapper.Error(Exception(it.message)))
        }


    }
    companion object {
        const val TAG = "NewsDataSourceImpl"
    }

}
