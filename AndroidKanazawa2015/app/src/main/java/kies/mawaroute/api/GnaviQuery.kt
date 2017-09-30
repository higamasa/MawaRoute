package kies.mawaroute.api

import kies.mawaroute.BuildConfig

class GnaviQuery {
    private val query = mutableListOf(
            Pair("format", "xml"),
            Pair("keyid", BuildConfig.GNAVI_API_KEY)
    )

    fun name(name: String): GnaviQuery {
        query += Pair("name", name)
        return this
    }

    fun location(lat: Double, lon: Double): GnaviQuery {
        query += Pair("latitude", lat.toString())
        query += Pair("longitude", lon.toString())
        return this
    }

    fun range(range: Int): GnaviQuery {
        // デフォルト 500m
        var queryRange = 2
        when (range) {
            in 0 until 300 -> queryRange = 1
            in 300 until 500 -> queryRange = 2
            in 500 until 1000 -> queryRange = 3
            in 1000 until 2000 -> queryRange = 4
            in 2000 until 3000 -> queryRange = 5
        }

        query += Pair("range", queryRange.toString())
        return this
    }

    fun hitPerPage(size: Int): GnaviQuery {
        query += Pair("hit_per_page", size.toString())
        return this
    }

    fun build(): Map<String, String> = query.toMap()
}
