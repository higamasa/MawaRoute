package kies.mawaroute.model

import org.parceler.Parcel
import org.parceler.Parcel.Serialization
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Parcel(Serialization.BEAN)
@Root(name = "access", strict = false)
data class Access(

        @field:Element(name = "line", required = false)
        var line: String = "",

        @field:Element(name = "station", required = false)
        var station: String = "",

        @field:Element(name = "station_exit", required = false)
        var stationExit: String = "",

        @field:Element(name = "walk", required = false)
        var walk: String = "",

        @field:Element(name = "note", required = false)
        var note: String = ""
)
