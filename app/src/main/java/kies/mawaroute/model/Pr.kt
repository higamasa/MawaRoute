package kies.mawaroute.model

import org.parceler.Parcel
import org.parceler.Parcel.Serialization
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Parcel(Serialization.BEAN)
@Root(strict = false)
data class Pr(

        @field:Element(name = "pr_short", required = false)
        var prShort: String = "",

        @field:Element(name = "pr_long", required = false)
        var prLong: String = ""
)
