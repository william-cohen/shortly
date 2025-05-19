package com.cohen.shortly

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ShortlyApplication

fun main(args: Array<String>) {
	runApplication<ShortlyApplication>(*args)
}
