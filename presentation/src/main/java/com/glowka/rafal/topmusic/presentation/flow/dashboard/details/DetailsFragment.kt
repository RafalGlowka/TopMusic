package com.glowka.rafal.topmusic.presentation.flow.dashboard.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.elevation
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.glowka.rafal.topmusic.domain.utils.inject
import com.glowka.rafal.topmusic.presentation.R
import com.glowka.rafal.topmusic.presentation.architecture.BaseFragment
import com.glowka.rafal.topmusic.presentation.flow.dashboard.details.DetailsViewModelToViewInterface.State
import com.glowka.rafal.topmusic.presentation.flow.dashboard.details.DetailsViewModelToViewInterface.ViewEvents
import com.glowka.rafal.topmusic.presentation.formatter.ReleaseDateFormatter
import com.glowka.rafal.topmusic.presentation.style.Colors
import com.glowka.rafal.topmusic.presentation.style.FontSize
import com.glowka.rafal.topmusic.presentation.style.Fonts
import com.glowka.rafal.topmusic.presentation.style.Margin
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.skydoves.landscapist.glide.GlideImage


class DetailsFragment :
  BaseFragment<State, ViewEvents, DetailsViewModelToViewInterface>() {

  val releaseDateFormatter: ReleaseDateFormatter by inject()

  override fun ComposeView.renderState(viewModelState: MutableState<State>) {
    setContent {
      val systemUiController = rememberSystemUiController()
      val useDarkIcons = MaterialTheme.colors.isLight

      MaterialTheme {
        systemUiController.setSystemBarsColor(
          color = Color.Transparent,
          darkIcons = useDarkIcons
        )
        val album = viewModelState.value.album
        Column(
          modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding()
        ) {
          GlideImage(
            modifier = Modifier
              .fillMaxWidth()
              .aspectRatio(1f),
            imageModel = album.artworkUrl100,
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
                contentDescription = getString(R.string.content_description_loading_image_error),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
              )
            }
          )
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(Margin.m4)
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
            LazyRow {
              items(album.genres.size) { index ->
                Text(
                  text = album.genres[index].name,
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
            Spacer(modifier = Modifier.height(40.dp))

            Column(
              modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
              verticalArrangement = Arrangement.Center,
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
                elevation = elevation(
                  defaultElevation = 0.dp,
                  pressedElevation = 0.dp
                ),
                onClick = {
                  viewModel.onViewEvent(ViewEvents.OpenURL)
                },
                modifier = Modifier.padding(Margin.m3)
              ) {
                Text(
                  text = getString(R.string.visit_the_album),
                  fontFamily = Fonts.Semibold,
                  fontSize = FontSize.base,
                  color = Colors.white,
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
fun labeledValue(label: String, value: String) {
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
