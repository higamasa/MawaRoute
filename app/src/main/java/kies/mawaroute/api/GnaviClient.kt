package kies.mawaroute.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory


object GnaviClient {

    val service: GnaviService = Retrofit.Builder()
            .baseUrl("https://api.gnavi.co.jp")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .client(GnaviHttpClient.client)
            .build()
            .create(GnaviService::class.java)
}
