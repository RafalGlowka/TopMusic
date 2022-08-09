package com.glowka.rafal.topmusic.data.remote

import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

data class SimpleObject(val name: String, val value: Int)

class StringList : ArrayList<String>()
class IntList : ArrayList<Int>()
class DoubleList : ArrayList<Double>()
class SimpleObjects : ArrayList<SimpleObject>()

data class TestSet<T : Any?>(
  val testName: String,
  val jsonString: String,
  val clazz: Class<T>,
  val structure: Any
)

@RunWith(Parameterized::class)
class JSONSerializerTest(val testSet: TestSet<*>) {

  companion object {

    @JvmStatic
    @Parameterized.Parameters(name = "test {index}: {0} - input:{1} result: {2}")
    fun data(): Collection<TestSet<*>> = listOf(
      TestSet("empty array", "[]", StringList::class.java, StringList()),
      TestSet(
        "list of strings",
        "[\"123\",\"124\",\"125\"]",
        StringList::class.java,
        listOf("123", "124", "125")
      ),
      TestSet("string", "\"Litwo ojczyzno\"", String::class.java, "Litwo ojczyzno"),
      TestSet("int", "123", Int::class.java, 123),
      TestSet("double", "123.12", Double::class.java, 123.12),
      TestSet("list of ints", "[123,124,125]", IntList::class.java, listOf(123, 124, 125)),
      TestSet(
        "list of doubles",
        "[123.1,124.0,125.9]",
        DoubleList::class.java,
        listOf(123.1, 124.0, 125.9)
      ),
      TestSet(
        "Simple object",
        "{\"name\":\"Anna\",\"value\":123}",
        SimpleObject::class.java,
        SimpleObject(name = "Anna", value = 123)
      ),
      TestSet(
        "list of objects",
        "[{\"name\":\"Anna\",\"value\":123},{\"name\":\"Zosia\",\"value\":13}]",
        SimpleObjects::class.java,
        listOf(SimpleObject(name = "Anna", value = 123), SimpleObject(name = "Zosia", value = 13))
      )
    )
  }

  private lateinit var serializer: JSONSerializer

  @Before
  fun prepare() {
    MockKAnnotations.init(this)
    serializer = JSONSerializerImpl()
  }

  @After
  fun finish() {
    unmockkAll()
    clearAllMocks()
  }

  @Test
  fun verifyToJSON() {
    // Given

    // When
    val result = serializer.toJSON(testSet.structure)

    //Then
    Assert.assertEquals(testSet.jsonString, result)
  }

  @Test
  fun verifyFromJSON() {
    // Given

    // When
    val result = fromJSON(testSet = testSet)

    //Then
    Assert.assertEquals(testSet.structure, result)
  }

  private fun <DATA : Any?> fromJSON(testSet: TestSet<DATA>): DATA =
    serializer.fromJSON<DATA>(
      string = testSet.jsonString,
      clazz = testSet.clazz
    )
}