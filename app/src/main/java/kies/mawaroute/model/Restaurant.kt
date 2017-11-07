package kies.mawaroute.model

import org.parceler.Parcel
import org.parceler.Parcel.Serialization
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Parcel(Serialization.BEAN)
@Root(name = "rest", strict = false)
data class Restaurant(

        @field:Element(name = "id")
        var id: String = "",

        @field:Element(name = "update_date")
        var updateDate: String = "",

        @field:Element(name = "name")
        var name: String = "",

        @field:Element(name = "name_kana")
        var nameKana: String = "",

        @field:Element(name = "latitude")
        var latitude: Double = 0.0,

        @field:Element(name = "longitude")
        var longitude: Double = 0.0,

        @field:Element(name = "category")
        var category: String = "",

        @field:Element(name = "url")
        var url: String = "",

        @field:Element(name = "url_mobile")
        var urlMobile: String = "",

        @field:Element(name = "coupon_url")
        var couponUrl: CouponUrl = CouponUrl(),

        @field:Element(name = "image_url")
        var imageUrl: ImageUrl = ImageUrl(),

        @field:Element(name = "address", required = false)
        var address: String = "",

        @field:Element(name = "tel", required = false)
        var tel: String = "",

        @field:Element(name = "tel_sub", required = false)
        var telSub: String = "",

        @field:Element(name = "fax", required = false)
        var fax: String = "",

        @field:Element(name = "opentime", required = false)
        var opentime: String = "",

        @field:Element(name = "holiday", required = false)
        var holiday: String = "",

        @field:Element(name = "access")
        var access: Access = Access(),

        @field:Element(name = "parking_lots", required = false)
        var parkingLots: Int = 0,

        @field:Element(name = "pr")
        var pr: Pr = Pr(),

        @field:Element(name = "budget", required = false)
        var budget: String = "",

        @field:Element(name = "party", required = false)
        var party: String = "",

        @field:Element(name = "lunch", required = false)
        var lunch: Int = 0,

        @field:Element(name = "credit_card", required = false)
        var creditCard: String = "",

        @field:Element(name = "e_money", required = false)
        var eMoney: String = ""
)
