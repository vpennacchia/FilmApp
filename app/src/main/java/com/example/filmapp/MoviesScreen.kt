package com.example.filmapp

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DrawerState
import androidx.compose.material.ModalDrawer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.rememberDrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.filmapp.navigation.Screen
import com.example.filmapp.navigation.screensInDrawer
import com.example.filmapp.dataFirebase.Genre
import com.example.filmapp.dataFirebase.Movie
import com.example.filmapp.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MovieScreen(navigateToDetail: (Movie) -> Unit, navController: NavHostController, modifier: Modifier = Modifier, viewModel: MainViewModel) {

    val genrestate by viewModel.genreState

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        when {
            genrestate.loading -> {
                CircularProgressIndicator(modifier.align(Alignment.Center))
            }
            genrestate.error != null -> {
                Text(genrestate.error!!)
            }

            else -> {
                MoviesScaffold(genresList = genrestate.list, viewModel, navigateToDetail, navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MoviesScaffold(
    genresList: List<Genre>,
    filmViewModel: MainViewModel,
    navigateToDetail: (Movie) -> Unit,
    navController: NavHostController
) {
    val drawerState = rememberDrawerState(androidx.compose.material.DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1E1E1E)) // Sfondo scuro moderno
                    .padding(vertical = 16.dp)
                    .width(250.dp)
            ) {
                LazyColumn() {
                    items(screensInDrawer) { item ->

                        if (item.dTitle == "Home" || item.dTitle == "My Account") {
                            DrawerItem(item = item) {
                                scope.launch {
                                    drawerState.close()
                                }

                                navController.navigate(item.dRoute)

                            }
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "FilmApp",
                            color = Color.White,
                            fontSize = 20.sp, // Leggermente più grande
                            fontWeight = FontWeight.Bold // Maggiore impatto visivo
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF1E1E1E), // Grigio scuro elegante
                        titleContentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                )
            }
        ) { padding ->

            GenresScreen(
                genresList = genresList,
                filmViewModel = filmViewModel,
                navigateToDetail = navigateToDetail,
                modifier = Modifier.fillMaxSize()
                    .background(Color(0xFF1E1E1E)).padding(padding)
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
        val moviesState = filmViewModel.movieByCategories
        Spacer(modifier = Modifier.height(16.dp))

        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        if (searchQuery.isNotEmpty()) {
            val filteredMovies = filmViewModel.movieByCategories
                .values
                .toList()
                .flatten()
                .filter { it.title.contains(searchQuery, ignoreCase = true) }
                .distinctBy { it.id }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    items(filteredMovies) { movie ->
                        MovieItem(movie, navigateToDetail,
                            modifier = Modifier
                            .fillMaxWidth(0.2f)
                            .height(250.dp) )
                    }
                }

        } else {
            LazyColumn {

                items(genresList) { genre ->
                    val movies = moviesState[genre.id] ?: emptyList()

                    Text(
                        text = genre.name.uppercase(),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp,
                            fontSize = 18.sp
                        ),
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.White
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        items(movies) { movie ->
                            MovieItem(movie, navigateToDetail)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie, navigateToDetail: (Movie) -> Unit, modifier: Modifier = Modifier.padding(8.dp).fillMaxSize()) {
    Column(
        modifier = modifier.clip(RoundedCornerShape(8.dp)).clickable { navigateToDetail(movie) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .clip(RoundedCornerShape(8.dp))
                .clickable { navigateToDetail(movie) },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w200/${movie.poster_path}",
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
            )
        }
    }
}

@Composable
fun DrawerItem(
    item: Screen.MovieScreen,
    onDrawerItemClicked : () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Transparent)
            .clickable { onDrawerItemClicked() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if(item.dTitle == "Home") Icons.Default.Home else Icons.Default.Person,
            contentDescription = item.dTitle,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = item.dTitle,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
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




