package com.example.themovieapp.presentation.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
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
import com.example.themovieapp.presentation.components.Header
import com.example.themovieapp.presentation.components.RatingBar
import com.example.themovieapp.presentation.components.cards.MovieCard
import com.example.themovieapp.presentation.components.cards.PersonCard
import com.example.themovieapp.presentation.components.cards.ReviewCard
import com.example.themovieapp.presentation.components.loadingitems.LoadingPersonCard
import com.example.themovieapp.presentation.navigation.Screen
import com.example.themovieapp.presentation.viewmodel.MovieDetailsViewModel
import com.example.themovieapp.utils.IMAGE_BASE_URL
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
    navController: NavController,tabPageString: String, viewModel: MovieDetailsViewModel = hiltViewModel()
) {

    val tabPage by remember {
        mutableStateOf(TabPage.valueOf(tabPageString))
    }
    val recommendedMovieListUiState by viewModel.recommendedMovieListUiState.collectAsState()
    val castAndCrewListUiState by viewModel.castAndCrewListUiState.collectAsState()
    val reviewListUiState by viewModel.reviewListUiState.collectAsState()
    val movieAndExtraDetailUiState = viewModel.movieAndExtraDetailUiState.collectAsState().value
    val title =  movieAndExtraDetailUiState.movieAndExtraDetails?.title ?: ""
    Header(title,navController,tabPage)
     { paddingValues ->
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
                            onClickFavourite = { viewModel.addMovieToFavourite(movie.id) },
                            modifier = Modifier
                        )
                        if (castAndCrewListUiState.isLoading) {
                                LazyRow {
                                    items(3) {
                                        LoadingPersonCard()
                                    }
                                }
                        } else {
                            if (castAndCrewListUiState.castAndCrew.isNotEmpty()) {
                                CastAndCrewContent(
                                    castAndCrewList = castAndCrewListUiState.castAndCrew,
                                    isLoading = castAndCrewListUiState.isLoading,
                                    castAndCrewForMovie = {
                                        navController.navigate("${Screen.CastAndCrewScreen.route}/${movie.id}")
                                    })
                            }
                            if (reviewListUiState.review.isNotEmpty()) {

                                ReviewContent(
                                    reviewList = reviewListUiState.review,
                                    reviewForMovie = {
                                        navController.navigate("${Screen.ReviewScreen.route}/${movie.id}")

                                    })
                            }
                            if (movieAndExtraDetailUiState.movieAndExtraDetails.postersPathList?.isNotEmpty() == true) {
                                MediaContent(
                                    extraMovieDetails = movieAndExtraDetailUiState.movieAndExtraDetails,
                                    modifier = Modifier,
                                    moreBackDrops = {
                                        navController.navigate("${Screen.BackdropScreen.route}/${movie.id}")
                                    },
                                    morePosters = {
                                        navController.navigate("${Screen.PosterScreen.route}/${movie.id}")

                                    }
                                )
                            }
                            if (recommendedMovieListUiState.movieList.isNotEmpty()) {

                                RecommendationContent(
                                    recommendedMovieList = recommendedMovieListUiState.movieList,
                                    navController = navController,
                                    tabPage = tabPage
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
private fun BackDrop(
    movieDetails: MovieDetailsAndExtraDetails, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(IMAGE_BASE_URL.plus(movieDetails.backdropPath)).crossfade(true)
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
                .data(IMAGE_BASE_URL.plus(movieDetails.posterPath)).crossfade(true)
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
private fun MovieContent(
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
            verticalArrangement = Arrangement.spacedBy(8.dp),
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
            if (rating != null) {
                RatingBar(rating)
            }

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
private fun MoreMovieContent(extraMovieDetails: MovieDetailsAndExtraDetails) {
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
        if (extraMovieDetails.budget != 0L) {
            Column {
                ColumnView(
                    label = "Budget: ",
                    value = "$ ${extraMovieDetails.budget?.div(1000000)}M"
                )
            }

        }
        if (extraMovieDetails.revenue != 0L) {
            ColumnView(label = "Revenue:", value = "$ ${extraMovieDetails.revenue?.div(1000000)}M")
        }
        val duration = extraMovieDetails.runtime?.toDuration(DurationUnit.MINUTES)
        ColumnView(label = "Duration: ", value = duration.toString())
        ColumnView(label = "Status: ", value = extraMovieDetails.status.toString())
    }
}

@Composable
private fun ColumnView(label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold
        )
        Text(text = value, style = MaterialTheme.typography.titleLarge)
    }

}

@Composable
private fun CastAndCrewContent(
    castAndCrewList: List<CastAndCrew>,
    isLoading: Boolean,
    castAndCrewForMovie: () -> Unit = {}
) {
    var isCast by remember {
        mutableStateOf(true)
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.SpaceBetween
    ) {
        Row(
            horizontalArrangement = Arrangement.Absolute.spacedBy(4.dp)

        ) {
            Text(
                text = "Cast",
                style = MaterialTheme.typography.titleLarge,
                color = if (isCast) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSecondary,
                textDecoration = if (isCast) TextDecoration.Underline else TextDecoration.None,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .clickable { isCast = true },
            )
            Text(
                text = "Crew",
                style = MaterialTheme.typography.titleLarge,
                color = if (!isCast) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSecondary,
                textDecoration = if (!isCast) TextDecoration.Underline else TextDecoration.None,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .clickable { isCast = false },

                )
        }
        Text(
            text = "See All ",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(start = 5.dp)
                .clickable {
                    castAndCrewForMovie()
                }
        )
    }

    val castList = castAndCrewList.filter { it.isCast }.sortedBy { it.order }
    val crewList = castAndCrewList.filter { !it.isCast }
    val list = if (isCast) castList else crewList
    LazyRow {
        items(list.size) { index ->
            val castAndCrew = list[index]
            PersonCard(
                title = castAndCrew.name,
                role = castAndCrew.role,
                photo = castAndCrew.profilePath
            )
        }
    }
}

@Composable
private fun ReviewContent(reviewList: List<Review>, reviewForMovie: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Reviews ",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(start = 5.dp),
            )
            Text(
                text = reviewList.size.toString(),
                color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.7f),
                style = MaterialTheme.typography.headlineSmall
            )
        }
        Text(
            text = "Read All Reviews",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .clickable {
                    reviewForMovie()
                }
        )

    }
    LazyRow {
        items(reviewList.size) { index ->
            val review = reviewList[index]
            ReviewCard(
                review = review,
                modifier = Modifier
                    .height(250.dp),
                overflow = TextOverflow.Ellipsis

            )
        }
    }

}

