package dev.jcarvalho.todokofu

import org.springframework.boot.WebApplicationType
import org.springframework.fu.kofu.application
import org.springframework.fu.kofu.webflux.webFlux

val app = application(WebApplicationType.REACTIVE) {
	webFlux {
		router {

		}
		codecs {
			jackson()
		}
	}
}

fun main(args: Array<String>) {
	app.run(args)
}
