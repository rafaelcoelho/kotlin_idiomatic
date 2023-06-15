package com.personal.studies.idiomatic.coroutines

import kotlinx.coroutines.*

fun main() {
    runBlocking {
        supervisorScope {
            launch {
                println(Thread.currentThread().name)
            }

            launch(context = Dispatchers.Default) {
                supervisorScope {
                    for (i in 1..100) {
                        println("Default $i")
                        yield()

                        if (i == 59) throw RuntimeException()
                    }
                }
            }

            launch(context = Dispatchers.IO) {
                for (i in 1..200) {
                    println("IO $i")
                    yield()
                }
            }
        }
//            .reinvokeOnCompletion {
//            println("All the jobs are done and it is $it")
//        }
    }
}
