package com.github.brake.links_inventory.repository

import com.github.brake.links_inventory.repository.RepositoryImpl.ID_NAME
import com.github.brake.links_inventory.repository.RepositoryImpl.TIMESTAMP
import com.github.brake.links_inventory.repository.RepositoryImpl.TIMESTAMPS
import com.github.brake.links_inventory.repository.RepositoryImpl.URL
import redis.clients.jedis.Jedis

// TODO: deal with DB server errors
/**
 * Global ID maintained with key [RepositoryImpl.ID_NAME] and incremented on each call of [save]
 * Each URL along with its timestamp saved with hash of Global ID
 * In addition each timestamp saved to ordered list as score with Global ID as data (to retrieve all
 * ID by given time interval)
 */
private class RepositorySaverImpl(private val db: Jedis) : RepositorySaver {
    override fun save(url: String, timestamp: Long) {
        val id = db.incr(ID_NAME).toString()

        db.hmset(id, mapOf(URL to url, TIMESTAMP to timestamp.toString()))
        db.zadd(TIMESTAMPS, timestamp.toDouble(), id)
    }
}

/** Factory return [RepositorySaver] instance */
fun repositorySaver(db: Jedis): RepositorySaver = RepositorySaverImpl(db)