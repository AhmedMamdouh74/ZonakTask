package com.example.zonaktask

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.domain.model.ArticlesItem
import com.example.zonaktask.news.NewsContract
import com.example.zonaktask.news.NewsViewModel
import com.example.zonaktask.newsdetails.NewsDetails
import com.example.zonaktask.ui.theme.ZonakTaskTheme
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val newsViewModel: NewsViewModel by viewModels()
    private val categories = listOf(
        "business", "entertainment", "general",
        "health", "science", "sports", "technology"
    )


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZonakTaskTheme {
                val navController = rememberNavController()
                NavigationGraph(navController = navController)
                handleEvents(navController)


            }
        }
    }


    @Composable
    fun NavigationGraph(navController: NavHostController) {
        NavHost(navController, startDestination = Constance.NEWS_HOME) {
            composable(Constance.NEWS_HOME) {
                NewsFragment()
            }
            composable(Constance.NEWS_DETAILS) {
                NewsDetails(navController)
            }


        }
    }

    @Composable
    fun NewsFragment() {
        val selectedCategory = newsViewModel.selectedCategory.collectAsState().value
        LaunchedEffect(Unit) {
            newsViewModel.invokeAction(NewsContract.Action.LoadingNews(selectedCategory))
        }
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(20.dp))
            NewsCategoryTabs()
            Spacer(modifier = Modifier.height(10.dp))
            RenderStates()
        }

    }


    @Composable
    fun RenderStates() {
        when (val state = newsViewModel.states.collectAsState().value) {
            is NewsContract.State.Error -> {
                NewsErrorDialog(errorMessage = state.message) {

                }
                Log.d(TAG, "Error occurred: ${state.message}")
            }

            is NewsContract.State.Loading -> {
                ShimmerLoadingEffect()

            }

            is NewsContract.State.Success -> {
                Log.d(TAG, "RenderStates: Success with ${state.articlesItem.size} articles")
                ArticlesList(articles = state.articlesItem)
            }
        }
    }

    @Composable
    fun NewsErrorDialog(errorMessage: String, onclick: @Composable () -> Unit) {
        AlertDialog(onDismissRequest = { }, confirmButton = {
            Button(onClick = {
                onclick
            }) {
                Text(text = "OK")
            }
        }, title = {
            Text(text = errorMessage)
        })
    }

    @Composable
    fun ShimmerLoadingEffect() {
        val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .shimmer(shimmer)
                .padding(10.dp)
        ) {
            items(5) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(Color.Gray, RoundedCornerShape(8.dp))
                        .padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    @Composable
    fun ArticlesList(articles: List<ArticlesItem?>) {
        LazyColumn {
            items(articles) { article ->
                NewsCard(article)
            }
        }
    }


    @Composable
    fun handleEvents(navController: NavHostController) {
        val event = newsViewModel.events.value
        event.let {
            when (it) {
                is NewsContract.Event.NavigateToNewsDetails -> {

                    navController.currentBackStackEntry?.savedStateHandle?.apply {
                        set("newsDetails", it.articlesItem)
                    }
                    navController.navigate("newsDetails")

                }

                NewsContract.Event.Idle -> {}
                null -> {}
            }
        }
    }


    @Composable
    fun NewsCard(articlesItem: ArticlesItem?) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 12.dp)
                .clickable { newsViewModel.invokeAction(NewsContract.Action.NewsClicked(articlesItem!!)) }
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Image(
                    painter = rememberAsyncImagePainter(model = articlesItem?.urlToImage ?: ""),
                    contentDescription = "News Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth()
                        .background(Color.LightGray, RoundedCornerShape(10.dp))
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = articlesItem?.title ?: "",
                    style = TextStyle(
                        color = colorResource(id = R.color.colorBlack),
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = articlesItem?.author ?: "",
                    style = TextStyle(color = colorResource(id = R.color.grey))
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = articlesItem?.publishedAt ?: "",
                        style = TextStyle(color = colorResource(id = R.color.grey2))
                    )

                }
            }
        }
    }


    @Composable
    fun NewsCategoryTabs(
        modifier: Modifier = Modifier,
    ) {


        ScrollableTabRow(
            selectedTabIndex = newsViewModel.selectedIndex.intValue,
            containerColor = Color.Transparent,
            divider = {},
            indicator = {},
            modifier = modifier
        ) {

            categories.forEachIndexed { index, category ->
                Tab(
                    selected = newsViewModel.selectedIndex.intValue == index,
                    onClick = {
                        if (newsViewModel.selectedIndex.intValue != index) {
                            newsViewModel.selectedIndex.intValue = index
                            newsViewModel.setSelectedCategory(category)
                        }
                    },

                    selectedContentColor = Color.White,
                    unselectedContentColor = Color(0xFF39A552),
                    modifier = if (newsViewModel.selectedIndex.intValue == index)
                        Modifier
                            .padding(end = 2.dp)
                            .background(
                                Color(0xFF39A552),
                                RoundedCornerShape(50)
                            )
                    else
                        Modifier
                            .padding(end = 2.dp)
                            .border(2.dp, Color(0xFF39A552), RoundedCornerShape(50)),
                    text = { Text(text = category) }
                )


            }

        }

    }

    companion object {
        const val TAG = "MainActivity"
    }
}
