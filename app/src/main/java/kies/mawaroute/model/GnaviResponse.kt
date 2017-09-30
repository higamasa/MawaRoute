package kies.mawaroute.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(strict = false)
data class GnaviResponse(

        @field:Element(name = "total_hit_count")
        var totalHitCount: Int = 0,

        @field:Element(name = "hit_per_page")
        var hitPerPage: Int = 0,

        @field:Element(name = "page_offset")
        var pageOffset: Int = 0,

        @field:ElementList(inline = true)
        var shops: List<Shop> = mutableListOf()
)
