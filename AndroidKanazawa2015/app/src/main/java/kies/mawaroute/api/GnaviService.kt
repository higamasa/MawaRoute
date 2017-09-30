package kies.mawaroute.api

import io.reactivex.Single
import kies.mawaroute.BuildConfig
import kies.mawaroute.model.GnaviResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface GnaviService {

    @GET("RestSearchAPI/${BuildConfig.GNAVI_API_VERSION}")
    fun restaurantSearch(@QueryMap query: Map<String, String>): Single<GnaviResponse>
}
