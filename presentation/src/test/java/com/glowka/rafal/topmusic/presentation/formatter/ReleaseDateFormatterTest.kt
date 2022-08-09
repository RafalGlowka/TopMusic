package com.glowka.rafal.topmusic.presentation.formatter

import com.glowka.rafal.topmusic.domain.utils.EMPTY
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.util.*

@RunWith(Parameterized::class)
class ReleaseDateFormatterTest(private val dataIn: String, private val expectedResult: String) {

  companion object {

    @JvmStatic
    @Parameterized.Parameters(name = "test {index}: {0} - input:{1} result: {2}")
    fun data(): Collection<Array<*>> = listOf(
      arrayOf("2000-12-31", "Released December 31, 2000"),
      arrayOf("2022-01-21", "Released January 21, 2022"),
      arrayOf("", String.EMPTY),
      arrayOf("incorrect data", String.EMPTY),
    )
  }

  private lateinit var formatter: ReleaseDateFormatter

  @Before
  fun prepare() {
    MockKAnnotations.init(this)
    formatter = ReleaseDateFormatterImpl()
  }

  @After
  fun finish() {
    unmockkAll()
    clearAllMocks()
  }

  @Test
  fun check() {
    // Given
    Locale.setDefault(Locale("en", "EN"))
    // When
    val result = formatter.format(dataIn)

    //Then
    Assert.assertEquals(expectedResult, result)
  }
}