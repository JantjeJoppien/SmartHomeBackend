package com.joppien.smarthome

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SmarthomeApplication

fun main(args: Array<String>) {
	runApplication<SmarthomeApplication>(*args)
}
