package kies.mawaroute.model

import org.parceler.Parcel
import org.parceler.Parcel.Serialization
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Parcel(Serialization.BEAN)
@Root(name = "image_url", strict = false)
data class ImageUrl(

        @field:Element(name = "shop_image1", required = false)
        var restaurantImage1: String = "",

        @field:Element(name = "shop_image2", required = false)
        var restaurantImage2: String = "",

        @field:Element(name = "qrcode", required = false)
        var qrcode: String = ""

)
