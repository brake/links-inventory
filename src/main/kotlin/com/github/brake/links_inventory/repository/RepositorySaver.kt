package com.github.brake.links_inventory.repository

/** Save single URL with timestamp in repository */
interface RepositorySaver {
    fun save(url: String, timestamp: Long)
}
