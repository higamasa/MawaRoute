package kies.mawaroute.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(strict = false)
data class Pref(

        @Element(name = "pref_code")
        var prefCode: String,

        @Element(name = "pref_name")
        var prefName: String,

        @Element(name = "area_code")
        var areaCode: String
)
