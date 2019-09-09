package com.github.brake.links_inventory

import java.util.*

object Config {
    fun getRedisHost(): String {
        val config = object {}.javaClass.classLoader.getResourceAsStream(CONFIG_NAME) ?: return DB_HOST_DEFAULT
        return config.reader().use {
            Properties().run {
                load(it)
                getProperty(DB_HOST_PROPERTY_NAME, DB_HOST_DEFAULT)
            }
        }
    }
}