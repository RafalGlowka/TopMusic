package com.glowka.rafal.topmusic.modules

import com.glowka.rafal.topmusic.domain.service.SnackBarService
import com.glowka.rafal.topmusic.presentation.service.ToastServiceImpl
import com.glowka.rafal.topmusic.domain.service.ToastService
import com.glowka.rafal.topmusic.domain.utils.CoroutineErrorHandler
import com.glowka.rafal.topmusic.presentation.R
import com.glowka.rafal.topmusic.presentation.architecture.FragmentActivityAttachment
import com.glowka.rafal.topmusic.presentation.architecture.FragmentNavigatorImpl
import com.glowka.rafal.topmusic.presentation.architecture.ScreenNavigator
import com.glowka.rafal.topmusic.presentation.service.SnackBarServiceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.dsl.binds

val appModule = module {

  single<ToastService> {
    ToastServiceImpl(
      context = androidContext()
    )
  }

  single<SnackBarService> {
    SnackBarServiceImpl()
  }

  single<CoroutineErrorHandler> {
    CoroutineErrorHandler(toastService = get())
  }

  single {
    FragmentNavigatorImpl(
      containerId = R.id.fragment_container
    )
  } binds(arrayOf(
    FragmentActivityAttachment::class,
    ScreenNavigator::class
  ))
}