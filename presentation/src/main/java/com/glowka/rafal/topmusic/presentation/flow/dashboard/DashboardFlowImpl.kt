package com.glowka.rafal.topmusic.presentation.flow.dashboard

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import com.glowka.rafal.topmusic.domain.utils.EmptyParam
import com.glowka.rafal.topmusic.domain.utils.logD
import com.glowka.rafal.topmusic.presentation.architecture.BaseFlow
import com.glowka.rafal.topmusic.presentation.architecture.Screen
import com.glowka.rafal.topmusic.presentation.flow.dashboard.details.DetailsViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.flow.dashboard.list.ListViewModelToFlowInterface
import com.glowka.rafal.topmusic.presentation.utils.exhaustive


sealed class DashboardResult {
  object Terminated : DashboardResult()
}

class DashboardFlowImpl :
  BaseFlow<EmptyParam, DashboardResult>(flowScopeName = DashboardFlow.SCOPE_NAME), DashboardFlow {

  override fun onStart(param: EmptyParam): Screen<*, *, *> {
    return showScreen(
      screen = DashboardFlow.Screens.List,
      param = EmptyParam.EMPTY
    ) { event ->
      when (event) {
        is ListViewModelToFlowInterface.Event.ShowDetails -> showDetails(event)
        ListViewModelToFlowInterface.Event.Back -> finish(result = DashboardResult.Terminated)
      }.exhaustive
    }
  }

  private fun showDetails(showDetails: ListViewModelToFlowInterface.Event.ShowDetails) {
    logD("show Details")
    showScreen(
      screen = DashboardFlow.Screens.Details,
      param = DetailsViewModelToFlowInterface.Param(
        album = showDetails.album,
      ),
    ) { event ->
      when (event) {
        DetailsViewModelToFlowInterface.Event.Back -> {
          switchBackTo(DashboardFlow.Screens.List)
        }
        is DetailsViewModelToFlowInterface.Event.OpenURL -> {
          val intent = Intent(Intent.ACTION_VIEW)
          intent.data = Uri.parse(event.url)
          navigator.startActivity(intent)
        }
      }.exhaustive
    }
  }

}