package com.github.brake.links_inventory.handlers

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext

suspend fun PipelineContext<Unit, ApplicationCall>.handleAsForbidden() {
    call.respond(HttpStatusCode.Forbidden)
}