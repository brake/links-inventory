package com.github.brake.links_inventory.handlers

import com.github.brake.links_inventory.domainsError
import com.github.brake.links_inventory.domainsOk
import com.github.brake.links_inventory.repository.RepositoryRetriever
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext

const val ERROR_MALFORMED_LONG = "Error: parameter [%s] should be long integer, '%s' passed"
const val ERROR_PARAMETER_MISSING = "Error: mandatory parameter [%s] not specified"
const val RANGE_START_NAME = "from"
const val RANGE_STOP_NAME = "to"

suspend fun PipelineContext<Unit, ApplicationCall>.handleGetVisitedDomains(retriever: RepositoryRetriever)  {
    val timeInterval = getTimeIntervalFromRequest()
    if (timeInterval != null) {
        val result = retriever.getUniqueByTimeInterval(timeInterval)
        call.respond(domainsOk(result))
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.tryToGetTime(param: String): Long? {
    val time: String? = call.request.queryParameters[param]

    return if (time != null) try {
        time.toLong()
    } catch (_: NumberFormatException) {
        call.respond(domainsError(ERROR_MALFORMED_LONG.format(param, time)))
        null
    } else {
        call.respond(domainsError(ERROR_PARAMETER_MISSING.format(param)))
        null
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.getTimeIntervalFromRequest(): Pair<Long, Long>? {
    return tryToGetTime(RANGE_START_NAME)?.let { start ->
        tryToGetTime(RANGE_STOP_NAME)?.let { stop ->
            start to stop
        }
    }
}