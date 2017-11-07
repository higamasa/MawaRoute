package kies.mawaroute.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(strict = false)
data class Pr(

        @field:Element(name = "pr_short", required = false)
        var prShort: String = "",

        @field:Element(name = "pr_long", required = false)
        var prLong: String = ""
)
