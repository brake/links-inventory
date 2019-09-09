package com.github.brake.links_inventory.util

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.routing.Routing
import io.ktor.routing.routing

fun Application.configureWithRouting(routingBlock: Routing.() -> Unit) = apply {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
        routing(routingBlock)
    }
}
