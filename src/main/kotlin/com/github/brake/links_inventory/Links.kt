package com.github.brake.links_inventory

data class Links(val links: List<String>)

fun links(vararg lnk: String): Links = Links(lnk.toList())