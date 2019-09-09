package com.github.brake.links_inventory.repository

import com.github.brake.links_inventory.repository.RepositoryImpl.TIMESTAMPS
import com.github.brake.links_inventory.repository.RepositoryImpl.URL
import redis.clients.jedis.Jedis
import java.net.URI

// TODO: deal with DB server errors
/**
 * Gets all IDs of records having score in range of given time interval.
 * After that retrieves each data object by its ID and takes associated URL.
 */
private class RepositoryRetrieverImpl(private val db: Jedis) : RepositoryRetriever {
    override fun getUniqueByTimeInterval(timeInterval: Pair<Long, Long>): Set<String> {
        val (start, stop) = timeInterval.let {
            it.first.toDouble() to it.second.toDouble()
        }
        return db.zrangeByScore(TIMESTAMPS, start, stop)
            .asSequence()
            .map { id -> db.hget(id, URL) }
            .map { url -> URI(url).host ?: url }
            .toSet()
    }
}

/** Factory return [RepositoryRetriever] instance */
fun repositoryRetriever(db: Jedis): RepositoryRetriever = RepositoryRetrieverImpl(db)