package com.glowka.rafal.topmusic.presentation.flow.dashboard.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Bottom
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.glowka.rafal.topmusic.domain.model.Album
import com.glowka.rafal.topmusic.presentation.R
import com.glowka.rafal.topmusic.presentation.architecture.BaseFragment
import com.glowka.rafal.topmusic.presentation.flow.dashboard.list.ListViewModelToViewInterface.State
import com.glowka.rafal.topmusic.presentation.flow.dashboard.list.ListViewModelToViewInterface.ViewEvents
import com.glowka.rafal.topmusic.presentation.style.Colors
import com.glowka.rafal.topmusic.presentation.style.FontSize
import com.glowka.rafal.topmusic.presentation.style.Fonts
import com.glowka.rafal.topmusic.presentation.style.Margin
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.skydoves.landscapist.glide.GlideImage

class ListFragment :
  BaseFragment<State, ViewEvents, ListViewModelToViewInterface>() {

  override fun ComposeView.renderState(viewModelState: MutableState<State>) {
    setContent {
      val systemUiController = rememberSystemUiController()
      val useDarkIcons = MaterialTheme.colors.isLight

      MaterialTheme {
        systemUiController.setSystemBarsColor(
          color = Color.Transparent,
          darkIcons = useDarkIcons
        )
        val items = viewModelState.value.items

        SwipeRefresh(
          modifier = Modifier.systemBarsPadding().fillMaxSize(),
          state = rememberSwipeRefreshState(viewModelState.value.isRefreshing),
          onRefresh = { viewModel.onViewEvent(ViewEvents.RefreshList) },
        ) {
          if (items.isNotEmpty()) {
            LazyVerticalGrid(
              modifier = Modifier.padding(10.dp),
              columns = GridCells.Fixed(count = 2)
            ) {
              header {
                Text(
                  modifier = Modifier.padding(Margin.m4),
                  text = getString(R.string.list_title),
                  color = Colors.dark,
                  fontFamily = Fonts.Bold,
                  fontSize = FontSize.title
                ) // or any composable for your single row
              }
              items(
                items.size,
                null
              ) {
                AlbumItem(items[it])
              }
            }
          } else {
            Text(
              modifier = Modifier
                .fillMaxWidth(),
              textAlign = TextAlign.Center,
              text = viewModelState.value.errorMessage,
            )
          }
        }
      }
    }
  }

  @Composable
  fun AlbumItem(album: Album) {
    Card(
      Modifier
        .clickable {
          viewModel.onViewEvent(ViewEvents.PickedAlbum(album))
        }
        .fillMaxWidth()
        .aspectRatio(1f)
        .padding(6.dp),
      shape = RoundedCornerShape(20.dp),
      elevation = 5.dp

    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .aspectRatio(1f)
      ) {
        GlideImage(
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
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  Color(0x00000000),
                  Color(0xc0000000),
                ),
              )
            )
        ) {

        }
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(Margin.m3),
          verticalArrangement = Bottom
//          contentAlignment = Alignment.BottomStart

        ) {
          Text(
            modifier = Modifier.wrapContentSize(),
            textAlign = TextAlign.Left,
            text = album.name,
            color = Color.White,
            fontFamily = Fonts.Semibold,
            fontSize = FontSize.base
          )
          Text(
            modifier = Modifier.wrapContentSize(),
            textAlign = TextAlign.Left,
            text = album.artistName,
            color = Color.Gray,
            fontFamily = Fonts.Medium,
            fontSize = FontSize.small
          )
        }
      }
    }
  }

  private fun LazyGridScope.header(
    content: @Composable LazyGridItemScope.() -> Unit
  ) {
    item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
  }

}