package com.personal.studies.idiomatic.coroutines

import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.system.measureTimeMillis

fun main() {
    runBlocking {
        println(measureTimeMillis { fetchSomeDataConcurrent("RockStarProgrammer") })
        println(measureTimeMillis { fetchSomeDataSequential("RockStarProgrammer") })

        println(measureTimeMillis { fetchSomeDataConcurrent("TheNbaPlayer") })
        println(measureTimeMillis { fetchSomeDataSequential("TheNbaPlayer") })
    }
}

suspend fun fetchSomeDataConcurrent(dataKey: String) = coroutineScope {
    println("Fetching data concurrent")
    val name = getName(dataKey)
    val picture = getPicture(dataKey)

    JoinData(name = name.await(), picture = picture.await())
}

suspend fun fetchSomeDataSequential(dataKey: String) = coroutineScope {
    println("Fetching data sequential")
    val name = getName(dataKey).await()
    val picture = getPicture(dataKey).await()

    JoinData(name = name, picture = picture)
}

data class JoinData(val name: String, val picture: String)

private fun CoroutineScope.getName(key: String) = async {
    delay(225)
    key
}

private fun CoroutineScope.getPicture(key: String) = async {
    delay(2000)
    Random.nextBytes(42).toString() + key
}