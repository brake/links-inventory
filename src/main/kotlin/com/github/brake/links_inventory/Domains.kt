package com.github.brake.links_inventory

data class Domains(val domains: Collection<String>, val status: String = "ok")

fun domainsError(message: String) = Domains(listOf(), message)

fun domainsOk(domains: Collection<String>) = Domains(domains)