package kies.mawaroute.view.databinding

import android.databinding.BindingAdapter
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import com.squareup.picasso.Picasso
import kies.mawaroute.R
import kies.mawaroute.view.transform.CircleTransform

object DataBindingHelper {

    @JvmStatic
    @BindingAdapter("shopImageSrc")
    fun ImageView.imageSrc(imageUrl: String) {
        if (imageUrl.isNotEmpty()) {
            Picasso.with(this.context)
                    .load(imageUrl)
                    .transform(CircleTransform())
                    .into(this)
            this.scaleType = ImageView.ScaleType.FIT_XY
        } else {
            val bg = ContextCompat.getDrawable(this.context, R.drawable.shop_image_placeholder)
            bg.setColorFilter(ContextCompat.getColor(this.context, R.color.blue), PorterDuff.Mode.OVERLAY)
            this.background = bg
            this.scaleType = ImageView.ScaleType.CENTER_INSIDE
            this.setImageResource(R.drawable.ic_rice_vector)
        }
    }
}
