package kies.mawaroute.model

import org.parceler.Parcel
import org.parceler.Parcel.Serialization
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Parcel(Serialization.BEAN)
@Root(name = "coupon_url", strict = false)
data class CouponUrl(

        @field:Element(name = "pc", required = false)
        var pc: String = "",

        @field:Element(name = "mobile", required = false)
        var mobile: String = ""
)
