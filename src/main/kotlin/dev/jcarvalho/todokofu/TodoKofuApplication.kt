package dev.jcarvalho.todokofu

import org.springframework.boot.WebApplicationType
import org.springframework.data.mongodb.core.*
import org.springframework.fu.kofu.application
import org.springframework.fu.kofu.mongo.reactiveMongodb
import org.springframework.fu.kofu.webflux.webFlux
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import java.util.*

val app = application(WebApplicationType.REACTIVE) {
	webFlux {
		router {
			val repository = ref<TodoItemRepository>()
			GET("/todo") {
				ok().body(repository.findAll())
			}
			POST("/todo") { request ->
				ok().body(
						request.bodyToMono<ItemRequest>()
								.flatMap { repository.create(it.title) }
				)
			}
			DELETE("/todo/{id}") { request ->
				val id = request.pathVariable("id")
				repository.findById(id).flatMap { item ->
					if (item.completed) {
						status(403).build()
					} else {
						repository.update(item.copy(completed = true))
								.flatMap { noContent().build() }
					}
				}.switchIfEmpty(notFound().build())
			}
		}
		codecs {
			jackson()
		}
	}
	reactiveMongodb()
	beans {
	    bean<TodoItemRepository>()
	}
}

data class ItemRequest(
		val title: String
)

class TodoItemRepository(private val mongo: ReactiveMongoOperations) {

	fun findAll() = mongo.findAll<TodoItem>()

	fun findById(id: String) = mongo.findById<TodoItem>(id)

	fun create(title: String) = mongo.save(TodoItem(id = UUID.randomUUID().toString(), title = title))

	fun update(item: TodoItem) = mongo.save(item)

}

data class TodoItem(
		val id: String,
		val title: String,
		val completed: Boolean = false
)

fun main(args: Array<String>) {
	app.run(args)
}
