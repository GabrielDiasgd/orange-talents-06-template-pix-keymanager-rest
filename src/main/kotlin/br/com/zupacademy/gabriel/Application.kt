package br.com.zupacademy.gabriel

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("br.com.zupacademy.gabriel")
		.start()
}
