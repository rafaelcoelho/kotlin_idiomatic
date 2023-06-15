package com.personal.studies.idiomatic.coroutines

import kotlinx.coroutines.*

fun main() {
    runBlocking {

        supervisorScope {
            val handler = CoroutineExceptionHandler { _, exception ->
                println("CoroutineExceptionHandler got $exception with suppressed ${exception.suppressed.contentToString()}")
//                cancel("Stop The World", exception)
            }

            launch(handler) {
                println(Thread.currentThread().name)
                generateSequence(1L) { it * 2 }
                    .forEach {
                        println("The value is $it")
                        delay(500)
                    }
            }


            launch(handler) {
                supervisorScope {
                    for (i in 1..1_000_0000) {
                        if (i % 10_000 == 0)
                            println("Default $i")
                        yield()

                        if (i == 999_999) throw RuntimeException("Inject some failure $i")
                    }
                }
            }

            launch(handler) {
                for (i in 1..2_000_000) {
                    if (i % 20_000 == 0)
                        println("IO $i")
                    yield()

                    if (i == 1_000_999) throw RuntimeException("Inject some failure $i")

                }
            }

        }.invokeOnCompletion {
            println("All the jobs are done and it is $it")
        }
    }
}
