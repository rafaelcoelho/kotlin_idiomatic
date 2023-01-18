package com.personal.studies.idiomatic

import java.util.function.Consumer

open class Lambda {
    companion object {
        fun returnValueFromLabel() = test@{ el: Int ->
            if (el < 10) return@test el
            else return@test el.plus(10)
        }

        fun destructuring(value: Int) = Some({ value * 2 }, "The handler operation is times 2")
    }

    data class Some(val block: () -> Int, val msg: String) : Lambda()
}

fun main(args: Array<String>) {
    println(Lambda.returnValueFromLabel()(10))

    val (handler, msg) = Lambda.destructuring(2)

    val cmd = object : Command {
        override fun handler(): Consumer<String> {
            return Consumer<String> { println("1 - msg is: $it and length is ${it.length}") }
        }
    }

    runCatching {
        cmd.handler()
            .andThen { println("2 - msg is: $it and length is ${it.length}") }
            .andThen { println("3 - msg is: $it and length is ${it.length}") }
            .andThen { println("4 - msg is: $it and length is ${it.length}") }
            .andThen { error("Some trouble") }
            .andThen { println("5 - msg is: $it and length is ${it.length}") }
            .accept("Hello World")
    }
        .onFailure { println("Failed $it") }
        .onSuccess { println("Success $it") }

    println(handler())
    println(msg)
}

interface Command {
    fun handler(): Consumer<String>
}