package com.glowka.rafal.topmusic.data.remote

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Exhaustive
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.collection

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

class JSONSerializerSpec : DescribeSpec() {

  init {

    describe("JSON serialization and deserialization test") {
      val testSet = listOf(
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
      val serializer: JSONSerializer = JSONSerializerImpl()

      Exhaustive.collection(testSet).checkAll { testSet ->

        it("object to JSON") {
          serializer.toJSON(testSet.structure) shouldBe testSet.jsonString
        }

        it("JSON to object") {
          serializer.fromJSON(testSet.jsonString, testSet.clazz) shouldBe testSet.structure
        }

      }
    }

  }
}


