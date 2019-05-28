package dev.jcarvalho.todokofu

import org.springframework.boot.WebApplicationType
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.findAll
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

	fun create(title: String) = mongo.save(TodoItem(id = UUID.randomUUID().toString(), title = title))

}

data class TodoItem(
		val id: String,
		val title: String,
		val completed: Boolean = false
)

fun main(args: Array<String>) {
	app.run(args)
}
