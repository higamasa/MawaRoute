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

    fun hitPerPage(size: Int): GnaviQuery {
        query += Pair("hit_per_page", size.toString())
        return this
    }

    fun build(): Map<String, String> {
        return query.toMap()
    }
}
