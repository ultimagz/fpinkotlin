package com.fpinkotlin.generators

import io.kotlintest.properties.Gen
import java.util.*


fun <T> list(gen: Gen<T>, minLength: Int = 0, maxLength: Int = 100): Gen<kotlin.collections.List<T>> =
  object : Gen<kotlin.collections.List<T>> {
    override fun generate(): kotlin.collections.List<T> {
      val length: Int = (maxLength - minLength).let {
        if (it <= 0)
          0
        else
          minLength + Random().nextInt(it)
      }
      val range: IntRange = 0 until length
      return range.map { gen.generate() }.toList()
    }
  }

fun <T>  forAll(generator: Gen<T>, fn: (a: T) -> Boolean, numTests: Int = 1_000) {
  for (k in 0..numTests) {
    val a = generator.generate()
    val passed = fn(a)
    if (!passed) {
      throw AssertionError("Property failed for\n$a")
    }
  }
}

class IntGenerator(private val min: Int = 0, val max: Int = Int.MAX_VALUE): Gen<Int> {
    val RANDOM = Random()
    override fun generate(): Int = RANDOM.nextInt(max - min) + min
}

class CharGenerator(private val min: Char = ' ', val max: Char = '~'): Gen<Char> {
    val RANDOM = Random()
    override fun generate(): Char = (RANDOM.nextInt(max.toInt() - min.toInt()) + min.toInt()).toChar()
}

class IntPairGenerator(private val min: Int = 0, private val max: Int = 100) : Gen<Pair<Int, Int>> {
    private val gen = IntGenerator(min, max)
    override fun generate(): Pair<Int, Int> = Pair(gen.generate(), gen.generate())
}

class IntDoublePairGenerator: Gen<Pair<Int, Double>> {
    override fun generate(): Pair<Int, Double> = Pair(Gen.int().generate(), Gen.double().generate())
}

class CharListGenerator(private val minLength: Int = 0, private val maxLength: Int = 100) : Gen<Pair<Array<Char>,
        List<Char>>> {

    override fun generate(): Pair<Array<Char>, List<Char>> {
        val array: Array<Char> = list(CharGenerator(), minLength, maxLength).generate().toTypedArray()
        return Pair(array, listOf(*array))
    }
}

class IntListGenerator(private val minLength: Int = 0, private val maxLength: Int = 100) : Gen<Pair<Array<Int>,
    List<Int>>> {

    override fun generate(): Pair<Array<Int>, List<Int>> {
        val array: Array<Int> = list(IntGenerator(), minLength, maxLength).generate().toTypedArray()
        return Pair(array, listOf(*array))
    }
}

class IntListPairGenerator(private val minLength: Int = 0, private val maxLength: Int = 100) : Gen<Pair<List<Int>,
    List<Int>>> {

    override fun generate(): Pair<List<Int>, List<Int>> {
        val array1: Array<Int> = list(IntGenerator(), minLength, maxLength).generate().toTypedArray()
        val array2: Array<Int> = list(IntGenerator(), minLength, maxLength).generate().toTypedArray()
        return Pair(listOf(*array1), listOf(*array2))
    }
}


