package dev.jcarvalho.todokofu

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TodoKofuApplication

fun main(args: Array<String>) {
	runApplication<TodoKofuApplication>(*args)
}
