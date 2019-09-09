package com.github.brake.links_inventory

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.brake.links_inventory.handlers.handleSaveVisitedLinks
import com.github.brake.links_inventory.repository.CountingRepositorySaver
import com.github.brake.links_inventory.util.configureWithRouting
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.routing.post
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication

val testLinks = links("https://ya.ru",
    "https://ya.ru?q=123",
    "funbox.ru",
    "https://stackoverflow.com/questions/11828270/how-to-exit-the-vim-editor")

private fun TestApplicationEngine.handlePost(links: Links) = handleRequest(HttpMethod.Post, VISITED_LINKS_PATH) {
    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    setBody(links.toJsonString())
}

private fun Links.toJsonString() = jacksonObjectMapper().writeValueAsString(this)
private fun String.deserializeToLinksResponse(): LinksResponse = jacksonObjectMapper().readValue(this)

class TestPost : StringSpec({
    "Test receiving links" {
        val saver = CountingRepositorySaver()

        withTestApplication({
            configureWithRouting {
                post(VISITED_LINKS_PATH) {
                    handleSaveVisitedLinks(saver)
                }
            }
        }) {
            with(handlePost(testLinks)) {
                response.status() shouldBe HttpStatusCode.OK
                response.content!!.deserializeToLinksResponse() shouldBe LinksResponse()
                saver.counter shouldBe testLinks.links.size
            }
        }
    }

    "Test receiving empty links list" {
        val saver = CountingRepositorySaver()

        withTestApplication({
            configureWithRouting {
                post(VISITED_LINKS_PATH) {
                    handleSaveVisitedLinks(saver)
                }
            }
        }) {
            with(handlePost(Links(listOf()))) {
                response.status() shouldBe HttpStatusCode.OK
                response.content!!.deserializeToLinksResponse() shouldBe LinksResponse()
                saver.counter shouldBe 0
            }
        }
    }
})