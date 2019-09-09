package com.github.brake.links_inventory.repository

class CountingRepositorySaver : RepositorySaver {
    var counter: Int = 0

    override fun save(url: String, timestamp: Long) {
        counter++
    }
}
