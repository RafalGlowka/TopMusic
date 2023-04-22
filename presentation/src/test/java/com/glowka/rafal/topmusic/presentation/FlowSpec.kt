package com.glowka.rafal.topmusic.presentation

import com.glowka.rafal.topmusic.domain.service.FakeToastService
import com.glowka.rafal.topmusic.domain.service.ToastService
import com.glowka.rafal.topmusic.domain.utils.CoroutineErrorHandler
import io.kotest.core.spec.style.DescribeSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.module

abstract class FlowSpec(body: DescribeSpec.() -> Unit = {}) : DescribeSpec(body) {

  val toastService = FakeToastService()
  val dispatcher: TestDispatcher = UnconfinedTestDispatcher()

  init {

    beforeSpec {
      Dispatchers.setMain(dispatcher)
      initKoinForViewModelTest()
    }
    afterSpec {
      Dispatchers.resetMain()
      cleanUpKoin()
    }
  }

  open fun Module.prepareKoinContext() {}

  private fun initKoinForViewModelTest() {

    startKoin {
      modules(listOf<Module>(
        module {
          single<CoroutineErrorHandler> {
            CoroutineErrorHandler(toastService = get())
          }
          single<ToastService> {
            toastService
          }
          prepareKoinContext()
        }
      ))
      createEagerInstances()
    }
  }

  private fun cleanUpKoin() {
    stopKoin()
  }
}


