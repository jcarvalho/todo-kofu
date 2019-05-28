package dev.jcarvalho.todokofu

import org.springframework.boot.WebApplicationType
import org.springframework.fu.kofu.application
import org.springframework.fu.kofu.webflux.webFlux
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Flux

val app = application(WebApplicationType.REACTIVE) {
	webFlux {
		router {
			GET("/todo") {
				ok().body(Flux.just(TodoItem(id = "my-item", title = "Buy Milk")))
			}
		}
		codecs {
			jackson()
		}
	}
}

data class TodoItem(
		val id: String,
		val title: String,
		val completed: Boolean = false
)

fun main(args: Array<String>) {
	app.run(args)
}