@Composable
private fun MediaContent(
    extraMovieDetails: MovieDetailsAndExtraDetails,
    modifier: Modifier = Modifier,
    moreBackDrops: () -> Unit,
    morePosters: () -> Unit
) {
    var isBackdrops by remember {
        mutableStateOf(true)
    }

    val postersPathList = extraMovieDetails.postersPathList
    val backdropsPathList = extraMovieDetails.backdropsPathList

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 8.dp)
    ) {

        Text(
            text = "Backdrops (${backdropsPathList?.size})",
            style = MaterialTheme.typography.titleLarge,
            color = if (isBackdrops) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSecondary,
            textDecoration = if (isBackdrops) TextDecoration.Underline else TextDecoration.None,
            modifier = Modifier
                .padding(start = 5.dp)
                .clickable { isBackdrops = true },

            )
        Text(
            text = "Posters (${postersPathList?.size})",
            style = MaterialTheme.typography.titleLarge,
            color = if (!isBackdrops) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSecondary,
            textDecoration = if (!isBackdrops) TextDecoration.Underline else TextDecoration.None,
            modifier = Modifier
                .padding(start = 5.dp)
                .clickable { isBackdrops = false },
        )

    }
    val mediaList = if (isBackdrops) backdropsPathList else postersPathList
    LazyRow {
        if (mediaList != null) {
            val itemCount = if (mediaList.size > 10) 10 else mediaList.size
            items(itemCount) { index ->
                val imagesPath = mediaList[index]
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(IMAGE_BASE_URL.plus(imagesPath))
                        .crossfade(true)
                        .build(),
                    error = painterResource(id = R.drawable.ic_broken_image),
                    contentDescription = "hello",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .heightIn(min = 200.dp)
                )
                if (mediaList.size > 5) {
                    if (index == itemCount - 1) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .heightIn(min = if (isBackdrops) 200.dp else 300.dp)
                                .width(150.dp)
                                .clickable {
                                    if (isBackdrops) moreBackDrops() else
                                        morePosters()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "View More")
                            Icon(
                                imageVector = Icons.Default.ArrowCircleRight,
                                contentDescription = "",
                                modifier = Modifier.align(
                                    Alignment.CenterEnd
                                )
                            )
                        }
                    }
                }
            }
        }
    }
    MoreMovieContent(extraMovieDetails = extraMovieDetails)
}

@Composable
private fun RecommendationContent(recommendedMovieList: List<MovieDetailsAndExtraDetails?>, navController: NavController,tabPage: TabPage) {
    Text(
        text = stringResource(R.string.recommended_movies),
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.titleLarge
    )

    LazyRow {
        items(recommendedMovieList.size) { index ->
            val recommendedMovie = recommendedMovieList[index]
            if (recommendedMovie != null) {
                MovieCard(title = recommendedMovie.title,
                    date = recommendedMovie.releaseDate,
                    rating = recommendedMovie.voteAverage,
                    photo = recommendedMovie.posterPath,
                    moreMovieDetails = {
                        navController.navigate("${Screen.DetailScreen.route}/${recommendedMovie.id}/${tabPage.name}")

                    })
            }
        }
    }

}

@Composable
private fun VideoPlayer(
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