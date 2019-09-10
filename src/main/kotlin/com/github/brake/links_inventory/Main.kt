package com.github.brake.links_inventory

import com.fasterxml.jackson.databind.SerializationFeature
import com.github.brake.links_inventory.handlers.handleAsForbidden
import com.github.brake.links_inventory.handlers.handleGetVisitedDomains
import com.github.brake.links_inventory.handlers.handleSaveVisitedLinks
import com.github.brake.links_inventory.repository.repositoryRetriever
import com.github.brake.links_inventory.repository.repositorySaver
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import redis.clients.jedis.Jedis

const val DB_HOST_PROPERTY_NAME = "db_host"
const val DB_HOST_DEFAULT = "localhost"
const val WEB_PORT = 8080
const val VISITED_DOMAINS_URL = "/visited_domains"
const val VISITED_LINKS_PATH = "/visited_links"

// TODO: add logging
// TODO: use DI to deliver db to the deep of repository
// TODO: refactor with locations (https://ktor.io/servers/features/locations.html)
fun main() {
    val dbHost = Config.getRedisHost()

    embeddedServer(Netty, WEB_PORT) {
        install(ContentNegotiation) {
            jackson {
                enable(SerializationFeature.INDENT_OUTPUT)
            }
        }
        routing {
            get(VISITED_DOMAINS_URL) {
                handleGetVisitedDomains(repositoryRetriever(Jedis(dbHost)))
            }
            post(VISITED_LINKS_PATH) {
                handleSaveVisitedLinks(repositorySaver(Jedis(dbHost)))
            }
            get {
                handleAsForbidden()
            }
            post {
                handleAsForbidden()
            }
        }
    }.start(wait = true)
}

