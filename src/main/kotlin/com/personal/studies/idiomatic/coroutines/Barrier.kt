package com.personal.studies.idiomatic.coroutines

import kotlinx.coroutines.*
import kotlin.random.Random
import kotlin.system.measureTimeMillis

fun main() {
    runBlocking {
        CoroutineScope(Dispatchers.IO).launch {
            println("Running on  0 ---------> " + Thread.currentThread().name)
            withContext(Dispatchers.Unconfined) {
                println("Running on  1 ---------> " + Thread.currentThread().name)
                println(measureTimeMillis { println(fetchSomeDataConcurrent("RockStarProgrammer_concurrent")) })
                println(measureTimeMillis { println(fetchSomeDataSequential("RockStarProgrammer")) })

                println(measureTimeMillis { println(fetchSomeDataConcurrent("TheNbaPlayer")) })
                println(measureTimeMillis { println(fetchSomeDataSequential("TheNbaPlayer")) })
            }
            println("Running on  10 ---------> " + Thread.currentThread().name)
        }.join()
    }

//    runBlocking {
//        println(measureTimeMillis { println(fetchSomeDataConcurrent("RockStarProgrammer")) })
//        println(measureTimeMillis { println(fetchSomeDataSequential("RockStarProgrammer")) })
//
//        println(measureTimeMillis { println(fetchSomeDataConcurrent("TheNbaPlayer")) })
//        println(measureTimeMillis { println(fetchSomeDataSequential("TheNbaPlayer")) })
//
//        println("Running on  2 ---------> " + Thread.currentThread().name)
//    }
}

suspend fun fetchSomeDataConcurrent(dataKey: String) = coroutineScope {
    println("Running on  1 2 ---------> " + Thread.currentThread().name)

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
    println("Running on  1 2 3---------> " + Thread.currentThread().name)

    delay(2010)

    println("Running on  1 2 3---------> " + Thread.currentThread().name)
    key
}

private fun CoroutineScope.getPicture(key: String) = async {
    delay(2000)
    Random.nextBytes(42).toString() + key
}