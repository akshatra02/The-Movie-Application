package com.example.themovieapp.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.themovieapp.R
import com.example.themovieapp.data.source.remote.MoviesApi
import com.example.themovieapp.domain.model.CastAndCrew
import com.example.themovieapp.domain.model.Movie
import com.example.themovieapp.domain.model.MovieDetailsAndExtraDetails
import com.example.themovieapp.domain.model.Review
import com.example.themovieapp.presentation.components.BottomTab
import com.example.themovieapp.presentation.components.MovieCard
import com.example.themovieapp.presentation.components.PersonCard
import com.example.themovieapp.presentation.components.ReviewCard
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.presentation.viewmodel.MovieDetailsViewModel
import com.example.themovieapp.utils.TabPage
import com.example.themovieapp.utils.toDate
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController, viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    var tabPage by remember {
        mutableStateOf(TabPage.HOME)
    }
    val movieDetailUiState = viewModel.movieDetailsUiState.collectAsState().value
    val recommendedMovieListUiState = viewModel.recommendedMovieListUiState.collectAsState().value
    val castAndCrewListUiState = viewModel.castAndCrewListUiState.collectAsState().value
    val reviewListUiState = viewModel.reviewListUiState.collectAsState().value
    val movieAndExtraDetailUiState = viewModel.movieAndExtraDetailUiState.collectAsState().value
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(R.drawable.movie_icon),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(
                                CircleShape
                            )
                    )
                    movieDetailUiState.movieDetails?.let {
                        Text(
                            text = it.title, style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            },
        )
    }, bottomBar = {
        BottomAppBar {
            BottomTab(navController = navController,
                tabPage = tabPage,
                onTabSelected = { tabPage = it })
        }

    }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues),

            ) {
            item {
                movieAndExtraDetailUiState.movieAndExtraDetails?.let { movie ->
                    Column {

                        BackDrop(movieDetails = movie, modifier = Modifier.height(350.dp))
                        MovieContent(
                            movie = movie,
                            onClickFavourite = { viewModel.addMovieToFavourite() },
                            modifier = Modifier
                        )
                        if (castAndCrewListUiState.isLoading && reviewListUiState.isLoading && recommendedMovieListUiState.isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            if (castAndCrewListUiState.castAndCrew.isNotEmpty()) {
                                CastAndCrewContent(
                                    castAndCrewList = castAndCrewListUiState.castAndCrew,
                                    castAndCrewForMovie = { navController.navigate(Screen.CastAndCrewScreen.route) })
                            }
                            if (reviewListUiState.review.isNotEmpty()) {

                                ReviewContent(reviewList = reviewListUiState.review)
                            }
                            if (movieAndExtraDetailUiState.movieAndExtraDetails.imagesPathList?.isNotEmpty() == true) {
                                MediaContent(
                                    extraMovieDetails = movieAndExtraDetailUiState.movieAndExtraDetails
                                )
                            }
                            if (recommendedMovieListUiState.movieList.isNotEmpty()) {

                                RecommendationContent(
                                    recommendedMovieList = recommendedMovieListUiState.movieList,
                                    navController = navController
                                )
                            }
                        }


                    }
                }
            }
        }
    }
}


@Composable
fun BackDrop(
    movieDetails: MovieDetailsAndExtraDetails, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(MoviesApi.IMAGE_BASE_URL.plus(movieDetails.backdropPath)).crossfade(true)
                .build(),
            error = painterResource(id = R.drawable.ic_broken_image),
            contentScale = ContentScale.Crop,
            contentDescription = "",
            colorFilter = ColorFilter.tint(
                MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                BlendMode.ColorBurn
            ),
            modifier = modifier
//                .align(Alignment.BottomEnd)
        )
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(MoviesApi.IMAGE_BASE_URL.plus(movieDetails.posterPath)).crossfade(true)
                .build(),

            error = painterResource(id = R.drawable.ic_broken_image),
            contentDescription = "",
            modifier = modifier
                .size(150.dp)
                .align(Alignment.Center)

        )
    }
}

