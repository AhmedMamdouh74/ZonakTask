package com.example.zonaktask.newsdetails

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.domain.model.ArticlesItem
import com.example.zonaktask.Constance
import com.example.zonaktask.R


@Composable
fun NewsDetails(navController: NavController) {
    val news =
        navController.previousBackStackEntry?.savedStateHandle?.get<ArticlesItem>(Constance.NEWS_DETAILS)
    val url = news?.url ?: return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        // Coil for image loading
        Image(
            painter = rememberAsyncImagePainter(model = news.urlToImage ?: ""),
            contentDescription = "News Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .background(Color.LightGray, RoundedCornerShape(10.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = news.title ?: "",
            style = TextStyle(
                color = colorResource(id = R.color.colorBlack),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = news.author ?: "",
            style = TextStyle(color = colorResource(id = R.color.grey))
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = news.publishedAt ?: "",
            style = TextStyle(color = colorResource(id = R.color.grey2)),
            modifier = Modifier.align(Alignment.End)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = news.content ?: "",
            style = TextStyle(color = colorResource(id = R.color.grey2))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Open article in browser
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
               navController.context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Read Full Article")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Share article link
        Button(
            onClick = {
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, url)
                }
                startActivity(
                    navController.context,
                    Intent.createChooser(shareIntent, "Share via"),
                    null
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Share Article")
        }
    }
}
