package com.github.brake.links_inventory.handlers

import com.github.brake.links_inventory.Links
import com.github.brake.links_inventory.LinksResponse
import com.github.brake.links_inventory.repository.RepositorySaver
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext
import java.time.Instant

/**
 * Saves list of visited links with timestamp of moment of receiving request.
 * Timestamp will be produced by means of Instant::toEpochMilli()
 */
// TODO: deal with DB errors on save
suspend fun PipelineContext<Unit, ApplicationCall>.handleSaveVisitedLinks(saver: RepositorySaver) {
    val links = call.receive<Links>().links
    saver.saveLinksWithTimestamp(links, Instant.now().toEpochMilli())

    call.respond(LinksResponse())
}

private fun RepositorySaver.saveLinksWithTimestamp(links: Collection<String>, timestamp: Long) {
    links.forEach { link ->
        save(link, timestamp)
    }
}