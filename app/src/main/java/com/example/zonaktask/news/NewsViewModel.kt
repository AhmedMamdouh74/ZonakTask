package com.example.zonaktask.news

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.common.ResultWrapper
import com.example.domain.model.ArticlesItem
import com.example.domain.usecase.GetNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val getNewsUseCase: GetNewsUseCase) : ViewModel(),
    NewsContract.ViewModel {
    val selectedIndex = mutableIntStateOf(0)
    private val _selectedCategory = MutableStateFlow("business") // default category
    val selectedCategory: StateFlow<String> get() = _selectedCategory
    private val _states =
        MutableStateFlow<NewsContract.State>(NewsContract.State.Loading("loading"))
    override val states = _states

    private val _events = mutableStateOf<NewsContract.Event>(NewsContract.Event.Idle)
    override val events = _events

    override fun invokeAction(action: NewsContract.Action) {
        when (action) {
            is NewsContract.Action.LoadingNews -> loadNews(action.category)
            is NewsContract.Action.NewsClicked -> navigateToNewsDetails(action.articlesItem)
        }
    }

    private fun navigateToNewsDetails(articlesItem: ArticlesItem) {
        viewModelScope.launch(IO) {
            _events.value=(
                NewsContract.Event.NavigateToNewsDetails(
                    articlesItem
                )
            )


        }

    }

    fun setSelectedCategory(category: String) {
        _selectedCategory.value = category
        invokeAction(NewsContract.Action.LoadingNews(category))
    }

   private fun loadNews(category: String) {
        viewModelScope.launch(IO) {
            getNewsUseCase.invoke(category).collect { result ->
                when (result) {
                    is ResultWrapper.Error -> {

                        Log.d(TAG, "loadNews: error${result.error.localizedMessage}")


                    }

                    ResultWrapper.Loading -> {

                        Log.d(TAG, "loadNews: loading")
                        _states.emit(NewsContract.State.Loading("loading"))
                    }

                    is ResultWrapper.Success -> {
                        _states.emit(NewsContract.State.Success(result.data ?: listOf()))
                        Log.d(TAG, "loadNews:success ${result.data?.size} ")
                    }

                    is ResultWrapper.ServerError -> _states.emit(NewsContract.State.Error(result.error.serverMessage))

                }
            }
        }

    }

    companion object {
        const val TAG = "NewsViewModel"
    }
}