@Composable
fun MovieContent(
    movie: MovieDetailsAndExtraDetails,
    onClickFavourite: () -> Unit,
    modifier: Modifier
) {
    val isFavourite = movie.isFavourite

    val rating = movie.voteAverage

    Column(
        modifier = modifier.padding(vertical = 16.dp)
    ) {
        Row {

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = movie.genreNames.toString().replace("[", "").replace("]", "")
                        .replace(", ", " • "),
                    style = MaterialTheme.typography.titleMedium,
                    fontStyle = FontStyle.Italic,
//                textAlign = TextAlign.Center,
                    modifier = modifier
                        .alpha(0.6f)
                )
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.secondary

                )

                Text(
                    text = toDate(movie.releaseDate),
                    style = MaterialTheme.typography.titleMedium,
                    fontStyle = FontStyle.Italic,
                    modifier = modifier
                        .alpha(0.6f)
                )
                if (movie.tagline != null) {
                    Text(text = movie.tagline)
                }
            }
        }
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.overall_rating),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = stringResource(R.string.rating_score).format(rating),
                style = MaterialTheme.typography.titleLarge
            )

        }
        Button(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = ButtonDefaults.shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFavourite) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimaryContainer,
                contentColor = if (isFavourite) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer
            ),
            onClick = { onClickFavourite() },
        ) {
            Text(
                text = if (isFavourite) "Remove from Favourite"
                else "Add to Favourite"
            )

        }
        Text(
            text = stringResource(R.string.overview),
            modifier = modifier.padding(vertical = 10.dp),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(text = movie.overview)


    }
}

@Composable
fun MoreMovieContent(extraMovieDetails: MovieDetailsAndExtraDetails) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val videoLink = extraMovieDetails.videoLink
        if (videoLink != null) {
            VideoPlayer(videoId = videoLink, lifecycleOwner = LocalLifecycleOwner.current)
        }
        Text(
            text = "More Details",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.secondary,
        )

        Text(text = "Budget: ${extraMovieDetails.budget ?: 0}")
        Text(text = "Revenue: ${extraMovieDetails.revenue ?: 0}")
        val duration = extraMovieDetails.runtime?.toDuration(DurationUnit.MINUTES)
        Text(text = "Duration: $duration")
        Text(text = "Status: ${extraMovieDetails.status}")
    }
}

@Composable
fun CastAndCrewContent(castAndCrewList: List<CastAndCrew>, castAndCrewForMovie: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.cast_and_crew),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(start = 5.dp)
        )
//        Text(
//            text = "See All ",
//            style = MaterialTheme.typography.titleMedium,
//            modifier = Modifier
//                .padding(start = 5.dp)
//                .clickable {
//                    castAndCrewForMovie()
//                }
//        )
    }
    LazyRow {
        items(castAndCrewList.size) { index ->
            val castAndCrew = castAndCrewList[index]
            PersonCard(
                title = castAndCrew.name,
                role = castAndCrew.role,
                photo = castAndCrew.profilePath
            )
        }
    }
}

@Composable
fun ReviewContent(reviewList: List<Review>) {
    Text(
        text = "Reviews :",
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.titleLarge
    )

    LazyRow {
        items(reviewList.size) { index ->
            val review = reviewList[index]
            val avatar = review.avatarPath ?: " "
            ReviewCard(
                name = review.author,
                content = review.content,
                photo = avatar
            ) {

            }
        }
    }

}

@Composable
fun MediaContent(extraMovieDetails: MovieDetailsAndExtraDetails) {
    Text(
        text = stringResource(R.string.media),
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.titleLarge
    )
    val imagesPathList =
        extraMovieDetails.imagesPathList
    LazyRow {
        imagesPathList?.size?.let {
            items(it) { index ->
                val imagesPath = imagesPathList[index]
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(MoviesApi.IMAGE_BASE_URL.plus(imagesPath))
                        .crossfade(true)
                        .build(),
                    error = painterResource(id = R.drawable.ic_broken_image),
                    contentDescription = "hello",
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(300.dp)
                )
            }
        }
    }
    MoreMovieContent(extraMovieDetails = extraMovieDetails)
}

@Composable
fun RecommendationContent(recommendedMovieList: List<Movie>, navController: NavController) {
    Text(
        text = stringResource(R.string.recommended_movies),
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.titleLarge
    )

    LazyRow {
        items(recommendedMovieList.size) { index ->
            val recommendedMovie = recommendedMovieList[index]
            MovieCard(title = recommendedMovie.title,
                date = recommendedMovie.releaseDate,
                rating = recommendedMovie.voteAverage,
                photo = recommendedMovie.posterPath,
                moreMovieDetails = {
                    navController.navigate("${Screen.DetailScreen.route}/${recommendedMovie.id}")

                })
        }
    }

}

@Composable
fun VideoPlayer(
    videoId: String,
    lifecycleOwner: LifecycleOwner
) {
    Text(
        text = stringResource(R.string.watch_trailer),
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.titleLarge
    )

    AndroidView(modifier = Modifier.fillMaxWidth(), factory = { context ->
        YouTubePlayerView(context).apply {
            lifecycleOwner.lifecycle.addObserver(this)

            addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(videoId, 0f)
                }
            })
        }

    })

}