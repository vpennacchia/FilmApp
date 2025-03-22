package com.example.filmapp

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.filmapp.dataFirebase.Movie
import com.example.filmapp.viewmodel.MainViewModel
import androidx.compose.ui.platform.LocalContext
import com.example.filmapp.viewmodel.MovieViewModel

@SuppressLint("UnrememberedMutableState")
@Composable
fun MovieDetailScreen(movie: Movie, onAddListClick: (Movie) -> Unit, onFavoriteClick : (Movie) -> Unit, movieViewModel: MovieViewModel, viewModel: MainViewModel = viewModel()) {

    val myListMovies by movieViewModel.myListMovies.collectAsState()
    val myFavMovies by movieViewModel.favoritesMovies.collectAsState()

    var isInMyList by remember {
        mutableStateOf(myListMovies.any { it.id == movie.id })
    }

    var isFavorite by remember {
        mutableStateOf(myFavMovies.any { it.id == movie.id })
    }


    val providers by viewModel.providers.collectAsState()

    viewModel.getMovieProviders(movie.id)


    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = rememberAsyncImagePainter("https://image.tmdb.org/t/p/w500/${movie.backdrop_path}"),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Black.copy(alpha = 0.6f), Color.Black)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(
                        onClick = {
                            onFavoriteClick(movie)
                            isFavorite = !isFavorite
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ThumbUp,
                            contentDescription = "Aggiungi ai preferiti",
                            tint = if(isFavorite) Color.Red else Color.White,
                            modifier = Modifier.size(20.dp) // Icona più piccola per allineamento
                        )
                    }
                }
            }

            Text(
                text = movie.overview,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Release Date", color = Color.Gray, fontSize = 12.sp)
                    Text(movie.release_date, color = Color.White, fontSize = 14.sp)
                }
                Column {
                    Text("Rating", color = Color.Gray, fontSize = 12.sp)
                    Text(movie.vote_average.toString(), color = Color.White, fontSize = 14.sp)
                }
            }

            StreamingProvidersRow(providers, viewModel)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        onAddListClick(movie)
                        isInMyList = !isInMyList
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (isInMyList) Icons.Default.Check else Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("My List", color = Color.White)
                }
            }
        }
    }
}


@Composable
fun StreamingProvidersRow(providers: List<String>, viewModel: MainViewModel = viewModel()) {


    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(
            text = "Available on:",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        if (providers.isEmpty()) {
            Text(
                text = "No streaming platforms available",
                color = Color.LightGray,
                fontSize = 12.sp
            )
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(providers) { provider ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        var providerName = provider.lowercase()
                        val context = LocalContext.current

                        if (providerName == "rakuten tv") { providerName = "rakuten" } else if (providerName == "google play movies") { providerName = "google" }

                        if(providerName != "timvision" && providerName != "netflix basic with ads" && providerName != "mediaset infinity") {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    "https://logo.clearbit.com/${providerName.replace("\\s+".toRegex(), "")}.com"
                                ),
                                contentDescription = provider,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color.DarkGray)
                                    .clickable {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.getProviderLink(providerName)))
                                        context.startActivity(intent)
                                    }
                            )
                            Text(
                                text = provider,
                                fontSize = 10.sp,
                                color = Color.LightGray,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}



