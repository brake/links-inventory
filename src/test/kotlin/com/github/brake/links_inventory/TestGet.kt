package com.github.brake.links_inventory

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.brake.links_inventory.handlers.*
import com.github.brake.links_inventory.repository.repositoryRetrieverTest
import com.github.brake.links_inventory.util.configureWithRouting
import io.kotlintest.matchers.collections.shouldBeEmpty
import io.kotlintest.matchers.collections.shouldContainAll
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.routing.get
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication

private fun TestApplicationEngine.handleGet(path: String) = handleRequest(HttpMethod.Get, path)

class TestGet : StringSpec({
    val intervalWithResult = 1L to 5L
    val intervalNoResult = 10L to 20L
    val expectedUrls = setOf("ya.ru", "funbox.ru", "stackoverflow.com")

    fun Pair<Long, Long>.fromToText() = "$RANGE_START_NAME=$first&$RANGE_STOP_NAME=$second"

    fun String.deserializeToDomains(): Domains = jacksonObjectMapper().readValue(this)

    "Test correct GET request (visited_domains)" {

        withTestApplication({
            configureWithRouting {
                get(VISITED_DOMAINS_URL) {
                    handleGetVisitedDomains(repositoryRetrieverTest(expectedUrls, intervalWithResult))
                }
            }
        }) {
            with(handleGet("$VISITED_DOMAINS_URL?${intervalWithResult.fromToText()}")) {
                response.status() shouldBe HttpStatusCode.OK
                with(response.content!!.deserializeToDomains()) {
                    domains.shouldContainAll(expectedUrls)
                    status shouldBe "ok"
                }
            }
        }
    }

    "Test correct GET request (empty answer)" {
        withTestApplication({
            configureWithRouting {
                get(VISITED_DOMAINS_URL) {
                    handleGetVisitedDomains(repositoryRetrieverTest(expectedUrls, intervalWithResult))
                }
            }
        }) {
            with(handleGet("$VISITED_DOMAINS_URL?${intervalNoResult.fromToText()}")) {
                response.status() shouldBe HttpStatusCode.OK
                with(response.content!!.deserializeToDomains()) {
                    domains.shouldBeEmpty()
                    status shouldBe "ok"
                }
            }
        }
    }

    "Test incorrect GET request without parameters" {
        withTestApplication({
            configureWithRouting {
                get(VISITED_DOMAINS_URL) {
                    handleGetVisitedDomains(repositoryRetrieverTest(expectedUrls, intervalWithResult))
                }
            }
        }) {
            with(handleGet(VISITED_DOMAINS_URL)) {
                response.status() shouldBe HttpStatusCode.OK
                with(response.content!!.deserializeToDomains()) {
                    domains.shouldBeEmpty()
                    status shouldContain ERROR_PARAMETER_MISSING.format(RANGE_START_NAME)
                }
            }
        }
    }

    "Test incorrect GET request without parameter '$RANGE_STOP_NAME'" {
        withTestApplication({
            configureWithRouting {
                get(VISITED_DOMAINS_URL) {
                    handleGetVisitedDomains(repositoryRetrieverTest(expectedUrls, intervalWithResult))
                }
            }
        }) {
            with(handleGet("$VISITED_DOMAINS_URL?$RANGE_START_NAME=10")) {
                response.status() shouldBe HttpStatusCode.OK
                with(response.content!!.deserializeToDomains()) {
                    domains.shouldBeEmpty()
                    status shouldContain ERROR_PARAMETER_MISSING.format(RANGE_STOP_NAME)
                }
            }
        }
    }

    "Test incorrect GET request without parameter '$RANGE_START_NAME'" {
        withTestApplication({
            configureWithRouting {
                get(VISITED_DOMAINS_URL) {
                    handleGetVisitedDomains(repositoryRetrieverTest(expectedUrls, intervalWithResult))
                }
            }
        }) {
            with(handleGet("$VISITED_DOMAINS_URL?$RANGE_STOP_NAME=10")) {
                response.status() shouldBe HttpStatusCode.OK
                with(response.content!!.deserializeToDomains()) {
                    domains.shouldBeEmpty()
                   status shouldContain ERROR_PARAMETER_MISSING.format(RANGE_START_NAME)
                }
            }
        }
    }

    "Test incorrect GET request having malformed Long" {
        val wrongNumber = "aaa"

        withTestApplication({
            configureWithRouting {
                get(VISITED_DOMAINS_URL) {
                    handleGetVisitedDomains(repositoryRetrieverTest(expectedUrls, intervalWithResult))
                }
            }
        }) {
            with(handleGet("$VISITED_DOMAINS_URL?$RANGE_START_NAME=$wrongNumber")) {
                response.status() shouldBe HttpStatusCode.OK
                with(response.content!!.deserializeToDomains()) {
                    domains.shouldBeEmpty()
                    status shouldContain ERROR_MALFORMED_LONG.format(RANGE_START_NAME, wrongNumber)
                }
            }
        }
    }
})