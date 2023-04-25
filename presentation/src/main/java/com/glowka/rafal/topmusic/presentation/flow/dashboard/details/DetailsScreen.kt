package com.glowka.rafal.topmusic.presentation.flow.dashboard.details

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.domain.model.Genre
import com.glowka.rafal.topmusic.domain.utils.inject
import com.glowka.rafal.topmusic.presentation.R
import com.glowka.rafal.topmusic.presentation.formatter.ReleaseDateFormatter
import com.glowka.rafal.topmusic.presentation.style.Colors
import com.glowka.rafal.topmusic.presentation.style.FontSize
import com.glowka.rafal.topmusic.presentation.style.Fonts
import com.glowka.rafal.topmusic.presentation.style.Margin
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.skydoves.landscapist.glide.GlideImage

private val releaseDateFormatter: ReleaseDateFormatter by inject()

@Composable
internal fun DetailsScreen(
  viewState: DetailsViewModelToViewInterface.ViewState,
  onViewEvent: (DetailsViewModelToViewInterface.ViewEvents) -> Unit
) {
  val systemUiController = rememberSystemUiController()
  val useDarkIcons = MaterialTheme.colors.isLight

  MaterialTheme {
    systemUiController.setSystemBarsColor(
      color = if (useDarkIcons) Color(0x44FFFFFF) else Color(0x44000000),
      darkIcons = useDarkIcons
    )
    BoxWithConstraints(
      modifier = Modifier
        .navigationBarsPadding()
        .fillMaxSize()
    ) {
      val height = maxHeight
      Column(
        modifier = Modifier
          .fillMaxSize()
          .heightIn(min = height)
          .verticalScroll(rememberScrollState())
      ) {
        Column(
          modifier = Modifier
            .wrapContentHeight(unbounded = true)
            .heightIn(min = height)
        ) {
          AlbumArtWork(url = viewState.album.artworkUrl100)
          AlbumDescription(
            modifier = Modifier
              .fillMaxWidth()
              .run {
                if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
                  this.weight(1f)
                } else {
                  this.wrapContentHeight()
                }
              },
            album = viewState.album,
            onViewEvent = onViewEvent
          )
        }
      }
      BackIcon(onBackClick = { onViewEvent(DetailsViewModelToViewInterface.ViewEvents.Close) })
    }
  }
}

@Composable
private fun BackIcon(onBackClick: () -> Unit) {
  IconButton(
    modifier = Modifier
      .wrapContentSize()
      .padding(16.dp)
      .systemBarsPadding(),
    onClick = onBackClick
  ) {
    Icon(
      modifier = Modifier
        .width(44.dp)
        .height(44.dp)
        .padding(10.dp)
        .drawBehind {
          drawCircle(
            color = Colors.buttonBack,
            radius = 22.dp.toPx()
          )
        },
      painter = painterResource(id = R.drawable.ic_back),
      contentDescription = stringResource(id = R.string.content_description_back_button)
    )
  }
}

@Composable
private fun AlbumArtWork(url: String) {
  GlideImage(
    modifier = Modifier
      .fillMaxWidth()
      .aspectRatio(1f),
//      .size(1200.dp, 1200.dp),
    imageModel = { url },
    loading = {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(1f),
        contentAlignment = Alignment.Center
      ) {
        CircularProgressIndicator(
          modifier = Modifier
            .fillMaxWidth(fraction = 0.5f)
            .aspectRatio(1f)
        )
      }
    },
    failure = {
      Image(
        painterResource(R.drawable.music_error),
        contentDescription = stringResource(id = R.string.content_description_loading_image_error),
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )
    }
  )
}

@Composable
private fun GenresRow(genres: List<Genre>) {
  LazyRow {
    items(genres.size) { index ->
      Text(
        text = genres[index].name,
        color = Colors.blue,
        fontFamily = Fonts.Medium,
        fontSize = FontSize.base,
        modifier = Modifier
          .border(
            width = 2.dp,
            color = Colors.blue,
            shape = RoundedCornerShape(15.dp)
          )
          .padding(
            vertical = Margin.m1,
            horizontal = Margin.m2
          ),
      )
      Spacer(modifier = Modifier.width(Margin.m2))
    }
  }
}

@Composable
private fun AlbumDescription(
  modifier: Modifier,
  album: Album,
  onViewEvent: (DetailsViewModelToViewInterface.ViewEvents) -> Unit
) {
  Column(
    modifier = modifier
      .padding(start = Margin.m4, end = Margin.m4, top = Margin.m4, bottom = 0.dp)
  ) {

    Text(
      modifier = Modifier.wrapContentSize(),
      textAlign = TextAlign.Left,
      text = album.artistName,
      color = Colors.authorName,
      fontSize = FontSize.big,
      fontFamily = Fonts.Regular
    )
    Text(
      modifier = Modifier.wrapContentSize(),
      textAlign = TextAlign.Left,
      color = Colors.dark,
      text = album.name,
      fontSize = FontSize.title,
      fontFamily = Fonts.Bold
    )
    Spacer(modifier = Modifier.width(Margin.m4))
    GenresRow(genres = album.genres)
    Spacer(modifier = Modifier.height(40.dp))
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
      verticalArrangement = Arrangement.Bottom,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        modifier = Modifier.wrapContentSize(),
        textAlign = TextAlign.Center,
        text = releaseDateFormatter.format(album.releaseDate),
        color = Colors.gray,
        fontSize = FontSize.small,
        fontFamily = Fonts.Medium
      )
      Text(
        modifier = Modifier.wrapContentSize(),
        textAlign = TextAlign.Center,
        color = Colors.gray,
        text = album.copyright,
        fontSize = FontSize.small,
        fontFamily = Fonts.Medium
      )
      Spacer(modifier = Modifier.height(Margin.m6))
      Button(
        shape = RoundedCornerShape(15.dp),
        elevation = ButtonDefaults.elevation(
          defaultElevation = 0.dp,
          pressedElevation = 0.dp
        ),
        onClick = {
          onViewEvent(DetailsViewModelToViewInterface.ViewEvents.OpenURL)
        },
        modifier = Modifier.padding(Margin.m3)
      ) {
        Text(
          text = stringResource(id = R.string.visit_the_album),
          fontFamily = Fonts.Semibold,
          fontSize = FontSize.base,
          color = Colors.white,
        )
      }
      Spacer(modifier = Modifier.height(Margin.m6))
    }
  }
}

@Composable
private fun labeledValue(label: String, value: String) {
  Row(modifier = Modifier.padding(10.dp)) {
    Text(
      modifier = Modifier.fillMaxWidth(0.3f),
      textAlign = TextAlign.Left,
      text = label
    )

    Text(
      modifier = Modifier.fillMaxWidth(0.7f),
      textAlign = TextAlign.Left,
      text = value
    )
  }
}

@Preview
@Composable
private fun DetailsScreenPreview() {
  DetailsScreen(
    viewState = DetailsViewModelToViewInterface.ViewState(
      album = Album(
        id = "123",
        name = "Super Jazz",
        artistName = "Artist",
        releaseDate = "2022.03.18",
        artworkUrl100 = "www.wp.pl",
        genres = listOf(Genre(id = "123", name = "Jazz")),
        copyright = "some copyright",
        url = "www.wp.pl",
        countryCode = "us",
      )
    ),
    onViewEvent = {}
  )
}