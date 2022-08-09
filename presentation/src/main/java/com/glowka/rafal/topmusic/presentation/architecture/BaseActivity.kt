package com.glowka.rafal.topmusic.presentation.architecture

import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.glowka.rafal.topmusic.domain.service.SnackBarService
import com.glowka.rafal.topmusic.domain.utils.inject
import com.glowka.rafal.topmusic.presentation.R

open class BaseActivity : AppCompatActivity() {

  private val navigator: FragmentActivityAttachment by inject()
  val snackBarService: SnackBarService by inject()

  override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
    super.onCreate(savedInstanceState, persistentState)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
      window.setDecorFitsSystemWindows(false)
    } else @Suppress("DEPRECATION") {
      window.decorView.systemUiVisibility =
        window.decorView.systemUiVisibility or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
  }

  override fun onStart() {
    super.onStart()
    snackBarService.attach(findViewById(R.id.rootView)) // Some abstract of ActivityAware service is needed
    navigator.attach(this)
  }

  override fun onStop() {
    navigator.detach()
    super.onStop()
  }

  override fun onBackPressed() {
    val currentFragment = supportFragmentManager.fragments.lastBaseFragment()
    if (currentFragment?.onBackPressed() != false) {
      return
    }
    finish()
  }
}

fun List<Fragment>.lastBaseFragment(): BaseFragment<*, *, *>? {
  return lastOrNull { fragment -> fragment is BaseFragment<*, *, *> } as BaseFragment<*, *, *>
}