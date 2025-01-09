package com.example.filmapp

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalDrawer
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.filmapp.objects.Genre
import com.example.filmapp.objects.Movie
import kotlinx.coroutines.launch

@Composable
fun MovieScreen(navigateToDetail: (Movie) -> Unit,  modifier: Modifier = Modifier) {
    val filmViewModel: MainViewModel = viewModel()

    val genrestate by filmViewModel.genreState

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        when {
            genrestate.loading -> {
                CircularProgressIndicator(modifier.align(Alignment.Center))
            }
            genrestate.error != null -> {
                Text(genrestate.error!!)
            }

            else -> {
                MoviesScaffold(genresList = genrestate.list, filmViewModel, navigateToDetail)
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MoviesScaffold(
    genresList: List<Genre>,
    filmViewModel: MainViewModel,
    navigateToDetail: (Movie) -> Unit
) {
    val drawerState = rememberDrawerState(androidx.compose.material.DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onMenuItemClicked = { menuItem ->
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("FilmApp", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                    },
                    backgroundColor = Color.Black
                )
            }
        ) { padding ->

            GenresScreen(
                genresList = genresList,
                filmViewModel = filmViewModel,
                navigateToDetail = navigateToDetail,
                modifier = Modifier.fillMaxSize()
                    .background(Color.Black).padding(padding)
            )
        }
    }
}


@Composable
fun GenresScreen(
    genresList: List<Genre>,
    filmViewModel: MainViewModel,
    navigateToDetail: (Movie) -> Unit,
    modifier: Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(genresList) { genre ->
                Text(
                    text = genre.name,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color(0xFFFFFFFF)
                )

                val moviesState = filmViewModel.movieByCategories
                val movies = moviesState[genre.id] ?: emptyList()

                val filteredMovies = if (searchQuery.isEmpty()) {
                    movies
                } else {
                    movies.filter { it.title.contains(searchQuery, ignoreCase = true) }
                }

                if (filteredMovies.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        items(filteredMovies) { movie ->
                            MovieItem(movie, navigateToDetail)
                        }
                    }
                } else if (searchQuery.isNotEmpty()) {
                    Text(
                        text = "Nessun film trovato per \"$searchQuery\"",
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DrawerContent(onMenuItemClicked: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .padding(16.dp)
    ) {
        Text(
            text = "Menu",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Aggiungi elementi di menu
        listOf("Home", "Preferiti").forEach { menuItem ->
            TextButton(
                onClick = { onMenuItemClicked(menuItem) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = menuItem,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Cerca un film...", color = Color.Gray) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color.White
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear Icon",
                        tint = Color.White
                    )
                }
            }
        },
        singleLine = true,
        modifier = modifier
            .padding(horizontal = 16.dp) // Margini orizzontali
            .padding(top = 8.dp) // Margine dall'alto
            .fillMaxWidth(0.9f) // Larghezza relativa allo schermo (90%)
            .clip(RoundedCornerShape(8.dp)) // Angoli arrotondati
            .background(Color.Black), // Colore di sfondo
        )
}

@Composable
fun MovieItem(movie: Movie,  navigateToDetail: (Movie) -> Unit ) {
    Column(
        modifier = Modifier.padding(8.dp).fillMaxSize().clickable { navigateToDetail(movie) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w200/${movie.poster_path}",
            contentDescription = null,
            modifier = Modifier.fillMaxSize().aspectRatio(1f)
        )
    }
}

@Composable
fun MovieDetailScreen(movie: Movie) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = movie.title, style = MaterialTheme.typography.titleLarge, color = Color(0xFFFFFFFF))
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = rememberAsyncImagePainter("https://image.tmdb.org/t/p/w500/${movie.backdrop_path}"),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = movie.overview, style = MaterialTheme.typography.bodySmall, color = Color(0xFFFFFFFF))
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Release Date", style = MaterialTheme.typography.bodySmall, color = Color(0xFFFFFFFF))
                Text(text = "Vote Average", style = MaterialTheme.typography.bodySmall, color = Color(0xFFFFFFFF))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = movie.release_date, style = MaterialTheme.typography.bodySmall, color = Color(0xFFFFFFFF))
                Text(text = movie.vote_average.toString(), style = MaterialTheme.typography.bodySmall, color = Color(0xFFFFFFFF))
            }
        }

    }
}


