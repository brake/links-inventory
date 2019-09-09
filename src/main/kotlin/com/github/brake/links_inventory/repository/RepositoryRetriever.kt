package com.github.brake.links_inventory.repository

/** Retrieve data from repository */
interface RepositoryRetriever {
    fun getUniqueByTimeInterval(timeInterval: Pair<Long, Long>): Set<String>
}