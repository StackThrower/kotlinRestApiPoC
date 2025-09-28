package com.example.poc.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.example.poc"]) // scan all modules' packages
class RestApiApplication

fun main(args: Array<String>) {
    runApplication<RestApiApplication>(*args)
}

