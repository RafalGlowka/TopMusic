package com.glowka.rafal.topmusic.presentation.utils

import org.junit.Assert

class EventsRecorder<EVENT : Any>(val expectedEvent: EVENT?) {

  var result: Boolean? = null

  fun listen(event: EVENT) {
    println("event: $event")
    result = if (event == expectedEvent) {
      if (result != null) println("Duplicate Event already registered")
      result == null
    } else {
      println("Event different $event")
      println("than expected $expectedEvent")
      false
    }
  }

  fun assert() {
    Assert.assertEquals(true, result)
  }

  fun assertNoEvent() {
    Assert.assertEquals(null, result)
  }

}