package com.glowka.rafal.topmusic.presentation.architecture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment

abstract class BaseFragment<
    VIEW_STATE : Any,
    VIEW_EVENT : Any,
    VIEW_MODEL : ViewModelToViewInterface<VIEW_STATE, VIEW_EVENT>> : Fragment() {
  protected val viewModel: VIEW_MODEL by injectViewModel()

  final override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return ComposeView(requireContext()).apply {
      renderState(viewModel.state)
    }
  }

  abstract fun ComposeView.renderState(viewModelState: MutableState<VIEW_STATE>)

  fun onBackPressed(): Boolean {
    return viewModel.onBackPressed()
  }

  companion object {
    const val ARG_SCOPE = "scope"
    const val ARG_SCREEN_TAG = "screenTag"
  }

}