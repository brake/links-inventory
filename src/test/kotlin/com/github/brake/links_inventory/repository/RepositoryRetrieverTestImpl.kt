package com.github.brake.links_inventory.repository


private class RepositoryRetrieverTestImpl(private val setOk: Set<String>,
                                          private val intervalOk: Pair<Long, Long>)
    : RepositoryRetriever {
    override fun getUniqueByTimeInterval(timeInterval: Pair<Long, Long>): Set<String> =
        if (timeInterval != intervalOk) setOf()
        else setOk
}


/** Factory return [RepositoryRetriever] instance */
fun repositoryRetrieverTest(setOk: Set<String>, intervalOk: Pair<Long, Long>): RepositoryRetriever =
    RepositoryRetrieverTestImpl(setOk, intervalOk)