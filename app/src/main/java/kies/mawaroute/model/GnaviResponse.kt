package kies.mawaroute.model

import org.parceler.Parcel
import org.parceler.Parcel.Serialization
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Parcel(Serialization.BEAN)
@Root(strict = false)
data class GnaviResponse(

        @field:Element(name = "total_hit_count")
        var totalHitCount: Int = 0,

        @field:Element(name = "hit_per_page")
        var hitPerPage: Int = 0,

        @field:Element(name = "page_offset")
        var pageOffset: Int = 0,

        @field:ElementList(name = "rest", inline = true)
        var restaurants: List<Restaurant> = mutableListOf()
)
