package com.example.zonaktask.news

import androidx.compose.runtime.MutableState
import com.example.domain.model.ArticlesItem
import kotlinx.coroutines.flow.Flow

class NewsContract {
    interface ViewModel {
        val states: Flow<State>
        val events: MutableState<Event>
        fun invokeAction(action: Action)
    }

    sealed class State {
        class Loading(val message: String) : State()
        class Error(val message: String) : State()
        class Success(val articlesItem: List<ArticlesItem?>) : State()
    }

    sealed class Action {
        class LoadingNews(val category: String) : Action()
        class NewsClicked(val articlesItem: ArticlesItem) : Action()
    }

    sealed class Event {
        data object Idle : Event()
        class NavigateToNewsDetails(val articlesItem: ArticlesItem) : Event()
    }
}